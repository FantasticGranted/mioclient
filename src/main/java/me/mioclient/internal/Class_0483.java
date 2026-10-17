package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

public class Class_0483 extends EntityDrawEvent {
   public static final Class_0483 field_1537 = new Class_0483();

   public Class_0483() {
      super();
   }

   public static Class_0483 method_2(Entity var0, PoseStack var1, MultiBufferSource var2) {
      field_1537.field_581 = var0;
      field_1537.field_2127 = var1;
      field_1537.field_3084 = var2;
      field_1537.method_616();
      return field_1537;
   }
}
