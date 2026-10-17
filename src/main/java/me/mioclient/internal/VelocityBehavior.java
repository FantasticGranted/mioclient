package me.mioclient.internal;

import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.InteractBlockEvent;
import me.mioclient.event.ExplosionEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.module.movement.VelocityModule;

/** Base for a Velocity module mode: hooks packet/explosion events to modify knockback. */
public abstract class VelocityBehavior implements MinecraftAccess {
   public final VelocityModule field_88;

   public VelocityBehavior(VelocityModule var1) {
      super();
      this.field_88 = var1;
   }

   public abstract void method_5(PacketReceiveEvent var1);

   public void method_2(PacketSendEvent var1) {
   }

   public void method_40() {
   }

   public void method_2(ExplosionEvent var1) {
   }

   public void method_9(Event_19 var1) {
   }

   public void method_5(InteractBlockEvent var1) {
   }

   public void onDisable() {
   }
}
