package me.mioclient.event;

import java.util.List;
import me.mioclient.internal.Event;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

/** Fired while a container screen renders a tooltip. */
public class RenderTooltipEvent extends Event {
   public static int field_1663;
   public static int field_1664;
   public final Screen field_1665;
   public List<ClientTooltipComponent> field_1666;
   public int field_1321;
   public int field_1322;

   public RenderTooltipEvent(Screen var1, List<ClientTooltipComponent> var2, int var3, int var4, int var5, int var6) {
      super();
      this.field_1665 = var1;
      this.field_1666 = var2;
      this.field_1321 = var3;
      this.field_1322 = var4;
      field_1663 = var5;
      field_1664 = var6;
   }

   public Screen method_562() {
      return this.field_1665;
   }

   public List<ClientTooltipComponent> method_563() {
      return this.field_1666;
   }

   public void method_29(List<ClientTooltipComponent> var1) {
      this.field_1666 = var1;
   }

   public int getX() {
      return this.field_1321;
   }

   public void setX(int var1) {
      this.field_1321 = var1;
   }

   public int getY() {
      return this.field_1322;
   }

   public void setY(int var1) {
      this.field_1322 = var1;
   }
}
