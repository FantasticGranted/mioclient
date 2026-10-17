package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.Hub;
import me.mioclient.api.GuiComponent;
import me.mioclient.record.JsonProfile;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class Class_0040 extends ClickGuiPanel {
   public final TabbedClickGui field_79;
   public boolean field_80;

   public Class_0040(TabbedClickGui var1) {
      super("List");
      this.field_79 = var1;
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      if (this.field_80) {
         for (GuiComponent var8 : this.field_2378) {
            var8.method_4(var3, var5);
         }

         this.method_142();
         this.field_80 = false;
      }

      super.method_2(var1, var2, var3, var5);
   }

   public void method_56() {
      this.field_80 = true;
      this.field_2378.clear();

      for (JsonProfile var2 : Hub.field_2597.method_2(this.field_79.method_369()).getRegistry()) {
         this.field_2378.add(new Class_1062(this.field_79, this, var2));
      }
   }
}
