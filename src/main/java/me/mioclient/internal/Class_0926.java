package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.api.Class_1041;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.MovementInputEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.TravelEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.module.movement.ElytraFlyModule;
import net.minecraft.client.player.ClientInput;
import net.minecraft.world.phys.Vec2;
import me.mioclient.internal.Inputs;

public class Class_0926 extends ElytraFlyBehavior implements Class_1041 {
   public static AbstractModule_28 field_144 = Hub.field_2595.method_78(AbstractModule_28.class);

   public Class_0926(ElytraFlyModule var1) {
      super(var1);
   }

   @Override
   public boolean method_295() {
      return this.method_535() ? false : !PlayerUtil.method_428() || mc.player.onGround();
   }

   @Override
   public void method_2(PostTickEvent var1) {
   }

   @Override
   public void method_2(PacketSendEvent var1) {
      if (this.method_295()) {
         field_144.method_1053().method_9(var1);
      }
   }

   @Override
   public void method_2(MoveEvent var1) {
      boolean var2 = !Hub.field_2602.method_984().method_9(300L);
      if (!PlayerUtil.method_428() && !var2) {
         float var3 = this.field_2275.speed2.getValue();
         ClientInput var4 = mc.player.input;
         if (var4.moveVector.y == 0.0F && var4.moveVector.x == 0.0F) {
            var4.moveVector = new Vec2(var4.moveVector.x, (1F));
         }

         double[] var5 = MovementUtil.method_2(mc.player.getViewYRot(RenderUtil.method_776()), var4, (double)var3);
         var1.method_7(var5[0], var5[1]);
         this.field_2275.field_4392.reset();
      }
   }

   @Override
   public void method_5(MovementInputEvent var1) {
      if (!mc.player.onGround()) {
         Inputs.setJump(var1.method_276(), mc.player.tickCount % 3 == 0);
      }
   }

   @Override
   public void method_9(TravelEvent var1) {
   }

   @Override
   public void method_9(Event_19 var1) {
   }

   @Override
   public void method_2(PlayerTickEvent var1) {
   }

   @Override
   public void method_5(PacketReceiveEvent var1) {
   }
}
