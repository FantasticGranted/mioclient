package me.mioclient.record;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import me.mioclient.api.MinecraftAccess;

/** BooleanConsumer for a confirmation screen: closes the screen and runs the given action when confirmed. */
public final class ConfirmCallback implements BooleanConsumer, MinecraftAccess {
   public final Runnable onConfirm;

   public ConfirmCallback(Runnable var1) {
      super();
      this.onConfirm = var1;
   }

   public void accept(boolean var1) {
      mc.setScreen(null);
      if (var1) {
         this.onConfirm.run();
      }
   }

   public Runnable getOnConfirm() {
      return this.onConfirm;
   }
}
