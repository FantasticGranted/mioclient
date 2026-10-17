package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.Event_19;
import me.mioclient.event.MoveEvent;

/** Flight anti-kick mode: none or plain. */
public enum AntiKickMode implements Nameable {
   NONE("None"),
   PLAIN("Plain") {
      public long field_677;

      @Override
      public void method_2(Event_19 var1) {
         if (!MinecraftAccess.mc.player.onGround()) {
            if (System.currentTimeMillis() - this.field_677 >= 500L && !MinecraftAccess.mc.player.onGround()) {
               this.field_677 = System.currentTimeMillis();
               var1.setY(var1.method_395() - 0.04);
            }
         }
      }
   },
   ALTERNATIVE("Alternative") {
      public boolean field_535;

      @Override
      public void method_2(MoveEvent var1) {
         if (!MinecraftAccess.mc.player.onGround()) {
            double var2 = 0.04;
            if (this.field_535) {
               var2 = -var2;
            }

            var1.setY(var1.method_395() + var2);
            MinecraftAccess.mc.player.setDeltaMovement(MinecraftAccess.mc.player.getDeltaMovement().add(0.0, var2, 0.0));
            this.field_535 = !this.field_535;
         }
      }
   };

   public final String field_1428;

    AntiKickMode(String var3) {
      this.field_1428 = var3;
   }

   @Override
   public String getName() {
      return this.field_1428;
   }

   public void method_2(MoveEvent var1) {
   }

   public void method_2(Event_19 var1) {
   }
}
