package me.mioclient.event;

import me.mioclient.api.MinecraftAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class Event_64 extends MoveEvent {
   public Event_64(Vec3 var1) {
      super(var1, null);
   }

   public static boolean method_33(Object var0) {
      return MinecraftAccess.mc.player == null ? false : var0 instanceof Entity && MinecraftAccess.mc.player.getVehicle() == var0;
   }
}
