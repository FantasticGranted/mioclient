package me.mioclient.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.mioclient.Hub;
import me.mioclient.enum_.WaypointInfoMode;
import me.mioclient.enum_.SettingMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.MathUtil;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.Colors;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.internal.VectorUtil;
import me.mioclient.internal.NamedPosition;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.client.FontsModule;
import me.mioclient.setting.Setting;
import net.minecraft.client.Camera;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import org.lwjgl.opengl.GL32C;

public class WaypointsModule extends Module {
   public static FontsModule field_984 = Hub.field_2595.method_78(FontsModule.class);
   public Setting<Float> distance;
   public Setting<Float> lineWidth;
   public Setting<Boolean> box;
   public Setting<Boolean> beam;
   public Setting<Boolean> tracers;
   public Setting<Boolean> name;
   public Setting<WaypointInfoMode> info;
   public Setting<Float> textScale;
   public Setting<Boolean> textBackground;
   public Setting<Boolean> eyesAlign;
   public Setting<Color> color;
   public Setting<Integer> beamAlpha;
   public final CopyOnWriteArrayList<NamedPosition> field_3317;

   public WaypointsModule() {
      super("Waypoints", "Renders certain places you mark using ;waypoints.", Category.RENDER);
      Settings.initialize(this);
      this.field_3317 = new CopyOnWriteArrayList<>();
      this.distance.method_2("Unlimited", SettingMode.MAX);
      this.setDrawn(false);
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (mc.player.tickCount % 2 != 0) {
         ArrayList var2 = new ArrayList();

         for (NamedPosition var4 : (Iterable<NamedPosition>)(Iterable<?>)((List<NamedPosition>)Hub.field_2604.getRegistry())) {
            if (var4 != null && (var4.method_106() == null || var4.method_600() == null || var4.getName() == null)) {
               var2.add(var4);
            }
         }

         for (NamedPosition var7 : (Iterable<NamedPosition>)(Iterable<?>)(var2)) {
            Hub.field_2604.method_5(var7);
         }

         this.field_3317.clear();

         for (NamedPosition var8 : (Iterable<NamedPosition>)(Iterable<?>)((List<NamedPosition>)Hub.field_2604.getRegistry())) {
            if (var8 != null
               && var8.method_600() != null
               && var8.getName() != null
               && var8.method_106() != null
               && var8.isToggled()
               && (
                  mc.getCurrentServer() == null && var8.method_106().equalsIgnoreCase("singleplayer")
                     || mc.getCurrentServer() != null && var8.method_106().equalsIgnoreCase(mc.getCurrentServer().ip)
               )) {
               this.field_3317.add(var8);
            }
         }

         this.field_3317.sort(Comparator.comparing(var0 -> mc.player.distanceToSqr(var0.method_733())));
      }
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      if (!mc.options.hideGui) {
         Camera var2 = mc.gameRenderer.getMainCamera();
         Vec3 var3 = new Vec3(0.0, 0.0, 1.0)
            .xRot(-((float)Math.toRadians((double)var2.xRot())))
            .yRot(-((float)Math.toRadians((double)var2.yRot())))
            .add(mc.getEntityRenderDispatcher().camera.position());

         for (NamedPosition var5 : this.field_3317) {
            if (var5 != null
               && var5.method_600() != null
               && var5.getName() != null
               && var5.method_106() != null
               && (
                  !(
                        var5.method_733().distanceTo(mc.gameRenderer.getMainCamera().position())
                           > (double)(this.distance.getValue() * 1e+03F)
                     )
                     || this.distance.getValue() == 101F
               )) {
               Vec3 var6 = method_4(var5);
               if (var6 != null) {
                  float var7 = VectorUtil.method_5(mc.player.position(), var6);
                  Vec3 var8 = var6;
                  if (var7 >= 1.2e+02F) {
                     float[] var9 = RotationManager.method_78(var6);
                     Vec3 var10 = mc.player.getPosition(RenderUtil.method_776());
                     var8 = new Vec3(
                        var10.x()
                           + (double)(1e+02F * (float)Math.cos(Math.toRadians((double)(var9[0] + (float)Class_0245.field_685)))),
                        var6.y,
                        var10.z()
                           + (double)(1e+02F * (float)Math.sin(Math.toRadians((double)(var9[0] + (float)Class_0245.field_685))))
                     );
                  }

                  if (this.beam.getValue()) {
                     AABB var13 = new AABB(
                           var8.add(-Class_0245.field_689, -Class_0245.field_689, -Class_0245.field_689),
                           var8.add(Class_0245.field_689, Class_0245.field_689, Class_0245.field_689)
                        )
                        .setMaxY((double)mc.level.getMaxY())
                        .setMinY((double)mc.level.getMinY());
                     BoxRenderer.method_5(var1.method_10(), var13, Colors.method_9(this.color.getValue(), this.beamAlpha.getValue()));
                  }

                  if (this.box.getValue()) {
                     BoxRenderer.method_2(
                        var1.method_10(),
                        new AABB(
                           var6.add(-Class_0245.field_689, -Class_0245.field_689, -Class_0245.field_689),
                           var6.add(Class_0245.field_689, Class_0245.field_689, Class_0245.field_689)
                        ),
                        this.color.getValue(),
                        this.lineWidth.getValue()
                     );
                  }

                  if (this.tracers.getValue()) {
                     GL32C.glLineWidth(this.lineWidth.getValue());
                     RenderUtil.field_2672.method_2(var1.method_10(), var3, var8, this.color.getValue());
                     GL32C.glLineWidth(1F);
                  }

                  if (this.name.getValue()) {
                     if (this.eyesAlign.getValue()) {
                        double var14 = Mth.lerp((double)var1.method_880(), mc.player.yo, mc.player.getY());
                        var8 = var8.with(Axis.Y, var14 + (double)mc.player.getEyeHeight(mc.player.getPose()));
                     }

                     String var15 = new PlaceholderText().method_2(this.info.getValue().method_37(var6)).method_2(var5.getName()).method_9("\u0001 \u0001");
                     double var16 = MathUtil.method_2(mc.gameRenderer.getMainCamera().position(), var8, (double)this.textScale.getValue().floatValue());
                     float var12 = (float)Class_0245.field_689;
                     if (this.textBackground.getValue()) {
                        RenderUtil.field_2672
                           .method_2(
                              var1.method_10(),
                              var8,
                              -0.03F,
                              (float)Class_0245.field_689,
                              -(FontRenderer.field_3143.method_221(var15) / 2F) - 2F,
                              5F,
                              var16 * 2.0,
                              new Color(0, 0, 0, 100)
                           );
                        if (!field_984.isToggled()) {
                           var12 = 0.23F;
                        }
                     }

                     RenderUtil.field_2672
                        .method_2(
                           var1.method_881(),
                           var15,
                           var8,
                           0.0F,
                           var12,
                           -(FontRenderer.field_3143.method_221(var15) / 2F),
                           0.0F,
                           var16,
                           Color.WHITE,
                           true
                        );
                  }
               }
            }
         }
      }
   }

   public static Vec3 method_4(NamedPosition var0) {
      Vec3 var1 = var0.method_733();
      String var2 = BlockUtil.method_1071().getDisplayName().toLowerCase();
      if (var0.method_600().equals("overworld") && var2.equals("nether")) {
         return var1.multiply(
            0.125, 1.0, 0.125
         );
      } else if (var0.method_600().equals("nether") && var2.equals("overworld")) {
         return var1.multiply(
            8.0, 1.0, 8.0
         );
      } else {
         return !var0.method_600().equals(var2) ? null : var1;
      }
   }
}
