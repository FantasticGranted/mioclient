package me.mioclient.module.combat;

import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.enum_.Class_1172;
import me.mioclient.enum_.Priority;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.Class_0819;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.Class_1149;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.ChatUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.record.BlockHitInfo;
import me.mioclient.record.DirectionalBlockPos;
import me.mioclient.record.RenderStyle;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class PusherModule extends Module {
   public Setting<Float> range;
   public Setting<Integer> delay;
   public Setting<Boolean> strictDirection;
   public Setting<Boolean> rotate;
   public Setting<Boolean> raytrace;
   public Setting<Boolean> multiTask;
   public Setting<Boolean> autoDisable;
   public Setting<Boolean> targeting;
   public Setting<Boolean> ignoreNaked;
   public Setting<Boolean> onlySafe;
   public Setting<Boolean> onlyHole;
   public Setting<Boolean> render;
   public Setting<Color> fill;
   public Setting<Color> outline;
   public Setting<Float> lineWidth;
   public Setting<Boolean> fade;
   public Setting<Float> fadeTime;
   public final Timer field_3018;
   public Player field_2290;
   public boolean field_3019;
   public BlockPos field_3020;
   public boolean field_3021;
   public final Class_1149 field_3022;
   public final Class_0819 field_3023;

   public PusherModule() {
      super("Pusher", "Pushes your enemies out of their holes.", Category.COMBAT);
      Settings.initialize(this);
      this.field_3018 = new Timer();
      this.field_3022 = new Class_1149();
      this.field_3023 = new Class_0819(this);
      Hub.field_2616.method_2(new RenderStyle(this, this.fill, this.outline, this.lineWidth, this.fadeTime, () -> true, this.fade, 450));
   }

   @Override
   public void onEnable() {
      this.field_3021 = false;
   }

   @Override
   public String getInfo() {
      return this.field_2290 != null ? this.field_2290.getGameProfile().name() : super.getInfo();
   }

   @Subscribe
   public void method_5(Event_7 var1) {
      if (mc.isLocalServer()) {
         if (!this.field_3021) {
            Hub.field_2619
               .method_2(() -> ChatUtil.method_2(Component.literal("Pusher doesn't work in singleplayer."), ChatUtil.method_38(-2), Priority.HIGH), 1);
            this.field_3021 = true;
         }
      } else if (!mc.player.isUsingItem() || this.multiTask.getValue()) {
         this.field_2290 = this.field_3023.method_691();
         if (this.field_2290 != null) {
            if (this.autoDisable.getValue()) {
               boolean var2 = BlockPos.betweenClosedStream(this.field_2290.getBoundingBox())
                  .anyMatch(var0 -> mc.level.getBlockState(var0).is(Blocks.PISTON_HEAD) || mc.level.getBlockState(var0).is(Blocks.PISTON));
               if (var2) {
                  this.disable();
                  return;
               }
            }

            if (this.field_3018.method_9((long)this.delay.getValue().intValue())) {
               this.field_3019 = false;
               this.field_3018.reset();
               this.method_882();
               this.method_883();
            }
         }
      }
   }

   public void method_882() {
      int var1 = InventoryUtil.method_5(Items.REDSTONE_BLOCK);
      boolean var2 = false;
      if (var1 == -1) {
         var1 = InventoryUtil.method_5(Items.REDSTONE_TORCH);
         var2 = true;
      }

      int var3 = mc.player.getInventory().selected;
      if (var1 != -1) {
         DirectionalBlockPos var4 = null;

         for (Direction var6 : Plane.HORIZONTAL) {
            BlockPos var7 = this.field_2290.blockPosition().above().relative(var6);
            if (BlockUtil.method_33(var7) instanceof PistonBaseBlock && !this.method_2(this.field_2290, var7, var6)) {
               var4 = this.method_2(var2, var7);
            }
         }

         if (var4 != null) {
            this.field_3019 = true;
            if (this.rotate.getValue()) {
               Hub.field_2598.method_2(RotationManager.method_2(var4.getPos().getCenter(), var4.getDirection()), 500);
            }

            InventoryUtil.method_16(var1);
            InteractionUtil.method_2(var4.getPos(), var4.getDirection(), false, InteractionHand.MAIN_HAND);
            PacketSender.method_9(InteractionHand.MAIN_HAND);
            InventoryUtil.method_16(var3);
            if (this.render.getValue()) {
               Hub.field_2616.method_2(this, var4.getPos());
            }
         }
      }
   }

   public void method_883() {
      if (!this.field_3019) {
         int var1 = InventoryUtil.method_2(Items.PISTON, Items.STICKY_PISTON);
         int var2 = mc.player.getInventory().selected;
         if (var1 != -1) {
            BlockHitInfo var3 = null;

            for (Direction var5 : Plane.HORIZONTAL) {
               BlockPos var6 = this.field_2290.blockPosition().above().relative(var5);
               if (!this.method_2(this.field_2290, var6, var5)) {
                  if (this.method_632(var6) == var5.getOpposite()) {
                     var3 = null;
                     break;
                  }

                  if (InteractionUtil.method_7(var6, true)) {
                     BlockHitInfo var7 = this.method_78(var6, var5.getOpposite());
                     if (var7 != null) {
                        var3 = var7;
                     }
                  }
               }
            }

            if (var3 != null) {
               this.field_3020 = var3.getPos();
               Hub.field_2598.method_2(RotationManager.method_78(var3.method_564()), 500);
               InventoryUtil.method_16(var1);
               InteractionUtil.method_2(var3.getPos(), var3.getDirection(), false, InteractionHand.MAIN_HAND);
               PacketSender.method_9(InteractionHand.MAIN_HAND);
               InventoryUtil.method_16(var2);
               if (this.render.getValue()) {
                  Hub.field_2616.method_2(this, var3.getPos());
               }
            }
         }
      }
   }

   public DirectionalBlockPos method_2(boolean var1, BlockPos var2) {
      DirectionalBlockPos var3 = null;

      for (Direction var7 : Direction.values()) {
         BlockPos var8 = var2.relative(var7);
         if (mc.level.getBlockState(var8).is(Blocks.REDSTONE_BLOCK)) {
            return null;
         }

         if (InteractionUtil.method_7(var8, true)) {
            Direction var9 = this.method_2(var1, var8, var7.getOpposite());
            if (var9 != null) {
               var3 = new DirectionalBlockPos(var9, var8);
            }
         }
      }

      return var3;
   }

   public Direction method_2(boolean var1, BlockPos var2, Direction var3) {
      if (!var1) {
         return InteractionUtil.method_9(var2, this.strictDirection.getValue());
      } else {
         for (Direction var7 : Direction.values()) {
            BlockPos var8 = var2.relative(var7);
            if (var3 != var7 && Hub.field_2622.method_105(var8) && (!this.strictDirection.getValue() || InteractionUtil.method_39(var8).contains(var7.getOpposite()))) {
               return var7;
            }
         }

         return null;
      }
   }

   public BlockHitInfo method_78(BlockPos var1, Direction var2) {
      for (Direction var6 : Direction.values()) {
         BlockPos var7 = var1.relative(var6);
         if (Hub.field_2622.method_105(var7)
            && (!this.strictDirection.getValue() || InteractionUtil.method_39(var7).contains(var6.getOpposite()))
            && (!this.raytrace.getValue() || BlockUtil.method_2(Class_1172.field_3634.method_38(var7)))) {
            if (!this.rotate.getValue()) {
               Vec3 var11 = mc.player.getEyePosition().relative(var2.getOpposite(), 1.0);
               return new BlockHitInfo(var6, var1, var11);
            }

            for (Vec3 var9 : Class_1172.field_3634.method_2(var7, var6.getOpposite())) {
               Direction var10 = this.field_3022.method_4(RotationManager.method_78(var9));
               if (var10 == var2.getOpposite()) {
                  return new BlockHitInfo(var6, var1, var9);
               }
            }
         }
      }

      return null;
   }

   public boolean method_2(LivingEntity var1, BlockPos var2, Direction var3) {
      if (mc.player.getEyePosition().distanceTo(var2.getCenter()) > (double)this.method_884()) {
         return true;
      } else {
         BlockPos var4 = var2.relative(var3.getOpposite(), 2);
         int var5 = 0;

         for (BlockPos var7 : PlayerUtil.method_5(var1)) {
            if (Hub.field_2622.method_105(var7)) {
               var5++;
            }
         }

         boolean var8 = mc.level.getBlockState(var4.below()).canBeReplaced();
         return var5 > 2 && var8 ? true : !mc.level.getBlockState(var4).canBeReplaced();
      }
   }

   public Direction method_632(BlockPos var1) {
      BlockState var2 = mc.level.getBlockState(var1);
      return var2.getBlock() instanceof PistonBaseBlock ? (Direction)var2.getValue(DirectionalBlock.FACING) : null;
   }

   public float method_884() {
      return this.range.getValue();
   }
}
