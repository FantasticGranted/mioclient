package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import net.minecraft.world.InteractionHand;

/** Which hand the Swing module animates. */
public enum SwingHand implements Nameable {
   MAINHAND("Mainhand") {
      @Override
      public InteractionHand method_12() {
         return InteractionHand.MAIN_HAND;
      }
   },
   OFFHAND("Offhand") {
      @Override
      public InteractionHand method_12() {
         return InteractionHand.OFF_HAND;
      }
   },
   PACKET("Packet"),
   VANILLA("Vanilla");

   public final String displayName;

    SwingHand(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public InteractionHand method_12() {
      return null;
   }
}
