package me.mioclient.internal;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;

/** Custom RenderTarget subclass used for the client's off-screen render targets. */
public class MioFramebuffer extends RenderTarget {
   public static MioFramebuffer field_4204;

   public MioFramebuffer(int var1, int var2) {
      super("mio", false);
      this.resize(var1, var2);
   }

   public static MioFramebuffer method_1165() {
      if (field_4204 == null) {
         Minecraft var0 = Minecraft.getInstance();
         field_4204 = new MioFramebuffer(var0.getMainRenderTarget().width, var0.getMainRenderTarget().height);
      }

      return field_4204;
   }

   public static void method_78(Runnable var0) {
      // TODO(port): offscreen FBO bind (beginWrite/endWrite) removed in 26.1; render inline
      var0.run();
   }

   public static void method_16(float var0) {
      // TODO(port): blur mask FBO path removed in 26.1
   }

   public static void method_2(Runnable var0, float var1) {
      method_78(var0);
      method_16(var1);
   }
}
