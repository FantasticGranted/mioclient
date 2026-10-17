package me.mioclient.mixin;

import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.FinishUsingItemEvent;
import me.mioclient.internal.Class_0114;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ItemStack.class})
public abstract class MixinItemStack {
   public MixinItemStack() {
      super();
   }

   @Shadow
   public abstract Item getItem();

   @Inject(
      method = {"finishUsingItem"},
      at = {@At("HEAD")}
   )
   private void finishUsing(Level var1, LivingEntity var2, CallbackInfoReturnable<ItemStack> var3) {
      if (var2 instanceof LocalPlayer) {
         FinishUsingItemEvent var4 = new FinishUsingItemEvent((ItemStack)(Object)this);
         MinecraftAccess.lookups.method_36(var4);
      }
   }

   @Inject(
      method = {"hasFoil"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hasGlintHook(CallbackInfoReturnable<Boolean> var1) {
      if (Class_0114.method_22(this.getItem())) {
         var1.setReturnValue(true);
         var1.cancel();
      }
   }
}
