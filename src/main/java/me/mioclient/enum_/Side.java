package me.mioclient.enum_;

import me.mioclient.record.Vec2i;

public enum Side {
   UP(0, -1),
   RIGHT(1, 0),
   DOWN(0, 1),
   LEFT(-1, 0);

   public final Vec2i field_1082;

    Side(int var3, int var4) {
      this.field_1082 = new Vec2i(var3, var4);
   }

   public Vec2i method_376() {
      return this.field_1082;
   }

   public Side method_377() {
      return switch (this) {
         case UP -> DOWN;
         case RIGHT -> LEFT;
         case DOWN -> UP;
         case LEFT -> RIGHT;
      };
   }
}
