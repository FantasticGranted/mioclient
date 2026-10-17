package me.mioclient.module.abstract_;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;
import me.mioclient.Hub;
import me.mioclient.module.Category;
import me.mioclient.setting.BooleanSetting;
import me.mioclient.setting.CustomSetting3;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class AbstractModule_1 extends AbstractModule_8 {
   public final Object field_2943 = this.method_860();
   public Object field_2944;
   public Object field_2945;
   public Object field_2946;

   public AbstractModule_1() {
      super("SelfTrap", "Covers you with blocks.", Category.COMBAT);
      this.method_854();
      this.unregister(this.range);
      this.unregister(this.antiStep);
      this.unregister(this.ignoreNakeds);
   }

   @Override
   public String getInfo() {
      return null;
   }

   @Override
   public List<BlockPos> method_17() {
      List var1 = super.method_17();
      if ((!this.method_856() || this.method_859() != null) && (!this.method_855() || Hub.field_2605.method_224())) {
         if (var1.isEmpty() && this.method_861()) {
            this.method_38(false);
         }

         return var1;
      } else {
         return Collections.emptyList();
      }
   }

   @Override
   public Player method_691() {
      return mc.player;
   }

   public void method_854() {
      this.field_2944 = this.add(new BooleanSetting("OnlyHole", false));
      this.field_2945 = this.add(new BooleanSetting("Smart", false).method_460());
      this.field_2946 = this.add(
         new CustomSetting3<>(
               "EnemyRange", 3F, 1F, 6F, var1 -> this.method_857()
            )
            .method_22("m")
      );
   }

   public boolean method_855() {
      return (Boolean)((Setting)this.field_2944).getValue();
   }

   public boolean method_856() {
      return (Boolean)((Setting)this.field_2945).getValue();
   }

   public boolean method_857() {
      return ((Setting)this.field_2945).method_194();
   }

   public float method_858() {
      return (Float)((Setting)this.field_2946).getValue();
   }

   public Entity method_859() {
      return StreamSupport.<Entity>stream(mc.level.entitiesForRendering().spliterator(), false)
         .filter(
            var1 -> var1 != mc.player
                  && mc.player.distanceTo(var1) <= this.method_858()
                  && var1 instanceof Player
                  && !Hub.field_2603.method_1009(var1.getName().getString())
                  && var1.isAlive()
         )
         .min(Comparator.comparing(mc.player::distanceToSqr))
         .orElse(null);
   }

   public Object method_860() {
      return this.add(new BooleanSetting("Complete", false).method_2(this.autoDisable), this.autoDisable);
   }

   public boolean method_861() {
      return (Boolean)((Setting)this.field_2943).getValue();
   }
}
