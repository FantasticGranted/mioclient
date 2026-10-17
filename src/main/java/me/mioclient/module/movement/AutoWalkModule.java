package me.mioclient.module.movement;

import me.mioclient.Hub;
import me.mioclient.enum_.Priority;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.MovementInputEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.SoundId;
import me.mioclient.internal.ChatUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.network.chat.Component;
import me.mioclient.nick.Settings;
import net.minecraft.world.phys.Vec2;
import me.mioclient.internal.Inputs;

public class AutoWalkModule extends Module {
   public Setting<Boolean> autoJump;
   public Setting<Boolean> stuckNotify;
   public Setting<SoundId> sound;
   public Setting<Float> volume;
   public static boolean field_264;
   public boolean field_265;

   public AutoWalkModule() {
      super("AutoWalk", "Lets your forward key rest.", Category.MOVEMENT);
      Settings.initialize(this);
      this.field_265 = true;
   }

   @Override
   public void onDisable() {
      field_264 = true;
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (Hub.field_2614.method_874() == 0.0) {
         if (!this.field_265 && this.stuckNotify.getValue()) {
            ChatUtil.method_2(Component.literal("You are stuck!"), ChatUtil.method_2(this), Priority.HIGH);
            Hub.field_2606.method_2(this.sound.getValue()).method_230(this.volume.getValue());
         }

         this.field_265 = true;
      } else {
         this.field_265 = false;
      }
   }

   @Subscribe(
      method_800 = -100
   )
   public void method_5(MovementInputEvent var1) {
      var1.method_276().moveVector = new Vec2(var1.method_276().moveVector.x, var1.method_278() ? var1.method_277() : 1F);
      Inputs.setForward(var1.method_276(), true);
      if (this.autoJump.getValue()) {
         Inputs.setJump(var1.method_276(), true);
      }
   }

   @Subscribe(
      method_800 = -100
   )
   public static void method_7(MovementInputEvent var0) {
      if (field_264) {
         var0.method_276().moveVector = new Vec2(var0.method_276().moveVector.x, 0.0F);
         Inputs.setForward(var0.method_276(), false);
         Inputs.setJump(var0.method_276(), false);
         field_264 = false;
      }
   }

   static {
      lookups.method_5(AutoWalkModule.class);
   }
}
