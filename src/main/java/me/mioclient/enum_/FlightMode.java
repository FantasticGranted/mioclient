package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.Event_19;
import me.mioclient.event.MoveEvent;
import me.mioclient.internal.MovementUtil;
import me.mioclient.module.movement.FlightModule;
import net.minecraft.core.Direction.Axis;

/** Flight mode. */
public enum FlightMode implements Nameable {
   VANILLA("Vanilla") {
      @Override
      public void method_2(FlightModule var1, MoveEvent var2) {
         MinecraftAccess.mc.player.getAbilities().flying = true;
         MinecraftAccess.mc.player.getAbilities().setFlyingSpeed((float)(var1.method_874() * 0.05F));
      }
   },
   STATIC("Static") {
      public int field_31;

      @Override
      public void method_2(FlightModule var1, MoveEvent var2) {
         double var3 = var1.vertical.getValue() ? (double)var1.vSpeed.getValue().floatValue() : 0.0;
         boolean var5 = var1.antiKick.getValue() == AntiKickMode.ALTERNATIVE;
         if (MinecraftAccess.mc.player.input.keyPresses.jump()) {
            this.field_31++;
            if (var5) {
               if (this.field_31 >= 18) {
                  var3 = -0.1;
               }

               if (this.field_31 >= 21) {
                  this.field_31 = 0;
               }
            }

            var2.setY(var3);
            MinecraftAccess.mc.player.setDeltaMovement(MinecraftAccess.mc.player.getDeltaMovement().with(Axis.Y, var3));
         } else if (MinecraftAccess.mc.player.input.keyPresses.shift()) {
            var2.setY(-var3);
            MinecraftAccess.mc.player.setDeltaMovement(MinecraftAccess.mc.player.getDeltaMovement().with(Axis.Y, -var3));
         } else {
            var2.setY(0.0);
            MinecraftAccess.mc.player.setDeltaMovement(MinecraftAccess.mc.player.getDeltaMovement().with(Axis.Y, 0.0));
            if (!MinecraftAccess.mc.player.verticalCollision && var1.glide.getValue() != 0.0F) {
               MinecraftAccess.mc.player.setDeltaMovement(MinecraftAccess.mc.player.getDeltaMovement().add(0.0, (double)(-var1.glide.getValue()), 0.0));
               var2.setY(MinecraftAccess.mc.player.getDeltaMovement().y);
            }
         }

         double[] var6 = MovementUtil.method_2(var2, var1.method_874());
         MinecraftAccess.mc.player.setDeltaMovement(var6[0], MinecraftAccess.mc.player.getDeltaMovement().y, var6[1]);
      }
   },
   JETPACK("JetPack") {
      @Override
      public void method_2(FlightModule var1, Event_19 var2) {
         if (MinecraftAccess.mc.options.keyJump.isDown()) {
            MinecraftAccess.mc.player.setDeltaMovement(MinecraftAccess.mc.player.getDeltaMovement().add(0.0, var1.method_874() * 0.1, 0.0));
         }
      }
   },
   SOURCE("Source") {
      @Override
      public void method_2(FlightModule var1, MoveEvent var2) {
         double var3 = var1.method_874();
         MovementUtil.method_2(var2, var3);
         if (MovementUtil.method_363()) {
            var2.setY(
               (double)(-Math.signum(MinecraftAccess.mc.player.input.moveVector.y))
                  * Math.sin(Math.toRadians((double)MinecraftAccess.mc.player.getXRot()))
                  * var3
            );
         } else {
            var2.setY(0.0);
         }
      }
   };

   public final String field_2357;

    FlightMode(String var3) {
      this.field_2357 = var3;
   }

   @Override
   public String getName() {
      return this.field_2357;
   }

   public void method_2(FlightModule var1, Event_19 var2) {
   }

   public void method_2(FlightModule var1, MoveEvent var2) {
   }
}
