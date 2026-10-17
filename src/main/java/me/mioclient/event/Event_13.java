package me.mioclient.event;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.internal.EntityDrawEvent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

public class Event_13 extends EntityDrawEvent {
   public static final Event_13 field_399 = new Event_13();

   public Event_13() {
      super();
   }

   public static Event_13 method_9(Entity var0, PoseStack var1, MultiBufferSource var2) {
      field_399.field_581 = var0;
      field_399.field_2127 = var1;
      field_399.field_3084 = var2;
      field_399.method_616();
      return field_399;
   }
}
