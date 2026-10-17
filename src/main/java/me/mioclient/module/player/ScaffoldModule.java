package me.mioclient.module.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import me.mioclient.Hub;
import me.mioclient.enum_.Class_0147;
import me.mioclient.enum_.SelfFillMode;
import me.mioclient.enum_.ScaffoldSwapMode;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.Event_7;
import me.mioclient.event.SafeWalkEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.FilteredRaycastContext;
import me.mioclient.internal.RaycastBuilder;
import me.mioclient.internal.BoxUtil;
import me.mioclient.internal.RaycastUtil;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.mixin.ducks.DuckAbstractBlock;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.record.BlockFace;
import me.mioclient.record.FacePlacement;
import me.mioclient.record.RenderStyle;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import me.mioclient.internal.Inputs;

public class ScaffoldModule extends Module {
   public static AbstractModule_28 field_144 = Hub.field_2595.method_78(AbstractModule_28.class);
   public Setting<SelfFillMode> selection;
   public Setting<Set<Block>> blocks;
   public Setting<ScaffoldSwapMode> swap;
   public Setting<Integer> delay;
   public Setting<Integer> offset;
   public Setting<Boolean> tower;
   public Setting<Boolean> safeWalk;
   public Setting<Boolean> airPlace;
   public Setting<Boolean> strictDirection;
   public Setting<Boolean> telly;
   public Setting<Boolean> rotate;
   public Setting<Boolean> stopSprinting;
   public Setting<Boolean> attack;
   public Setting<Boolean> render;
   public Setting<Float> lineWidth;
   public Setting<Color> fill;
   public Setting<Color> outline;
   public Setting<Boolean> fade;
   public Setting<Float> fadeTime;
   public final Timer field_164;
   public final Timer field_165;
   public final Timer field_166;
   public final Timer field_167;
   public final Timer field_168;
   public BlockFace field_169;
   public float[] field_170;
   public int field_171;
   public int field_172;
   public double field_173;
   public float field_174;

   public ScaffoldModule() {
      super("Scaffold", "Speedbridges for you automatically.", Category.PLAYER);
      Settings.initialize(this);
      this.field_164 = new Timer();
      this.field_165 = new Timer();
      this.field_166 = new Timer();
      this.field_167 = new Timer();
      this.field_168 = new Timer();
      this.field_172 = -1;
      this.field_174 = -1F;
      Hub.field_2616.method_2(new RenderStyle(this, this.fill, this.outline, this.lineWidth, this.fadeTime, () -> false, this.fade, 450));
   }

   @Override
   public void onToggle() {
      this.field_164.reset();
      this.field_165.reset();
      this.field_169 = null;
      this.field_170 = null;
      this.field_171 = 0;
      this.field_172 = -1;
   }

   @Override
   public void onEnable() {
      if (!this.method_535()) {
         this.field_173 = mc.player.getY();
      }
   }

   @Subscribe
   public void method_5(Event_7 var1) {
      if (this.field_174 > 0.0F) {
         this.method_84(this.field_174);
         this.field_174 = -1F;
      }

      if (!mc.player.isHandsBusy()) {
         if (this.field_167.method_9(50L) || !mc.player.onGround() || !field_144.movementSync.getValue()) {
            if (!MovementUtil.method_363() || mc.player.horizontalCollision) {
               this.field_173 = mc.player.getY();
            }

            this.method_79();
            this.field_169 = this.method_81();
            boolean var2 = this.field_172 == -1 || mc.player.getInventory().getItem(this.field_172).isEmpty();
            if (this.swap.getValue() == ScaffoldSwapMode.NONE && this.method_80(mc.player.getMainHandItem()) == 0) {
               var2 = true;
            }

            if (var2) {
               this.field_169 = null;
            } else {
               this.method_2(this.field_169);
               if (!RotationManager.method_514() && this.field_169 != null) {
                  this.method_78(this.method_2(Class_0147.PRE));
               }

               if (this.field_170 != null) {
                  Hub.field_2598.method_2(this.field_170, 5);
               }

               if (this.field_165.method_9(500L) || RotationManager.method_513()) {
                  this.field_170 = null;
               }

               this.method_77();
               if (this.field_169 != null && this.field_166.method_9((long)this.delay.getValue().intValue())) {
                  if (!this.method_5(this.field_169.getPos(), this.field_172)) {
                     return;
                  }

                  this.swap
                     .getValue()
                     .method_9(
                        this.field_172,
                        () -> {
                           if (this.render.getValue()) {
                              Hub.field_2616.method_2(this, this.field_169.getPos());
                           }

                           FacePlacement var1x = FacePlacement.method_2(this.field_169, true);
                           Vec3 var2x = var1x.method_564();
                           if (var1x.getDirection() == Direction.DOWN) {
                              var2x = var2x.with(Axis.Y, (double)this.field_169.getPos().getY());
                           } else {
                              var2x = var2x.add(
                                 0.0,
                                 var1x.getShape().isEmpty()
                                    ? Class_0245.field_688
                                    : var1x.getShape().bounds().maxY - 0.01,
                                 0.0
                              );
                           }

                           boolean var3 = Hub.field_2602.method_993();
                           PacketSender.method_2(Inputs.shift(true));
                           InteractionUtil.method_2(this.field_169.getPos(), var2x, var1x.getDirection(), this.airPlace.getValue(), InteractionHand.MAIN_HAND);
                           if (!var3) {
                              PacketSender.method_2(Inputs.shift(false));
                           }

                           if (RotationManager.method_514()) {
                              this.method_78(this.method_2(Class_0147.POST.method_2(var1x, var2x)));
                           }

                           this.field_166.reset();
                           this.field_165.reset();
                           this.field_164.reset();
                        }
                     );
               }

               if (this.field_164.method_9(150L)) {
                  this.field_164.reset();
                  this.field_169 = null;
               }
            }
         }
      }
   }

   @Subscribe
   public void method_9(SafeWalkEvent var1) {
      if (this.safeWalk.getValue() && !mc.player.isHandsBusy()) {
         var1.method_463();
      }
   }

   @Subscribe
   public void method_2(MoveEvent var1) {
      if (this.safeWalk.getValue() && RotationManager.method_513() && mc.player.onGround() && !mc.player.isHandsBusy()) {
         AABB var2 = mc.player.getBoundingBox().move(var1.method_380(), 0.0, var1.method_396());
         boolean var3 = mc.player.isShiftKeyDown();
         if (mc.level.noCollision(var2.expandTowards(0.0, -1.0, 0.0))) {
            if (!var3) {
               mc.player.setShiftKeyDown(true);
            }

            var1.method_7(0.0, 0.0);
         } else if (!var3) {
            mc.player.setShiftKeyDown(false);
         }
      }
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (!var1.method_464()) {
         if (var1.method_127() instanceof ServerboundContainerClickPacket) {
            this.field_167.reset();
         }
      }
   }

   public void method_77() {
      int var1 = (int)Math.floor(mc.player.getY());
      if (this.field_171 != var1 || RotationManager.method_513()) {
         this.field_171 = var1;
         if (this.tower.getValue() && mc.player.input.keyPresses.jump() && mc.player.zza == 0.0F && mc.player.xxa == 0.0F) {
            if (RotationManager.method_513()) {
               if (mc.player.onGround()) {
                  this.field_174 = this.method_83();
                  this.method_84(0.36813F);
               }
            } else if (this.field_169 != null) {
               mc.player.setDeltaMovement(0.0, 0.41999998688698, 0.0);
            }
         }
      }
   }

   public void method_2(BlockFace var1) {
      if (var1 != null) {
         if (this.attack.getValue() && this.field_168.method_9((long)Hub.field_2623.method_878())) {
            Entity var2 = InteractionUtil.method_2(var1.getPos(), Hub.field_2623.method_523());
            if (var2 != null) {
               float[] var3 = RotationManager.method_14(var2);
               this.method_78(var3);
               this.field_168.reset();
            }
         }
      }
   }

   public void method_78(float[] var1) {
      if (this.rotate.getValue()) {
         if (RotationManager.method_514()) {
            Hub.field_2598.method_9(var1, 10);
            PacketSender.method_2(mc.player.getX(), mc.player.getY(), mc.player.getZ(), var1[0], var1[1], mc.player.onGround());
         } else {
            this.field_170 = var1;
         }
      }
   }

   public void method_79() {
      if (this.method_80(mc.player.getMainHandItem()) > 0) {
         this.field_172 = mc.player.getInventory().selected;
      } else {
         int var1 = mc.player.getInventory().selected;
         int var2 = this.method_80(mc.player.getInventory().getItem(var1));

         for (int var3 = 0; var3 < 9; var3++) {
            int var4 = this.method_80(mc.player.getInventory().getItem(var3));
            if (var4 > var2) {
               var1 = var3;
               var2 = var4;
            }
         }

         if (var2 == 0) {
            this.field_172 = -1;
         } else {
            this.field_172 = var1;
         }
      }
   }

   public int method_80(ItemStack var1) {
      if (var1.getItem() instanceof BlockItem var2
         && this.selection.getValue().method_2(var2.getBlock(), this.blocks.getValue())
         && ((DuckAbstractBlock)(Object)var2.getBlock()).isCollidable()
         && this.method_2(var2)) {
         return BoxUtil.method_2(var2.getBlock().defaultBlockState().getCollisionShape(mc.level, BlockPos.ZERO)) ? 2 : 1;
      }

      return 0;
   }

   public boolean method_2(BlockItem var1) {
      int var2 = RaycastUtil.method_886();
      return !(var1.getBlock() instanceof FallingBlock) || var2 <= 1;
   }

   public float[] method_2(Class_0147 var1) {
      if (var1 == Class_0147.PRE) {
         FacePlacement var2 = FacePlacement.method_2(this.field_169, false);
         float[] var3 = RotationManager.method_78(
            var2.method_564()
               .add(
                  0.0,
                  var2.getShape().isEmpty() ? Class_0245.field_688 : var2.getShape().bounds().maxY - 0.01,
                  0.0
               )
         );
         if (MovementUtil.method_363()) {
            double[] var4 = MovementUtil.method_2(mc.player.getYRot(), mc.player.input, 1.0);
            float var5 = (float)(Math.toDegrees(Math.atan2(var4[1], var4[0])) - 2.7e+02);
            if (Mth.degreesDifferenceAbs(var5, var3[0]) < 6e+01F) {
               var3[0] = var5;
            }
         }

         this.field_165.reset();
         return var3;
      } else {
         return RotationManager.method_78(
            var1.field_427
               .add(
                  0.0,
                  var1.field_426.getShape().isEmpty()
                     ? Class_0245.field_688
                     : var1.field_426.getShape().bounds().maxY - 0.01,
                  0.0
               )
         );
      }
   }

   public BlockFace method_81() {
      BlockPos var1 = null;
      Vec3 var2 = BoxUtil.method_2(mc.player.getBoundingBox());
      if (this.telly.getValue()) {
         var2 = var2.with(Axis.Y, this.field_173);
      }

      boolean var3 = mc.player.isPassenger() ? mc.player.getVehicle().verticalCollisionBelow : mc.player.verticalCollisionBelow;
      if (mc.player.isPassenger()) {
         var2 = BoxUtil.method_2(mc.player.getVehicle().getBoundingBox());
      }

      double var4 = this.method_82((float)var2.y());

      for (int var6 = 0; var6 < this.offset.getValue() + 1 && (MovementUtil.method_363() && mc.player.onGround() || var6 <= 0); var6++) {
         var1 = BlockPos.containing(var2.with(Axis.Y, var4)).relative(PlayerUtil.method_426(), var6);
         if (!var3 && !this.telly.getValue() && !mc.level.getBlockState(var1).canBeReplaced()) {
            var1 = var1.above();
         }

         if (this.method_5(var1, this.field_172)) {
            break;
         }

         var1 = null;
      }

      if (var1 == null) {
         return null;
      } else {
         Direction var12 = InteractionUtil.method_9(var1, this.strictDirection.getValue());
         if (var12 != null) {
            return new BlockFace(var1, var12);
         } else {
            for (int var7 = 1; var7 < 3; var7++) {
               for (Direction var11 : Direction.values()) {
                  if (var11 != Direction.UP) {
                     var12 = InteractionUtil.method_9(var1.relative(var11, var7), this.strictDirection.getValue());
                     if (var12 != null) {
                        return new BlockFace(var1.relative(var11), var12);
                     }
                  }
               }
            }

            return null;
         }
      }
   }

   public double method_82(float var1) {
      AABB var2 = mc.player.getBoundingBox();
      boolean var3 = mc.player.onGround();
      if (mc.player.isPassenger()) {
         var2 = mc.player.getVehicle().getBoundingBox();
         var3 = mc.player.getVehicle().onGround();
      }

      double var4 = var2.minX;
      double var6 = var2.minZ;
      double var8 = var2.maxX;
      double var10 = var2.maxZ;
      ArrayList var12 = new ArrayList();
      var12.add(BoxUtil.method_2(var2).with(Axis.Y, (double)var1));
      if (var3) {
         var12.addAll(
            List.of(
               new Vec3(var4, (double)var1, var6),
               new Vec3(var8, (double)var1, var6),
               new Vec3(var4, (double)var1, var10),
               new Vec3(var8, (double)var1, var10)
            )
         );
      }

      double var13 = -999.0;

      for (Vec3 var16 : (Iterable<Vec3>)(Iterable<?>)(var12)) {
         FilteredRaycastContext var17 = new RaycastBuilder(var16, var16.add(0.0, -1.0, 0.0))
            .method_22((Entity)(mc.player.isPassenger() ? mc.player.getVehicle() : mc.player))
            .method_565();
         BlockHitResult var18 = RaycastUtil.method_2(var17);
         double var19 = var18.getType() == Type.MISS
            ? var16.y() - 1.0
            : var18.getLocation().y() - 0.009999999776482582;
         var13 = Math.max(var13, var19);
      }

      return var13;
   }

   public boolean method_5(BlockPos var1, int var2) {
      ItemStack var3 = mc.player.getInventory().getItem(var2 == -1 ? mc.player.getInventory().selected : var2);
      return var3.getItem() instanceof BlockItem var4
         ? InteractionUtil.method_2(var1, var4.getBlock(), true, this.attack.getValue())
         : InteractionUtil.method_9(var1, true, this.attack.getValue());
   }

   public float method_83() {
      return (float)mc.player.getAttribute(Attributes.JUMP_STRENGTH).getBaseValue();
   }

   public void method_84(float var1) {
      mc.player.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue((double)var1);
   }
}
