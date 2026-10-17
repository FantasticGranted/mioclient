package me.mioclient.event;

import me.mioclient.internal.Event;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

/** Fired for a status effect on a living entity. */
public class StatusEffectEvent extends Event {
   public final Holder<MobEffect> field_3956;

   public StatusEffectEvent(Holder<MobEffect> var1) {
      super();
      this.field_3956 = var1;
   }

   public Holder<MobEffect> method_1026() {
      return this.field_3956;
   }
}
