package me.mioclient.module.abstract_;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.enum_.Class_1054;
import me.mioclient.enum_.Class_1172;
import me.mioclient.enum_.PreType;
import me.mioclient.enum_.Priority;
import me.mioclient.event.FinishUsingItemEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.EntityMoveEvent;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.LockUtil;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.ChatUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.combat.OffhandModule;
import me.mioclient.module.movement.NoSlowModule;
import me.mioclient.module.player.SpeedMineModule;
import me.mioclient.record.Rotation;
import me.mioclient.record.RenderStyle;
import me.mioclient.record.RotationResult;
import me.mioclient.setting.BooleanSetting;
import me.mioclient.setting.ColorSetting;
import me.mioclient.setting.CustomSetting3;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractModule_32 extends Module {
   public static final AbstractModule_28 field_4245 = Hub.field_2595.method_78(AbstractModule_28.class);
   public static final AbstractModule_2 field_4246 = Hub.field_2595.method_78(AbstractModule_2.class);
   public static final NoSlowModule noslow = Hub.field_2595.method_78(NoSlowModule.class);
   public final Setting<Integer> delay = this.add(new CustomSetting3<>("Delay", 50, 0, 250).method_22("ms"));
   public final Setting<Integer> bPT = this.add(new CustomSetting3<>("BPT", 8, 1, 8));
   public final Setting<Boolean> airPlace = this.add(new BooleanSetting("AirPlace", false));
   public final Setting<Boolean> strictDirection = this.add(new BooleanSetting("StrictDirection", false));
   public final Setting<Boolean> raytrace = this.add(new BooleanSetting("Raytrace", false));
   public final Setting<Boolean> rotate = this.add(new BooleanSetting("Rotate", false));
   public final Setting<Boolean> attack = this.add(new BooleanSetting("Attack", true));
   public final Setting<Boolean> autoDisable = this.add(new BooleanSetting("AutoDisable", false).method_220());
   public final Setting<Boolean> jump = this.add(new BooleanSetting("Jump", true).method_2(this.autoDisable));
   public final Setting<Boolean> chorus = this.add(new BooleanSetting("Chorus", false).method_2(this.autoDisable));
   public final Setting<Boolean> pearl = this.add(new BooleanSetting("Pearl", false).method_2(this.autoDisable));
   public final Setting<Boolean> rubberBand = this.add(new BooleanSetting("RubberBand", false).method_2(this.autoDisable));
   public final Setting<Boolean> render = this.add(new BooleanSetting("Render", false).method_460());
   public final Setting<Color> fill = this.add(new ColorSetting("Fill", new Color(189, 153, 255, 8), var1x -> this.render.method_194()));
   public final Setting<Color> outline = this.add(new ColorSetting("Outline", new Color(189, 153, 255, 91), var1x -> this.render.method_194()));
   public final Setting<Float> lineWidth = this.add(
      new CustomSetting3<>("LineWidth", 1F, 0.1F, 3F)
         .method_2(this.render)
   );
   public final Setting<Boolean> fade = this.add(new BooleanSetting("Fade", true).method_2(this.render));
   public final Setting<Float> fadeTime = this.add(
      new CustomSetting3<>("FadeTime", 1F, 0.1F, 1F)
         .method_22("s")
         .method_2(this.render, this.fade)
   );
   public final Timer field_3756 = new Timer();
   public final Timer field_4265 = new Timer();
   public boolean field_4266;
   public Vec3 field_4267;
   public Direction field_4138;
   public Entity field_4239;
   public int field_4268;
   public int field_4269;
   public double field_609;
   public boolean field_4270;
   public boolean field_4271;
   public boolean field_4272;
   public boolean field_4273;
   public boolean field_4274;

   public AbstractModule_32(String var1, String var2, Category var3, String... var4) {
      super(var1, var2, var3, var4);
      Hub.field_2616.method_2(new RenderStyle(this, this.fill, this.outline, this.lineWidth, this.fadeTime, () -> true, this.fade, 450));
   }

   public AbstractModule_32(String var1, Category var2, String... var3) {
      this(var1, "", var2, var3);
   }

   @Override
   public void onEnable() {
      if (!this.method_535()) {
         this.field_609 = mc.player.getY();
      }

      this.field_4268 = 0;
      this.field_4269 = 0;
      this.field_4274 = false;
   }

   @Override
   public <T> Setting<T> add(Setting<T> var1) {
      if (!this.getRegistry().contains(this.fadeTime)) {
         return super.add(var1);
      } else {
         this.getRegistry().add(this.getRegistry().indexOf(this.attack), var1);
         return var1;
      }
   }

   public abstract List<BlockPos> method_17();

   public boolean method_2(BlockPos var1) {
      return true;
   }

   public void method_2(BlockPos var1, boolean var2) {
   }

   public boolean method_37() {
      return true;
   }

   public void method_33() {
      this.field_4266 = false;
      if (this.field_4274) {
         this.field_4274 = false;
      } else if (!OffhandModule.method_639() && !SpeedMineModule.method_1124()) {
         if (!mc.player.isSpectator() && mc.player.isAlive() && !mc.player.isHandsBusy() && !mc.player.isSleeping()) {
            if (!this.method_107()) {
               if (!this.field_4271 || !noslow.method_573()) {
                  if (this.field_3756.method_9((long)this.delay.getValue().intValue()) && this.method_37()) {
                     List var1 = this.method_17();
                     int var2 = this.method_34();
                     synchronized (var1) {
                        this.method_2(var2, var1);
                        this.method_2(var1);
                        if (!var1.isEmpty()) {
                           LockUtil.method_7(() -> this.method_9(var1));
                        }
                     }

                     this.field_4268 = 0;
                  }
               }
            }
         }
      }
   }

   public void method_2(List<BlockPos> var1) {
      if (this.bPT.getValue() > 1) {
         var1.sort(Comparator.comparing(var1x -> InteractionUtil.method_9(var1x, this.strictDirection.getValue()) != null ? -1 : 1));
      }
   }

   @Subscribe(
      method_800 = 100
   )
   public void method_2(Event_7 var1) {
      this.field_4267 = null;
      this.field_4239 = null;
      this.method_33();
   }

   @Subscribe
   public void method_2(EntityMoveEvent var1) {
      if (var1.method_213() == PreType.POST && (double)var1.method_575() > Class_0245.field_688 && this.jump.getValue()) {
         this.disable();
      }
   }

   @Subscribe
   public void method_2(FinishUsingItemEvent var1) {
      if (var1.method_602().getItem() == Items.CHORUS_FRUIT && !mc.player.isShiftKeyDown() && !field_4246.isToggled()) {
         if (this.chorus.getValue()) {
            this.disable();
         } else {
            this.field_4274 = true;
         }
      }
   }

   @Subscribe
   public void method_2(PacketReceiveEvent var1) {
      if (this.attack.getValue() && var1.method_127() instanceof ClientboundAddEntityPacket var2 && var2.getType() == EntityType.END_CRYSTAL) {
         if (OffhandModule.method_639()) {
            return;
         }

         BlockPos var9 = BlockPos.containing(var2.getX(), var2.getY(), var2.getZ());
         List var4 = this.method_17();
         this.method_2(this.method_34(), var4);

         for (BlockPos var6 : (Iterable<BlockPos>)(Iterable<?>)(var4)) {
            if (var6.closerThan(var9, 2.0)) {
               if (!Hub.field_2623.method_5(var2.getId(), true)) {
                  return;
               }

               PacketSender.method_9(InteractionHand.MAIN_HAND);
               break;
            }
         }

         LockUtil.method_7(() -> this.method_9(var4));
      }

      if (var1.method_127() instanceof ClientboundPlayerPositionPacket var7) {
         this.field_609 = var7.change().position().y();
         if (mc.player.position().distanceToSqr(var7.change().position().x(), var7.change().position().y(), var7.change().position().z()) >= 1.0
            && this.rubberBand.getValue()) {
            this.disable();
            return;
         }
      }

      if (var1.method_127() instanceof ClientboundRespawnPacket && this.field_4270) {
         this.disable();
      }

      if (var1.method_127() instanceof ClientboundOpenScreenPacket var8 && !this.field_3756.method_9(100L)) {
         PacketSender.method_2(new ServerboundContainerClosePacket(var8.getContainerId()));
         var1.method_463();
      }
   }

   public void method_9(List<BlockPos> var1) {
      if (!var1.isEmpty()) {
         for (BlockPos var3 : var1) {
            if (this.attack.getValue() && this.field_4265.method_9((long)Hub.field_2623.method_878())) {
               this.field_4239 = InteractionUtil.method_2(var3, Hub.field_2623.method_523());
               if (this.field_4239 != null) {
                  if (this.rotate.getValue()) {
                     float[] var4 = RotationManager.method_14(this.field_4239);
                     Hub.field_2598.method_2(RotationManager.method_2(var4, field_4245.method_1051()), this.method_52() + 1);
                  }

                  this.field_4265.reset();
                  break;
               }
            }
         }

         int var10 = this.method_34();
         int var11 = mc.player.getInventory().selected;
         Rotation var12 = null;

         for (BlockPos var6 : var1) {
            if (this.field_4269 >= 30) {
               this.field_4269 = 0;
               ChatUtil.method_2(Component.literal(this.getName()).append(" is out of blocks!"), ChatUtil.method_38(-2), Priority.HIGH);
               this.field_4268 = 1337;
               this.disable();
               break;
            }

            if (var10 == -1 || mc.player.getInventory().getItem(var10).isEmpty()) {
               this.field_4269++;
               break;
            }

            if (this.field_4268 >= this.bPT.getValue()) {
               break;
            }

            RotationResult var7 = this.method_9(var6);
            if (var7.method_114()) {
               this.field_4269 = 0;
            }

            if (var7.method_114() && var6 != var1.getLast() && this.method_35()) {
               Rotation var8 = var7.method_209();
               if (var12 == null || !var12.method_4(var6)) {
                  if (var6 == var1.getFirst()) {
                     Rotation var9 = new Rotation(new float[]{mc.player.getYRot(), mc.player.getXRot()});
                     if (var9.method_4(var6)) {
                        continue;
                     }
                  }

                  PacketSender.method_2(
                     mc.player.getX(),
                     mc.player.getY(),
                     mc.player.getZ(),
                     var8.method_221()[0],
                     var8.method_221()[1],
                     mc.player.onGround()
                  );
                  var12 = var8;
               }
            }
         }

         if (!this.field_4273) {
            this.field_4273 = true;
            Hub.field_2619.method_2(() -> this.field_4273 = false, 1);
         }

         if (this.field_4272 && this.method_374()) {
            PacketSender.method_1100();
         }

         InventoryUtil.method_16(var11);
         this.field_4272 = false;
      }
   }

   public RotationResult method_9(BlockPos var1) {
      if (OffhandModule.method_639()) {
         return RotationResult.NONE;
      } else {
         this.field_4138 = InteractionUtil.method_2(var1, this.strictDirection.getValue(), this.raytrace.getValue());
         if (this.field_4138 == null && !this.airPlace.getValue()) {
            return RotationResult.NONE;
         } else {
            float[] var2 = RotationManager.method_2(var1.getCenter(), this.field_4138);
            boolean var3 = this.raytrace.getValue();
            if (this.field_4138 != null) {
               var3 &= !BlockUtil.method_2(Class_1172.field_3634.method_2(var1.relative(this.field_4138), this.field_4138.getOpposite()));
            } else {
               var3 &= !BlockUtil.method_2(var1);
            }

            if (!this.method_2(var1)) {
               return RotationResult.NONE;
            } else {
               boolean var4 = true;
               if (!this.field_4272) {
                  InventoryUtil.method_16(this.method_34());
                  if (this.method_374()) {
                     PacketSender.method_1100();
                  }

                  this.field_4272 = true;
               }

               if (!var3 && this.method_5(var1)) {
                  if (RotationManager.method_513() && field_4245.method_1052()) {
                     var2[0] = (float)((double)var2[0] + Math.random() * 0.009999999776482582);
                     var2[1] = (float)((double)var2[1] + Math.random() * 0.009999999776482582);
                  }

                  this.field_3756.reset();
                  if (this.render.getValue()) {
                     Hub.field_2616.method_2(this, var1);
                  }

                  this.field_4267 = var1.getCenter();
                  if (this.field_4138 != null) {
                     this.field_4267 = this.field_4267.relative(this.field_4138, Class_0245.field_688);
                  }

                  if (this.rotate.getValue()) {
                     Hub.field_2598.method_2(RotationManager.method_2(var2, field_4245.method_1051()), this.method_52());
                  }

                  this.field_4268++;
                  var4 = false;
               }

               this.method_2(var1, var4);
               return new RotationResult(!var4, new Rotation(var2));
            }
         }
      }
   }

   public boolean method_5(BlockPos var1) {
      return InteractionUtil.method_2(var1, this.field_4138, this.airPlace.getValue(), this.method_374() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
   }

   public int method_34() {
      return InventoryUtil.method_5(Items.OBSIDIAN) == -1
         ? InventoryUtil.method_7(
            (Predicate<ItemStack>)(var0 -> {
               if (var0.getItem() instanceof BlockItem var1
                  && var1.getBlock() != Blocks.ANVIL
                  && var1.getBlock().getExplosionResistance() >= 6e+02F) {
                  return true;
               }

               return false;
            })
         )
         : InventoryUtil.method_5(Items.OBSIDIAN);
   }

   public void method_7(BlockPos var1) {
      if (!mc.level.getBlockState(var1).canBeReplaced() && mc.level.getBlockState(var1).getDestroySpeed(mc.level, var1) == 0.0F) {
         mc.player.connection.send(new ServerboundPlayerActionPacket(Action.START_DESTROY_BLOCK, var1, Direction.DOWN));
         mc.player.connection.send(new ServerboundPlayerActionPacket(Action.STOP_DESTROY_BLOCK, var1, Direction.DOWN));
      }
   }

   public boolean method_35() {
      return field_4245.movementSync.getValue() && field_4245.rotations.getValue() == Class_1054.SILENT
         ? false
         : this.rotate.getValue()
            && this.bPT.getValue() > 1
            && mc.player.onGround()
            && Hub.field_2615.method_1161() > 1
            && !this.field_4273;
   }

   public boolean method_107() {
      double var1 = mc.player.getY() - this.field_609;
      if (mc.player.onGround()) {
         this.field_609 = mc.player.getY();
      }

      if ((
            !(mc.player.getY() > this.field_609 + 0.1)
               || !(var1 >= 0.4)
               || !this.jump.getValue()
         )
         && !mc.player.isSleeping()) {
         return false;
      } else {
         this.method_38(false);
         return true;
      }
   }

   public void method_2(int var1, List<BlockPos> var2) {
      ArrayList var3 = new ArrayList();
      Block var4 = null;
      if (var1 != -1 && mc.player.getInventory().getItem(var1).getItem() instanceof BlockItem var5) {
         var4 = var5.getBlock();
      }

      for (BlockPos var11 : var2) {
         double var7 = mc.player.getEyePosition().distanceToSqr((double)var11.getX(), (double)var11.getY(), (double)var11.getZ());
         if (!InteractionUtil.method_2(var11, var4, true, this.attack.getValue()) || !(var7 <= 36.0)) {
            var3.add(var11);
         } else if (InteractionUtil.method_78(var11) == null) {
            this.field_4266 = true;
         }
      }

      for (BlockPos var12 : (Iterable<BlockPos>)(Iterable<?>)(var3)) {
         var2.remove(var12);
      }
   }

   public boolean method_374() {
      return field_4245.method_1052() && RotationManager.method_513() && this.airPlace.getValue() && this.field_4266;
   }

   public BlockPos method_425() {
      return PlayerUtil.method_425();
   }

   public int method_52() {
      return 20;
   }

   public void method_5(boolean var1) {
      this.field_4270 = var1;
   }

   public void method_185() {
      this.field_4271 = true;
   }
}
