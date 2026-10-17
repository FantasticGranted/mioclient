package me.mioclient.internal;

import com.mojang.blaze3d.pipeline.RenderTarget;
import me.mioclient.mixin.ducks.DuckWorldRenderer;
import net.minecraft.client.renderer.LevelRenderer;

/** Base ESP shader that swaps in the entity-outlines framebuffer; concrete color modes extend it. */
public abstract class EntityOutlineShader extends Class_0726 {
   public RenderTarget field_2895;

   public EntityOutlineShader() {
      super();
   }

   @Override
   public void method_696() {
      LevelRenderer var1 = mc.levelRenderer;
      DuckWorldRenderer var2 = (DuckWorldRenderer)(Object)var1;
      this.field_2895 = var1.getItemEntityTarget();
      var2.setEntityOutlineTarget(this.field_2302);
   }

   @Override
   public void method_697() {
      if (this.field_2895 != null) {
         LevelRenderer var1 = mc.levelRenderer;
         DuckWorldRenderer var2 = (DuckWorldRenderer)(Object)var1;
         var2.setEntityOutlineTarget(this.field_2895);
         this.field_2895 = null;
      }
   }

   public void method_434() {
      this.method_39(() -> this.field_2301.endOutlineBatch());
   }
}
