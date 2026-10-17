package me.mioclient.internal;

import me.mioclient.mixin.ducks.DuckHandledScreen;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.AfterInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.HopperScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class Class_0665 implements AfterInit {
   public Class_0665() {
      super();
   }

   public void afterInit(Minecraft var1, Screen var2, int var3, int var4) {
      if (!ChestSearchBar.field_3025.isToggled()) {
         ChestSearchBar.field_3026 = null;
      } else {
         if (!(var2 instanceof ContainerScreen) && !(var2 instanceof ShulkerBoxScreen) && !(var2 instanceof HopperScreen)) {
            ChestSearchBar.field_3026 = null;
         } else {
            Minecraft var5 = Minecraft.getInstance();
            AbstractContainerScreen var6 = (AbstractContainerScreen)var2;
            DuckHandledScreen var7 = (DuckHandledScreen)var6;
            ChestSearchBar.field_3026 = new EditBox(var5.font, var7.getX() + 81, var7.getY() + 5, 88, 10, Component.literal(""));
            ChestSearchBar.field_3026.setValue(ChestSearchBar.text);
            ChestSearchBar.field_3026.setFocused(false);
            ChestSearchBar.field_3026.setMaxLength(32);
            ChestSearchBar.field_3026.setBordered(false);
            ChestSearchBar.field_3026.setResponder(var0 -> ChestSearchBar.text = var0);
            ((java.util.List)var2.children()).add(ChestSearchBar.field_3026);
         }
      }
   }
}
