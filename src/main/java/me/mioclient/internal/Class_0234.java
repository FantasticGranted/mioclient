package me.mioclient.internal;

import java.util.function.Consumer;
import me.mioclient.api.MinecraftAccess;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.opengl.GlStateManager;

public class Class_0234 {
   public final PostChain field_645;
   public int field_646;
   public int field_647;

   public Class_0234(Identifier var1, Consumer<Class_0234> var2) {
      super();
      Minecraft var3 = Minecraft.getInstance();
      this.field_645 = var3.getShaderManager().getPostChain(var1, LevelTargetBundle.MAIN_TARGETS);
      if (this.field_645 == null) {
         throw new RuntimeException("Failed to load post chain: " + var1);
      }

      this.method_266();
      var2.accept(this);
   }

   public static Class_0234 method_2(String var0, Consumer<Class_0234> var1) {
      return new Class_0234(Identifier.fromNamespaceAndPath(MinecraftAccess.method_244() ? "mio" : "minecraft", String.format("shaders/post/%s.json", var0)), var1);
   }

   public void method_266() {
      Minecraft var1 = Minecraft.getInstance();
      int var2 = var1.getWindow().getWidth();
      int var3 = var1.getWindow().getHeight();
      // TODO(26.1): PostChain no longer exposes setupDimensions(int, int); the frame graph sizes passes internally.
      this.field_646 = var2;
      this.field_647 = var3;
   }

   public void method_2(String var1, float var2) {
      // TODO(26.1): PostPass no longer exposes its shader program; uniforms are declared in the post JSON config.
   }

   public void method_9(String var1, RenderTarget var2) {
      // TODO(26.1): sampler binding is declared via PostPass$Input in the post JSON config now.
   }

   public void method_31(float var1) {
      this.method_266();
      GlStateManager._disableBlend();
      GlStateManager._disableDepthTest();
      this.field_645.process(Minecraft.getInstance().getMainRenderTarget(), GraphicsResourceAllocator.UNPOOLED);
      GlStateManager._disableBlend();
      GlStateManager._blendFuncSeparate(770, 771, 770, 771);
      GlStateManager._enableDepthTest();
   }

   public PostChain method_267() {
      return this.field_645;
   }
}
