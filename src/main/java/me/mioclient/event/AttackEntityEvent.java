package me.mioclient.event;

import me.mioclient.enum_.PreType;
import me.mioclient.internal.Event;
import net.minecraft.world.entity.Entity;

/** Fired before the player attacks an entity. */
public class AttackEntityEvent extends Event {
   public final PreType field_1339;
   public Entity field_581;

   public AttackEntityEvent(PreType var1, Entity var2) {
      super();
      this.field_1339 = var1;
      this.field_581 = var2;
   }

   public Entity method_11() {
      return this.field_581;
   }

   public PreType method_213() {
      return this.field_1339;
   }
}
