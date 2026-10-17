package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.api.CloseableState;
import me.mioclient.enum_.CursorShape;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** ClickGui widget for a Setting<SoundId>: opens the sound picker. */
public class SoundSettingWidget extends SettingWidget<SoundId> {
   @SuppressWarnings({"unchecked", "rawtypes"})
   public SoundSettingWidget(ClickGuiPanel var1, CloseableState var2, Setting<?> var3) {
      super(var1, var2, (Setting) var3);
   }

   @Override
   public void method_2(double var1, double var3, int var5) {
      if (!this.method_911() && this.method_5(var1, var3) && var5 == 0) {
         mc.setScreen(new SoundSelectGui(this.field_3138, this.method_912()));
      }
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      if (!this.method_911()) {
         super.method_2(var1, var2, var3, var5);
         if (this.method_5(var3, var5)) {
            ClickGui.field_3460 = CursorShape.POINTER;
         }

         Render2D.method_4(
            var2,
            (float)(this.field_418.getX() + this.method_170()),
            (float)(this.field_418.getY() + this.field_419) + 0.5F,
            (float)(this.field_418.getX() + this.field_418.method_216() - 1),
            (float)(this.field_418.getY() + this.field_419 + this.method_66()) - 0.5F,
            this.method_852().color.getValue()
         );
         String var7 = new PlaceholderText().method_2(this.field_3138.getValue().getName()).method_2(this.field_3138.getName()).method_9("\u0001: \u0001");
         this.method_2(
            var2,
            var7,
            () -> FontRenderer.field_3143
                  .method_9(
                     var1,
                     var7,
                     (float)(this.field_418.getX() + 4),
                     (float)this.field_418.getY() + this.method_850() - this.method_169() + (float)this.field_419,
                     this.method_852().textColor.getValue()
                  )
         );
      }
   }
}
