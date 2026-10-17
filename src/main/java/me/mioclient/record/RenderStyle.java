package me.mioclient.record;

import java.awt.Color;
import java.util.Objects;
import me.mioclient.api.ValueHolder;
import me.mioclient.deobf.Named;

/**
 * A per-module fading render style for ESP boxes: the owning module plus fill/outline color,
 * line width, fade timing, a render condition, a fade toggle, and a duration. Identity is the owner.
 */
public final class RenderStyle {
   public final Named owner;
   public final ValueHolder<Color> fill;
   public final ValueHolder<Color> outline;
   public final ValueHolder<Float> lineWidth;
   public final ValueHolder<Float> fadeTime;
   public final ValueHolder<Boolean> condition;
   public final ValueHolder<Boolean> fade;
   public final int duration;

   public RenderStyle(
      Named var1,
      ValueHolder<Color> var2,
      ValueHolder<Color> var3,
      ValueHolder<Float> var4,
      ValueHolder<Float> var5,
      ValueHolder<Boolean> var6,
      ValueHolder<Boolean> var7,
      int var8
   ) {
      super();
      this.owner = var1;
      this.fill = var2;
      this.outline = var3;
      this.lineWidth = var4;
      this.fadeTime = var5;
      this.condition = var6;
      this.fade = var7;
      this.duration = var8;
   }

   public Color getFillColor() {
      return this.fill.getValue();
   }

   public Color getOutlineColor() {
      return this.outline.getValue();
   }

   public float method_760() {
      return this.lineWidth.getValue();
   }

   public float getFadeDuration() {
      return this.fade.getValue() ? this.fadeTime.getValue() * (float)this.duration : 0.0F;
   }

   public boolean isConditionMet() {
      return this.condition.getValue();
   }

   public boolean isFading() {
      return this.fade.getValue();
   }

   @Override
   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         RenderStyle var2 = (RenderStyle)var1;
         return Objects.equals(this.owner, var2.owner);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.owner);
   }

   public Named getOwner() {
      return this.owner;
   }

   public ValueHolder<Color> getFillSetting() {
      return this.fill;
   }

   public ValueHolder<Color> getOutlineSetting() {
      return this.outline;
   }

   public ValueHolder<Float> getLineWidthSetting() {
      return this.lineWidth;
   }

   public ValueHolder<Float> getFadeTimeSetting() {
      return this.fadeTime;
   }

   public ValueHolder<Boolean> getConditionSetting() {
      return this.condition;
   }

   public ValueHolder<Boolean> getFadeSetting() {
      return this.fade;
   }

   public int getDuration() {
      return this.duration;
   }
}
