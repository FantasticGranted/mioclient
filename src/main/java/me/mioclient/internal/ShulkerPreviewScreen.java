package me.mioclient.internal;

import me.mioclient.api.MinecraftAccess;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ShulkerBoxMenu;

/** ShulkerBoxScreen subclass that previews a shulker's contents over another screen. */
public class ShulkerPreviewScreen extends ShulkerBoxScreen implements MinecraftAccess {
   public final Screen field_1707;

   public ShulkerPreviewScreen(ShulkerBoxMenu var1, Inventory var2, Component var3, Screen var4) {
      super(var1, var2, var3);
      this.field_1707 = var4;
   }

   public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
      super.extractRenderState(context, mouseX, mouseY, delta);
   }

   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
      return false;
   }

   public boolean keyPressed(KeyEvent event) {
      if (event.key() == 256) {
         mc.setScreen(this.field_1707);
         return true;
      } else {
         return super.keyPressed(event);
      }
   }
}
