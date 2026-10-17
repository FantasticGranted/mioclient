package me.mioclient.baritone.api.pathing.goals;
import net.minecraft.world.phys.Vec3;
public class GoalXZ implements Goal {
   private final int x, z;
   public GoalXZ(int x, int z) { this.x = x; this.z = z; }
   public int getX() { return x; }
   public int getZ() { return z; }
   public static GoalXZ fromDirection(Vec3 pos, float yaw, double distance) { return new GoalXZ(0, 0); }
}
