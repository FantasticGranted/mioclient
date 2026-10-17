package me.mioclient.internal;

import com.mojang.blaze3d.pipeline.RenderTarget;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;

/**
 * Multisampled (MSAA) framebuffer that renders a Runnable at N samples and blits the
 * anti-aliased result back to the target framebuffer; instances are cached per sample count.
 * TODO(port): MSAA path disabled in 26.1 (RenderTarget no longer exposes GL framebuffer ids).
 */
public class MsaaFramebuffer {
   public static final int DEFAULT_SAMPLES = 2;
   public static final int MAX_SAMPLES = 16;
   public static final Map<Integer, MsaaFramebuffer> CACHE = new HashMap<>();
   public static final List<MsaaFramebuffer> ACTIVE = new ArrayList<>();
   public final int samples;
   public boolean writing;

   public MsaaFramebuffer(int var1) {
      int var2 = Math.clamp((long)var1, 2, MAX_SAMPLES);
      if ((var2 & var2 - 1) != 0) {
         throw new IllegalArgumentException(String.valueOf(var2));
      } else {
         this.samples = var2;
      }
   }

   public static boolean method_608() {
      return !ACTIVE.isEmpty();
   }

   public static MsaaFramebuffer method_107(int var0) {
      return CACHE.computeIfAbsent(var0, var1 -> new MsaaFramebuffer(var0));
   }

   public static void method_2(int var0, Runnable var1) {
      method_2(var0, Minecraft.getInstance().getMainRenderTarget(), var1);
   }

   public static void method_2(int var0, RenderTarget var1, Runnable var2) {
      // TODO(port): MSAA render path disabled in 26.1; render at 1x
      var2.run();
   }
}
