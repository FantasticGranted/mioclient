package me.mioclient.enum_;

import me.mioclient.Hub;
import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.EntityUtil;
import me.mioclient.module.combat.AutoCrystalModule;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

/** AutoCrystal swing timing: on place or on hit. */
public enum CrystalSwingMode implements MinecraftAccess, Nameable {
   PLACE("Place"),
   HIT("Hit");

   public final String field_3183;
   public static AutoCrystalModule field_3184 = Hub.field_2595.method_78(AutoCrystalModule.class);

    CrystalSwingMode(String var3) {
      this.field_3183 = var3;
   }

   public double method_34(Player var1) {
      if (field_3184.field_4141) {
         return 0.1;
      } else if (!field_3184.facePlace.getValue()
         || !(EntityUtil.method_2((Entity)var1) <= field_3184.field_4085.getValue())
            && !(InventoryUtil.method_2(var1) <= (float)field_3184.armorThreshold.getValue().intValue())) {
         if (mc.level.getBlockState(mc.player.blockPosition()).is(Blocks.NETHER_PORTAL)) {
            return 0.6;
         } else {
            double var2 = (double)field_3184.minDamage.getValue().floatValue();
            double var4 = (double)field_3184.minDamage2.getValue().floatValue();
            if (field_3184.minDamage.method_468()) {
               var2 = (double)EntityUtil.method_2((Entity)var1);
            }

            if (field_3184.method_1141()) {
               var2 = Math.min(var2, 5.0);
               var4 = 3.0;
            }

            return Math.min(this == HIT ? var4 : var2, (double)EntityUtil.method_2((Entity)var1));
         }
      } else {
         return 0.6;
      }
   }

   public double method_924() {
      return field_3184.method_1140() ? 999.0 : (double)(this == HIT ? field_3184.maxSelfDamage2.getValue() : field_3184.maxSelfDamage.getValue()).floatValue();
   }

   public boolean method_2(Player var1, double var2, double var4, DamageResult var6) {
      boolean var7 = mc.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING) || mc.player.getMainHandItem().is(Items.TOTEM_OF_UNDYING);
      boolean var8 = var6.equals(DamageResult.KILL);
      if (field_3184.forceSuicide.getValue()) {
         return false;
      } else {
         if (field_3184.method_1140()) {
            var2 = 0.0;
         }

         if (!(var2 >= (double)EntityUtil.method_76()) || !field_3184.antiSuicide.getValue() || var8 && var7) {
            if (var8) {
               return false;
            } else {
               return this == PLACE && var2 > 0.0 && var4 / var2 < (double)field_3184.damageRatio.getValue().floatValue()
                  ? true
                  : !(var4 >= (double)EntityUtil.method_2((Entity)var1)) && (!(var4 >= this.method_34(var1)) || !(var2 <= this.method_924()));
            }
         } else {
            return true;
         }
      }
   }

   @Override
   public String getName() {
      return this.field_3183;
   }
}
