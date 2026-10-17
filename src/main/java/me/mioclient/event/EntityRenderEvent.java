package me.mioclient.event;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.internal.Event;
import net.minecraft.world.entity.Entity;

/** Fired while an entity is rendered. */
public class EntityRenderEvent extends Event {
   public final PoseStack field_16;
   public final Entity field_17;

   public EntityRenderEvent(PoseStack var1, Entity var2) {
      super();
      this.field_16 = var1;
      this.field_17 = var2;
   }

   public PoseStack method_10() {
      return this.field_16;
   }

   public Entity method_11() {
      return this.field_17;
   }
}
