package me.mioclient.module.abstract_;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.event.Event_26;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.ResizableHudElement;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.ExplosionDamage;
import me.mioclient.module.client.FontsModule;
import me.mioclient.module.client.HUDModule;
import me.mioclient.setting.BooleanSetting;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.world.entity.EquipmentSlot;

public class AbstractModule_12 extends AbstractModule_26 {
   public static HUDModule hud = Hub.field_2595.method_78(HUDModule.class);
   public final Setting<Boolean> durability = this.add(new BooleanSetting("Durability", true));
   public final Setting<Boolean> percentage = this.add(new BooleanSetting("Percentage", true));
   public final Setting<Boolean> barColor = this.add(new BooleanSetting("BarColor", true));

   public AbstractModule_12() {
      super("Armor");
      this.method_2(new ResizableHudElement(this) {
         @Override
         public boolean method_176() {
            return false;
         }
      });
   }

   @Subscribe(
      method_800 = 1
   )
   public void method_2(Event_26 var1) {
      if (hud.isToggled() || this.method_22()) {
         int var2 = var1.method_881().guiWidth();
         int var3 = var1.method_881().guiHeight();
         byte var4 = 15;

         for (int var5 = 3; var5 >= 0; var5--) {
            ItemStack var6 = (ItemStack)mc.player.getInventory().equipment.get(net.minecraft.world.entity.EquipmentSlot.values()[2 + (var5)]);
            if (!var6.isEmpty()) {
               int var7 = this.method_643();
               this.method_2(var1.method_881(), var6, var2 / 2 + var4, var3 - var7);
               if (var6.isDamageableItem() && this.durability.getValue()) {
                  String var8 = String.format("%d%s", ExplosionDamage.method_5(var6), "%");
                  float var9 = FontsModule.field_364.isToggled() ? 0.7F : 0.65F;
                  if (!this.percentage.getValue()) {
                     var8 = var8.substring(0, var8.length() - 1);
                     var9 = 1F;
                  }

                  float var10 = (float)(var3 - var7) - 3.8F - (float)(this.percentage.getValue() ? 0 : 2);
                  FontRenderer.field_3143
                     .method_9(
                        var1.method_881(),
                        var8,
                        (float)var2 / 2F
                           + (float)var4
                           + 9F
                           - FontRenderer.field_3143.method_221(var8) * 0.5F * var9
                           - var9,
                        var10,
                        var9,
                        this.barColor.getValue() ? new Color(var6.getBarColor(), false) : this.method_9(var10)
                     );
               }

               var4 += 18;
            }
         }
      }
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1) {
      super.method_2(var1);
   }

   @Override
   public float[] method_31() {
      return new float[]{0.0F, 0.0F};
   }

   public void method_2(GuiGraphicsExtractor var1, ItemStack var2, int var3, int var4) {
      var1.item(var2, var3, var4);
      var1.itemDecorations(mc.font, var2, var3, var4);
      GlStateManager._enableBlend();
   }

   public int method_643() {
      int var1;
      if ((mc.player.isUnderWater() || mc.player.getAirSupply() != mc.player.getMaxAirSupply())
         && mc.player.getAirSupply() > 0
         && !mc.player.isCreative()) {
         var1 = 65;
      } else if (mc.player.isPassenger() && mc.player.getVehicle() instanceof LivingEntity var2) {
         var1 = (int)(
            (double)Class_0245.field_684
               + Math.ceil((double)((var2.getMaxHealth() - 1F) / 2e+01F))
                  * 1e+01
         );
      } else {
         var1 = 55 - (mc.player.isCreative() ? 17 : 0);
      }

      return var1;
   }
}
