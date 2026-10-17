package me.mioclient.event;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.internal.Event;
import me.mioclient.internal.RenderUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;

public class Event_42 extends Event {
   public final Screen field_3088;
   public GuiGraphicsExtractor field_2999;
   public float field_465;
   public float field_466;
   public Runnable field_3089 = null;

   public Event_42(Screen var1) {
      super();
      this.field_3088 = var1;
   }

   public Screen method_562() {
      return this.field_3088;
   }

   public PoseStack method_10() {
      return RenderUtil.mioPose(this.field_2999);
   }

   public float method_59() {
      return this.field_465;
   }

   public float method_60() {
      return this.field_466;
   }

   public GuiGraphicsExtractor method_881() {
      return this.field_2999;
   }

   public void method_9(GuiGraphicsExtractor var1) {
      this.field_2999 = var1;
   }

   public void setX(float var1) {
      this.field_465 = var1;
   }

   public void setY(float var1) {
      this.field_466 = var1;
   }

   public void method_31(Runnable var1) {
      this.field_3089 = var1;
   }

   public void method_273() {
      if (this.field_3089 != null) {
         this.field_3089.run();
      }
   }
}
