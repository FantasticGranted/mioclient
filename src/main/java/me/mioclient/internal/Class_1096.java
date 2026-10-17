package me.mioclient.internal;

import java.util.Comparator;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.combat.CombatmineModule;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public final class Class_1096 implements MinecraftAccess {
   public final CombatmineModule field_3408;

   public Class_1096(CombatmineModule var1) {
      super();
      this.field_3408 = var1;
   }

   public Player method_691() {
      return mc.level
         .players()
         .stream()
         .filter(
            var1 -> var1.isAlive()
                  && var1 != mc.player
                  && mc.player.distanceTo(var1) <= this.field_3408.method_884()
                  && !Hub.field_2603.getId(var1)
         )
         .filter(var1 -> !this.field_3408.ignoreNaked.getValue() || PlayerUtil.method_29((LivingEntity)var1))
         .min(Comparator.comparing(var0 -> Mth.degreesDifferenceAbs(mc.player.getYRot(), RotationManager.method_14(var0)[0])))
         .orElse(null);
   }

   public AABB method_374(Player var1) {
      return var1.getBoundingBox().inflate(-BoxUtil.field_2280, 0.0, -BoxUtil.field_2280).setMaxY(var1.getY());
   }
}
