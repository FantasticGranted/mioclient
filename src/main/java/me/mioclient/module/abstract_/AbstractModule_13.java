package me.mioclient.module.abstract_;

import java.awt.Color;
import java.util.List;
import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.Class_0470;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.CrystalPlacementFinder;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.record.RenderStyle;
import me.mioclient.record.CrystalPlacement;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import me.mioclient.nick.Settings;

public final class AbstractModule_13 extends Module {
   public static final AbstractModule_28 field_3564 = Hub.field_2595.method_78(AbstractModule_28.class);
   public Setting<Boolean> place;
   public Setting<Integer> delay;
   public Setting<Float> range;
   public Setting<Boolean> strictDirection;
   public Setting<Boolean> rotate;
   public Setting<Boolean> liquidPlace;
   public Setting<Boolean> airPlace;
   public Setting<Boolean> break_;
   public Setting<Integer> delay2;
   public Setting<Float> range2;
   public Setting<Boolean> tPSSync;
   public Setting<Boolean> instant;
   public Setting<Boolean> extrapolation;
   public Setting<Integer> ticks;
   public Setting<Boolean> refill;
   public Setting<Integer> slot;
   public Setting<Boolean> render;
   public Setting<Color> fill;
   public Setting<Color> outline;
   public Setting<Boolean> fade;
   public Setting<Float> fadeTime;
   public Setting<Boolean> targeting;
   public Setting<Float> minDamage;
   public Setting<Float> maxSelfDamage;
   public Setting<Boolean> antiSucide;
   public final CrystalPlacementFinder field_3590;
   public final Class_0470 field_3591;
   public final Timer field_3592;
   public final Timer field_3593;
   public Player field_2290;
   public CrystalPlacement field_3594;
   public BlockPos field_3595;

   public AbstractModule_13() {
      super(
         "BedAura",
         new PlaceholderText().method_2(String.valueOf(ChatFormatting.RED)).method_9("Blows your enemies up using beds. \n\u0001Doesn't work in the overworld."),
         Category.COMBAT
      );
      Settings.initialize(this);
      this.field_3590 = new CrystalPlacementFinder(this);
      this.field_3591 = new Class_0470(this);
      this.field_3592 = new Timer();
      this.field_3593 = new Timer();
      Hub.field_2616
         .method_2(
            new RenderStyle(this, this.fill, this.outline, () -> 1F, this.fadeTime, () -> false, this.fade, 1000)
         );
      this.range.method_31("PlaceRange");
      this.range2.method_31("BreakRange");
      this.delay.method_31("PlaceDelay");
      this.delay2.method_31("BreakDelay");
   }

   @Override
   public String getInfo() {
      return this.field_2290 == null ? null : this.field_2290.getGameProfile().name();
   }

   @Subscribe
   public void method_9(Event_7 var1) {
      this.field_2290 = this.method_691();
      if (this.field_2290 == null) {
         this.field_3594 = null;
      } else {
         this.field_3594 = this.field_3590.method_22(this.field_2290);
         this.field_3595 = this.field_3591.method_31(this.field_2290);
      }

      if (this.field_3594 != null) {
         this.method_1024();
         if (this.break_.getValue() && this.method_2(this.field_3593, this.delay2)) {
            this.method_289(this.field_3595);
         }

         if (this.place.getValue() && this.method_2(this.field_3592, this.delay)) {
            this.method_2(this.field_3594);
         }
      }
   }

   public Player method_691() {
      Player var1 = null;
      float var2 = 99999F;

      for (Player var4 : mc.level.players()) {
         if (var4 != mc.player && !Hub.field_2603.getId(var4) && (double)var2 > mc.player.getEyePosition().distanceTo(var4.position())) {
            var1 = var4;
            var2 = (float)mc.player.getEyePosition().distanceTo(var4.position());
         }
      }

      return var1;
   }

   public void method_2(CrystalPlacement var1) {
      int var2 = InventoryUtil.method_7((Predicate<ItemStack>)(var0 -> var0.getItem() instanceof BedItem));
      int var3 = mc.player.getInventory().selected;
      if (var2 != -1) {
         InventoryUtil.method_78(var2);
         this.method_9(var1);
         InventoryUtil.method_78(var3);
      }
   }

   public void method_9(CrystalPlacement var1) {
      if (var1 != null) {
         if (InteractionUtil.method_7(var1.getPlacePos(), true)) {
            float[] var2;
            if (this.rotate.getValue()) {
               var2 = RotationManager.method_78(var1.getHitResult().getLocation());
            } else {
               var2 = RotationManager.method_7(var1.getPlacePos().getCenter(), var1.getTargetPos().getCenter());
            }

            Hub.field_2598.method_2(var2, 500);
            BlockHitResult var3 = var1.getHitResult();
            Hub.field_2615.method_3(() -> PacketSender.method_2(InteractionHand.MAIN_HAND, var3));
            mc.player.swing(InteractionHand.MAIN_HAND);
            AABB var4 = AABB.encapsulatingFullBlocks(var1.getPlacePos(), var1.getTargetPos());
            var4 = var4.setMaxY(var4.minY + 0.5625);
            var4 = var4.inflate(0.0020000000949949026);
            if (this.render.getValue()) {
               Hub.field_2616.method_2(this, var4);
            }

            if (this.instant.getValue()) {
               Hub.field_2615.method_22(() -> {
                  Direction var2x = this.method_4(var1.getPlacePos(), Direction.UP);
                  BlockHitResult var3x = new BlockHitResult(var1.getPlacePos().getCenter(), var2x, var1.getPlacePos(), false);
                  PacketSender.method_2(InteractionHand.MAIN_HAND, var3x);
                  mc.player.swing(InteractionHand.MAIN_HAND);
               });
            }

            this.field_3592.reset();
         }
      }
   }

   public void method_289(BlockPos var1) {
      if (var1 != null) {
         Direction var2 = this.method_4(var1, Direction.UP);
         BlockHitResult var3 = new BlockHitResult(var1.getCenter(), var2, var1, false);
         Hub.field_2615.method_22(() -> {
            mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, var3);
            mc.player.swing(InteractionHand.MAIN_HAND);
         });

         for (Direction var5 : Plane.HORIZONTAL) {
            BlockPos var6 = var1.relative(var5);
            if (mc.level.getBlockState(var6).getBlock() instanceof BedBlock) {
               Hub.field_2622.method_99(var6);
               break;
            }
         }

         Hub.field_2622.method_99(var1);
         this.field_3593.reset();
      }
   }

   public void method_1024() {
      if (this.refill.getValue()) {
         int var1 = this.slot.getValue() - 1;
         ItemStack var2 = mc.player.getInventory().getItem(var1);
         if (!(var2.getItem() instanceof BedItem)) {
            int var3 = InventoryUtil.method_2((Predicate<ItemStack>)(var0 -> var0.getItem() instanceof BedItem), true);
            if (var3 != -1) {
               InventoryUtil.method_9(var3, InventoryUtil.getId(var1));
            }
         }
      }
   }

   public Direction method_4(BlockPos var1, Direction var2) {
      List var3 = InteractionUtil.method_39(var1);
      if (!var3.isEmpty()) {
         var3.getFirst();
      }

      return var2;
   }

   public boolean method_2(BlockPos var1, BedPart var2) {
      BlockState var3 = mc.level.getBlockState(var1);
      return var3.getBlock() instanceof BedBlock && var3.getValue(BedBlock.PART) == var2;
   }

   public boolean method_2(Timer var1, Setting<Integer> var2) {
      float var3 = this.tPSSync.getValue() ? Hub.field_2602.method_990() : 1F;
      return var1.method_9((long)((float)(50L * (long)((Integer)var2.getValue()).intValue()) * var3));
   }

   public AABB method_16(LivingEntity var1) {
      if (var1 instanceof Player var2 && this.extrapolation.getValue()) {
         return Hub.field_2612.method_2(var2, this.ticks.getValue());
      }

      return var1.getBoundingBox();
   }
}
