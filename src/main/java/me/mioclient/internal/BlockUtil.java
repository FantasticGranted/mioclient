package me.mioclient.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;
import me.mioclient.Hub;
import me.mioclient.api.BlockFilter;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Dimension;
import me.mioclient.enum_.Class_1172;
import me.mioclient.module.combat.AutoCrystalModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

/**
 * World/block query helpers: raycasting and visibility, block-state tests (replaceable,
 * liquid, obsidian variants), mining-speed calculation and distance-sorted target selection.
 */
public class BlockUtil implements MinecraftAccess {
   public static final AutoCrystalModule field_3862 = Hub.field_2595.method_78(AutoCrystalModule.class);
   public static final List<Block> field_3863 = List.of(Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.GILDED_BLACKSTONE);
   public static final List<Block> field_3864 = List.of(
      Blocks.DEEPSLATE_BRICKS,
      Blocks.CRACKED_DEEPSLATE_BRICKS,
      Blocks.DEEPSLATE_TILES,
      Blocks.CRACKED_DEEPSLATE_TILES,
      Blocks.DEEPSLATE,
      Blocks.CHISELED_DEEPSLATE,
      Blocks.SCULK
   );
   public static final List<Block> field_3865 = List.of(
      Blocks.WAXED_OXIDIZED_COPPER, Blocks.WAXED_OXIDIZED_CUT_COPPER, Blocks.WAXED_COPPER_BLOCK, Blocks.WAXED_OXIDIZED_COPPER_GRATE
   );

   public BlockUtil() {
      super();
   }

   public static List<LevelChunk> method_1069() {
      ArrayList var0 = new ArrayList();
      int var1 = (Integer)mc.options.renderDistance().get();

      for (int var2 = -var1; var2 <= var1; var2++) {
         for (int var3 = -var1; var3 <= var1; var3++) {
            LevelChunk var4 = mc.level
               .getChunkSource()
               .getChunkNow((int)mc.player.getX() / 16 + var2, (int)mc.player.getZ() / 16 + var3);
            if (var4 != null) {
               var0.add(var4);
            }
         }
      }

      return var0;
   }

   public static List<BlockEntity> method_1070() {
      ArrayList var0 = new ArrayList();

      for (LevelChunk var2 : method_1069()) {
         if (!var2.getBlockEntities().isEmpty()) {
            try {
               var0.addAll(new ArrayList(var2.getBlockEntities().values()));
            } catch (Throwable var4) {
               field_3862.method_463();
               break;
            }
         }
      }

      return var0;
   }

   public static boolean method_2(ChunkAccess var0, BlockPos var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      BlockState var7 = var0.getBlockState(var1);
      if (var7.is(Blocks.CHEST)) {
         BlockState var8 = var0.getBlockState(var1.below());
         boolean var9 = var7.getValue(ChestBlock.TYPE) == ChestType.SINGLE;
         if (method_1071() == Dimension.THE_NETHER) {
            if (var2 && var9 && var8.is(Blocks.NETHER_BRICKS)) {
               return true;
            }

            return var3 && field_3863.contains(var8.getBlock());
         }

         if (method_1071() == Dimension.OVERWORLD) {
            if (!var9) {
               return false;
            }

            if (var6 && var1.getY() <= 34) {
               boolean var10 = var8.is(Blocks.CHISELED_TUFF_BRICKS);
               if (field_3865.contains(var8.getBlock()) || var10) {
                  boolean var11 = false;

                  for (Direction var15 : Direction.values()) {
                     if (var15.getAxis().isHorizontal()) {
                        BlockState var16 = var0.getBlockState(var1.relative(var15));
                        if (var10) {
                           if (!var16.is(Blocks.AIR)) {
                              var11 = true;
                              break;
                           }
                        } else if (field_3865.contains(var16.getBlock())) {
                           return true;
                        }
                     }
                  }

                  if (var10 && !var11) {
                     return true;
                  }
               }
            }

            if (var4 && var1.getY() <= -32 && var1.getY() >= -51 && field_3864.contains(var8.getBlock())) {
               return true;
            }

            return var5 && (var8.is(Blocks.MOSSY_COBBLESTONE) || var8.is(Blocks.COBBLESTONE));
         }
      }

      return false;
   }

   public static List<BlockPos> method_2(Vec3 var0, float var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      BlockPos var4 = BlockPos.containing(var0);

      for (float var5 = -var1; var5 < var1; var5 += 1F) {
         for (float var6 = -var1; var6 < var1; var6 += 1F) {
            for (float var7 = -var1; var7 < var1; var7 += 1F) {
               BlockPos var8 = var4.offset((int)var5, (int)var6, (int)var7);
               if (var4.closerThan(var8, (double)var1) && (!var2 || !mc.level.getBlockState(var8).isAir())) {
                  var3.add(var8);
               }
            }
         }
      }

      return var3;
   }

   public static Vec3 method_2(BlockPos var0, Class_1172 var1) {
      for (Vec3 var3 : var1.method_38(var0)) {
         if (method_9(var3)) {
            return var3;
         }
      }

      return null;
   }

   public static boolean method_2(Vec3i var0) {
      return method_9(Vec3.atCenterOf(var0));
   }

   public static boolean method_2(Collection<Vec3> var0) {
      for (Vec3 var2 : var0) {
         if (method_9(var2)) {
            return true;
         }
      }

      return false;
   }

   public static boolean method_9(Vec3 var0) {
      Vec3 var1 = mc.player.getEyePosition();
      BlockPos var2 = BlockPos.containing(var0);
      if (var0.distanceTo(var1) > 128.0) {
         return false;
      } else {
         FilteredRaycastContext var3 = new RaycastBuilder(mc.player.getEyePosition(), var0).method_2(BlockFilter.method_107(var2)).method_565();
         BlockHitResult var4 = RaycastUtil.method_2(var3);
         if (var4.getType() == Type.MISS || var4.getLocation().equals(var0)) {
            return true;
         } else {
            if (var4 instanceof BlockHitResult var5 && var5.getBlockPos().equals(var2)) {
               return true;
            }

            return false;
         }
      }
   }

   public static boolean method_2(Vec3 var0, Vec3 var1) {
      Vec3 var2 = mc.player.getEyePosition();
      if (var0.distanceTo(var2) > 128.0) {
         return false;
      } else if (mc.level.clip(new ClipContext(var2, var0, net.minecraft.world.level.ClipContext.Block.COLLIDER, Fluid.NONE, mc.player)).getType() == Type.MISS) {
         return true;
      } else {
         return mc.player.getY() >= var0.y
            ? mc.level.clip(new ClipContext(var2, var1, net.minecraft.world.level.ClipContext.Block.COLLIDER, Fluid.NONE, mc.player)).getType() == Type.MISS
            : false;
      }
   }

   public static boolean method_3(BlockPos var0) {
      if (var0 == null) {
         return false;
      } else {
         BlockState var1 = mc.level.getBlockState(var0);
         if (!mc.player.isCreative() && var1.getDestroySpeed(mc.level, var0) < 0.0F) {
            return false;
         } else {
            return var1.isAir() ? false : var1.getShape(mc.level, var0) != Shapes.empty();
         }
      }
   }

   public static double method_2(ItemStack var0, BlockState var1, boolean var2) {
      float var3 = var1.getDestroySpeed(null, null);
      return var3 == -1F
         ? 0.0
         : method_9(var0, var1, var2) / (double)var3 / (double)(var1.requiresCorrectToolForDrops() && !var0.isCorrectToolForDrops(var1) ? 100 : 30);
   }

   public static double method_9(ItemStack var0, BlockState var1, boolean var2) {
      double var3 = (double)var0.getDestroySpeed(var1);
      if (var3 > 1.0) {
         int var5 = EnchantmentUtil.method_2(Enchantments.EFFICIENCY, var0);
         if (var5 > 0 && !var0.isEmpty()) {
            var3 += (double)(var5 * var5 + 1);
         }
      }

      if (MobEffectUtil.hasDigSpeed(mc.player)) {
         var3 *= (double)(
            1F + (float)(MobEffectUtil.getDigSpeedAmplification(mc.player) + 1) * 0.2F
         );
      }

      if (mc.player.hasEffect(MobEffects.MINING_FATIGUE)) {
         float var6 = switch (mc.player.getEffect(MobEffects.MINING_FATIGUE).getAmplifier()) {
            case 0 -> 0.3F;
            case 1 -> 0.09F;
            case 2 -> 0.0027F;
            default -> 0.00081F;
         };
         var3 *= (double)var6;
      }

      if (mc.player.isEyeInFluid(FluidTags.WATER) && !EnchantmentUtil.method_9(Enchantments.AQUA_AFFINITY, EquipmentSlot.HEAD)) {
         var3 /= 5.0;
      }

      if (!var2) {
         var3 /= 5.0;
      }

      return var3;
   }

   public static Dimension method_1071() {
      if (mc.level == null) {
         return Dimension.OVERWORLD;
      } else {
         String var0 = mc.level.dimension().identifier().getPath();

         return switch (var0) {
            case "the_nether" -> Dimension.THE_NETHER;
            case "the_end" -> Dimension.THE_END;
            default -> Dimension.OVERWORLD;
         };
      }
   }

   public static Entity method_2(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, float var6, float var7) {
      return method_2(var0, var1, var2, var3, var4, var5, var6, var7, false, false);
   }

   public static Entity method_2(
      boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, float var6, float var7, boolean var8, boolean var9
   ) {
      return method_2(var0, var1, var2, var3, var4, var5, var6, var7, false, false, Comparator.comparing(mc.player::distanceToSqr), null);
   }

   public static Entity method_2(
      boolean var0,
      boolean var1,
      boolean var2,
      boolean var3,
      boolean var4,
      boolean var5,
      float var6,
      float var7,
      boolean var8,
      boolean var9,
      Comparator<Entity> var10,
      Predicate<Entity> var11
   ) {
      Entity var12 = StreamSupport.<Entity>stream(mc.level.entitiesForRendering().spliterator(), false)
         .filter(
            var7x -> !(var7x instanceof ThrownExperienceBottle)
                  && method_78(var7x)
                  && (
                     var7x instanceof Player && var0 && (!var9 || PlayerUtil.method_29((LivingEntity)((Player)var7x)))
                        || EntityUtil.method_5(var7x) && var2
                        || EntityUtil.method_9(var7x) && var3
                        || var7x instanceof Piglin && var1
                        || var7x instanceof NeutralMob && var1
                        || var7x instanceof EndCrystal && var4
                        || var7x instanceof Projectile && var5 && var7x.isAttackable()
                  )
         )
         .filter(var2x -> method_39(var2x) <= (double)(RaycastUtil.getId(var2x) ? var7 : var6))
         .filter(var1x -> var11 == null || var11.test(var1x))
         .min(var10)
         .orElse(null);
      if (var12 != null && !(var12 instanceof Player) && var12.hasCustomName() && var8) {
         var12 = null;
      }

      return var12;
   }

   public static boolean method_78(Entity var0) {
      if (var0 == mc.player) {
         return false;
      } else {
         if (var0 instanceof Player var1 && Hub.field_2603.getId(var1)) {
            return false;
         }

         return var0.isAlive();
      }
   }

   public static double method_39(Entity var0) {
      AABB var1 = var0.getBoundingBox();
      Vec3 var2 = BoxUtil.method_4(mc.player).getBottomCenter().add(0.0, (double)mc.player.getEyeHeight(), 0.0);
      if (!(var0 instanceof EnderDragon var3)) {
         return method_2(var2, var1);
      } else {
         double var4 = 9999.0;

         for (EnderDragonPart var9 : var3.getSubEntities()) {
            var4 = Math.min(var4, method_2(var2, var9.getBoundingBox()));
         }

         return var4;
      }
   }

   public static double method_2(Vec3 var0, AABB var1) {
      return var0.distanceTo(BoxUtil.method_9(var0, var1));
   }

   public static boolean method_36(BlockPos var0) {
      return mc.level.getBlockState(var0).getBlock() == Blocks.BEDROCK || mc.level.getBlockState(var0).getBlock() == Blocks.OBSIDIAN;
   }

   public static boolean method_14(BlockPos var0) {
      return mc.level.getBlockState(var0).isAir();
   }

   public static boolean method_17(BlockPos var0) {
      return !mc.level.getFluidState(var0).isEmpty();
   }

   public static boolean method_37(BlockPos var0) {
      return Hub.field_2622.method_105(var0);
   }

   public static Block method_33(BlockPos var0) {
      return mc.level.getBlockState(var0).getBlock();
   }

   public static boolean method_2(HitResult var0) {
      if (var0 == null) {
         return false;
      } else {
         if (var0 instanceof BlockHitResult var1) {
            BlockPos var2 = var1.getBlockPos();
            BlockState var3 = mc.level.getBlockState(var2);
            if (var3.hasBlockEntity()) {
               return true;
            }

            if (InteractionUtil.method_2(var3.getBlock())) {
               return true;
            }
         }

         if (!(var0 instanceof EntityHitResult var4)) {
            return false;
         } else {
            Entity var5 = var4.getEntity();
            return var5 instanceof VehicleEntity || var5 instanceof Villager;
         }
      }
   }
}
