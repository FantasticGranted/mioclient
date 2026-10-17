package me.mioclient.internal;

import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.TravelEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.module.movement.ElytraFlyModule;
import net.minecraft.core.Direction.Axis;

public class Class_0321 extends ElytraFlyBehavior {
   public long field_303;
   public float field_1086;
   public final Timer field_1087 = new Timer();

   public Class_0321(ElytraFlyModule var1) {
      super(var1);
   }

   @Override
   public void onEnable() {
      this.field_1086 = 0.0F;
      this.field_303 = System.currentTimeMillis();
   }

   @Override
   public void method_2(PlayerTickEvent var1) {
   }

   @Override
   public void method_2(MoveEvent var1) {
      if (this.field_2275.method_1178()) {
         if (!MovementUtil.method_363()) {
            this.field_303 = System.currentTimeMillis();
         }

         if (this.field_2275.accelerate.getValue()) {
            this.field_1086 = (float)MovementUtil.method_2(
               (double)this.field_2275.speed2.getValue().floatValue(),
               (double)this.field_2275.accelMin.getValue().floatValue(),
               (double)this.field_2275.accelTime.getValue().floatValue(),
               this.field_303
            );
         } else {
            this.field_1086 = this.field_2275.speed2.getValue();
         }

         double[] var2 = MovementUtil.method_2(var1, (double)this.field_1086);
         float var3 = -this.field_2275.glide.getValue();
         mc.player.setDeltaMovement(var2[0], (double)var3, var2[1]);
         var1.setY((double)var3);
         if (mc.player.input.keyPresses.shift()) {
            var1.setY(-0.5);
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().with(Axis.Y, -0.5));
         }

         PacketSender.method_1099();
         if (this.field_2275.antiAfk.getValue() && var1.method_380() == 0.0 && var1.method_396() == 0.0 && this.field_1087.method_9(2500L)) {
            var1.method_7(
               Math.sin(Math.toRadians((double)(mc.player.tickCount % Class_0245.field_686))) * 0.03,
               Math.cos(Math.toRadians((double)(mc.player.tickCount % Class_0245.field_686))) * 0.03
            );
         } else if (MovementUtil.method_363()) {
            this.field_1087.reset();
         }
      }
   }

   @Override
   public void method_5(PacketReceiveEvent var1) {
   }

   @Override
   public void method_2(PacketSendEvent var1) {
   }

   @Override
   public void method_9(Event_19 var1) {
   }

   @Override
   public void method_2(PostTickEvent var1) {
   }

   @Override
   public void method_9(TravelEvent var1) {
   }
}
