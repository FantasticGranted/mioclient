package me.mioclient.internal;

import com.google.gson.JsonElement;
import com.mojang.blaze3d.vertex.PoseStack;
import java.io.IOException;
import me.mioclient.Hub;
import me.mioclient.api.GuiComponent;
import me.mioclient.api.CloseableState;
import me.mioclient.enum_.Class_0430;
import me.mioclient.enum_.Class_0836;
import me.mioclient.record.JsonProfile;
import me.mioclient.setting.CustomSetting;
import me.mioclient.setting.Setting;
import me.mioclient.setting.StringSetting;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class Class_0516 extends ClickGuiPanel {
   public final Setting<String> save = new StringSetting("Save", "");
   public final Setting<Class_0836> category = new CustomSetting<>("Category", Class_0836.MODULES);
   public final TabbedClickGui field_1634;
   public Class_0430 field_1635 = Class_0430.NEW;
   public JsonProfile field_1636;
   public boolean field_80;

   public Class_0516(TabbedClickGui var1) {
      super("Preset");
      this.field_1634 = var1;
      this.save.method_9(() -> {
         try {
            this.method_549().method_494(this.save.getValue());
            var1.method_367().method_56();
         } catch (Exception var3) {
            var3.printStackTrace();
         }

         this.save.method_78("");
      });
      this.category.method_9(() -> {
         var1.method_9(this.category.getValue());
         var1.method_367().method_56();
      });
   }

   @Override
   public void init() {
      this.method_56();
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      if (this.field_80) {
         this.method_56();
         this.field_80 = false;
      }

      super.method_2(var1, var2, var3, var5);
   }

   public void method_56() {
      this.field_2378.clear();
      switch (this.field_1635) {
         case NEW:
            this.method_29(new EnumSettingWidget(this, CloseableState.field_3056, this.category));
            this.method_29(new StringSettingWidget(this, CloseableState.field_3056, this.save));
            this.method_29(new ButtonComponent(this, "Refresh", () -> {
               this.method_549().method_56();
               this.field_1634.method_367().method_56();
            }));
            this.method_29(new ButtonComponent(this, "Restore", () -> {
               JsonElement var0 = Hub.field_2597.method_1174().method_358();
               if (var0 != null) {
                  Class_0836.ALL.fromJson(var0);
               }
            }));
            break;
         case EDIT:
            this.method_29(new LabelComponent(this, new PlaceholderText().method_2(this.field_1636.getName()).method_9("Preset \u0001")));
            this.method_29(new ButtonComponent(this, "Load", () -> {
               Hub.field_2597.method_1174().method_357();
               this.method_549().method_465(this.field_1636.getName());
            }));
            this.method_29(new ButtonComponent(this, "Save", () -> {
               try {
                  this.method_549().method_494(this.field_1636.getName());
                  this.field_1634.method_367().method_56();
               } catch (Exception var2) {
               }
            }));
            this.method_29(new ButtonComponent(this, "Delete", () -> {
               this.method_549().method_493(this.field_1636.getName());
               this.method_2(Class_0430.NEW);
            }));
            this.method_29(new ButtonComponent(this, "Back", () -> this.method_2(Class_0430.NEW)));
      }

      this.field_1634.method_367().method_56();
   }

   public void method_29(GuiComponent var1) {
      this.field_2378.add(var1);
   }

   public JsonProfileRegistry method_549() {
      return Hub.field_2597.method_2(this.field_1634.method_369());
   }

   public void method_9(JsonProfile var1) {
      this.field_1636 = var1;
      this.method_2(Class_0430.EDIT);
   }

   public JsonProfile method_550() {
      return this.field_1635 != Class_0430.EDIT ? null : this.field_1636;
   }

   public void method_2(Class_0430 var1) {
      this.field_1635 = var1;
      this.field_80 = true;
   }
}
