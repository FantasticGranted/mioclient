package me.mioclient.internal;

import me.mioclient.api.AttackingAccess;
import me.mioclient.api.MinecraftAccess;
import net.minecraft.client.CameraType;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;

/**
 * Entity helper predicates: effective health, hostile/angry detection across mob types,
 * water/passive checks, on-ground test, camera-entity check and step-height override.
 */
public class EntityUtil implements MinecraftAccess {
   public EntityUtil() {
      super();
   }

   public static float method_76() {
      return method_2((Entity) mc.player);
   }

   public static float method_2(Entity var0) {
      return var0 instanceof LivingEntity var1 ? var1.getHealth() + var1.getAbsorptionAmount() : 2F;
   }

   public static boolean method_9(Entity var0) {
      if (var0 instanceof EnderMan var6) {
         return var6.isCreepy();
      } else if (var0 instanceof AttackingAccess var5) {
         return var5.mio$isAttacking();
      } else if (var0 instanceof Wolf var4) {
         Holder<WolfVariant> var18 = var4.get(DataComponents.WOLF_VARIANT);
         return var18 != null && var4.getTexture().equals(var18.value().adultInfo().angry().texturePath());
      } else if (var0 instanceof NeutralMob var3) {
         return var3.isAngry();
      } else if (!(var0 instanceof AbstractPiglin var1)) {
         return var0 instanceof PolarBear ? true : var0 instanceof Enemy;
      } else {
         PiglinArmPose var2 = var1.getArmPose();
         return var2 == PiglinArmPose.CROSSBOW_CHARGE || var2 == PiglinArmPose.CROSSBOW_HOLD || var2 == PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON;
      }
   }

   public static boolean method_5(Entity var0) {
      return var0 instanceof AbstractFish || var0 instanceof AgeableMob || var0 instanceof Squid || var0 instanceof Dolphin;
   }

   public static boolean method_2(LivingEntity var0) {
      return !mc.level.noBlockCollision(var0, var0.getBoundingBox().expandTowards(0.0, -0.1, 0.0));
   }

   public static boolean method_7(Entity var0) {
      if (mc.options.getCameraType() != CameraType.FIRST_PERSON) {
         return false;
      } else {
         return var0 == mc.player ? false : mc.crosshairPickEntity == var0;
      }
   }

   public static void method_9(LivingEntity var0, float var1) {
      var0.getAttribute(Attributes.STEP_HEIGHT).setBaseValue((double)var1);
   }
}
