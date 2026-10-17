package me.mioclient.module.abstract_;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import me.mioclient.Hub;
import me.mioclient.enum_.Class_1172;
import me.mioclient.enum_.Priority;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.ChatUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.internal.ExplosionDamage;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.combat.AutoCrystalModule;
import me.mioclient.module.combat.OffhandModule;
import me.mioclient.record.RenderStyle;
import me.mioclient.record.TargetedPlacement;
import me.mioclient.record.PlayerTarget;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class AbstractModule_39 extends Module {
   public static final AbstractModule_28 field_3764 = Hub.field_2595.method_78(AbstractModule_28.class);
   public static final OffhandModule field_3765 = Hub.field_2595.method_78(OffhandModule.class);
   public static final AutoCrystalModule field_3766 = Hub.field_2595.method_78(AutoCrystalModule.class);
   public Setting<Integer> delay;
   public Setting<Float> range;
   public Setting<Float> wallRange;
   public Setting<Float> targetRange;
   public Setting<Float> minDamage;
   public Setting<Float> maxSelfDamage;
   public Setting<Boolean> antiSuicide;
   public Setting<Boolean> rotate;
   public Setting<Boolean> strictDirection;
   public Setting<Boolean> airPlace;
   public Setting<Boolean> liquidPlace;
   public Setting<Boolean> inhibit;
   public Setting<Boolean> sequential;
   public Setting<Boolean> render;
   public Setting<Color> fill;
   public Setting<Color> outline;
   public Setting<Boolean> fade;
   public Setting<Float> fadeTime;
   public Setting<Boolean> pause;
   public Setting<Boolean> mining;
   public Setting<Boolean> eating;
   public Setting<Float> health;
   public CompletableFuture<TargetedPlacement> field_3789;
   public final MutableBlockPos field_3790;
   public final Timer field_3791;
   public final AtomicReference<TargetedPlacement> field_3792;
   public PlayerTarget field_3793;

   public AbstractModule_39() {
      super(
         "AnchorAura",
         new PlaceholderText().method_2(String.valueOf(ChatFormatting.RED)).method_9("Explodes respawn anchors to kill players. \n\u0001Doesn't work in the nether."),
         Category.COMBAT
      );
      Settings.initialize(this);
      this.field_3790 = new MutableBlockPos();
      this.field_3791 = new Timer();
      this.field_3792 = new AtomicReference<>();
      Hub.field_2616
         .method_2(
            new RenderStyle(this, this.fill, this.outline, () -> 1F, this.fadeTime, () -> false, this.fade, 450)
         );
   }

   @Override
   public void onEnable() {
      this.field_3793 = null;
      if (this.field_3789 != null) {
         try {
            this.field_3789.complete(null);
         } catch (CancellationException var2) {
         }
      }
   }

   @Override
   public void onToggle() {
      this.field_3792.set(null);
   }

   @Override
   public String getInfo() {
      return this.field_3793 != null ? "%s, %.1f".formatted(this.field_3793.getPlayer().getName().getString(), this.field_3793.method_445()) : null;
   }

   @Subscribe
   public void method_5(Event_7 var1) {
      if (mc.level.dimension() == net.minecraft.world.level.Level.NETHER) {
         ChatUtil.method_2(Component.nullToEmpty("Doesn't work in the nether!"), ChatUtil.method_38(-1), Priority.HIGH);
         this.disable();
      } else {
         this.method_1061();
         if (!this.method_639()) {
            TargetedPlacement var2 = this.field_3792.get();
            if (var2 != null) {
               if (this.method_12() != null) {
                  BlockPos var3 = var2.getPos();
                  BlockState var4 = mc.level.getBlockState(var3);
                  int var5 = mc.player.getInventory().selected;
                  if (var4.is(Blocks.RESPAWN_ANCHOR)) {
                     Direction var6 = this.method_82(var3);
                     if (var6 == null || !this.field_3791.method_9((long)(this.delay.getValue() + Hub.field_2602.method_983()))) {
                        return;
                     }

                     if ((Integer)var4.getValue(RespawnAnchorBlock.CHARGE) == 0) {
                        this.method_2(var3, var6, var5);
                     }

                     if (this.rotate.getValue()) {
                        Hub.field_2598.method_2(RotationManager.method_78(var3.getCenter()), 100);
                     }

                     this.method_2(var3, var6, Vec3.atCenterOf(var3), InteractionHand.OFF_HAND);
                     if (this.inhibit.getValue()) {
                        mc.level.setBlockAndUpdate(var3, Blocks.AIR.defaultBlockState());
                     }
                  }

                  this.method_2(var3, var2.getDirection(), this.method_12());
               }
            }
         }
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (this.sequential.getValue() && this.field_3792.get() != null) {
         if (var1.method_127() instanceof ClientboundBlockUpdatePacket var2
            && this.field_3793 != null
            && var2.getBlockState().isAir()
            && var2.getPos().equals(this.field_3792.get().getPos())) {
            this.method_2(this.field_3792.get().getPos(), this.field_3792.get().getDirection(), this.method_12());
         }
      }
   }

   public void method_2(BlockPos var1, Direction var2, int var3) {
      Vec3 var4 = var1.getCenter().relative(var2, Class_0245.field_688);
      InteractionHand var5 = InventoryUtil.method_7(Items.GLOWSTONE);
      if (this.rotate.getValue()) {
         Hub.field_2598.method_2(RotationManager.method_78(var4), 100);
      }

      if (var5 != null) {
         this.method_2(var1, var2, var4, InteractionHand.MAIN_HAND);
      } else {
         int var6 = InventoryUtil.method_5(Items.GLOWSTONE);
         if (var6 != -1) {
            InventoryUtil.method_16(var6);
            this.method_2(var1, var2, var4, InteractionHand.MAIN_HAND);
            InventoryUtil.method_16(var3);
         }
      }

      this.field_3791.reset();
   }

   public void method_2(BlockPos var1, Direction var2, InteractionHand var3) {
      if (!this.method_176(var1)) {
         int var4 = mc.player.getInventory().selected;
         int var5 = InventoryUtil.method_5(Items.RESPAWN_ANCHOR);
         boolean var6 = InteractionUtil.method_78(var1) == null && field_3764.method_1052() && this.airPlace.getValue();
         if (var5 != -1 || var3 != InteractionHand.MAIN_HAND) {
            if (this.rotate.getValue()) {
               Hub.field_2598.method_2(RotationManager.method_2(var1.getCenter(), var2), 100);
            }

            boolean var7 = !mc.player.getMainHandItem().is(Items.RESPAWN_ANCHOR);
            if (var7) {
               InventoryUtil.method_16(var5);
            }

            if (var6) {
               var3 = PlayerUtil.method_5(var3);
               PacketSender.method_1100();
            }

            if (InteractionUtil.method_9(var1, this.method_310(var1.relative(var2)), var2, this.airPlace.getValue(), var3) && this.render.getValue()) {
               Hub.field_2616.method_2(this, var1);
            }

            if (var6) {
               PacketSender.method_1100();
            }

            if (var7) {
               InventoryUtil.method_16(var4);
            }
         }
      }
   }

   public void method_2(BlockPos var1, Direction var2, Vec3 var3, InteractionHand var4) {
      Hub.field_2615.method_22(() -> {
         PacketSender.method_2(InteractionHand.MAIN_HAND, new BlockHitResult(var3, var2, var1, false));
         PacketSender.method_9(var4);
      });
   }

   public void method_1061() {
      if (this.field_3792.get() != null && this.method_468(this.field_3792.get().getPos())) {
         this.field_3792.set(null);
      }

      if (this.field_3789 != null && this.field_3789.isDone() && this.field_3792.get() == null) {
         try {
            TargetedPlacement var1 = this.field_3789.get();
            if (var1 == null) {
               this.field_3792.set(null);
            } else {
               this.field_3792.set(var1);
               this.field_3793 = var1.getTarget();
            }
         } catch (ExecutionException | InterruptedException var2) {
            var2.printStackTrace();
         }
      }

      if (this.field_3789 == null || this.field_3789.isDone() || this.field_3789.isCancelled()) {
         if (this.field_3793 != null && PlayerTarget.isInvalid(this.field_3793.getPlayer(), (double)this.targetRange.getValue().floatValue())) {
            this.field_3793 = null;
         }

         this.field_3789 = CompletableFuture.supplyAsync(this::method_1062, THREAD_POOL).exceptionally(var0 -> null);
      }
   }

   public Direction method_82(BlockPos var1) {
      List var2 = InteractionUtil.method_39(var1);
      Direction var3 = this.strictDirection.getValue() ? null : Direction.DOWN;
      if (!var2.isEmpty()) {
         var3 = (Direction)var2.get(0);
      }

      return var3;
   }

   public Vec3 method_310(BlockPos var1) {
      List<Vec3> var2 = Class_1172.field_3634.method_38(var1);
      return var2.stream()
         .filter(var1x -> BlockUtil.method_9(var1x) || mc.player.getEyePosition().distanceTo(var1x) <= (double)this.wallRange.getValue().floatValue())
         .min(Comparator.comparing(var0 -> mc.player.getEyePosition().distanceTo(var0)))
         .orElse(var1.getCenter());
   }

   public TargetedPlacement method_1062() {
      TargetedPlacement var1 = null;

      for (double var2 = -Math.floor((double)this.range.getValue().floatValue());
         var2 < Math.ceil((double)this.range.getValue().floatValue());
         var2 += 1.0
      ) {
         for (double var4 = -Math.floor((double)this.range.getValue().floatValue());
            var4 < Math.ceil((double)this.range.getValue().floatValue());
            var4 += 1.0
         ) {
            for (double var6 = -Math.floor((double)this.range.getValue().floatValue());
               var6 < Math.ceil((double)this.range.getValue().floatValue());
               var6 += 1.0
            ) {
               this.field_3790.set(mc.player.getX() + var2, mc.player.getEyeY() + var4, mc.player.getZ() + var6);
               if ((InteractionUtil.method_2(this.field_3790, Blocks.RESPAWN_ANCHOR, true, false) || this.method_176(this.field_3790))
                  && this.field_3790.getY() >= mc.level.getMinY()
                  && this.field_3790.getY() <= mc.level.getMaxY()) {
                  List var8 = Class_1172.field_3634.method_38(this.field_3790);
                  boolean var9 = BlockUtil.method_2(var8);
                  double var10 = mc.player.getEyePosition().distanceTo(this.field_3790.getCenter());
                  if ((var9 || !(var10 > (double)this.wallRange.getValue().floatValue())) && !(var10 > (double)this.range.getValue().floatValue())) {
                     Direction var12 = InteractionUtil.method_2(this.field_3790, this.strictDirection.getValue(), this.wallRange.getValue() <= 0.0F);
                     PlayerTarget var13 = this.method_270(this.field_3790);
                     if (var12 == null && this.airPlace.getValue() || this.method_176(this.field_3790)) {
                        var12 = Direction.DOWN;
                     }

                     if (var12 == null && !mc.level.getFluidState(this.field_3790).isEmpty() && this.liquidPlace.getValue()) {
                        var12 = Direction.DOWN;
                     }

                     if (var12 != null && var13 != null && !this.method_2(this.field_3790, var13)) {
                        TargetedPlacement var14 = new TargetedPlacement(this.field_3790.immutable(), var12, var13);
                        if (this.method_176(this.field_3790)) {
                           return var14;
                        }

                        var1 = var14.method_2(var1);
                     }
                  }
               }
            }
         }
      }

      return var1;
   }

   public PlayerTarget method_270(BlockPos var1) {
      PlayerTarget var2 = null;

      for (Player var4 : mc.level.players()) {
         if (!PlayerTarget.isInvalid(var4, (double)this.targetRange.getValue().floatValue())) {
            double var5 = (double)ExplosionDamage.method_2(
               var1.getCenter(), var4, var4.getBoundingBox(), 5.0, true, this.method_176(var1) ? var1 : null, null
            );
            if (!((double)this.minDamage.getValue().floatValue() > var5) && !(var5 < Class_0245.field_688) && (var2 == null || var5 > var2.method_445())) {
               var2 = new PlayerTarget(var4, var5);
            }
         }
      }

      return var2;
   }

   public InteractionHand method_12() {
      boolean var1 = field_3765.isToggled() && field_3765.item.getValue().method_98(false) == Items.RESPAWN_ANCHOR;
      InteractionHand var2 = InventoryUtil.method_7(Items.RESPAWN_ANCHOR);
      if (var1 && var2 == null) {
         return null;
      } else {
         return var1 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      }
   }

   public boolean method_2(BlockPos var1, PlayerTarget var2) {
      if (var2 == null) {
         return true;
      } else {
         boolean var3 = this.method_176(var1);
         double var4 = (double)ExplosionDamage.method_2(
            Vec3.atCenterOf(var1),
            mc.player,
            mc.player.getBoundingBox(),
            5.0,
            true,
            this.method_176(var1) ? var1 : null,
            null
         );
         boolean var6 = var2.method_445() >= (double)EntityUtil.method_2((net.minecraft.world.entity.Entity)var2.getPlayer());
         double var7 = (double)ExplosionDamage.method_2(
            Vec3.atCenterOf(var1),
            var2.getPlayer(),
            var2.getPlayer().getBoundingBox(),
            5.0,
            true,
            this.method_176(var1) ? var1 : null,
            null
         );
         boolean var9 = !var6 && var4 >= (double)this.maxSelfDamage.getValue().floatValue()
            || var4 >= (double)EntityUtil.method_76() && this.antiSuicide.getValue();
         if (var3 && !var9) {
            return false;
         } else {
            return !((double)this.minDamage.getValue().floatValue() > var7) && !(var7 < Class_0245.field_688) ? var9 : true;
         }
      }
   }

   public boolean method_468(BlockPos var1) {
      List var2 = Class_1172.field_3634.method_38(var1);
      double var3 = (double)(!BlockUtil.method_2(var2) ? this.wallRange.getValue() : this.range.getValue()).floatValue();
      if (!this.method_2(var1, this.method_270(var1)) && !(mc.player.getEyePosition().distanceTo(var1.getCenter()) > var3)) {
         return mc.level.getBlockState(var1).isAir()
               && (this.method_82(var1) == null || InteractionUtil.method_2(var1, this.strictDirection.getValue(), this.wallRange.getValue() <= 0.0F) == null)
               && !this.airPlace.getValue()
            ? true
            : !InteractionUtil.method_2(var1, Blocks.RESPAWN_ANCHOR, true, false) && !this.method_176(var1);
      } else {
         return true;
      }
   }

   public boolean method_176(BlockPos var1) {
      return mc.level.getBlockState(var1).is(Blocks.RESPAWN_ANCHOR);
   }

   public boolean method_639() {
      if (this.method_535()) {
         return true;
      } else if (mc.player.isSleeping()) {
         return true;
      } else if (mc.player.isUsingItem() && this.eating.getValue()) {
         return true;
      } else if (mc.gameMode.isDestroying() && this.mining.getValue()) {
         return true;
      } else {
         return field_3766.isToggled()
               && field_3766.field_4122.get() != null
               && this.field_3793 != null
               && field_3766.field_4122.get().method_445() > this.field_3793.method_445()
            ? true
            : EntityUtil.method_76() < this.health.getValue();
      }
   }
}
