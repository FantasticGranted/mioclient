package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import me.mioclient.api.EntityRenderHook;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.RenderMode;
import me.mioclient.mixin.ducks.DuckBoatEntityRenderer;
import me.mioclient.record.RenderContext;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.boat.Boat;
import org.joml.Quaternionf;

/** EntityRenderHook for boats and chest boats. */
public class BoatRenderHook implements MinecraftAccess, EntityRenderHook<Boat> {
   public static final Quaternionf field_3881 = new Quaternionf();

   public BoatRenderHook() {
      super();
   }

   public void method_2(Boat var1, float var2, PoseStack var3) {
      var3.pushPose();
      var3.translate(0.0F, 0.375F, 0.0F);
      var3.mulPose(Axis.YP.rotationDegrees(180.0F - Mth.lerp(var2, var1.yRotO, var1.getYRot())));
      float var5 = (float)var1.getHurtTime() - var2;
      float var6 = var1.getDamage() - var2;
      if (var6 < 0.0F) {
         var6 = 0.0F;
      }

      if (var5 > 0.0F) {
         var3.mulPose(Axis.XP.rotationDegrees(Mth.sin(var5) * var5 * var6 / 10.0F * (float)var1.getHurtDir()));
      }

      float var7 = var1.getBubbleAngle(var2);
      if (!Mth.equal(var7, 0.0F)) {
         var3.mulPose(field_3881.setAngleAxis(var1.getBubbleAngle(var2) * Class_0245.field_690, 1.0F, 0.0F, 1.0F));
      }

      // TODO26.1: BoatRenderer no longer exposes texturesAndModels (per-type model map gone);
      // boats now render through EntityModel<BoatRenderState> + SubmitNodeCollector. Wire the
      // custom part rendering back up once the submit-pipeline boat path is ported.

      var3.popPose();
   }
}
