package me.mioclient.event;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.internal.HudRenderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class Event_3 extends HudRenderEvent {
   public static final Event_3 field_1557 = new Event_3();

   public Event_3() {
      super();
   }

   public static Event_3 method_9(PoseStack var0, float var1) {
      field_1557.field_2997 = var0;
      field_1557.field_2998 = var1;
      field_1557.field_2999 = new GuiGraphicsExtractor(Minecraft.getInstance(), new net.minecraft.client.renderer.state.gui.GuiRenderState(), Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight());
      field_1557.method_616();
      return field_1557;
   }
}
