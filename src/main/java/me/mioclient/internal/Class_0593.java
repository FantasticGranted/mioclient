package me.mioclient.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import me.mioclient.api.MinecraftAccess;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public final class Class_0593 implements MinecraftAccess {
   public static boolean field_1856 = false;
   public static List<ClientTooltipComponent> field_1666;

   public Class_0593() {
      super();
      throw new AssertionError();
   }

   public static List<ClientTooltipComponent> method_2(GuiGraphicsExtractor var0, List<Component> var1, Optional<TooltipComponent> var2) {
      field_1856 = true;
      var0.setTooltipForNextFrame(mc.font, var1, var2, 0, 0);
      field_1856 = false;
      return field_1666 == null ? new ArrayList<>() : new ArrayList<>(field_1666);
   }

   public static boolean method_607() {
      return field_1856;
   }

   public static void method_29(List<ClientTooltipComponent> var0) {
      field_1666 = var0;
   }
}
