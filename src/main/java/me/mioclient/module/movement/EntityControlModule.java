package me.mioclient.module.movement;

import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Event_64;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.KeybindUtil;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.RenderUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class EntityControlModule extends Module {
   public Setting<Boolean> entitySpeed;
   public Setting<Double> speed;
   public Setting<Boolean> noCollision;
   public Setting<Boolean> flight;
   public Setting<Float> vertical;
   public Setting<Boolean> glide;
   public Setting<Boolean> accelerate;
   public Setting<Boolean> copyYaw;
   public Setting<Boolean> horseJump;
   public boolean field_535;
   public long field_303;

   public EntityControlModule() {
      super("EntityControl", "Helps with riding entities.", Category.MOVEMENT);
      Settings.initialize(this);
      this.speed.method_9(() -> this.field_303 = System.currentTimeMillis());
   }

   @Subscribe
   public void method_2(Event_64 var1) {
      if (!this.method_535()) {
         if (!MovementUtil.method_363()) {
            this.field_303 = System.currentTimeMillis();
         }

         if (mc.player.isPassenger()) {
            Entity var2 = mc.player.getVehicle();
            if (var2 != null) {
               if (this.copyYaw.getValue()) {
                  var2.setYRot(mc.player.getViewYRot(RenderUtil.method_776()));
               }

               if (var2 instanceof LivingEntity var3 && var3.getItemBySlot(EquipmentSlot.SADDLE).isEmpty()) {
                  return;
               }

               double var11 = this.speed.getValue();
               double var5 = (double)this.vertical.getValue().floatValue();
               if (var11 >= 2e+01) {
                  var11 = 19.99;
               }

               if (this.accelerate.getValue()) {
                  var11 = MovementUtil.method_2(
                     this.speed.getValue(), 0.1, this.speed.getValue(), this.field_303
                  );
               }

               if (this.entitySpeed.getValue() && !this.method_1044()) {
                  double[] var9 = MovementUtil.method_2(mc.player.getViewYRot(RenderUtil.method_776()), mc.player.input, var11);
                  double var7;
                  if (this.flight.getValue()) {
                     if (var2 instanceof Boat var10) {
                        var10.setNoGravity(true);
                     }

                     if (mc.options.keyJump.isDown()) {
                        var7 = var5;
                     } else if (!this.glide.getValue() && !KeybindUtil.method_2(mc.options.keySprint)) {
                        var7 = this.field_535 ? -0.04125000163912773 : 0.04125000163912773;
                     } else {
                        var7 = -var5;
                     }
                  } else {
                     var7 = var1.method_395();
                  }

                  Vec3 var12 = new Vec3(var9[0], var7, var9[1]);
                  var1.method_4(var12);
                  this.field_535 = !this.field_535;
               }
            }
         }
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundPlayerPositionPacket) {
         this.field_303 = System.currentTimeMillis();
      }
   }

   public boolean method_107(Object var1) {
      return false;
   }

   public boolean method_1044() {
      return mc.player.isPassenger() && mc.player.getVehicle().horizontalCollision && this.noCollision.getValue();
   }
}
