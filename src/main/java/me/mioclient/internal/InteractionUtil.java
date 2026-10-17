package me.mioclient.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Class_1010;
import me.mioclient.enum_.Class_1172;
import me.mioclient.mixin.ducks.DuckAbstractBlock;
import me.mioclient.mixin.ducks.DuckLivingEntity;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.module.misc.KillEffectsModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.ExperienceBottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import me.mioclient.internal.Inputs;

/**
 * Block/entity interaction helpers: place, break and interact with blocks (doors,
 * buttons, chests...), compute placeable faces, and swap/attack via the interaction manager.
 */
public class InteractionUtil implements MinecraftAccess {
   public static final Timer field_3195 = new Timer();
   public static boolean field_3196 = false;
   public static final Direction[] field_3197 = new Direction[]{Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP};
   public static AbstractModule_28 field_144 = Hub.field_2595.method_78(AbstractModule_28.class);
   public static KillEffectsModule field_3198 = Hub.field_2595.method_78(KillEffectsModule.class);

   public InteractionUtil() {
      super();
   }

   public static Direction method_78(BlockPos var0) {
      return method_9(var0, false);
   }

   public static Direction method_9(BlockPos var0, boolean var1) {
      return method_2(var0, var1, false);
   }

   public static Direction method_2(BlockPos var0, boolean var1, boolean var2) {
      for (Direction var6 : field_3197) {
         if (var0.offset(var6.getUnitVec3i()).getY() < mc.level.getMaxY()) {
            BlockPos var7 = var0.relative(var6);
            if ((!var2 || BlockUtil.method_2(Class_1172.field_3634.method_2(var7, var6.getOpposite())))
               && Hub.field_2622.method_105(var7)
               && (!var1 || method_39(var7).contains(var6.getOpposite()))) {
               return var6;
            }
         }
      }

      return null;
   }

   public static List<Direction> method_39(BlockPos var0) {
      ArrayList var1 = new ArrayList<>(List.of(Direction.values()));

      for (Direction var5 : Direction.values()) {
         BlockPos var6 = var0.relative(var5);
         BlockState var7 = mc.level.getBlockState(var6);
         boolean var8 = !var7.canBeReplaced() && var7.isSolidRender();
         if (var8) {
            var1.remove(var5);
         }
      }

      if (var0.getY() + 1 >= mc.level.getMaxY()) {
         var1.remove(Direction.UP);
      }

      double var9 = mc.player.getEyePosition().x - var0.getCenter().x;
      double var10 = mc.player.getEyePosition().z - var0.getCenter().z;
      if (var9 > Class_0245.field_688) {
         var1.remove(Direction.WEST);
      } else if (var9 < -Class_0245.field_688) {
         var1.remove(Direction.EAST);
      } else {
         var1.remove(Direction.WEST);
         var1.remove(Direction.EAST);
      }

      if (var10 > Class_0245.field_688) {
         var1.remove(Direction.NORTH);
      } else if (var10 < -Class_0245.field_688) {
         var1.remove(Direction.SOUTH);
      } else {
         var1.remove(Direction.NORTH);
         var1.remove(Direction.SOUTH);
      }

      if (mc.player.getEyePosition().y < (double)(var0.getY() + (field_144.strictDirection.getValue() == Class_1010.GRIM ? 1 : 0))) {
         var1.remove(Direction.UP);
      }

      if (mc.player.getEyePosition().y > (double)var0.getY()) {
         var1.remove(Direction.DOWN);
      }

      return var1;
   }

   public static BlockPos method_2(BlockPos var0, int var1, boolean var2, boolean var3) {
      return method_2(var0, var1, var2, var3, var0x -> true);
   }

   public static BlockPos method_2(BlockPos var0, int var1, boolean var2, boolean var3, Predicate<BlockPos> var4) {
      for (int var5 = 1; var5 <= var1; var5++) {
         for (Direction var9 : field_3197) {
            BlockPos var10 = var0.relative(var9, var5);
            if (var4.test(var10)
               && method_9(var10, var3) != null
               && mc.level.getEntitiesOfClass(Player.class, new AABB(var10), var1x -> var2).isEmpty()) {
               return var10;
            }
         }
      }

      return null;
   }

   public static boolean method_5(BlockPos var0, boolean var1) {
      return method_2(var0, var1, InteractionHand.MAIN_HAND);
   }

   public static boolean method_2(BlockPos var0, boolean var1, InteractionHand var2) {
      return method_2(var0, null, method_78(var0), var1, var2);
   }

   public static boolean method_2(BlockPos var0, Vec3 var1, Direction var2, boolean var3, InteractionHand var4) {
      if (mc.level != null && mc.player != null && mc.gameMode != null) {
         Block var6 = mc.player.getItemInHand(var4).getItem() instanceof BlockItem var7 ? var7.getBlock() : null;
         if (!method_2(var0, var6, false)) {
            return false;
         } else {
            boolean var11 = var3 && var2 == null;
            if (var2 == null) {
               if (!var3) {
                  return false;
               }

               var2 = mc.hitResult instanceof BlockHitResult var8 ? var8.getDirection() : Direction.DOWN;
            }

            BlockPos var12 = var11 ? var0 : var0.relative(var2);
            boolean var13 = method_31(var12);
            if (var1 == null) {
               var1 = var0.getCenter().add(Vec3.atLowerCornerOf(var2.getUnitVec3i()).scale(Class_0245.field_688));
            }

            if (var13) {
               mc.player.connection.send(Inputs.shift(true));
            }

            field_3196 = true;
            InteractionResult var10 = mc.gameMode
               .useItemOn(mc.player, var4, new BlockHitResult(var1, var11 ? var2 : var2.getOpposite(), var12, false));
            field_3196 = false;
            if (var10.consumesAction()) {
               mc.player.swing(var4);
            }

            if (var13) {
               mc.player.connection.send(Inputs.shift(false));
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean method_2(BlockPos var0, Direction var1, boolean var2, InteractionHand var3) {
      return method_9(var0, null, var1, var2, var3);
   }

   public static boolean method_9(BlockPos var0, Vec3 var1, Direction var2, boolean var3, InteractionHand var4) {
      if (mc.level != null && mc.player != null && mc.gameMode != null) {
         if (!method_2(var0, mc.player.getItemInHand(var4).getItem() instanceof BlockItem var5 ? var5.getBlock() : null, false)) {
            return false;
         } else {
            boolean var8 = var3 && var2 == null;
            if (var2 == null) {
               List var9 = method_39(var0);
               if (!var3) {
                  return false;
               }

               var2 = var9.isEmpty() ? Direction.DOWN : (Direction)var9.getFirst();
            }

            BlockPos var10 = var8 ? var0 : var0.relative(var2);
            boolean var7 = method_31(var10);
            if (var7) {
               mc.player.connection.send(Inputs.shift(true));
            }

            if (var1 == null) {
               var1 = var0.getCenter().add(Vec3.atLowerCornerOf(var2.getUnitVec3i()).scale(Class_0245.field_688));
            }

            PacketSender.method_2(var4, new BlockHitResult(var1, var8 ? var2 : var2.getOpposite(), var10, false));
            if (field_144.method_1052() && field_144.movementSync.getValue()) {
               if (!mc.player.swinging
                  || mc.player.swingTime >= ((DuckLivingEntity)(Object)mc.player).getSwingDuration() / 2
                  || mc.player.swingTime < 0) {
                  mc.player.swingTime = -1;
                  mc.player.swinging = true;
                  mc.player.swingingArm = var4;
               }
            } else {
               mc.player.swing(var4);
            }

            if (var7) {
               mc.player.connection.send(Inputs.shift(false));
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean method_2(BlockPos var0, Block var1, boolean var2) {
      return method_2(var0, var1, var2, false);
   }

   public static boolean method_2(BlockPos var0, Block var1, boolean var2, boolean var3) {
      if (!mc.level.getBlockState(var0).canBeReplaced() || !mc.level.getWorldBorder().isWithinBounds(var0) || !Level.isInSpawnableBounds(var0)) {
         return false;
      } else if (var0.getY() < mc.level.getMaxY() && var0.getY() >= mc.level.getMinY()) {
         VoxelShape var4 = var1 == null ? Shapes.empty() : var1.defaultBlockState().getCollisionShape(mc.level, BlockPos.ZERO);
         AABB var5 = var4.isEmpty() ? new AABB(var0) : BoxUtil.method_2(var4.bounds(), var0);
         if (var1 != null && !((DuckAbstractBlock)(Object)var1).isCollidable()) {
            return true;
         } else {
            for (Entity var7 : mc.level
               .getEntitiesOfClass(
                  Entity.class,
                  var5,
                  var0x -> !(var0x instanceof ThrownExperienceBottle)
                        && !(var0x instanceof ItemEntity)
                        && !(var0x instanceof ItemFrame)
                        && !(var0x instanceof ExperienceOrb)
               )) {
               if ((BoxUtil.method_2(var5, var7.getBoundingBox()) || var7 == mc.player) && !(var7 instanceof Arrow)) {
                  if (!(var7 instanceof Player)) {
                     if (!(var7 instanceof EndCrystal) || !var3) {
                        return !var2;
                     }
                  } else if (var7.getBoundingBox().intersects(var5)) {
                     return false;
                  }
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean method_7(BlockPos var0, boolean var1) {
      return method_9(var0, var1, false);
   }

   public static boolean method_9(BlockPos var0, boolean var1, boolean var2) {
      if (mc.level.getBlockState(var0).canBeReplaced() && mc.level.getWorldBorder().isWithinBounds(var0)) {
         for (Entity var4 : mc.level
            .getEntitiesOfClass(
               Entity.class,
               new AABB(var0),
               var0x -> !(var0x instanceof ThrownExperienceBottle) && !(var0x instanceof ItemEntity) && !(var0x instanceof ExperienceOrb)
            )) {
            if ((BoxUtil.method_2(new AABB(var0), var4.getBoundingBox()) || var4 == mc.player)
               && (!(var4 instanceof EndCrystal) || !var2)
               && !(var4 instanceof Arrow)) {
               if (var4 instanceof Player) {
                  return false;
               }

               if (var1) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean method_2(BlockPos var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      if (!BlockUtil.method_36(var0)) {
         return false;
      } else {
         BlockPos var7 = var0.offset(0, 1, 0);
         Block var8 = mc.level.getBlockState(var7).getBlock();
         if (var4) {
            if (var8 != Blocks.AIR && var8 != Blocks.FIRE) {
               return false;
            }
         } else if (var8 != Blocks.AIR) {
            return false;
         }

         if (var1 && mc.level.getBlockState(var0.offset(0, 2, 0)).getBlock() != Blocks.AIR) {
            return false;
         } else {
            AABB var9 = new AABB(
               (double)var7.getX(),
               (double)var7.getY(),
               (double)var7.getZ(),
               (double)(var7.getX() + 1),
               (double)var7.getY() + (var5 ? 0.9 : 2.0),
               (double)(var7.getZ() + 1)
            );
            int var10 = 0;

            for (Entity var12 : mc.level.entitiesForRendering()) {
               if (!var12.isRemoved() && var12.isAlive()) {
                  AABB var13 = var12.getBoundingBox();
                  if (var12 instanceof Player && var12 != mc.player && var6) {
                     var13 = var13.inflate(0.01);
                  } else if (var12 instanceof EndCrystal && !var3) {
                     continue;
                  }

                  if (var13.intersects(var9)) {
                     var10++;
                     break;
                  }
               }
            }

            return !var2 || var10 == 0;
         }
      }
   }

   public static Block getId(BlockPos var0) {
      return var0 == null ? null : mc.level.getBlockState(var0).getBlock();
   }

   public static boolean method_16(BlockPos var0) {
      return ((DuckAbstractBlock)(Object)getId(var0)).isCollidable();
   }

   public static Entity method_2(BlockPos var0, int var1) {
      for (Entity var3 : mc.level.entitiesForRendering()) {
         if (var3 instanceof EndCrystal && var3.getBoundingBox().intersects(new AABB(var0)) && var3.tickCount >= var1) {
            method_29(var3);
            return var3;
         }
      }

      return null;
   }

   public static void method_29(Entity var0) {
      method_2(var0, false);
   }

   public static void method_2(Entity var0, boolean var1) {
      if (Hub.field_2623.method_80(var0.getId())) {
         if (field_3198.isToggled() && var0 instanceof Player) {
            field_3198.method_185((Player)var0);
         }

         mc.player.resetAttackStrengthTicker();
      }
   }

   public static void method_2(Entity var0, InteractionHand var1) {
      mc.gameMode.interact(mc.player, var0, new EntityHitResult(var0), var1);
      mc.player.swing(var1);
   }

   public static boolean method_2(Block var0) {
      return var0 instanceof CraftingTableBlock
         || var0 instanceof AnvilBlock
         || var0 instanceof ButtonBlock
         || var0 instanceof BasePressurePlateBlock
         || var0 instanceof BaseEntityBlock
         || var0 instanceof BedBlock
         || var0 instanceof FenceGateBlock
         || var0 instanceof DoorBlock
         || var0 instanceof NoteBlock
         || var0 instanceof TrapDoorBlock;
   }

   public static boolean method_31(BlockPos var0) {
      BlockState var1 = mc.level.getBlockState(var0);
      Block var2 = var1.getBlock();
      if (var2 == Blocks.RESPAWN_ANCHOR) {
         return true;
      } else {
         return var1.hasBlockEntity()
            ? true
            : var2 instanceof BedBlock
               || var2 instanceof AbstractChestBlock
               || var2 instanceof DoorBlock
               || var2 instanceof TrapDoorBlock
               || var2 instanceof ButtonBlock
               || var2 instanceof ComposterBlock
               || var2 instanceof NoteBlock
               || var2 instanceof AnvilBlock
               || var2 instanceof AbstractFurnaceBlock
               || var2 instanceof CraftingTableBlock
               || var2 instanceof EnchantingTableBlock
               || var2 instanceof CakeBlock;
      }
   }

   public Direction method_2(Direction var1, int var2) {
      return Direction.values()[2 + (var1.get3DDataValue() + var2) % Direction.values().length];
   }

   public static boolean method_2(Item var0) {
      return var0 instanceof EggItem
         || var0 instanceof SnowballItem
         || var0 instanceof EnderpearlItem
         || var0 instanceof ExperienceBottleItem
         || var0 instanceof ThrowablePotionItem
         || var0 instanceof TridentItem;
   }

   public static void method_873() {
      field_3195.reset();
   }

   public static boolean method_932() {
      return !field_3195.method_9(50L);
   }
}
