package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.SlotClickEvent;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({AbstractContainerMenu.class})
public class MixinScreenHandler {
   public MixinScreenHandler() {
      super();
   }

   @WrapWithCondition(
      method = {"moveItemStackTo"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/inventory/Slot;setChanged()V"
      )}
   )
   private boolean insertItemHook(Slot var1) {
      MinecraftAccess.lookups.method_36(new SlotClickEvent(var1));
      return true;
   }
}
