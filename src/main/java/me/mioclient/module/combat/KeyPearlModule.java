package me.mioclient.module.combat;

import me.mioclient.Hub;
import me.mioclient.enum_.PreType;
import me.mioclient.event.Event_19;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.movement.ElytraFlyModule;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.PosRot;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult.Type;
import me.mioclient.nick.Settings;

public class KeyPearlModule extends Module {
   public static final ElytraFlyModule elytrafly = Hub.field_2595.method_78(ElytraFlyModule.class);
   public Setting<Boolean> noEntityTrace;
   public Setting<Boolean> solid;
   public Setting<Boolean> disableOnElytra;
   public Setting<Boolean> disableOnRiding;

   public KeyPearlModule() {
      super("KeyPearl", "Throws an ender pearl.", Category.COMBAT);
      Settings.initialize(this);
      this.setDrawn(false);
   }

   @Override
   public void onEnable() {
      if (this.method_535()) {
         this.disable();
      } else {
         boolean var1 = elytrafly.isToggled() && this.disableOnElytra.getValue() && !mc.player.onGround() && PlayerUtil.method_4(mc.player);
         if (mc.player.isPassenger() && this.disableOnRiding.getValue()) {
            var1 = true;
         }

         if (var1) {
            this.disable();
         }
      }
   }

   @Subscribe
   public void method_9(Event_19 var1) {
      if (var1.method_213() == PreType.PRE) {
         int var2 = InventoryUtil.method_5(Items.ENDER_PEARL);
         int var3 = InventoryUtil.method_9(Items.ENDER_PEARL);
         int var4 = mc.player.getInventory().selected;
         boolean var5 = mc.hitResult instanceof EntityHitResult && this.noEntityTrace.getValue();
         if (var3 != -1 && !var5 && !mc.player.isFallFlying() && !elytrafly.method_1183() && !mc.player.isPassenger()) {
            if (mc.hitResult instanceof BlockHitResult var6
               && !mc.level.getBlockState(var6.getBlockPos()).canBeReplaced()
               && var6.getType() != Type.MISS
               && !this.solid.getValue()) {
               this.disable();
               return;
            }

            if (!OffhandModule.method_639()) {
               boolean var8 = var2 == -1;
               this.method_7(var8 ? var3 : var2, var8);
               mc.player
                  .connection
                  .send(
                     new PosRot(
                        mc.player.getX(),
                        mc.player.getY(),
                        mc.player.getZ(),
                        mc.player.getYRot(),
                        mc.player.getXRot(),
                        mc.player.onGround()
                     , false)
                  );
               PacketSender.method_2(InteractionHand.MAIN_HAND);
               PacketSender.method_9(InteractionHand.MAIN_HAND);
               this.method_7(var8 ? var3 : var4, var8);
               Hub.field_2599.method_867().stream().filter(var0 -> var0.pearl.getValue()).forEach(Module::disable);
               this.disable();
            }
         } else {
            this.disable();
         }
      }
   }

   public void method_7(int var1, boolean var2) {
      if (var2) {
         InventoryUtil.method_39(var1);
      } else {
         InventoryUtil.method_16(var1);
      }
   }
}
