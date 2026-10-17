package me.mioclient.event;

import me.mioclient.internal.Event;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;

/** Fired when the player interacts with an item. */
public class InteractItemEvent extends Event {
   public ServerboundUseItemPacket field_3958;

   public InteractItemEvent(ServerboundUseItemPacket var1) {
      super();
      this.field_3958 = var1;
   }

   public ServerboundUseItemPacket method_1102() {
      return this.field_3958;
   }

   public void method_2(ServerboundUseItemPacket var1) {
      this.field_3958 = var1;
   }

   public void method_9(float[] var1) {
      this.method_2(new ServerboundUseItemPacket(this.field_3958.getHand(), this.field_3958.getSequence(), var1[0], var1[1]));
      this.method_463();
   }
}
