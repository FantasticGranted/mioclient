package me.mioclient.internal;

import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.Component;

/** Confirmation screen shown before closing Minecraft. */
public class CloseConfirmScreen extends ConfirmScreen {
   public CloseConfirmScreen() {
      super(new Class_0142(), Component.literal("Confirm Game Close"), Component.literal("Are you sure that you want to close minecraft?"));
   }
}
