package me.mioclient.mixin;

import me.mioclient.internal.RenderUtil;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import java.util.Optional;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.RenderTooltipEvent;
import me.mioclient.event.Event_42;
import me.mioclient.internal.KeybindUtil;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Class_0593;
import me.mioclient.internal.ChestSearchBar;
import me.mioclient.mixin.ducks.DuckDrawContext;
import me.mioclient.module.abstract_.AbstractModule_37;
import me.mioclient.module.misc.ChestSearchBarModule;
import me.mioclient.record.ContainerTooltip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.inventory.Slot;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AbstractContainerScreen.class})
public class MixinHandledScreen implements MinecraftAccess {
   private static final ChestSearchBarModule chestsearchbar = Hub.field_2595.method_78(ChestSearchBarModule.class);
   private static final AbstractModule_37 tooltips = Hub.field_2595.method_78(AbstractModule_37.class);
   @Shadow
   @Nullable
   protected Slot hoveredSlot;
   @Unique
   private int lastMX;
   @Unique
   private int lastMY;
   @Unique
   private int mio$lastTooltipX;
   @Unique
   private int mio$lastTooltipY;
   @Unique
   private Slot mio$lastSlot;
   @Unique
   private MapItemSavedData mio$cachedState;
   @Unique
   private int mio$cachedId;
   @Unique
   private final Event_42 event = new Event_42((Screen)(Object)this);

   public MixinHandledScreen() {
      super();
   }

   @Inject(
      method = {"keyPressed"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void keyPressed(KeyEvent var1, CallbackInfoReturnable<Boolean> var4) {
      if (chestsearchbar != null
         && chestsearchbar.isToggled()
         && ChestSearchBar.field_3026 != null
         && ChestSearchBar.field_3026.isFocused()
         && mc.options.keyInventory.matches(var1)) {
         var4.setReturnValue(true);
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   private void tick(CallbackInfo var1) {
      this.event.method_273();
   }

   @Inject(
      method = {"extractTooltip"},
      at = {@At("HEAD")}
   )
   private void drawMouseoverTooltipHook(GuiGraphicsExtractor var1, int var2, int var3, CallbackInfo var4) {
      this.event.method_9(var1);
      this.event.setX((float)var2);
      this.event.setY((float)var3);
      lookups.method_36(this.event);
   }

   @Inject(
      method = {"extractRenderState"},
      at = {@At("HEAD")}
   )
   private void render(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (!KeybindUtil.method_22(342)) {
         this.mio$lastSlot = this.hoveredSlot;
      }

      this.lastMX = var2;
      this.lastMY = var3;
   }

   @Inject(
      method = {"extractTooltip"},
      at = {@At("HEAD")}
   )
   private void renderWithTooltipHook(GuiGraphicsExtractor var1, int var2, int var3, CallbackInfo var4) {
      if (tooltips.isToggled() && this.mio$lastSlot != null && InventoryUtil.method_29(this.mio$lastSlot.getItem().getItem()) && KeybindUtil.method_22(342)) {
         this.hoveredSlot = this.mio$lastSlot;
      }
   }

   @Redirect(
      method = {"extractTooltip"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;setTooltipForNextFrame(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;IILnet/minecraft/resources/Identifier;)V"
      )
   )
   private void renderWithTooltip(GuiGraphicsExtractor var1, Font var2, List<Component> var3, Optional<TooltipComponent> var4, int var5, int var6, net.minecraft.resources.Identifier var10) {
      boolean var7 = !KeybindUtil.method_22(342);
      if (!var7 && this.mio$lastSlot != null && InventoryUtil.method_29(this.mio$lastSlot.getItem().getItem())) {
         var5 = this.mio$lastTooltipX;
         var6 = this.mio$lastTooltipY;
      } else {
         this.mio$lastTooltipX = var5;
         this.mio$lastTooltipY = var6;
      }

      List var8 = Class_0593.method_2(var1, var3, var4);
      RenderTooltipEvent var9 = new RenderTooltipEvent((Screen)(Object)this, var8, var5, var6, this.lastMX, this.lastMY);
      MinecraftAccess.lookups.method_36(var9);
      if (!var9.method_464()) {
         ((DuckDrawContext)var1).drawTooltipsHook(var2, var9.method_563(), var5, var6, DefaultTooltipPositioner.INSTANCE, var10);
      }
   }

    @Inject(
       method = {"extractSlot"},
       at = {@At("HEAD")}
    )
    private void drawSlot(GuiGraphicsExtractor var1, Slot var2, int var3, int var4, CallbackInfo var5) {
       this.mio$cachedState = null;
      ItemStack var6 = var2.getItem();
      if (var6.is(Items.FILLED_MAP)) {
         int var7 = ((MapId)var6.getOrDefault(DataComponents.MAP_ID, new MapId(-1))).id();
         if (var7 != -1) {
            this.mio$cachedId = var7;
            this.mio$cachedState = Hub.field_2624.method_9(var6, var7);
         }
      }
    }

   @WrapWithCondition(
      method = {"extractSlot"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;item(Lnet/minecraft/world/item/ItemStack;III)V"
      )}
   )
   private boolean drawSlotHook(GuiGraphicsExtractor var1, ItemStack var2, int var3, int var4, int var5) {
      return !tooltips.isToggled() || !tooltips.mapOverlay.getValue() || this.mio$cachedState == null;
   }

   @WrapWithCondition(
      method = {"extractSlot"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"
      )}
   )
   private boolean drawSlotHook(GuiGraphicsExtractor var1, Font var2, ItemStack var3, int var4, int var5, String var6, @Local(argsOnly = true) Slot var7) {
      if (tooltips.isToggled() && tooltips.mapOverlay.getValue() && this.mio$cachedState != null) {
         ContainerTooltip.method_2(RenderUtil.mioPose(var1.pose()), this.mio$cachedState, this.mio$cachedId, (float)var4, (float)var5);
         if (!KeybindUtil.method_22(342)) {
            var1.itemDecorations(var2, var3, var4, var5);
         }

         return false;
      } else {
         return true;
      }
   }
}
