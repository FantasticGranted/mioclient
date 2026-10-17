package me.mioclient.module.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import me.mioclient.Hub;
import me.mioclient.enum_.BlockSort;
import me.mioclient.enum_.NukerMode;
import me.mioclient.enum_.NukerShape;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.record.TimestampedBlockPos;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import me.mioclient.nick.Settings;

public class NukerModule extends Module {
   public static final AbstractModule_28 field_3146 = Hub.field_2595.method_78(AbstractModule_28.class);
   public static final ItemSaverModule field_3147 = Hub.field_2595.method_78(ItemSaverModule.class);
   public static final SpeedMineModule field_3148 = Hub.field_2595.method_78(SpeedMineModule.class);
   public Setting<NukerMode> mode;
   public Setting<NukerShape> shape;
   public Setting<BlockSort> sort;
   public Setting<Integer> delay;
   public Setting<Float> range;
   public Setting<Float> wallRange;
   public Setting<Boolean> strictDirection;
   public Setting<Boolean> rotate;
   public Setting<Boolean> creative;
   public Setting<Boolean> flatten;
   public Setting<Boolean> baritoneArea;
   public Setting<Set<Block>> whitelist;
   public final List<TimestampedBlockPos> field_3161;
   public final Timer field_3162;
   public final Timer field_3163;
   public BlockPos field_3164;
   public int field_3165;

   public NukerModule() {
      super("Nuker", "Breaks blocks nearby.", Category.PLAYER);
      Settings.initialize(this);
      this.field_3161 = new ArrayList<>();
      this.field_3162 = new Timer();
      this.field_3163 = new Timer();
      this.field_3164 = null;
      this.baritoneArea.method_334();
   }

   @Override
   public String getInfo() {
      return FontRenderer.method_3(this.mode.getValue());
   }

   @Override
   public void onDisable() {
      mc.options.keyAttack.setDown(false);
   }

   @Subscribe
   public void method_38(Event_7 var1) {
      if (!this.method_535()) {
         this.field_3161.removeIf(var0 -> System.currentTimeMillis() - var0.time > 1000L);
         this.field_3164 = null;
         this.field_3165 = 0;
         if (this.field_3163.method_9((long)this.delay.getValue().intValue())) {
            if (!field_3148.isToggled() || field_3148.method_4((long)this.delay.getValue().intValue())) {
               boolean var2 = this.creative.getValue() || field_3148.extraBreak.getValue();
               List<BlockPos> var3 = this.shape.getValue().method_2(this);
               var3.sort(Comparator.comparing(var1x -> this.sort.getValue().field_1223.apply(var1x)));

               for (BlockPos var5 : var3) {
                  if (this.method_920()) {
                     break;
                  }

                  if ((!this.flatten.getValue() || !((double)var5.getY() < mc.player.getY())) && this.method_483(var5)) {
                     this.method_610(var5);
                     this.field_3163.reset();
                     if (!var2 && this.field_3164 != null || field_3148.isToggled() && field_3148.extraBreak.getValue() && this.field_3165 >= 2) {
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   public void method_610(BlockPos var1) {
      if (BlockUtil.method_3(var1)) {
         Direction var2 = Direction.UP;
         if (this.strictDirection.getValue()) {
            List var3 = InteractionUtil.method_39(var1);
            if (var3.isEmpty()) {
               return;
            }

            var2 = (Direction)var3.get(0);
         }

         if (this.rotate.getValue()) {
            Hub.field_2598.method_2(RotationManager.method_2(var1.getCenter(), var2), 5);
         }

         this.field_3165++;
         if (!field_3148.isToggled()) {
            int var10 = -1;
            double var4 = -1.0;
            BlockState var6 = mc.level.getBlockState(var1);

            for (int var7 = 0; var7 < 9; var7++) {
               double var8 = (double)mc.player.getInventory().getItem(var7).getDestroySpeed(var6);
               if ((!field_3147.isToggled() || ItemSaverModule.method_101(mc.player.getInventory().getItem(var7))) && var8 > var4) {
                  var4 = var8;
                  var10 = var7;
               }
            }

            this.field_3164 = var1;
            this.field_3162.reset();
            if (this.creative.getValue()) {
               if (field_3146.method_1052()) {
                  InventoryUtil.method_16(var10);
                  PacketSender.method_2(Action.STOP_DESTROY_BLOCK, this.field_3164, var2);
                  PacketSender.method_2(Action.START_DESTROY_BLOCK, this.field_3164, var2);
                  PacketSender.method_2(Action.STOP_DESTROY_BLOCK, this.field_3164, var2);
                  PacketSender.method_2(Action.ABORT_DESTROY_BLOCK, this.field_3164.offset(0, 500, 0), var2);
               } else {
                  PacketSender.method_2(Action.START_DESTROY_BLOCK, this.field_3164, var2);
                  PacketSender.method_2(Action.STOP_DESTROY_BLOCK, this.field_3164, var2);
               }

               PacketSender.method_9(InteractionHand.MAIN_HAND);
               this.field_3161.add(new TimestampedBlockPos(this.field_3164, System.currentTimeMillis()));
            } else {
               InventoryUtil.method_16(var10);
               mc.gameMode.continueDestroyBlock(var1, var2);
               mc.player.swing(InteractionHand.MAIN_HAND);
            }
         } else {
            SpeedMineModule.field_3986 = true;
            mc.gameMode.startDestroyBlock(var1, var2);
            SpeedMineModule.field_3986 = false;
            this.field_3164 = var1;
            this.field_3162.reset();
         }
      }
   }

   public boolean method_483(BlockPos var1) {
      if (this.field_3161.stream().anyMatch(var1x -> var1x.getPos().equals(var1))) {
         return false;
      } else {
         double var2 = Math.sqrt(var1.distToCenterSqr(mc.player.getEyePosition()));
         if (!BlockUtil.method_2(var1) && var2 > (double)this.wallRange.getValue().floatValue()) {
            return false;
         } else if (this.mode.getValue() == NukerMode.SHULKERS && Hub.field_2622.method_101(var1)) {
            return false;
         } else {
            return this.baritoneArea.getValue() && Hub.field_2630.method_264() && !Hub.field_2630.method_263(var1)
               ? false
               : this.mode.getValue().method_2(var1, InteractionUtil.getId(var1), this);
         }
      }
   }

   public boolean method_920() {
      if (!field_3148.isToggled()) {
         return false;
      } else {
         BlockPos var1 = field_3148.method_1120();
         if (var1 == null) {
            var1 = field_3148.method_1121();
         }

         BlockPos var2 = null;
         if (field_3148.method_1123() != null) {
            var2 = field_3148.method_1123().method_111();
         }

         return BlockUtil.method_3(var1) && (var2 == null || !var2.equals(var1));
      }
   }

   public BlockHitResult method_921() {
      if (mc.hitResult instanceof BlockHitResult var1 && var1.getType() == Type.BLOCK) {
         return var1;
      }

      return null;
   }
}
