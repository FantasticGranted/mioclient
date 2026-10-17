package me.mioclient.internal;

import java.util.List;
import me.mioclient.Hub;
import me.mioclient.api.Class_1291;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Class_1172;
import me.mioclient.module.combat.CombatmineModule;
import me.mioclient.module.player.SpeedMineModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

/** Base for a CombatMine mining strategy: validates target blocks and drives block breaking. */
public abstract class CombatMineMode implements MinecraftAccess, Class_1291 {
   public static SpeedMineModule speedmine = Hub.field_2595.method_78(SpeedMineModule.class);
   public final CombatmineModule field_2705;

   public CombatMineMode(CombatmineModule var1) {
      super();
      this.field_2705 = var1;
   }

   public boolean method_493(BlockPos var1) {
      if (var1.equals(this.field_2705.field_3712.method_112())) {
         return false;
      } else if (mc.player.getEyePosition().distanceTo(var1.getCenter()) > (double)this.field_2705.method_884()) {
         return false;
      } else if (!speedmine.method_686(var1)) {
         return false;
      } else {
         if (this.field_2705.strictDirection.getValue()) {
            List var2 = InteractionUtil.method_39(var1);
            if (var2.isEmpty()) {
               return false;
            }
         }

         return this.field_2705.raytrace.getValue() && !BlockUtil.method_2(Class_1172.field_3634.method_38(var1)) ? false : BlockUtil.method_3(var1);
      }
   }

   public boolean method_494(BlockPos var1) {
      return var1 == null ? false : var1.equals(speedmine.method_1120()) || var1.equals(speedmine.method_1121());
   }

   public boolean method_263(Entity var1) {
      return mc.level.noCollision(var1.getBoundingBox().expandTowards(0.0, -0.1, 0.0));
   }

   @Override
   public boolean method_206() {
      return false;
   }
}
