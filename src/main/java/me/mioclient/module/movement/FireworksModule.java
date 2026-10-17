package me.mioclient.module.movement;

import java.util.concurrent.TimeUnit;
import me.mioclient.Hub;
import me.mioclient.enum_.SettingMode;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.InteractBlockEvent;
import me.mioclient.event.SoundPlayEvent;
import me.mioclient.event.UseItemEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.mixin.ducks.DuckFireworkEntity;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.exploit.RocketModule;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import me.mioclient.internal.Inputs;

public final class FireworksModule extends Module {
   public static final RocketModule field_2675 = Hub.field_2595.method_78(RocketModule.class);
   public static final ElytraFlyModule field_2676 = Hub.field_2595.method_78(ElytraFlyModule.class);
   public Setting<Boolean> antiWaste;
   public Setting<Boolean> boost;
   public Setting<Boolean> withElytraFly;
   public Setting<Float> boostFactor;
   public Setting<Boolean> autoLaunch;
   public Setting<Boolean> onlyElytraFly;
   public Setting<Boolean> onlyFirst;
   public Setting<Boolean> await;
   public Setting<Float> delay;
   public Setting<Boolean> fastLaunch;
   public Setting<Integer> ticks;
   public Setting<Boolean> flight;
   public Setting<Float> horizontal;
   public Setting<Float> vertical;
   public final Timer field_2691;
   public boolean field_2692;
   public int field_2693;
   public int field_2694;
   public boolean field_2695;
   public boolean field_2696;

   public FireworksModule() {
      super("Fireworks", "Enhances the usage of firework rockets.", Category.MOVEMENT, "rockets");
      Settings.initialize(this);
      this.field_2691 = new Timer();
      this.delay.method_2("Auto", SettingMode.MIN);
   }

   @Subscribe
   public void method_9(InteractBlockEvent var1) {
      if (this.antiWaste.getValue()) {
         if (mc.player.getItemInHand(var1.method_12()).is(Items.FIREWORK_ROCKET)) {
            mc.gameMode.useItem(mc.player, var1.method_12());
            mc.player.swing(var1.method_12());
            var1.method_463();
         }
      }
   }

   @Subscribe
   public void method_2(UseItemEvent var1) {
      if (this.method_786()) {
         if (mc.player.isHolding(Items.FIREWORK_ROCKET)) {
            this.field_2694 = 0;
            this.method_787();
            this.field_2693 = this.ticks.getValue();
            this.field_2696 = true;
         }
      }
   }

   @Subscribe
   public void method_2(PostTickEvent var1) {
      if (this.field_2695) {
         mc.player.setShiftKeyDown(false);
         PacketSender.method_2(Inputs.shift(false));
         this.field_2695 = false;
         if (!PlayerUtil.method_4(mc.player)) {
            mc.player.stopFallFlying();
         }
      }

      if (this.field_2693 > 0) {
         this.method_787();
         this.field_2693--;
         this.field_2695 = this.field_2693 == 0;
      }

      if (this.autoLaunch.getValue() && (field_2676.isToggled() || !this.onlyElytraFly.getValue())) {
         if (!mc.player.isFallFlying()) {
            this.field_2691.setTime(-1L);
         }

         boolean var2 = !this.field_2691.method_2((double)this.delay.getValue().floatValue(), TimeUnit.SECONDS)
            || field_2675.isToggled() && field_2675.field_3473 != -1 && field_2675.field_3534 != null;
         if (this.method_784()) {
            var2 = true;
         }

         if (!var2) {
            if (!mc.player.isFallFlying()) {
               this.field_2692 = false;
            }

            if (!this.field_2692 || !this.onlyFirst.getValue()) {
               this.method_783();
            }
         }
      }
   }

   @Subscribe(
      method_800 = -100
   )
   public void method_2(MoveEvent var1) {
      if (this.flight.getValue() && this.field_2693 > 1 && this.method_784()) {
         this.method_5(var1);
      }

      if (mc.player.isFallFlying() && this.await.getValue() && this.autoLaunch.getValue()) {
         if (!this.method_784()) {
            var1.method_4(new Vec3(0.0, 0.0, 0.0));
         }
      }
   }

   @Subscribe
   public void method_2(SoundPlayEvent var1) {
      if (this.field_2693 > 0
         && var1.method_863() != null
         && var1.method_863().getIdentifier() != null
         && var1.method_863().getIdentifier().toString().contains("item.armor.equip")) {
         var1.method_463();
      }
   }

   public void method_5(MoveEvent var1) {
      float var2 = -0.001F;
      if (mc.player.input.keyPresses.jump()) {
         var2 = this.vertical.getValue();
      } else if (mc.player.input.keyPresses.shift()) {
         var2 = -this.vertical.getValue();
      }

      float var3 = this.horizontal.getValue();
      if ((double)var3 > 1.6) {
         var3 = Mth.clamp(
            1.6F + (float)this.field_2694 * 0.1F, 1.6F, var3
         );
         this.field_2694++;
      }

      double[] var4 = MovementUtil.method_2(var1, (double)var3);
      Vec3 var5 = new Vec3(var4[0], (double)var2, var4[1]);
      mc.player.setDeltaMovement(var5);
      var1.method_4(var5);
   }

   public void method_783() {
      if (this.autoLaunch.getValue() && mc.player.isFallFlying()) {
         InteractionHand var1 = InventoryUtil.method_7(Items.FIREWORK_ROCKET);
         int var2 = mc.player.getInventory().selected;
         int var3 = InventoryUtil.method_9(Items.FIREWORK_ROCKET);
         int var4 = InventoryUtil.method_5(Items.FIREWORK_ROCKET);
         if (var1 != null) {
            mc.gameMode.useItem(mc.player, var1);
            this.field_2691.reset();
         } else if (var3 != -1) {
            boolean var5 = var4 == -1;
            this.method_7(var5 ? var3 : var4, var5);
            mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
            field_2675.field_3535 = mc.player.position();
            this.method_7(var5 ? var3 : var2, var5);
            this.field_2691.reset();
         }

         this.field_2692 = true;
      }
   }

   public void method_7(int var1, boolean var2) {
      if (var2) {
         InventoryUtil.method_39(var1);
      } else {
         InventoryUtil.method_16(var1);
      }
   }

   public boolean method_784() {
      if (!this.field_2691.method_9(500L)) {
         return true;
      } else {
         for (Entity var2 : mc.level.entitiesForRendering()) {
            if (var2 instanceof DuckFireworkEntity var3 && var3.mio$getShooter() == mc.player) {
               return true;
            }
         }

         return false;
      }
   }

   public float method_100(boolean var1) {
      return this.isToggled() && this.boost.getValue() && (var1 || this.withElytraFly.getValue()) && this.method_784() ? this.boostFactor.getValue() : 0.0F;
   }

   public int method_785() {
      return this.field_2693;
   }

   public boolean method_786() {
      return PlayerUtil.method_4(mc.player)
         ? false
         : this.isToggled() && this.fastLaunch.getValue() && !mc.player.onGround() && InventoryUtil.method_9(Items.ELYTRA) != -1;
   }

   public void method_787() {
      int var1 = InventoryUtil.method_5(Items.ELYTRA);
      boolean var2 = var1 == -1;
      if (var1 == -1) {
         var1 = InventoryUtil.method_9(Items.ELYTRA);
      }

      if (var1 != -1) {
         int var3 = var1;
         if (var2) {
            var1 = 0;
            mc.gameMode.handleContainerInput(0, var3, 0, ContainerInput.SWAP, mc.player);
         }

         mc.gameMode.handleContainerInput(0, 6, var1, ContainerInput.SWAP, mc.player);
         PacketSender.method_1099();
         mc.player.startFallFlying();
         mc.gameMode.handleContainerInput(0, 6, var1, ContainerInput.SWAP, mc.player);
         if (var2) {
            mc.gameMode.handleContainerInput(0, var3, 0, ContainerInput.SWAP, mc.player);
         }
      }
   }
}
