package me.mioclient.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Class_0388;
import me.mioclient.module.movement.ElytraFlyModule;
import net.minecraft.client.player.ClientInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

/**
 * Local-player world-state and movement helpers: cobweb/water/lava tests, rounded block
 * position, and movement direction derived from keyboard input.
 */
public class PlayerUtil implements MinecraftAccess {
   public PlayerUtil() {
      super();
   }

   public static boolean method_5(Player var0) {
      return mc.level.getBlockStates(var0.getBoundingBox()).anyMatch(var0x -> var0x.is(Blocks.COBWEB));
   }

   public static boolean method_7(Player var0) {
      return InteractionUtil.getId(BlockPos.containing(var0.getX(), var0.getBoundingBox().minY, var0.getZ())) == Blocks.WATER || var0.isInWater();
   }

   public static boolean method_29(Player var0) {
      return method_7(var0) || var0.isInLava();
   }

   public static BlockPos method_425() {
      return method_9(mc.player);
   }

   public static BlockPos method_9(LivingEntity var0) {
      return BlockPos.containing(var0.getX(), (double)Math.round(var0.getY()), var0.getZ());
   }

   public static Direction method_426() {
      ClientInput var0 = mc.player.input;
      double[] var1 = MovementUtil.method_2(mc.player.getYRot(), var0, 1.0);
      return Direction.fromYRot(Math.toDegrees(Math.atan2(var1[1], var1[0])) - (double)Class_0245.field_685);
   }

   public static List<BlockPos> method_5(LivingEntity var0) {
      ArrayList<BlockPos> var1 = new ArrayList<>();
      Set<BlockPos> var2 = method_7(var0);

      for (BlockPos var4 : var2) {
         for (Vec3i var8 : Class_0388.field_1258.method_440()) {
            BlockPos var9 = var4.offset(var8);
            if (!var2.contains(var9)) {
               var1.add(var9);
            }
         }
      }

      return var1;
   }

   public static Set<BlockPos> method_7(LivingEntity var0) {
      BlockPos var1 = method_9(var0);
      HashSet var2 = new HashSet();
      AABB var3 = var0.getBoundingBox();
      var3 = var3.inflate(-BoxUtil.field_2280);
      var2.add(BlockPos.containing(var3.minX, (double)var1.getY(), var3.minZ));
      var2.add(BlockPos.containing(var3.minX, (double)var1.getY(), var3.maxZ));
      var2.add(BlockPos.containing(var3.maxX, (double)var1.getY(), var3.minZ));
      var2.add(BlockPos.containing(var3.maxX, (double)var1.getY(), var3.maxZ));
      return var2;
   }

   public static boolean method_427() {
      return mc.player.getFoodData().getFoodLevel() < 6;
   }

   public static boolean method_29(LivingEntity var0) {
      for (ItemStack var2 : java.util.List.of(var0.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.FEET), var0.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.LEGS), var0.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST), var0.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD))) {
         if (!var2.isEmpty()) {
            return true;
         }
      }

      return false;
   }

   public static boolean method_4(Player var0) {
      return var0.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);
   }

   public static boolean method_428() {
      if (ElytraFlyModule.field_885) {
         return mc.player.isFallFlying();
      } else {
         ElytraFlyModule.field_885 = true;
         boolean var0 = mc.player.isFallFlying();
         ElytraFlyModule.field_885 = false;
         return var0;
      }
   }

   public static InteractionHand method_5(InteractionHand var0) {
      return var0 == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
   }
}
