package me.mioclient.module.combat;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.enum_.SelfFillMode;
import me.mioclient.enum_.PreType;
import me.mioclient.enum_.Priority;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.LockUtil;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BoxUtil;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.ChatUtil;
import me.mioclient.mixin.ducks.DuckAbstractBlock;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.PosRot;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import me.mioclient.internal.Inputs;

public class SelfFillModule extends Module {
   public Setting<SelfFillMode> mode;
   public Setting<Boolean> rotate;
   public Setting<Boolean> persistent;
   public Setting<Boolean> jumpDisable;
   public Setting<Set<Block>> whitelist;
   public Setting<Boolean> attack;
   public Setting<Boolean> noLagBack;
   public Entity field_4239;
   public boolean field_3965;
   public double field_609;
   public final List<Double> field_4240;

   public SelfFillModule() {
      super("SelfFill", "Places solid blocks inside you blocking your lower hitbox.", Category.COMBAT, "burrow");
      Settings.initialize(this);
      this.field_4240 = List.of(
         0.4,
         0.75,
         1.01,
         1.15
      );
   }

   @Override
   public void onEnable() {
      if (this.method_535()) {
         this.disable();
      } else {
         this.field_609 = mc.player.getY();
         this.field_3965 = false;
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (mc.player.getY() > this.field_609 && this.persistent.getValue() && this.jumpDisable.getValue()) {
         this.method_68();
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundPlayerPositionPacket var2
         && mc.player.position().distanceToSqr(new Vec3(var2.change().position().x(), var2.change().position().y(), var2.change().position().z())) > 9.0) {
         this.field_609 = var2.change().position().y();
      }
   }

   @Subscribe
   public void method_9(Event_19 var1) {
      if (var1.method_213() != PreType.POST) {
         if (!mc.player.isPassenger()) {
            int var2 = 0;

            for (int var3 = -8; var3 < 8; var3++) {
               if (var3 != 0 && var3 != 1) {
                  BlockPos var4 = BlockPos.containing(mc.player.position()).offset(0, var3, 0);
                  if (mc.level.getBlockState(var4).getCollisionShape(mc.level, var4).isEmpty()) {
                     var2 = var3;
                     break;
                  }
               }
            }

            BlockPos var12 = PlayerUtil.method_425();
            if (mc.level.getEntitiesOfClass(Player.class, new AABB(var12), var0 -> var0 != mc.player).isEmpty()) {
               BlockState var13 = mc.level.getBlockState(var12);
               AABB var5 = mc.player.getBoundingBox().inflate(-BoxUtil.field_2280);
               boolean var6 = BlockPos.betweenClosedStream(var5.setMaxY(var5.minY)).count() != 1L;
               if (!var6 || !MovementUtil.method_363()) {
                  if (var13.canBeReplaced() && !var13.liquid() && mc.player.onGround() && var2 != 0) {
                     if (this.attack.getValue()) {
                        this.field_4239 = InteractionUtil.method_2(var12, 0);
                     }

                     if (this.field_4239 == null) {
                        int var7 = InventoryUtil.method_7((Predicate<ItemStack>)(var1x -> {
                           if (var1x.getItem() instanceof BlockItem var2x && this.mode.getValue().method_2(var2x.getBlock(), this.whitelist)) {
                              return true;
                           }

                           return false;
                        }));
                        int var8 = mc.player.getInventory().selected;
                        if (var7 == -1) {
                           ChatUtil.method_2(Component.literal(this.getName()).append(" is out of blocks!"), ChatUtil.method_38(-2), Priority.HIGH);
                           this.disable();
                        } else {
                           Direction var9 = InteractionUtil.method_78(var12);
                           if (var9 != null) {
                              float[] var10 = new float[]{var1.method_500(), (float)Class_0245.field_685};
                              if (this.field_4239 != null && this.rotate.getValue()) {
                                 var10 = RotationManager.method_14(this.field_4239);
                              }

                              int var11 = var2;
                              LockUtil.method_7(() -> this.method_2(var12, var9, var8, var7, var11));
                              if (this.rotate.getValue()) {
                                 Hub.field_2598.method_2(var10, 101);
                              }

                              if (this.field_3965 && !this.persistent.getValue()) {
                                 this.field_3965 = false;
                                 this.disable();
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void method_2(BlockPos var1, Direction var2, int var3, int var4, int var5) {
      boolean var6 = true;

      for (int var7 = 1; var7 < 3; var7++) {
         if (!mc.level.getBlockState(var1.above(var7)).canBeReplaced()) {
            var6 = false;
            break;
         }
      }

      if (var6) {
         mc.player.setShiftKeyDown(false);
         mc.player.connection.send(Inputs.shift(true));
         InventoryUtil.method_16(var4);
         Item var13 = mc.player.getMainHandItem().getItem();
         Block var8 = ((BlockItem)var13).getBlock();
         boolean var9 = ((DuckAbstractBlock)(Object)var8).isCollidable();
         if (var9) {
            for (double var11 : this.field_4240) {
               mc.player
                  .connection
                  .send(
                     new PosRot(
                        mc.player.getX(),
                        mc.player.getY() + var11,
                        mc.player.getZ(),
                        mc.player.getYRot(),
                        (float)Class_0245.field_685,
                        false
                     , false)
                  );
            }
         }

         AABB var14 = mc.player.getBoundingBox();
         mc.player.setBoundingBox(new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
         if (this.noLagBack.getValue()) {
            InteractionUtil.method_2(var1, null, var2, false, InteractionHand.MAIN_HAND);
         } else {
            InteractionUtil.method_2(var1, var2, false, InteractionHand.MAIN_HAND);
         }

         mc.player.setBoundingBox(var14);
         if (var9) {
            this.method_38((double)var5);
         }

         InventoryUtil.method_16(var3);
         mc.player.connection.send(Inputs.shift(false));
         this.field_3965 = true;
      }
   }

   public void method_38(double var1) {
      if (this.noLagBack.getValue()) {
         mc.player
            .connection
            .send(
               new PosRot(
                  mc.player.getX(),
                  mc.player.getY() + 1.0,
                  mc.player.getZ(),
                  mc.player.getYRot(),
                  (float)Class_0245.field_685,
                  true
               , false)
            );
         Hub.field_2619
            .method_2(
               () -> mc.player
                     .setPos(mc.player.getX(), mc.player.getY() + 1.0, mc.player.getZ()),
               1
            );
      } else {
         mc.player
            .connection
            .send(
               new PosRot(
                  mc.player.getX(),
                  mc.player.getY() + var1,
                  mc.player.getZ(),
                  mc.player.getYRot(),
                  (float)Class_0245.field_685,
                  false
               , false)
            );
      }
   }
}
