package me.mioclient.enum_;

import me.mioclient.record.FacePlacement;
import net.minecraft.world.phys.Vec3;

public enum Class_0147 {
   PRE,
   POST;

   public FacePlacement field_426;
   public Vec3 field_427;

   public Class_0147 method_2(FacePlacement var1, Vec3 var2) {
      this.field_426 = var1;
      this.field_427 = var2;
      return this;
   }
}
