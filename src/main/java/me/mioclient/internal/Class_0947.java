package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.event.Event_3;

public class Class_0947 extends HudRenderEvent {
   public static final Class_0947 field_2951 = new Class_0947();

   public Class_0947() {
      super();
   }

   public static Class_0947 method_2(PoseStack var0, float var1) {
      field_2951.field_2997 = var0;
      field_2951.field_2998 = var1;
      field_2951.field_2999 = Event_3.field_1557.method_881();
      field_2951.method_616();
      return field_2951;
   }
}
