package me.mioclient.module.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import me.mioclient.enum_.PreType;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.MovementInputEvent;
import me.mioclient.event.MouseButtonEvent;
import me.mioclient.event.ChatMessageEvent;
import me.mioclient.event.MouseUpdateEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.ChatUtil;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.mixin.ducks.DuckMinecraftClient;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.world.InteractionHand;
import me.mioclient.nick.Settings;
import me.mioclient.internal.Inputs;

public class AntiAFKModule extends Module {
   public Setting<Float> delay;
   public Setting<Boolean> autoReply;
   public Setting<Boolean> jump;
   public Setting<Boolean> sneak;
   public Setting<Boolean> rotate;
   public Setting<Boolean> attack;
   public Setting<Boolean> onlySwing;
   public final Timer field_3239;
   public final Timer field_3240;
   public boolean field_3241;
   public boolean field_3242;

   public AntiAFKModule() {
      super("AntiAFK", "Prevents you from being kicked for AFK-ing.", Category.MISC);
      Settings.initialize(this);
      this.field_3239 = new Timer();
      this.field_3240 = new Timer();
   }

   @Override
   public void onEnable() {
      this.field_3239.reset();
      this.field_3240.reset();
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (this.method_942()) {
         if (mc.options.keyJump.isDown() && this.jump.getValue()) {
            mc.options.keyJump.setDown(false);
         }

         if (this.field_3240.method_2((double)this.delay.getValue().floatValue(), TimeUnit.SECONDS)) {
            List var2 = this.method_941();
            Collections.shuffle(var2);
            if (!var2.isEmpty()) {
               ((Runnable)var2.get(0)).run();
            }

            this.field_3240.reset();
         }
      }
   }

   @Subscribe(
      method_800 = 100
   )
   public void method_9(ChatMessageEvent var1) {
      if (this.autoReply.getValue()
         && this.method_942()
         && var1.method_213() == PreType.PRE
         && var1.method_1033() != null
         && ChatUtil.method_107(var1.method_1033().getString())) {
         mc.executeIfPossible(
            () -> mc.player
                  .connection
                  .sendCommand(
                     new PlaceholderText()
                        .method_2((int)Math.floor(Math.random() * 1e+04))
                        .method_2(mc.player.getName().getString())
                        .method_9("r Hello! This is auto reply talking. \u0001 is currently AFK :'). [\u0001]")
                  )
         );
      }
   }

   @Subscribe
   public void method_2(MouseUpdateEvent var1) {
      this.field_3239.reset();
   }

   @Subscribe
   public void method_9(MouseButtonEvent var1) {
      this.field_3239.reset();
   }

   @Subscribe
   public void method_9(MovementInputEvent var1) {
      if (this.field_3241) {
         Inputs.setJump(var1.method_276(), true);
      }

      if (this.field_3242) {
         Inputs.setShift(var1.method_276(), true);
      }

      this.field_3241 = this.field_3242 = false;
   }

   public List<Runnable> method_941() {
      ArrayList<Runnable> var1 = new ArrayList<>();
      if (this.jump.getValue()) {
         var1.add(() -> this.field_3241 = true);
      }

      if (this.sneak.getValue()) {
         var1.add(() -> this.field_3242 = true);
      }

      if (this.rotate.getValue()) {
         Random var2 = new Random();
         var1.add(() -> {
            mc.player.setYRot(var2.nextFloat((float)Class_0245.field_686));
            mc.player.setXRot(var2.nextFloat(1.8e+02F) - (float)Class_0245.field_685);
         });
      }

      if (this.attack.getValue()) {
         var1.add(() -> {
            if (this.onlySwing.getValue()) {
               mc.player.swing(InteractionHand.MAIN_HAND);
            } else {
               ((DuckMinecraftClient)(Object)mc).attack();
            }
         });
      }

      return var1;
   }

   public boolean method_942() {
      return this.field_3239.method_9((long)(this.delay.getValue() * 1e+03F));
   }
}
