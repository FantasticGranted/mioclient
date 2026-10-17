package me.mioclient.internal;

import me.mioclient.event.ExplosionEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.mixin.ducks.DuckEntityVelocityUpdateS2CPacket;
import me.mioclient.module.movement.VelocityModule;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.phys.Vec3;

public class Class_0198 extends VelocityBehavior {
   public Class_0198(VelocityModule var1) {
      super(var1);
   }

   @Override
   public void method_5(PacketReceiveEvent var1) {
      int var2 = this.field_88.horizontal.getValue();
      int var3 = this.field_88.vertical.getValue();
      int var4 = this.field_88.inverse.getValue() ? -1 : 1;
      if (var1.method_127() instanceof ClientboundSetEntityMotionPacket var5 && var5.id() == mc.player.getId()) {
         DuckEntityVelocityUpdateS2CPacket var7 = (DuckEntityVelocityUpdateS2CPacket)(Object)var5;
         Vec3 var6 = var5.movement();
         double var9 = (double)((float)var2 * 0.01F) * (double)var4;
         double var10 = (double)((float)var3 * 0.01F);
         var7.setMovement(new Vec3(var6.x * var9, var6.y * var10, var6.z * var9));
         if (var2 == 0 && var3 == 0) {
            var1.method_463();
         }
      }
   }

   @Override
   public void method_2(ExplosionEvent var1) {
      if (this.field_88.explosions.getValue()) {
         int var2 = this.field_88.horizontal.getValue();
         int var3 = this.field_88.vertical.getValue();
         int var4 = this.field_88.inverse.getValue() ? -1 : 1;
         var1.method_52((float)((int)(var1.method_398() * (float)var2 * 0.01F * (float)var4)));
         var1.method_185((float)((int)(var1.method_399() * (float)var3 * 0.01F)));
         var1.method_92((float)((int)(var1.method_400() * (float)var2 * 0.01F * (float)var4)));
      }
   }
}
