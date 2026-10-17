package me.mioclient.enum_;

import java.util.Locale;
import me.mioclient.api.Nameable;
import net.minecraft.world.phys.Vec3;
import me.mioclient.api.MinecraftAccess;

/** Waypoint info display: none or coordinates. */
public enum WaypointInfoMode implements Nameable {
   NONE("None"),
   COORDS("Coords") {
      @Override
      public String method_37(Vec3 var1) {
         return String.format(Locale.US, "X: %.1f, Y: %.1f, Z: %.1f", var1.x(), var1.y(), var1.z());
      }
   },
   DISTANCE("Distance") {
      @Override
      public String method_37(Vec3 var1) {
         return String.format("%.1fm", var1.distanceTo(MinecraftAccess.mc.player.position())).replaceAll(",", ".");
      }
   };

   public final String displayName;

    WaypointInfoMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public String method_37(Vec3 var1) {
      return "";
   }
}
