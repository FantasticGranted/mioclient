package me.mioclient.enum_;

import java.util.function.Function;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public enum Class_1045 implements MinecraftAccess {
   DISTANCE(var0 -> var0.distanceToSqr(mc.player)),
   HEALTH(var0 -> (double)EntityUtil.method_2((Entity)var0));

   public final Function<LivingEntity, Double> field_3230;

    Class_1045(Function<LivingEntity, Double> var3) {
      this.field_3230 = var3;
   }

   public Function<LivingEntity, Double> method_939() {
      return this.field_3230;
   }
}
