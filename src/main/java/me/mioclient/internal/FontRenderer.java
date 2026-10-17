package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.client.FontsModule;
import me.mioclient.module.player.NameProtectModule;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

/**
 * Custom text renderer: draws strings/Text with the user-selected custom font (lazily
 * built from the Fonts module), with name-protection and string helpers. Shared singleton.
 */
public class FontRenderer implements MinecraftAccess {
   public static final NameProtectModule nameprotect = Hub.field_2595.method_78(NameProtectModule.class);
   public static final FontRenderer field_3143 = new FontRenderer();
   public static int field_3144 = 0;
   public MioGlyphRenderer font;

   public FontRenderer() {
      super();
   }

   public static String method_3(Object var0) {
      return Character.toUpperCase(var0.toString().charAt(0)) + var0.toString().toLowerCase().substring(1);
   }

   public static boolean method_2(String var0, char var1) {
      for (int var2 = 0; var2 < var0.length(); var2++) {
         if (var0.charAt(var2) == var1) {
            return true;
         }
      }

      return false;
   }

   public MioGlyphRenderer method_914() {
      if (this.font == null) {
         this.font = this.method_915();
      }

      return this.font;
   }

   public void method_2(MioGlyphRenderer var1) {
      if (var1 != null) {
         this.font = var1;
         System.gc();
      }
   }

   public MioGlyphRenderer method_915() {
      FontsModule var1 = FontsModule.field_364;
      return MioGlyphRenderer.method_2(var1.font.getValue(), var1.size.getValue(), var1.style.getValue());
   }

   public void method_2(GuiGraphicsExtractor var1, String var2, float var3, float var4, Color var5) {
      this.method_2(var1, Component.nullToEmpty(var2), var3, var4, var5);
   }

   public void method_2(GuiGraphicsExtractor var1, Component var2, float var3, float var4, Color var5) {
      if (FontsModule.field_364.isToggled()) {
         this.method_914().method_2(var1.pose(), var2.getVisualOrderText(), var3, var4, var5.hashCode(), false);
      } else {
         mc.font
            .drawInBatch(
               var2,
               (float)((int)var3),
               (float)((int)var4),
               var5.hashCode(),
               false,
               new org.joml.Matrix4f().mul(var1.pose()),
               this.method_914().method_476(),
               DisplayMode.NORMAL,
               0,
               15728880
            );
      }
   }

   public void method_9(GuiGraphicsExtractor var1, String var2, float var3, float var4, Color var5) {
      this.method_9(var1, Component.nullToEmpty(var2), var3, var4, var5);
   }

   public void method_9(GuiGraphicsExtractor var1, Component var2, float var3, float var4, Color var5) {
      if (FontsModule.field_364.isToggled()) {
         this.method_914().method_2(var1.pose(), var2.getVisualOrderText(), var3, var4, var5.hashCode(), true);
      } else {
         mc.font
            .drawInBatch(
               var2,
               (float)((int)var3),
               (float)((int)var4),
               var5.hashCode(),
               true,
               new org.joml.Matrix4f().mul(var1.pose()),
               this.method_914().method_476(),
               DisplayMode.NORMAL,
               0,
               15728880
            );
      }
   }

   public void method_2(GuiGraphicsExtractor var1, String var2, float var3, float var4, float var5, Color var6) {
      this.method_2(var1, Component.nullToEmpty(var2), var3, var4, var5, var6);
   }

   public void method_2(GuiGraphicsExtractor var1, Component var2, float var3, float var4, float var5, Color var6) {
      var1.pose().pushMatrix();
      var1.pose().scale(var5,  var5);
      this.method_2(var1, var2, var3 / var5, var4 / var5, var6);
      var1.pose().scale(1.0F / var5,  1.0F / var5);
      var1.pose().popMatrix();
   }

   public void method_9(GuiGraphicsExtractor var1, String var2, float var3, float var4, float var5, Color var6) {
      this.method_9(var1, Component.nullToEmpty(var2), var3, var4, var5, var6);
   }

   public void method_9(GuiGraphicsExtractor var1, Component var2, float var3, float var4, float var5, Color var6) {
      var1.pose().pushMatrix();
      var1.pose().scale(var5,  var5);
      this.method_9(var1, var2, var3 / var5, var4 / var5, var6);
      var1.pose().scale(1.0F / var5,  1.0F / var5);
      var1.pose().popMatrix();
   }

   public void method_474() {
      this.method_914().method_474();
   }

   public float method_221(String var1) {
      if (nameprotect != null && nameprotect.isToggled() && !this.method_535()) {
         String repl = nameprotect.name == null ? "" : nameprotect.name.getValue();
         var1 = var1.replace(mc.player.getName().getString(), repl);
      }

      if (FontsModule.field_364 != null && FontsModule.field_364.isToggled()) {
         return (float)this.method_914().method_473(var1);
      }
      return (float)mc.font.width(var1);
   }

   public int method_66() {
      if (FontsModule.field_364 != null && FontsModule.field_364.isToggled()) {
         return this.method_914().method_472() - 1;
      } else {
         return mc == null || mc.font == null ? 9 : 9;
      }
   }

   public static int method_916() {
      return FontsModule.field_364.shift.getValue();
   }

   public static float method_917() {
      return FontsModule.field_364.shadow.getValue();
   }

   public static int method_918() {
      return FontsModule.field_364.translate.getValue();
   }

   public static void method_35(int var0) {
      field_3144 = var0;
   }

   public static int method_919() {
      return field_3144;
   }

   public static String method_222(String var0) {
      return var0.replaceAll("[^a-zA-Z0-9.\\-]", "_");
   }


    public void method_2(PoseStack var1, String var2, float var3, float var4, Color var5) {
       this.method_2(var1, Component.nullToEmpty(var2), var3, var4, var5);
    }

    public void method_2(PoseStack var1, Component var2, float var3, float var4, Color var5) {
       if (FontsModule.field_364.isToggled()) {
          this.method_914().method_2(var1, var2.getVisualOrderText(), var3, var4, var5.hashCode(), false);
       } else {
          mc.font.drawInBatch(
             var2,
             (float)((int)var3),
             (float)((int)var4),
             var5.hashCode(),
             false,
             var1.last().pose(),
             this.method_914().method_476(),
             DisplayMode.NORMAL,
             0,
             15728880
          );
       }
    }

    public void method_9(PoseStack var1, String var2, float var3, float var4, Color var5) {
       this.method_9(var1, Component.nullToEmpty(var2), var3, var4, var5);
    }

    public void method_9(PoseStack var1, Component var2, float var3, float var4, Color var5) {
       if (FontsModule.field_364.isToggled()) {
          this.method_914().method_2(var1, var2.getVisualOrderText(), var3, var4, var5.hashCode(), true);
       } else {
          mc.font.drawInBatch(
             var2,
             (float)((int)var3),
             (float)((int)var4),
             var5.hashCode(),
             true,
             var1.last().pose(),
             this.method_914().method_476(),
             DisplayMode.NORMAL,
             0,
             15728880
          );
       }
    }
}
