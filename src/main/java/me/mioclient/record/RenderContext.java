package me.mioclient.record;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.enum_.RenderMode;

/** Rendering parameters: the active matrix stack plus the shape {@link me.mioclient.enum_.RenderMode}. */
public final class RenderContext {
   public final PoseStack field_2127;
   public final RenderMode renderMode;

   public RenderContext(PoseStack var1, RenderMode var2) {
      super();
      this.field_2127 = var1;
      this.renderMode = var2;
   }

   public PoseStack getMatrices() {
      return this.field_2127;
   }

   public RenderMode getRenderMode() {
      return this.renderMode;
   }
}
