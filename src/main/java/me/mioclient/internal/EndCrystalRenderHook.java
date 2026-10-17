package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.mioclient.Hub;
import me.mioclient.api.AttackTrackingAccess;
import me.mioclient.api.EntityRenderHook;
import me.mioclient.enum_.RenderMode;
import me.mioclient.mixin.ducks.DuckEndCrystalEntityRenderer;
import me.mioclient.ducks.DuckEndCrystalRenderState;
import me.mioclient.module.combat.AutoCrystalModule;
import me.mioclient.module.render.AnimationsModule;
import me.mioclient.record.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.object.crystal.EndCrystalModel;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.joml.Quaternionf;

/** EntityRenderHook for end crystals (AutoCrystal/animation rendering). */
public class EndCrystalRenderHook implements EntityRenderHook<EndCrystal> {
   public static AutoCrystalModule ac = Hub.field_2595.method_78(AutoCrystalModule.class);
   public static AnimationsModule animations = Hub.field_2595.method_78(AnimationsModule.class);
   public static final float field_871 = (float)Math.sin(Math.PI / 4);
   public final Quaternionf field_872 = new Quaternionf();
   public EndCrystal last;

   public EndCrystalRenderHook() {
      super();
   }

   public void method_2(EndCrystal var1, float var2, PoseStack var3) {
      this.last = var1;
      RenderContext var4 = new RenderContext(var3, RenderMode.BOTH);
      DuckEndCrystalEntityRenderer var5 = (DuckEndCrystalEntityRenderer)(Object)Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(var1);
      EndCrystalModel var5x = var5.mio$getModel();
      var3.pushPose();
      float var6 = EndCrystalRenderer.getY( var2);
      float var7 = ((float)var1.time + var2) * 3.0F;
      var3.scale(2.0F * this.method_174(), 2.0F * this.method_174(), 2.0F * this.method_174());
      var3.translate(0.0, -Class_0245.field_688, 0.0);
      if (var1.showsBottom() && !animations.method_136()) {
         EntityRenderHooks.method_2(var4, var5x.base);
      }

      var3.mulPose(Axis.YP.rotationDegrees(var7 * this.method_309()));
      var3.translate(0.0F, 1.5F + var6 / 2.0F, 0.0F);
      var3.mulPose(this.field_872.setAngleAxis((float) (Math.PI / 3), field_871, 0.0F, field_871));
      if (!animations.method_134()) {
         boolean var8 = var5x.innerGlass.visible;
         var5x.innerGlass.visible = false;
         var3.translate(0.0F, -1.5F, 0.0F);
         EntityRenderHooks.method_2(var4, var5x.outerGlass);
         var3.translate(0.0F, 1.5F, 0.0F);
         var5x.innerGlass.visible = var8;
      }

      var3.mulPose(this.field_872.setAngleAxis((float) (Math.PI / 3), field_871, 0.0F, field_871));
      var3.mulPose(Axis.YP.rotationDegrees(var7 * this.method_309()));
      if (!animations.method_133()) {
         boolean var8 = var5x.cube.visible;
         var5x.cube.visible = false;
         EntityRenderHooks.method_2(var4, var5x.innerGlass);
         var5x.cube.visible = var8;
      }

      var3.popPose();
   }

   public float method_174() {
      return animations.isToggled() && animations.crystals.getValue() ? animations.crystalScale.getValue() : 1.0F;
   }

   public float method_309() {
      if (this.last.tickCount < 10 && ac.isToggled() && ac.inhibit.getValue() && ((AttackTrackingAccess)this.last).isMioAttacked()) {
         return 1.5F;
      } else {
         return animations.isToggled() && animations.crystals.getValue() ? animations.rotationSpeed.getValue() : 1.0F;
      }
   }

   public static float applyFloatFactor(float var0, EndCrystalRenderState var1) {
      if (!animations.isToggled() || !animations.crystals.getValue()) {
         return var0;
      } else {
         EndCrystal var2 = ((DuckEndCrystalRenderState)var1).mio$getCrystal();
         float var3 = animations.floatFactor.getValue();
         if (var2 != null
            && var2.tickCount < 10
            && ac.isToggled()
            && animations.floatFactor.getValue() != 0.0F
            && ac.inhibit.getValue()
            && ((AttackTrackingAccess)var2).isMioAttacked()) {
            var3 *= ((AttackTrackingAccess)var2).getSpawnTime() % 2L == 0L ? 0.0F : 1.0F;
         }

         return ((var0 + 1.4F) / 0.4F) * var3 - 1.4F;
      }
   }
}
