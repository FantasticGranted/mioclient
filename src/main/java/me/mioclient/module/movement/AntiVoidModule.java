package me.mioclient.module.movement;

import me.mioclient.enum_.AntiVoidMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.FontRenderer;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class AntiVoidModule extends Module {
   public Setting<AntiVoidMode> mode;
   public Setting<Integer> height;
   public Setting<Boolean> forceGround;

   public AntiVoidModule() {
      super("AntiVoid", "Prevents you from falling into the void.", Category.MOVEMENT);
      Settings.initialize(this);
      this.setDrawn(false);
   }

   @Override
   public String getInfo() {
      return FontRenderer.method_3(this.mode.getValue());
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (!mc.player.onGround() && !(mc.player.fallDistance <= 0.0F)) {
         BlockHitResult var2 = mc.level
            .isBlockInLine(
               new ClipBlockStateContext(
                  mc.player.position(),
                  new Vec3(mc.player.getX(), (double)(mc.level.getMinY() - 1), mc.player.getZ()),
                  var0 -> var0.isSolid()
               )
            );
         if (var2.getType() == Type.MISS) {
            if (!((double)(mc.level.getMinY() + this.height.getValue()) < mc.player.getY())) {
               if (this.mode.getValue() == AntiVoidMode.TELEPORT) {
                  mc.player.setDeltaMovement(0.0, 1e+01, 0.0);
               } else {
                  mc.player.setDeltaMovement(mc.player.getDeltaMovement().with(Axis.Y, 0.0));
                  if (this.forceGround.getValue()) {
                     mc.player.setOnGround(true);
                  }
               }
            }
         }
      }
   }
}
