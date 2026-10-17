package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Event fired during 2D/HUD rendering, carrying the DrawContext, MatrixStack and tick delta. */
public class HudRenderEvent extends Event {
   public PoseStack field_2997;
   public float field_2998;
   public GuiGraphicsExtractor field_2999;

   public HudRenderEvent() {
      super();
   }

   public PoseStack method_10() {
      return this.field_2997;
   }

   public float method_880() {
      return this.field_2998;
   }

   public GuiGraphicsExtractor method_881() {
      return this.field_2999;
   }
}
