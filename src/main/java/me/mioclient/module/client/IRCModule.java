package me.mioclient.module.client;

import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.enum_.IrcInfoMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Event_23;
import me.mioclient.event.ChatReceiveEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.SoundId;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.MathUtil;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.Colors;
import me.mioclient.internal.Waypoint;
import me.mioclient.internal.VectorUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class IRCModule extends Module {
   public static final Identifier field_563 = Identifier.fromNamespaceAndPath("mio", "textures/warning.png");
   public Setting<Boolean> capes;
   public Setting<Boolean> gradient;
   public Setting<Boolean> chat;
   public Setting<String> prefix;
   public Setting<Boolean> chatSound;
   public Setting<SoundId> mode;
   public Setting<Float> volume;
   public Setting<Boolean> pings;
   public Setting<IrcInfoMode> info;
   public Setting<Boolean> pingSound;
   public Setting<SoundId> mode2;
   public Setting<Float> volume2;
   public int field_576;

   public IRCModule() {
      super("IRC", "Client user chat.", Category.CLIENT);
      Settings.initialize(this);
      this.setDrawn(false);
      this.capes.method_9(() -> Hub.field_2610.method_15(this.method_237()));
      this.mode.method_31("C-SoundMode");
      this.volume.method_31("C-SoundVolume");
      this.mode2.method_31("P-SoundMode");
      this.volume2.method_31("P-SoundVolume");
   }

   @Override
   public void onEnable() {
      Hub.field_2610.method_322();
   }

   @Override
   public void onDisable() {
      Hub.field_2610.method_202();
   }

   @Subscribe
   public void method_2(Event_23 var1) {
      float var2 = (float)mc.player.getEyePosition().distanceTo(var1.method_149().method_111().getCenter());
      if (var2 >= 9999F) {
         var1.method_463();
      } else {
         if (this.pingSound.getValue() && var1.method_149().method_1098()) {
            Hub.field_2606.method_2(this.mode2.getValue(), var1.method_149().method_111().getCenter(), this.volume2.getValue());
         }
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      Hub.field_2610.method_328().removeIf(var0 -> System.currentTimeMillis() - var0.getSpawnTime() > 7500L);
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      if (this.pings.getValue()) {
         synchronized (Hub.field_2610.method_328()) {
            for (Waypoint var4 : Hub.field_2610.method_328()) {
               if (var4.method_1098()) {
                  float var5 = (float)mc.player.getEyePosition().distanceTo(var4.method_111().getCenter());
                  if (!(var5 >= 9999F)) {
                     Vec3 var6 = Vec3.atLowerCornerOf(var4.method_111());
                     Vec3 var7 = var6;
                     if (var5 >= (float)Class_0245.field_685) {
                        float[] var8 = RotationManager.method_78(var6);
                        var7 = new Vec3(
                           mc.player.getX()
                              + (double)(7e+01F * (float)Math.cos(Math.toRadians((double)(var8[0] + (float)Class_0245.field_685)))),
                           var6.y,
                           mc.player.getZ()
                              + (double)(7e+01F * (float)Math.sin(Math.toRadians((double)(var8[0] + (float)Class_0245.field_685))))
                        );
                     }

                     float var21 = Mth.clamp(
                        1F - (float)(System.currentTimeMillis() - var4.getSpawnTime() - 6500L) / 1e+03F,
                        0.0F,
                        1F
                     );
                     float var9 = Mth.clamp(
                        (float)(System.currentTimeMillis() - var4.getSpawnTime()) / 1.5e+03F, 0.0F, 1F
                     );
                     var9 = this.method_186(var9);
                     var21 = this.method_98(var21);
                     float var10 = Math.min(VectorUtil.method_5(mc.player.getPosition(var1.method_880()), var6), 6F)
                        / 6F;
                     Vec3 var11 = var7.add(
                        0.5,
                        0.5,
                        0.5
                     );
                     double var12 = MathUtil.method_2(mc.gameRenderer.getMainCamera().position(), var11, 3.0);
                     AABB var14 = new AABB(
                        var11.add(-Class_0245.field_689, -Class_0245.field_689, -Class_0245.field_689),
                        var11.add(Class_0245.field_689, Class_0245.field_689, Class_0245.field_689)
                     );
                     var14 = var14.setMaxY(Mth.lerp((double)var9, var11.y, (double)mc.level.getMaxY()))
                        .setMinY(Mth.lerp((double)var9, var11.y, (double)mc.level.getMinY()));
                     BoxRenderer.method_5(
                        var1.method_10(),
                        var14,
                        Colors.method_2(
                           Colors.method_2(Color.red, Color.yellow, 1e+03, 0.0),
                           0.3F * var9 * var21 * var10
                        )
                     );
                     RenderUtil.field_2672
                        .method_2(
                           var1.method_881(),
                           var11,
                           256F,
                           256F,
                           var12 / 25.6 * (double)var9 * (double)var21,
                           Colors.method_2(Color.red, Color.yellow, 1e+03, 0.0),
                           field_563
                        );
                     if (this.info.getValue().method_670()) {
                        double var15 = MathUtil.method_2(mc.gameRenderer.getMainCamera().position(), var11, 1.5)
                           * (double)var9
                           * (double)var21;
                        String var17 = this.info.getValue().method_9(var4);
                        float var18 = -0.2F * (float)var12;
                        RenderUtil.field_2672
                           .method_2(
                              var1.method_10(),
                              var11,
                              0.0F,
                              var18,
                              -(FontRenderer.field_3143.method_221(var17) / 2F) - 2F,
                              5F,
                              var15 * 2.0,
                              new Color(0, 0, 0, 100)
                           );
                        RenderUtil.field_2672
                           .method_2(
                              var1.method_881(),
                              var17,
                              var11,
                              0.0F,
                              var18,
                              -(FontRenderer.field_3143.method_221(var17) / 2F),
                              0.0F,
                              var15,
                              Color.WHITE,
                              true
                           );
                     }
                  }
               }
            }
         }
      }
   }

   public float method_186(float var1) {
      double var2 = 2.0 * Class_0245.field_687 / 3.0;
      return var1 == 0.0F
         ? 0.0F
         : (float)(
            var1 == 1F
               ? 1.0
               : Math.pow(2.0, (double)(-1e+01F * var1))
                     * Math.sin(((double)(var1 * 1e+01F) - 0.75) * var2)
                  + 1.0
         );
   }

   public float method_98(float var1) {
      return var1 * var1 * var1;
   }

   @Subscribe
   public void method_2(ChatReceiveEvent var1) {
      if (this.chat.getValue()) {
         if (var1.method_219().length() > this.prefix.getValue().length() && var1.method_219().startsWith(this.prefix.getValue())) {
            var1.method_463();
            Hub.field_2610.method_214(var1.method_219().substring(this.prefix.getValue().length()).trim());
         }
      }
   }

   public String method_237() {
      if (!this.capes.getValue()) {
         return "none";
      } else {
         return switch (Hub.field_2609.method_802()) {
            case 3 -> "pepsi";
            case 5 -> "tetris";
            default -> "mio";
         };
      }
   }
}
