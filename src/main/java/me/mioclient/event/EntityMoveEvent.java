package me.mioclient.event;

import me.mioclient.enum_.PreType;

/** Fired when an entity moves (carries the movement vector). */
public class EntityMoveEvent {
   public final PreType field_1754;
   public float field_1755;

   public EntityMoveEvent(PreType var1, float var2) {
      super();
      this.field_1754 = var1;
      this.field_1755 = var2;
   }

   public PreType method_213() {
      return this.field_1754;
   }

   public float method_575() {
      return this.field_1755;
   }

   public void method_34(float var1) {
      this.field_1755 = var1;
   }
}
