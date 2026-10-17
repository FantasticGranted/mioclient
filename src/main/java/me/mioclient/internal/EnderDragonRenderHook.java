package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import java.lang.reflect.Field;
import me.mioclient.api.EntityRenderHook;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.RenderMode;
import me.mioclient.record.RenderContext;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.dragon.EnderDragonModel;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;

/** EntityRenderHook for the ender dragon. */
public class EnderDragonRenderHook implements MinecraftAccess, EntityRenderHook<EnderDragon> {
   private static final Field LEFT_FRONT_LEG = method_dragonField("leftFrontLeg");
   private static final Field LEFT_FRONT_LEG_TIP = method_dragonField("leftFrontLegTip");
   private static final Field LEFT_FRONT_FOOT = method_dragonField("leftFrontFoot");
   private static final Field LEFT_REAR_LEG = method_dragonField("leftRearLeg");
   private static final Field LEFT_REAR_LEG_TIP = method_dragonField("leftRearLegTip");
   private static final Field LEFT_REAR_FOOT = method_dragonField("leftRearFoot");
   private static final Field RIGHT_FRONT_LEG = method_dragonField("rightFrontLeg");
   private static final Field RIGHT_FRONT_LEG_TIP = method_dragonField("rightFrontLegTip");
   private static final Field RIGHT_FRONT_FOOT = method_dragonField("rightFrontFoot");
   private static final Field RIGHT_REAR_LEG = method_dragonField("rightRearLeg");
   private static final Field RIGHT_REAR_LEG_TIP = method_dragonField("rightRearLegTip");
   private static final Field RIGHT_REAR_FOOT = method_dragonField("rightRearFoot");

   private static Field method_dragonField(String name) {
      try {
         Field f = EnderDragonModel.class.getDeclaredField(name);
         f.setAccessible(true);
         return f;
      } catch (NoSuchFieldException e) {
         throw new RuntimeException(e);
      }
   }

   private static ModelPart method_dragonPart(Field f, EnderDragonModel model) {
      try {
         return (ModelPart) f.get(model);
      } catch (IllegalAccessException e) {
         throw new RuntimeException(e);
      }
   }

   public EnderDragonRenderHook() {
      super();
   }

   public void method_2(EnderDragon var1, float var2, PoseStack var3) {
      // TODO26.1: EnderDragon.getLatencyPos/getHeadPartYOffset moved to
      // EnderDragonRenderState (getHistoricalPos/getHeadPartYOffset). Rebuild
      // part-transform rendering once this hook receives the render state.
   }

   public void method_2(
      RenderContext var1, float var2, ModelPart var3, ModelPart var4, ModelPart var5, ModelPart var6, ModelPart var7, ModelPart var8, ModelPart var9
   ) {
      var7.xRot = 1.0F + var2 * 0.1F;
      var8.xRot = (float)(Class_0245.field_688 + (double)(var2 * 0.1F));
      var9.xRot = 0.75F + var2 * 0.1F;
      var4.xRot = 1.3F + var2 * 0.1F;
      var5.xRot = (float)(-Class_0245.field_688 - (double)(var2 * 0.1F));
      var6.xRot = 0.75F + var2 * 0.1F;
      EntityRenderHooks.method_2(var1, var3);
      EntityRenderHooks.method_2(var1, var4);
      EntityRenderHooks.method_2(var1, var7);
   }
}
