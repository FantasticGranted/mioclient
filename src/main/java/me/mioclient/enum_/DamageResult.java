package me.mioclient.enum_;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.EntityUtil;
import me.mioclient.module.combat.AutoCrystalModule;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;

public enum DamageResult implements MinecraftAccess {
   NONE(false),
   POP(true),
   KILL(true);

   public static AutoCrystalModule autoCrystal = Hub.field_2595.method_78(AutoCrystalModule.class);
   public final boolean damaging;

    DamageResult(boolean var3) {
      this.damaging = var3;
   }

   public static DamageResult of(LivingEntity var0, double var1) {
      boolean var3 = var1 >= (double)EntityUtil.method_2((Entity)var0);
      boolean var4 = var0.getOffhandItem().is(Items.TOTEM_OF_UNDYING) || var0.getMainHandItem().is(Items.TOTEM_OF_UNDYING);
      if (var3) {
         return var4 ? POP : KILL;
      } else {
         return NONE;
      }
   }
}
