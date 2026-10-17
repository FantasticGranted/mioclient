package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Warp recharge behaviour: hold, instant, or none. */
public enum WarpRechargeMode implements Nameable {
   HOLD("Hold"),
   INSTANT("Instant"),
   NONE("None");

   public final String displayName;

    WarpRechargeMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
