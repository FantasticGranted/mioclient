package me.mioclient.internal;

import net.minecraft.world.entity.animal.Animal;

/** An animal entity paired with an increment counter. */
public final class TrackedAnimal {
   public final Animal field_712;
   public int field_713;

   public TrackedAnimal(Animal var1) {
      super();
      this.field_712 = var1;
      this.field_713 = 0;
   }

   public void method_273() {
      this.field_713++;
   }

   public Animal method_274() {
      return this.field_712;
   }

   public int method_275() {
      return this.field_713;
   }
}
