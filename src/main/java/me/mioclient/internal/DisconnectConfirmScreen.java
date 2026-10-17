package me.mioclient.internal;

import me.mioclient.record.ConfirmCallback;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.Component;

/** Confirmation screen shown before disconnecting from a server. */
public class DisconnectConfirmScreen extends ConfirmScreen {
   public DisconnectConfirmScreen(Runnable var1) {
      super(new ConfirmCallback(var1), Component.literal("Confirm Disconnect"), Component.literal("Are you sure that you want to disconnect?"));
   }
}
