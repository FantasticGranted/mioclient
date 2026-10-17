package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.Hub;
import me.mioclient.api.GuiComponent;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.CursorShape;
import me.mioclient.enum_.Class_1290;
import me.mioclient.module.abstract_.AbstractModule_41;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Base ClickGui component anchored to a ClickGuiPanel: hover state, layout offset and animation. */
public abstract class PanelComponent implements MinecraftAccess, GuiComponent {
   public final AnimatedValue field_417 = new AnimatedValue(4F);
   public final ClickGuiPanel field_418;
   public int field_419;
   public int field_420;
   public boolean field_421 = false;

   public PanelComponent(ClickGuiPanel var1, int var2) {
      super();
      this.field_418 = var1;
      this.field_419 = var2;
   }

   public ClickGuiPanel method_167() {
      return this.field_418;
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      if (this.method_5(var3, var5)) {
         if (!this.field_421 && this.method_852().hover.getValue() && !this.method_2(Class_1290.SILENT)) {
            SoundSample var7 = Hub.field_2606.method_2(this.method_852().sound.getValue());
            if (var7 != null) {
               var7.method_230(this.method_852().volume.getValue());
            }
         }

         this.field_421 = true;
         Hub.method_755().method_998();
         ClickGui.field_3460 = CursorShape.POINTER;
      } else {
         this.field_421 = false;
      }

      if (this.method_852().bounce.getValue()) {
         this.field_417.method_3(this.method_5(var3, var5) ? 1F : 0.0F);
      } else {
         this.field_417.method_3(0.0F);
      }

      GuiComponent.super.method_2(var1, var2, var3, var5);
   }

   @Override
   public void method_26(int var1) {
      this.field_419 = var1;
   }

   @Override
   public int method_168() {
      return this.field_419;
   }

   public float method_169() {
      return this.field_417.method_45();
   }

   @Override
   public void method_2(double var1, double var3, int var5) {
      if (this.method_5(var1, var3) && !this.method_2(Class_1290.SILENT)) {
         if (var5 == 0 && this.method_171()) {
            if (this.method_852().leftClick.getValue()) {
               SoundSample var7 = Hub.field_2606.method_2(this.method_852().sound2.getValue());
               if (var7 != null) {
                  var7.method_230(this.method_852().volume2.getValue());
               }
            }
         } else if (var5 == 1 && this.method_852().rightClick.getValue()) {
            SoundSample var6 = Hub.field_2606.method_2(this.method_852().sound3.getValue());
            if (var6 != null) {
               var6.method_230(this.method_852().volume3.getValue());
            }
         }
      }
   }

   @Override
   public boolean method_5(double var1, double var3) {
      return var1 > (double)this.field_418.getX()
         && var1 < (double)(this.field_418.getX() + this.field_418.method_216())
         && var3 > (double)(this.field_418.getY() + this.field_419)
         && var3 < (double)(this.field_418.getY() + this.method_66() + this.field_419);
   }

   public int method_170() {
      return this.method_2(Class_1290.PADDING_SHIFT) ? 2 : 1;
   }

   public boolean method_171() {
      return this instanceof ModulePanel var1 ? !(var1.method_65() instanceof AbstractModule_41) : true;
   }

   public void method_2(Class_1290... var1) {
      for (Class_1290 var5 : var1) {
         this.field_420 = this.field_420 | var5.method_1154();
      }
   }

   public boolean method_2(Class_1290 var1) {
      return (this.field_420 & var1.method_1154()) == var1.method_1154();
   }
}
