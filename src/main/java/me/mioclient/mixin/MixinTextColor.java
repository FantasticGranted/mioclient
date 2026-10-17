package me.mioclient.mixin;

import java.util.function.Supplier;
import me.mioclient.api.SupplierAccessor;
import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({TextColor.class})
public class MixinTextColor implements SupplierAccessor {
   @Unique
   private Supplier<Integer> customColorSupplier = null;

   public MixinTextColor() {
      super();
   }

   @Inject(
      method = {"getValue"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getRgbHook(CallbackInfoReturnable<Integer> var1) {
      if (this.customColorSupplier != null) {
         var1.cancel();
         var1.setReturnValue(this.customColorSupplier.get());
      }
   }

   @Override
   public Supplier<Integer> getSupplier() {
      return this.customColorSupplier;
   }

   @Override
   public void setSupplier(Supplier<Integer> var1) {
      this.customColorSupplier = var1;
   }
}
