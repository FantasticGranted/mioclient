package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

/** Event fired while an entity is being rendered (entity + matrices + consumers). */
public class EntityDrawEvent extends Event {
   public Entity field_581;
   public PoseStack field_2127;
   public MultiBufferSource field_3084;

   public EntityDrawEvent() {
      super();
   }

   public Entity method_11() {
      return this.field_581;
   }

   public PoseStack method_10() {
      return this.field_2127;
   }

   public MultiBufferSource method_898() {
      return this.field_3084;
   }
}
