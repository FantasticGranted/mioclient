package me.mioclient.event;

import me.mioclient.internal.Event;
import net.minecraft.client.player.ClientInput;
import net.minecraft.world.phys.Vec2;

/** Fired before movement input is applied (pre). */
public class MovementInputEvent extends Event {
   public final ClientInput field_714;
   public final float field_715;

   public MovementInputEvent(ClientInput var1, float var2) {
      super();
      this.field_714 = var1;
      this.field_715 = var2;
   }

   public ClientInput method_276() {
      return this.field_714;
   }

   public float method_277() {
      return this.field_715;
   }

   public boolean method_278() {
      return this.field_715 != -1.0F;
   }

   public void reset() {
      this.field_714.keyPresses = net.minecraft.world.entity.player.Input.EMPTY;
      this.field_714.moveVector = new Vec2(this.field_714.moveVector.x, (0.0F));
      this.field_714.moveVector = new Vec2((0.0F), this.field_714.moveVector.y);
   }
}
