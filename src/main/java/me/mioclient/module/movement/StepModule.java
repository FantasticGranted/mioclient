package me.mioclient.module.movement;

import java.util.ArrayList;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.enum_.StepMode;
import me.mioclient.enum_.PreType;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.EntityMoveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.FontRenderer;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos;
import me.mioclient.nick.Settings;

public class StepModule extends Module {
   public static final HoleSnapModule field_2119 = Hub.field_2595.method_78(HoleSnapModule.class);
   public static AbstractModule_28 field_144 = Hub.field_2595.method_78(AbstractModule_28.class);
   public Setting<StepMode> mode;
   public Setting<Float> height;
   public Setting<Boolean> safeDisable;
   public Setting<Boolean> useTimer;
   public Setting<Float> timerSpeed;
   public boolean field_2125;
   public int field_2126;

   public StepModule() {
      super("Step", "Allows you to step up blocks without jumping.", Category.MOVEMENT);
      Settings.initialize(this);
      this.field_2126 = 0;
   }

   @Override
   public void onDisable() {
      this.field_2125 = false;
      this.field_2126 = 0;
      if (!this.method_535()) {
         EntityUtil.method_9(mc.player, 0.6F);
      }
   }

   @Override
   public String getInfo() {
      return FontRenderer.method_3(this.mode.getValue());
   }

   @Subscribe(
      method_800 = -100
   )
   public void method_2(PostTickEvent var1) {
      if (this.field_2126 > 0 && Hub.field_2605.method_221(PlayerUtil.method_425()) && this.safeDisable.getValue()) {
         this.disable();
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      this.field_2125 = false;
   }

   @Subscribe
   public void method_2(EntityMoveEvent var1) {
      if (!field_2119.method_405()) {
         if (mc.player.onGround() && !(mc.player.fallDistance > 0.0F) && mc.player.verticalCollision) {
            if (var1.method_213() == PreType.PRE) {
               var1.method_34(this.height.getValue());
            }

            if (var1.method_213() == PreType.POST) {
               if ((double)var1.method_575() <= 0.6) {
                  Hub.field_2596.method_38(this);
                  return;
               }

               if (this.mode.getValue() == StepMode.VANILLA) {
                  this.field_2126++;
                  return;
               }

               ArrayList var2 = new ArrayList();
               if (this.useTimer.getValue() && !PlayerUtil.method_5(mc.player)) {
                  Hub.field_2596.method_2(this, this.timerSpeed.getValue());
                  this.field_2125 = true;
               }

               this.field_2126++;
               if (var1.method_575() <= 1F) {
                  var2.addAll(
                     List.of(
                        0.42 * (double)var1.method_575(),
                        0.75 * (double)var1.method_575()
                     )
                  );
               } else if ((double)var1.method_575() <= 1.5) {
                  var2.addAll(
                     List.of(
                        0.42,
                        0.75,
                        1.0,
                        1.16,
                        1.23,
                        1.2
                     )
                  );
               } else if (var1.method_575() <= this.height.getValue()) {
                  var2.addAll(
                     List.of(
                        0.42,
                        0.78,
                        0.63,
                        0.51,
                        0.9,
                        1.21,
                        1.45,
                        1.43
                     )
                  );
               }

               if (!var2.isEmpty()) {
                  for (Double var4 : (Iterable<Double>)(Iterable<?>)(var2)) {
                     mc.player
                        .connection
                        .send(
                           new Pos(
                              mc.player.getX(), mc.player.getY() + var4, mc.player.getZ(), mc.player.onGround()
                           , false)
                        );
                  }
               }
            }
         } else {
            Hub.field_2596.method_38(this);
         }
      }
   }

   public boolean method_666() {
      return this.field_2125;
   }
}
