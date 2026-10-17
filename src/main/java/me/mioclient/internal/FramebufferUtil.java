package me.mioclient.internal;

import me.mioclient.api.FramebufferAccess;
import me.mioclient.api.MinecraftAccess;
import com.mojang.blaze3d.pipeline.RenderTarget;

/** Static holder/util for the shared entity-outline RenderTarget. */
public final class FramebufferUtil implements MinecraftAccess {
   public static RenderTarget field_2895;

   public FramebufferUtil() {
      super();
      throw new AssertionError();
   }

   public static void method_2(Class_0726 var0, boolean var1, Runnable var2) {
      method_2(var0, var1);
      var2.run();
      mc.renderBuffers().bufferSource().endBatch();
      method_31(var1);
   }

   public static void method_2(Class_0726 var0, boolean var1) {
      if (var1) {
         RenderManager.method_528();
      }

      field_2895 = ((FramebufferAccess)mc.levelRenderer).getFramebuffer();
      ((FramebufferAccess)mc.levelRenderer).setFramebuffer(var0.field_2302);
      // TODO(port): RenderTarget.beginWrite/endWrite removed in 26.1
      RenderManager.field_4422 = true;
   }

   public static void method_31(boolean var0) {
      RenderManager.field_4422 = false;
      ((FramebufferAccess)mc.levelRenderer).setFramebuffer(field_2895);
      // TODO(port): RenderTarget.beginWrite/endWrite removed in 26.1
      if (var0) {
         RenderManager.method_434();
      }
   }
}
