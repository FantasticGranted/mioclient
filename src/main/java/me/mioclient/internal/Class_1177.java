package me.mioclient.internal;

import me.mioclient.event.MoveEvent;
import me.mioclient.module.movement.SpeedModule;

public final class Class_1177 extends SpeedMode {
   public Class_1177(SpeedModule var1) {
      super(var1);
   }

   @Override
   public void method_9(MoveEvent var1) {
      if (!this.field_2537.method_526() && !this.field_2537.method_525() && !mc.player.isFallFlying() && !this.field_2537.method_678()) {
         MovementUtil.method_2(var1, (double)this.field_2537.speed.getValue().floatValue());
      }
   }
}
