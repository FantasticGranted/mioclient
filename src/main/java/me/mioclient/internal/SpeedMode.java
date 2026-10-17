package me.mioclient.internal;

import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.PreType;
import me.mioclient.event.Event_19;
import me.mioclient.event.MoveEvent;
import me.mioclient.module.movement.SpeedModule;

/** Base for a Speed module mode: hooks MoveEvent to apply one horizontal-speed strategy. */
public abstract class SpeedMode implements MinecraftAccess {
   public final SpeedModule field_2537;
   public double field_2538 = 0.2873;
   public double field_1570;
   public double field_1419;
   public int field_1571;

   public SpeedMode(SpeedModule var1) {
      super();
      this.field_2538 = 0.2873;
      this.field_2537 = var1;
   }

   public abstract void method_9(MoveEvent var1);

   public void method_2(Event_19 var1) {
      if (var1.method_213() != PreType.POST) {
         if (!MovementUtil.method_363()) {
            this.field_1571 = 4;
            this.field_1570 = 0.0;
         }

         double var2 = mc.player.getX() - mc.player.xo;
         double var4 = mc.player.getZ() - mc.player.zo;
         this.field_1419 = Math.sqrt(var2 * var2 + var4 * var4);
      }
   }

   public void method_738() {
   }

   public void onEnable() {
   }

   public boolean method_739() {
      return !mc.level.noCollision(mc.player.getBoundingBox().move(0.0, mc.player.getDeltaMovement().y, 0.0));
   }
}
