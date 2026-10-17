package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.Waypoint;
import net.minecraft.world.phys.Vec3;

/** What position info the IRC integration shows: distance, coords, or none. */
public enum IrcInfoMode implements Nameable {
   DISTANCE("Distance"),
   COORDS("Coords"),
   NONE("None");

   public final String displayName;

    IrcInfoMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public String method_9(Waypoint var1) {
      if (this == DISTANCE) {
         return "%.1fm".formatted(MinecraftAccess.mc.player.getEyePosition().distanceTo(var1.method_111().getCenter()));
      } else {
         Vec3 var2 = var1.method_111().getCenter();
         return "X:%.1f, Y:%.1f, Z:%.1f".formatted(var2.x, var2.y, var2.z);
      }
   }

   public boolean method_670() {
      return this != NONE;
   }
}
