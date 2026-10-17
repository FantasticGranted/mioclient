package me.mioclient.mixin;

import me.mioclient.api.BooleanStateAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(
   targets = {"com.mojang.blaze3d.opengl.GlStateManager$BooleanState"}
)
public abstract class MixinCapabilityTracker implements BooleanStateAccess {
   @Shadow
   private boolean enabled;

   public MixinCapabilityTracker() {
      super();
   }

   @Shadow
   public abstract void setEnabled(boolean var1);

   @Override
   public boolean getState() {
      return this.enabled;
   }

   @Override
   public void set(boolean var1) {
      this.setEnabled(var1);
   }
}
