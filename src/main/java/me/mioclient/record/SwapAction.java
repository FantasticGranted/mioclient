package me.mioclient.record;

import me.mioclient.api.SwapHandler;

/** A performed inventory swap (a {@link me.mioclient.api.SwapHandler} and the slot it acted on), kept so it can be undone. */
public final class SwapAction {
   public final SwapHandler handler;
   public final IntPair slot;

   public SwapAction(SwapHandler var1, IntPair var2) {
      super();
      this.handler = var1;
      this.slot = var2;
   }

   public SwapHandler getHandler() {
      return this.handler;
   }

   public IntPair getSlot() {
      return this.slot;
   }
}
