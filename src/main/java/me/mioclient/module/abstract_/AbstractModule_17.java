package me.mioclient.module.abstract_;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Event_50;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BoxUtil;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.module.Category;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import me.mioclient.nick.Settings;

public class AbstractModule_17 extends AbstractModule_32 {
   public Setting<Boolean> sequential;
   public Setting<Boolean> center;
   public Setting<Boolean> flatten;
   public Setting<Boolean> onlyObby;
   public final Timer field_2104;
   public boolean field_2105;

   public AbstractModule_17() {
      super("FeetPlace", "Surrounds your feet with obby.", Category.COMBAT, "surround");
      Settings.initialize(this);
      this.field_2104 = new Timer();
      this.method_5(true);
   }

   @Override
   public void onEnable() {
      if (!this.method_535()) {
         this.method_662();
      }

      super.onEnable();
      this.field_2104.reset();
   }

   @Override
   public List<BlockPos> method_17() {
      List var1 = this.method_2(new ArrayList<>(), this.method_425(), 0);
      var1.sort(this.method_661());
      return var1;
   }

   @Override
   public int method_52() {
      return 999;
   }

   @Override
   public int method_34() {
      return this.onlyObby.getValue() ? InventoryUtil.method_5(Items.OBSIDIAN) : super.method_34();
   }

   @Override
   public void method_33() {
      if (!mc.player.isPassenger()) {
         this.field_2105 = !Hub.field_2602.method_985().method_9(100L);
         if (this.field_2105) {
            this.field_609 = mc.player.getY();
         }

         try {
            super.method_33();
         } catch (Throwable var2) {
         }
      }
   }

   @Override
   public void method_2(List<BlockPos> var1) {
   }

   @Override
   public boolean method_107() {
      return this.field_2105 ? false : super.method_107();
   }

   @Subscribe
   public void method_9(Event_50 var1) {
      if (this.sequential.getValue()) {
         this.method_33();
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundPlayerPositionPacket var2
         && mc.player.position().distanceToSqr(new Vec3(var2.change().position().x(), var2.change().position().y(), var2.change().position().z())) > 9.0) {
         this.field_609 = var2.change().position().y();
         Hub.field_2602.method_985().reset();
      }
   }

   public List<BlockPos> method_2(List<Integer> var1, BlockPos var2, int var3) {
      int var4 = this.method_34();
      BlockState var5 = Blocks.OBSIDIAN.defaultBlockState();
      if (var4 != -1 && mc.player.getInventory().getItem(var4).getItem() instanceof BlockItem var6) {
         var5 = var6.getBlock().defaultBlockState();
      }

      ArrayList var15 = new ArrayList();
      var1.add(var2.hashCode());

      for (Vec3 var9 : this.method_468(var3)) {
         BlockPos var10 = var2.offset(BlockPos.containing(var9));
         if (var9.y != -1.0
            || (!Hub.field_2605.method_221(var10) || mc.player.onGround())
               && this.flatten.getValue()
               && !mc.player.isVisuallySwimming()
               && !var10.equals(Hub.field_2622.method_344())) {
            VoxelShape var11 = var5.getCollisionShape(mc.level, BlockPos.ZERO);
            AABB var12 = var11.isEmpty() ? new AABB(var10) : BoxUtil.method_2(var11.bounds(), var10);
            this.method_7(var10);
            if (!mc.level.getBlockState(var10).canBeReplaced()) {
               boolean var13 = BoxUtil.method_2(mc.player.getBoundingBox(), new AABB(var10));
               if (!var13) {
                  continue;
               }
            }

            if (!mc.level
                  .getEntitiesOfClass(LivingEntity.class, var12, var1x -> var1x == mc.player || BoxUtil.method_2(var12, var1x.getBoundingBox()))
                  .isEmpty()
               && !var1.contains(var10.hashCode())
               && var9.y == (double)var3) {
               var15.addAll(this.method_2(var1, var10, var3));
            } else if (mc.level.getBlockState(var10).canBeReplaced()) {
               Direction var17 = InteractionUtil.method_9(var10, this.strictDirection.getValue());
               if (var17 == null) {
                  BlockPos var14 = InteractionUtil.method_2(var10, 2, true, this.strictDirection.getValue());
                  if (var14 != null) {
                     var15.add(var14);
                  }
               }

               var15.add(var10);
            }
         }
      }

      return var15;
   }

   public List<Vec3> method_468(int var1) {
      return Arrays.asList(
         new Vec3(-1.0, (double)var1, 0.0),
         new Vec3(1.0, (double)var1, 0.0),
         new Vec3(0.0, (double)var1, 1.0),
         new Vec3(0.0, (double)var1, -1.0),
         new Vec3(0.0, (double)(var1 - 1), 0.0)
      );
   }

   public Comparator<BlockPos> method_661() {
      return Hub.field_2614.method_876() <= 1e+01
         ? Comparator.comparing(var1 -> this.method_2(mc.player.getYRot(), var1))
         : Comparator.comparing(var1 -> this.method_9(MovementUtil.method_496(), var1));
   }

   public float method_2(float var1, BlockPos var2) {
      return var2.getY() == PlayerUtil.method_425().getY() && this.method_38(mc.player)
         ? (float)PlayerUtil.method_425().distToLowCornerSqr((double)var2.getX(), (double)var2.getY(), (double)var2.getZ())
         : this.method_9(var1, var2);
   }

   public float method_9(float var1, BlockPos var2) {
      return Mth.degreesDifferenceAbs(var1, RotationManager.method_78(var2.getCenter())[0]) + (float)var2.getY() * 0.001F;
   }

   public void method_662() {
      if (!this.method_535()
         && this.center.getValue()
         && mc.player.onGround()
         && !Hub.field_2605.method_224()
         && PlayerUtil.method_5((LivingEntity)mc.player).size() > 4) {
         Vec3 var1 = new Vec3(
            Math.floor(mc.player.getX()) + 0.5,
            mc.player.getY(),
            Math.floor(mc.player.getZ()) + 0.5
         );
         mc.player
            .setDeltaMovement(
               (var1.x - mc.player.getX()) * 0.5,
               mc.player.getDeltaMovement().y(),
               (var1.z - mc.player.getZ()) * 0.5
            );
         mc.player.setPos(var1);
      }
   }

   public boolean method_38(LivingEntity var1) {
      for (BlockPos var3 : PlayerUtil.method_7(var1)) {
         VoxelShape var4 = mc.level.getBlockState(var3).getCollisionShape(mc.level, BlockPos.ZERO);
         if (!var4.isEmpty()) {
            AABB var5 = var4.bounds();
            if (!BoxUtil.method_7(var5)) {
               return true;
            }
         }
      }

      return false;
   }
}
