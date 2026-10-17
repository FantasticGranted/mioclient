package me.mioclient.module.movement;

import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.SafeWalkEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import me.mioclient.nick.Settings;

public class SafeWalkModule extends Module {
   public Setting<Boolean> sneak;
   public Setting<Integer> field_2155;
   public final Timer field_2156;

   public SafeWalkModule() {
      super("SafeWalk", "Helps not to fall from blocks if you're bad.", Category.MOVEMENT);
      Settings.initialize(this);
      this.field_2156 = new Timer();
   }

   @Override
   public void onDisable() {
      if (this.sneak.getValue()) {
         mc.options.keyShift.setDown(false);
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (this.sneak.getValue()) {
         BlockPos var2 = BlockPos.containing(mc.player.position()).below();
         BlockState var3 = mc.level.getBlockState(var2);
         if (!var3.is(Blocks.AIR) && !var3.getCollisionShape(mc.level, var2).isEmpty()) {
            mc.options.keyShift.setDown(!this.field_2156.method_9((long)this.field_2155.getValue().intValue()));
         } else {
            this.field_2156.reset();
            mc.options.keyShift.setDown(true);
         }
      }
   }

   @Subscribe
   public void method_2(SafeWalkEvent var1) {
      if (!mc.player.isSwimming() && !mc.player.isInLava() && !mc.player.isInWater() && !this.sneak.getValue()) {
         var1.method_463();
      }
   }
}
