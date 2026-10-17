package me.mioclient.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import me.mioclient.api.EntityRenderHook;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.record.RenderContext;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import net.minecraft.client.model.geom.ModelPart.Polygon;
import net.minecraft.client.renderer.GameRenderer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;
import com.mojang.blaze3d.opengl.GlStateManager;

/** Registry + dispatcher of per-EntityType render hooks; projects and renders entity models for ESP. */
public class EntityRenderHooks implements MinecraftAccess {
   public static final Vector4f field_1525 = new Vector4f();
   public static final Vector4f field_1526 = new Vector4f();
   public static final Vector4f field_1527 = new Vector4f();
   public static final Vector4f field_1528 = new Vector4f();
   public static final Map<EntityType<?>, EntityRenderHook<?>> field_1529 = new HashMap<>();
   public static final DefaultEntityRenderHook field_1530 = new DefaultEntityRenderHook();
   public static final BufferSource field_1531 = BufferSource.method_740();
   public static final BufferSource field_1532 = BufferSource.method_740();
   public static Color field_1533 = Color.white;
   public static Color field_1534 = Color.white;
   public static boolean field_1535;
   public static double field_1536;

   public EntityRenderHooks() {
      super();
      throw new AssertionError();
   }

   @Subscribe(
      method_800 = -9999
   )
   public static void method_9(Event_3 var0) {
      RenderUtil.field_2672.method_780();

      field_1531.method_529();


      field_1532.method_529();

      RenderUtil.field_2672.method_782();
   }

   public static void method_2(Color var0, Color var1) {
      field_1533 = var0;
      field_1534 = var1;
   }

   public static void method_16(boolean var0) {
      field_1535 = var0;
   }

   public static boolean method_505() {
      return field_1535;
   }

   public static void method_2(PoseStack var0, Entity var1) {
      var0.pushPose();
      Vec3 var2 = mc.gameRenderer.getMainCamera().position();
      float var3 = RenderUtil.method_776();
      if (var1 instanceof Class_0878) {
         var3 = 1.0F;
      }

      double var4 = Mth.lerp((double)var3, var1.xOld, var1.getX()) - var2.x;
      double var6 = Mth.lerp((double)var3, var1.yOld, var1.getY()) - var2.y;
      double var8 = Mth.lerp((double)var3, var1.zOld, var1.getZ()) - var2.z;
      Vec3 var10 = Vec3.ZERO;
      double var11 = var4 + var10.x;
      double var13 = var6 + var10.y;
      double var15 = var8 + var10.z;
      var0.pushPose();
      var0.translate(var11, var13, var15);
      EntityRenderHook var17 = method_16(var1);
      if (var17 != null) {
         var17.method_2(var1, RenderUtil.method_776(), var0);
      }

      var0.popPose();
   }

   public static <E extends Entity> EntityRenderHook<E> method_16(E var0) {
      return (EntityRenderHook<E>)field_1529.getOrDefault(var0.getType(), var0 instanceof LivingEntity ? field_1530 : null);
   }

   public static void method_2(RenderContext var0, ModelPart var1) {
      if (var1.visible) {
         var0.getMatrices().pushPose();
         var1.translateAndRotate(var0.getMatrices());

         for (Cube var3 : var1.cubes) {
            method_2(var0, var3);
         }

         for (ModelPart var5 : var1.children.values()) {
            method_2(var0, var5);
         }

         var0.getMatrices().popPose();
      }
   }

   public static void method_2(RenderContext var0, Cube var1) {
      for (Polygon var5 : var1.polygons) {
            field_1525.set(var5.vertices()[0].x() / 16.0F, var5.vertices()[0].y() / 16.0F, var5.vertices()[0].z() / 16.0F, 1.0F);
            field_1526.set(var5.vertices()[1].x() / 16.0F, var5.vertices()[1].y() / 16.0F, var5.vertices()[1].z() / 16.0F, 1.0F);
            field_1527.set(var5.vertices()[2].x() / 16.0F, var5.vertices()[2].y() / 16.0F, var5.vertices()[2].z() / 16.0F, 1.0F);
            field_1528.set(var5.vertices()[3].x() / 16.0F, var5.vertices()[3].y() / 16.0F, var5.vertices()[3].z() / 16.0F, 1.0F);
         if (field_1534.getAlpha() != 0) {
            if (!field_1531.method_435()) {
               field_1531.method_9(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            }

            field_1531.addVertex(var0.getMatrices().last().pose(), field_1525.x, field_1525.y, field_1525.z).setColor(field_1534.hashCode());
            field_1531.addVertex(var0.getMatrices().last().pose(), field_1526.x, field_1526.y, field_1526.z).setColor(field_1534.hashCode());
            field_1531.addVertex(var0.getMatrices().last().pose(), field_1527.x, field_1527.y, field_1527.z).setColor(field_1534.hashCode());
            field_1531.addVertex(var0.getMatrices().last().pose(), field_1528.x, field_1528.y, field_1528.z).setColor(field_1534.hashCode());
         }

         if (field_1533.getAlpha() != 0) {
            if (!field_1532.method_435()) {
               field_1532.method_9(Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH);
            }

            method_2(var0, field_1525.x, field_1525.y, field_1525.z, field_1526.x, field_1526.y, field_1526.z);
            method_2(var0, field_1526.x, field_1526.y, field_1526.z, field_1527.x, field_1527.y, field_1527.z);
            method_2(var0, field_1527.x, field_1527.y, field_1527.z, field_1528.x, field_1528.y, field_1528.z);
            method_2(var0, field_1525.x, field_1525.y, field_1525.z, field_1525.x, field_1525.y, field_1525.z);
         }
      }
   }

   public static void method_2(RenderContext var0, float var1, float var2, float var3, float var4, float var5, float var6) {
      field_1532.addVertex(var0.getMatrices().last().pose(), var1, var2, var3)
         .setColor(field_1533.hashCode())
         .setNormal(var0.getMatrices().last(), var4 - var1, var5 - var2, var6 - var3);
      field_1532.addVertex(var0.getMatrices().last().pose(), var4, var5, var6)
         .setColor(field_1533.hashCode())
         .setNormal(var0.getMatrices().last(), var4 - var1, var5 - var2, var6 - var3);
   }

   public static void method_5(double var0) {
      field_1536 = var0;
   }

   static {
      lookups.method_5(EntityRenderHooks.class);
      field_1529.put(EntityType.END_CRYSTAL, new EndCrystalRenderHook());
      field_1529.put(EntityType.BAT, new BoatRenderHook());
      field_1529.put(EntityType.OAK_CHEST_BOAT, new BoatRenderHook());
      field_1529.put(EntityType.LLAMA, new LlamaRenderHook());
      field_1529.put(EntityType.RABBIT, new RabbitRenderHook());
      field_1529.put(EntityType.ENDER_DRAGON, new EnderDragonRenderHook());
   }
}
