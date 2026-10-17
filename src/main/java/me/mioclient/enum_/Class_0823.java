package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.RotationManager;
import net.minecraft.world.phys.Vec3;

public enum Class_0823 implements MinecraftAccess, Nameable {
   KEEP("Keep"),
   INVERT("Invert"),
   POS("Pos");

   public final String field_2636;

    Class_0823(String var3) {
      this.field_2636 = var3;
   }

   @Override
   public String getName() {
      return this.field_2636;
   }

   public float method_3(Vec3 var1) {
      if (this == KEEP) {
         return mc.player.getYRot();
      } else {
         float var2 = RotationManager.method_78(var1)[0];
         if (this == INVERT) {
            var2 += 180.0F;
         }

         return var2;
      }
   }
}
