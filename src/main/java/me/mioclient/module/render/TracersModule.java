package me.mioclient.module.render;

import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.enum_.BodyPart;
import me.mioclient.enum_.ColorBlindMode;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.Colors;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_21;
import me.mioclient.setting.Setting;
import net.minecraft.client.Camera;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import org.lwjgl.opengl.GL32C;

public class TracersModule extends Module {
   public static AbstractModule_21 field_521 = Hub.field_2595.method_78(AbstractModule_21.class);
   public Setting<BodyPart> hitbox;
   public Setting<Float> maxDistance;
   public Setting<Float> lineWidth;
   public Setting<Color> color;
   public Setting<Boolean> stem;
   public Setting<Boolean> distanceColor;
   public Setting<Boolean> enemyColor;
   public Setting<Boolean> ignoreFriends;
   public Setting<Boolean> ignoreNakeds;

   public TracersModule() {
      super("Tracers", "Draws lines from your crosshair to the players nearby.", Category.RENDER);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      Camera var2 = mc.gameRenderer.getMainCamera();
      Vec3 var3 = new Vec3(0.0, 0.0, 1e+02)
         .xRot(-((float)Math.toRadians((double)var2.xRot())))
         .yRot(-((float)Math.toRadians((double)var2.yRot())))
         .add(mc.getEntityRenderDispatcher().camera.position());
      GL32C.glLineWidth(this.lineWidth.getValue());

      for (AbstractClientPlayer var5 : mc.level.players()) {
         if (mc.player != var5
            && !(mc.gameRenderer.getMainCamera().position().distanceTo(var5.position()) > (double)this.maxDistance.getValue().floatValue())
            && (!this.ignoreFriends.getValue() || !Hub.field_2603.getId(var5))
            && (!this.ignoreNakeds.getValue() || PlayerUtil.method_29(var5))) {
            Vec3 var6 = var5.getPosition(RenderUtil.method_776()).add(0.0, (double)(var5.getBbHeight() * this.hitbox.getValue().getHeightFraction()), 0.0);
            Color var7 = this.color.getValue();
            boolean var8 = this.distanceColor.getValue();
            if (var8) {
               double var9 = Mth.clamp(
                  mc.player.getEyePosition().distanceTo(var6), 8.0, 64.0
               );
               float var11 = 1F
                  - (float)((var9 - 8.0) / 56.0);
               var7 = Colors.method_9(Colors.method_2(Color.red, Color.green, var11), this.color.getValue().getAlpha());
            }

            if (Hub.field_2603.getId(var5)) {
               var7 = Colors.method_9(Hub.field_2603.method_1145(), this.color.getValue().getAlpha());
            } else if (Hub.field_2603.method_16(var5) && this.enemyColor.getValue()) {
               var7 = Colors.method_9(Hub.field_2603.method_1146(), this.color.getValue().getAlpha());
            }

            ColorBlindMode var12 = var8 ? field_521.scheme.getValue() : ColorBlindMode.NORMAL;
            Color var10 = var7;
            var12.method_5(
               () -> {
                  RenderUtil.field_2672.method_2(var1.method_10(), var3, var6, var10);
                  if (this.stem.getValue()) {
                     RenderUtil.field_2672
                        .method_2(
                           var1.method_10(),
                           var5.getPosition(RenderUtil.method_776()),
                           var5.getPosition(RenderUtil.method_776()).add(0.0, (double)var5.getEyeHeight(var5.getPose()), 0.0),
                           var10
                        );
                  }
               }
            );
         }
      }

      GL32C.glLineWidth(1F);
   }
}
