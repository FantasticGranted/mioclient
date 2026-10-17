package me.mioclient.internal;

import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.MovementInputEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.TravelEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.module.movement.ElytraFlyModule;

/** Base for an ElytraFly mode: hooks tick/move/packet events to implement one flight style. */
public abstract class ElytraFlyBehavior implements MinecraftAccess {
   public final ElytraFlyModule field_2275;

   public ElytraFlyBehavior(ElytraFlyModule var1) {
      super();
      this.field_2275 = var1;
   }

   public abstract void method_2(PlayerTickEvent var1);

   public abstract void method_2(MoveEvent var1);

   public abstract void method_5(PacketReceiveEvent var1);

   public abstract void method_2(PacketSendEvent var1);

   public abstract void method_9(Event_19 var1);

   public abstract void method_2(PostTickEvent var1);

   public abstract void method_9(TravelEvent var1);

   public void method_5(MovementInputEvent var1) {
   }

   public void onEnable() {
   }

   public void onDisable() {
   }
}
