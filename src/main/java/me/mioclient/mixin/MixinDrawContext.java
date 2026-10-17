package me.mioclient.mixin;

import java.util.List;
import me.mioclient.Hub;
import me.mioclient.internal.Class_0593;
import me.mioclient.module.abstract_.AbstractModule_37;
import me.mioclient.record.ContainerPreview;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiGraphicsExtractor.class})
public abstract class MixinDrawContext {
   @Unique
   private static AbstractModule_37 tooltips = Hub.field_2595.method_78(AbstractModule_37.class);

   public MixinDrawContext() {
      super();
   }

   @Shadow
   public abstract void item(ItemStack var1, int var2, int var3);

   @Inject(
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;itemBar(Lnet/minecraft/world/item/ItemStack;II)V"
      )},
      method = {"itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"}
   )
   private void drawItemInSlotHook(Font var1, ItemStack var2, int var3, int var4, @Nullable String var5, CallbackInfo var6) {
      if (tooltips.isToggled() && tooltips.majorityItem.getValue()) {
         for (ContainerPreview var8 : tooltips.field_1192) {
            if (var8.getContainer() == var2 && var8.getMostCommon() != null) {
               this.item(var8.getMostCommon(), var3, var4);
            }
         }
      }
   }

   @Inject(
      method = {"tooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;Lnet/minecraft/resources/Identifier;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void drawTooltipHook(
      Font var1, List<ClientTooltipComponent> var2, int var3, int var4, net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner var5, net.minecraft.resources.Identifier var6, CallbackInfo var7
   ) {
      if (Class_0593.method_607()) {
         Class_0593.method_29(var2);
         var7.cancel();
      }
   }
}
