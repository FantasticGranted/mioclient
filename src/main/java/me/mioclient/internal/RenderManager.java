package me.mioclient.internal;

import java.util.List;
import me.mioclient.api.MinecraftAccess;
import net.minecraft.client.renderer.MultiBufferSource;

/**
 * Owns the set of custom render subsystems (world/ESP renderers) and fans out
 * init/render/resize calls to each of them.
 */
public class RenderManager implements MinecraftAccess {
   public static EntityOutlineShader field_4416;
   public static EntityOutlineShader field_4417;
   public static EntityOutlineShader field_4418;
   public static EntityOutlineShader field_4419;
   public static EntityOutlineShader field_4420;
   public static List<EntityOutlineShader> renderers;
   public static boolean field_4422;
   public static boolean field_2003;

   public RenderManager() {
      super();
   }

   public static void init() {
      field_4416 = new Class_1130();
      field_4417 = new Class_1100();
      field_4418 = new Class_0259();
      field_4419 = new Class_0534();
      field_4420 = new Class_1123();
      renderers = List.of(field_4416, field_4417, field_4418, field_4419, field_4420);
   }

   public static void method_528() {
      for (EntityOutlineShader var1 : renderers) {
         var1.method_528();
      }
   }

   public static void method_434() {
      for (EntityOutlineShader var1 : renderers) {
         var1.method_434();
      }
   }

   public static void method_39(int var0, int var1) {
      if (mc != null) {
         for (EntityOutlineShader var3 : renderers) {
            var3.method_39(var0, var1);
         }
      }
   }

   public static boolean method_2(MultiBufferSource var0) {
      for (EntityOutlineShader var2 : renderers) {
         if (var2.field_2301 == var0) {
            return true;
         }
      }

      return false;
   }
}
