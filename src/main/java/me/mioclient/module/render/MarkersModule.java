package me.mioclient.module.render;

import com.mojang.math.Axis;
import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.event.Event_26;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.Colors;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class MarkersModule extends Module {
   public Setting<Float> field_1076;
   public Setting<Boolean> field_1077;

   public MarkersModule() {
      super("Markers", Category.RENDER);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_2(Event_26 var1) {
      float var2 = Mth.lerp(RenderUtil.method_776(), mc.player.yRotO, mc.player.getYRot());

      for (AbstractClientPlayer var4 : mc.level.players()) {
         double var5 = (double)mc.player.distanceTo(var4);
         if (mc.player != var4 && !(var5 > 32.0)) {
            float var7 = (float)(
               1.0
                  - Mth.clamp(
                     (var5 - 28.0) / 4.0,
                     0.0,
                     1.0
                  )
            );
            if (!((double)var7 < 0.1)) {
               float var8 = RotationManager.method_7(this.method_209(mc.player), this.method_209(var4))[0];
               if (!this.field_1077.getValue()
                  || !(
                     Mth.degreesDifferenceAbs(var8, var2)
                        <= (float)((Integer)mc.options.fov().get()).intValue() / 2F
                  )) {
                  double var9 = Math.toRadians((double)(var8 - var2 - (float)Class_0245.field_685));
                  float var11 = FontRenderer.field_3143.method_221(">") * 0.5F;
                  float var12 = Mth.clamp(
                        mc.player.getXRot() + 3e+01F, (float)(-Class_0245.field_685), (float)Class_0245.field_685
                     )
                     / (float)Class_0245.field_685;
                  float var13 = (float)(
                     Math.cos(var9) * (double)this.field_1076.getValue().floatValue() + (double)var1.method_881().guiWidth() * Class_0245.field_688
                  );
                  float var14 = (float)(
                     Math.sin(var9) * (double)this.field_1076.getValue().floatValue() * (double)var12
                        + (double)var1.method_881().guiHeight() * Class_0245.field_688
                  );
                  var1.method_10().pushPose();
                  var1.method_10().translate(var13, var14, 0.0F);
                  var1.method_10().mulPose(Axis.ZP.rotation((float)var9));
                  FontRenderer.field_3143
                     .method_2(
                        var1.method_881(),
                        ">",
                        -var11,
                        (float)((double)(-FontRenderer.field_3143.method_66()) * Class_0245.field_688),
                        Colors.method_9(this.method_114(var4), (int)(var7 * 255F))
                     );
                  var1.method_10().popPose();
               }
            }
         }
      }
   }

   public Color method_114(Player var1) {
      if (Hub.field_2603.getId(var1)) {
         return Hub.field_2603.method_1145();
      } else if (Hub.field_2603.method_16(var1)) {
         return Hub.field_2603.method_1146();
      } else {
         double var2 = Mth.clamp(
            mc.player.getEyePosition().distanceTo(var1.position()),
            8.0,
            32.0
         );
         float var4 = 1F
            - (float)((var2 - 8.0) / 24.0);
         return Colors.method_2(Color.red, Color.green, var4);
      }
   }

   public Vec3 method_209(Player var1) {
      return new Vec3(
         Mth.lerp((double)RenderUtil.method_776(), var1.xo, var1.getX()),
         Mth.lerp((double)RenderUtil.method_776(), var1.yo, var1.getY()),
         Mth.lerp((double)RenderUtil.method_776(), var1.zo, var1.getZ())
      );
   }
}
