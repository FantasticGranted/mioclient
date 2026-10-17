package me.mioclient.record;

import me.mioclient.enum_.Safety;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

/** A block marked with a Safety category, its BlockPos and Box. */
public final class SafetyBox {
   public final Safety safety;
   public final BlockPos field_355;
   public final AABB box;
   public final boolean field_2179;

   public SafetyBox(Safety var1, BlockPos var2, AABB var3, boolean var4) {
      super();
      this.safety = var1;
      this.field_355 = var2;
      this.box = var3;
      this.field_2179 = var4;
   }

   public Safety getSafety() {
      return this.safety;
   }

   public BlockPos getPos() {
      return this.field_355;
   }

   public AABB getBox() {
      return this.box;
   }

   public boolean method_676() {
      return this.field_2179;
   }
}
