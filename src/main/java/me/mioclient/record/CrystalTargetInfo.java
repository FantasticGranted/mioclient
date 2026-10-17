package me.mioclient.record;

import me.mioclient.enum_.DamageResult;
import me.mioclient.enum_.RaytraceMode;
import net.minecraft.world.entity.player.Player;

public final class CrystalTargetInfo {
   public final Player target;
   public final double damage;
   public final RaytraceMode raytraceMode;
   public final DamageResult damageResult;

   public CrystalTargetInfo(Player var1, double var2, RaytraceMode var4, DamageResult var5) {
      super();
      this.target = var1;
      this.damage = var2;
      this.raytraceMode = var4;
      this.damageResult = var5;
   }

   public Player method_444() {
      return this.target;
   }

   public double method_445() {
      return this.damage;
   }

   public RaytraceMode method_446() {
      return this.raytraceMode;
   }

   public DamageResult getDamageResult() {
      return this.damageResult;
   }
}
