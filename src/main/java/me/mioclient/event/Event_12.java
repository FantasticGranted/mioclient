package me.mioclient.event;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.internal.HudRenderEvent;

public class Event_12 extends HudRenderEvent {
   public static final Event_12 field_3284 = new Event_12();

   public Event_12() {
      super();
   }

   public static Event_12 method_5(PoseStack var0, float var1) {
      field_3284.field_2997 = var0;
      field_3284.field_2998 = var1;
      field_3284.method_616();
      return field_3284;
   }
}
