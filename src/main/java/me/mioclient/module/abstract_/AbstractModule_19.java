package me.mioclient.module.abstract_;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import me.mioclient.Hub;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.BoxUtil;
import me.mioclient.internal.BlockUtil;
import me.mioclient.module.Category;
import me.mioclient.module.player.SpeedMineModule;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import me.mioclient.nick.Settings;

public class AbstractModule_19 extends AbstractModule_32 {
   public static final SpeedMineModule field_3651 = Hub.field_2595.method_78(SpeedMineModule.class);
   public Setting<Float> range;
   public Setting<Boolean> multiTask;
   public Setting<Boolean> ignoreNakeds;
   public Setting<Boolean> onlyFish;
   public Setting<Boolean> selfSafety;
   public Setting<Boolean> extrapolation;
   public Setting<Integer> ticks;
   public Setting<Boolean> positions;
   public Setting<Boolean> head;
   public Setting<Boolean> legs;
   public Player field_2290;

   public AbstractModule_19() {
      super("WebAura", "Traps your enemies with cobwebs.", Category.COMBAT, "autoweb");
      Settings.initialize(this);
      this.jump.method_78(false);
      this.attack.method_78(false);
      this.unregister(this.autoDisable);
      this.unregister(this.jump);
      this.unregister(this.chorus);
      this.unregister(this.pearl);
      this.unregister(this.attack);
   }

   @Override
   public boolean method_107() {
      return false;
   }

   @Override
   public String getInfo() {
      return this.field_2290 != null ? this.field_2290.getGameProfile().name() : null;
   }

   @Override
   public List<BlockPos> method_17() {
      this.field_2290 = this.method_691();
      if (this.field_2290 != null && (this.multiTask.getValue() || !mc.player.isUsingItem())) {
         ArrayList<BlockPos> var1 = new ArrayList<>();
         AABB var2 = mc.player.getBoundingBox();
         if (this.extrapolation.getValue()) {
            var2 = Hub.field_2612.method_2(this.field_2290, BoxUtil.method_2(this.field_2290, this.ticks));
         }

         BlockPos var3 = BlockPos.containing(BoxUtil.method_2(var2));
         if (this.legs.getValue() && var2.minY - (double)var3.getY() <= Class_0245.field_688) {
            var1.add(var3.below());
            var1.add(var3);
         }

         if (this.head.getValue()) {
            var1.add(var3.above());
         }

         var1.removeIf(
            var1x -> !mc.level.getBlockState(var1x).canBeReplaced()
                  || !mc.level.getFluidState(var1x).isEmpty()
                  || !this.method_8(var1x)
                  || !BlockUtil.method_2(var1x) && this.raytrace.getValue()
         );
         return var1;
      } else {
         return Collections.emptyList();
      }
   }

   @Override
   public int method_34() {
      return InventoryUtil.method_5(Items.COBWEB);
   }

   @Override
   public void method_2(BlockPos var1, boolean var2) {
      if (var1.equals(field_3651.method_1121())) {
         field_3651.method_1116();
      }
   }

   public boolean method_8(BlockPos var1) {
      if (!this.selfSafety.getValue()) {
         return true;
      } else {
         AABB var2 = new AABB(var1);
         boolean var3 = mc.player.getBoundingBox().intersects(var2);
         if (this.extrapolation.getValue()) {
            for (int var4 = this.ticks.getValue(); var4 >= 0; var4--) {
               if (Hub.field_2612.method_2(mc.player, BoxUtil.method_2(mc.player, this.ticks)).intersects(var2)) {
                  var3 = true;
                  break;
               }
            }
         }

         return !var3;
      }
   }

   public Player method_691() {
      return mc.level
         .players()
         .stream()
         .filter(
            var1 -> {
               boolean var2 = !this.onlyFish.getValue() || var1.isVisuallySwimming();
               return var2
                  && var1 != mc.player
                  && !Objects.equals(var1.getName().getString(), mc.player.getGameProfile().name())
                  && !Hub.field_2603.method_1009(var1.getName().getString())
                  && var1.isAlive()
                  && !var1.isDeadOrDying()
                  && (!this.ignoreNakeds.getValue() || PlayerUtil.method_29((LivingEntity)var1))
                  && var1.distanceTo(mc.player) <= this.range.getValue();
            }
         )
         .min(Comparator.comparing(var0 -> var0.distanceToSqr(mc.player)))
         .orElse(null);
   }
}
