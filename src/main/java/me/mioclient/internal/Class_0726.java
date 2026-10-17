package me.mioclient.internal;

import me.mioclient.api.MinecraftAccess;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.world.entity.Entity;

public abstract class Class_0726 implements MinecraftAccess {
   public OutlineBufferSource field_2301;
   public RenderTarget field_2302;
   public ShaderProgram field_2303;

   public Class_0726() {
      super();
   }

   public void method_251(String var1) {
      this.field_2301 = new OutlineBufferSource();
      this.field_2302 = new TextureTarget(
         "mio-shader", mc.getWindow().getWidth(), mc.getWindow().getHeight(), false
      );
      this.field_2303 = new ShaderProgram("/base.vert", new PlaceholderText().method_2((Object)var1).method_9("/\u0001.frag"));
   }

   public void method_2(String var1, String var2) {
      this.field_2301 = new OutlineBufferSource();
      this.field_2302 = new TextureTarget(
         "mio-shader", mc.getWindow().getWidth(), mc.getWindow().getHeight(), false
      );
      this.field_2303 = new ShaderProgram(
         new PlaceholderText().method_2((Object)var1).method_9("/\u0001.vert"), new PlaceholderText().method_2((Object)var2).method_9("/\u0001.frag")
      );
   }

   public abstract boolean method_291();

   public abstract boolean method_31(Entity var1);

   public void method_696() {
   }

   public void method_697() {
   }

   public abstract void method_292();

   public void method_528() {
      // TODO(port): RenderTarget.clear/beginWrite removed in 26.1
   }

   public void method_39(Runnable var1) {
      // TODO(port): custom post shader needs GL texture ids (hidden in 26.1 RenderTarget); render inline
      var1.run();
   }

   public void method_39(int var1, int var2) {
      if (this.field_2302 != null) {
         this.field_2302.resize(var1, var2);
      }
   }
}
