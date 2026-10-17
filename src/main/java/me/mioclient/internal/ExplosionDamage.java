package me.mioclient.internal;

import java.util.Arrays;
import java.util.Map;
import me.mioclient.Hub;
import me.mioclient.api.BlockFilter;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.combat.AutoCrystalModule;
import me.mioclient.module.combat.CombatmineModule;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.EquipmentSlot;

/**
 * Predicts end-crystal / explosion damage on an entity: block-exposure ray sampling,
 * distance falloff, difficulty scaling, armor and blast-protection reduction.
 */
public class ExplosionDamage implements MinecraftAccess {
   public static AutoCrystalModule autoCrystal = Hub.field_2595.method_78(AutoCrystalModule.class);
   public static CombatmineModule combatMine = Hub.field_2595.method_78(CombatmineModule.class);

   public ExplosionDamage() {
      super();
   }

   public static float method_2(Vec3 var0, LivingEntity var1) {
      return method_2(var0, var1, 6.0, true, null);
   }

   public static float method_2(Vec3 var0, LivingEntity var1, AABB var2) {
      return method_2(var0, var1, var2, 6.0, true, null, null);
   }

   public static float method_9(Vec3 var0, LivingEntity var1) {
      return method_9(var0, var1, var1.getBoundingBox());
   }

   public static float method_9(Vec3 var0, LivingEntity var1, AABB var2) {
      return method_2(var0, var1, var2, 5.0, true, null, null);
   }

   public static float method_2(Vec3 var0, LivingEntity var1, double var2, boolean var4, BlockPos var5) {
      return method_2(var0, var1, var1.getBoundingBox(), var2, var4, var5, null);
   }

   public static float method_2(Vec3 var0, LivingEntity var1, AABB var2, double var3, boolean var5, BlockPos var6, BlockPos var7) {
      if (var1.equals(mc.player) && mc.player.isCreative()) {
         return 0.0F;
      } else {
         Vec3 var8 = new Vec3(
            Mth.lerp(Class_0245.field_688, var2.minX, var2.maxX), var2.minY, Mth.lerp(Class_0245.field_688, var2.minZ, var2.maxZ)
         );
         double var9 = var3 * 2.0;
         double var11 = var8.distanceTo(var0) / var9;
         if (var11 > 1.0) {
            return 0.0F;
         } else {
            float var13 = method_2(var0, var1, var2, var6, var7, var5);
            double var14 = (1.0 - var11) * (double)var13;
            float var16 = (float)(
               (var14 * var14 + var14) / 2.0 * 7.0 * var9
                  + 1.0
            );
            return Math.max(method_2(var1, method_5(var16)), 0.0F);
         }
      }
   }

   public static float method_2(Vec3 var0, Entity var1, BlockPos var2, boolean var3) {
      return method_2(var0, var1, var1.getBoundingBox(), var2, null, var3);
   }

   public static float method_2(Vec3 var0, Entity var1, AABB var2, BlockPos var3, BlockPos var4, boolean var5) {
      double var6 = 1.0
         / ((var2.maxX - var2.minX) * 2.0 + 1.0);
      double var8 = 1.0
         / ((var2.maxY - var2.minY) * 2.0 + 1.0);
      double var10 = 1.0
         / ((var2.maxZ - var2.minZ) * 2.0 + 1.0);
      double var12 = (1.0 - Math.floor(1.0 / var6) * var6)
         / 2.0;
      double var14 = (1.0 - Math.floor(1.0 / var10) * var10)
         / 2.0;
      if (!(var6 < 0.0) && !(var8 < 0.0) && !(var10 < 0.0)) {
         int var16 = 0;
         int var17 = 0;

         for (double var18 = 0.0; var18 <= 1.0; var18 += var6) {
            for (double var20 = 0.0; var20 <= 1.0; var20 += var8) {
               for (double var22 = 0.0; var22 <= 1.0; var22 += var10) {
                  double var24 = Mth.lerp(var18, var2.minX, var2.maxX);
                  double var26 = Mth.lerp(var20, var2.minY, var2.maxY);
                  double var28 = Mth.lerp(var22, var2.minZ, var2.maxZ);
                  Vec3 var30 = new Vec3(var24 + var12, var26, var28 + var14);
                  FilteredRaycastContext var31 = new RaycastBuilder(var30, var0)
                     .method_22(var1)
                     .method_2(
                        BlockFilter.method_2(Arrays.asList(var3, var3 == null ? null : combatMine.field_3712.method_111())),
                        BlockFilter.method_374(var4),
                        var5 ? BlockFilter.field_2969 : BlockFilter.field_2968
                     )
                     .method_565();
                  if (RaycastUtil.method_2(var31).getType() == Type.MISS) {
                     var16++;
                  }

                  var17++;
               }
            }
         }

         return (float)var16 / (float)var17;
      } else {
         return 0.0F;
      }
   }

   public static float method_2(LivingEntity var0, float var1) {
      DamageSource var2 = mc.level.damageSources().explosion(null);
      var1 = CombatRules.getDamageAfterAbsorb(var0, var1, var2, (float)var0.getArmorValue(), (float)var0.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
      if (var0.hasEffect(MobEffects.RESISTANCE)) {
         var1 = var1 * (float)(25 - (var0.getEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5) / 25F;
      }

      int var3 = 0;

      for (ItemStack var5 : java.util.List.of(var0.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.FEET), var0.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.LEGS), var0.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST), var0.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD))) {
         Map<net.minecraft.resources.ResourceKey<net.minecraft.world.item.enchantment.Enchantment>, Integer> var6 = EnchantmentUtil.method_7(var5);
         var3 += var6.getOrDefault(Enchantments.BLAST_PROTECTION, 0) * 2 + var6.getOrDefault(Enchantments.PROTECTION, 0);
      }

      if (var3 > 20 || autoCrystal.isToggled() && autoCrystal.assumeBestArmor.getValue()) {
         var3 = 20;
      }

      var1 *= 1F - (float)var3 / 25F;
      return var1 < 0.0F ? 0.0F : var1;
   }

   public static float method_5(float var0) {
      return switch (mc.level.getDifficulty()) {
         case PEACEFUL -> 0.0F;
         case EASY -> Math.min(var0 / 2F + 1F, var0);
         case HARD -> var0 * 3F / 2F;
         default -> var0;
      };
   }

   public static int method_5(ItemStack var0) {
      float var1 = ((float)var0.getMaxDamage() - (float)var0.getDamageValue()) / (float)var0.getMaxDamage();
      float var2 = 1F - var1;
      return Mth.clamp(100 - (int)Math.ceil((double)(var2 * 1e+02F)), 1, 100);
   }
}
