package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** A target body region (head/body/legs) with a display name and vertical height fraction. */
public enum BodyPart implements Nameable {
   HEAD("Head", 0.85F),
   BODY("Body", 0.5F),
   LEGS("Legs", 0.0F);

   public final String displayName;
   public final float heightFraction;

    BodyPart(String var3, float var4) {
      this.displayName = var3;
      this.heightFraction = var4;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public float getHeightFraction() {
      return this.heightFraction;
   }
}
