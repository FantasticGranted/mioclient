package me.mioclient.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.Hub;
import me.mioclient.api.GuiComponent;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.ScreenCorner;
import me.mioclient.module.client.HUDModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;

public class Class_0535 implements MinecraftAccess, GuiComponent {
   public static HUDModule hud = Hub.field_2595.method_78(HUDModule.class);
   public final TailList<HudElement> field_1683 = new TailList<>();
   public final ScreenCorner field_464;

   public Class_0535(ScreenCorner var1) {
      super();
      this.field_464 = var1;
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      for (int var7 = 0; var7 < this.field_1683.size(); var7++) {
         HudElement var8 = this.field_1683.get(var7);
         var8.method_189();
         boolean var9 = var8.method_176() || var8.method_200() > 0.0F;
         float[] var10 = var8.method_31();
         if (!var9) {
            var10 = new float[]{0.0F, 0.0F};
         }

         var8.method_39(var10);
         if (this.field_464 != ScreenCorner.NONE) {
            var8.method_2(this.method_2(var10[0], this.field_464), true);
            if (var7 == 0) {
               var8.method_9(this.method_9(var10[1], this.field_464), true);
            } else if (this.field_464 == ScreenCorner.BOTTOM_LEFT || this.field_464 == ScreenCorner.BOTTOM_RIGHT) {
               var8.method_9(this.field_1683.get(var7 - 1).method_60() - var10[1], true);
            }
         }

         if (var9) {

            var8.method_2(var1, var2, var3, var5);

         }

         if (var7 < this.field_1683.size() - 1 && this.field_464 != ScreenCorner.NONE && this.method_9(this.field_464) == 1) {
            this.field_1683.get(var7 + 1).method_9(var8.method_60() + var10[1], true);
         }
      }
   }

   public ScreenCorner method_3() {
      return this.field_464;
   }

   public TailList<HudElement> method_566() {
      return this.field_1683;
   }

   public int method_9(ScreenCorner var1) {
      return switch (var1) {
         case TOP_LEFT, TOP_RIGHT, TOP_CENTER, NONE -> 1;
         case BOTTOM_LEFT, BOTTOM_RIGHT -> -1;
      };
   }

   public float method_2(float var1, ScreenCorner var2) {
      return switch (var2) {
         case TOP_LEFT, BOTTOM_LEFT -> (float)hud.method_341();
         case TOP_RIGHT, BOTTOM_RIGHT -> (float)mc.getWindow().getGuiScaledWidth() - var1 - (float)hud.method_341();
         case TOP_CENTER -> (float)mc.getWindow().getGuiScaledWidth() / 2F - var1 / 2F;
         case NONE -> throw new IllegalArgumentException("dumb");
      };
   }

   public float method_9(float var1, ScreenCorner var2) {
      return switch (var2) {
         case TOP_LEFT, TOP_CENTER -> (float)hud.method_342();
         case TOP_RIGHT -> hud.method_339() + (float)hud.method_342();
         case NONE -> throw new IllegalArgumentException("dumb");
         case BOTTOM_LEFT, BOTTOM_RIGHT -> (float)mc.getWindow().getGuiScaledHeight() - var1 - this.method_567();
      };
   }

   public float method_567() {
      return Mth.clamp((float)hud.method_342() + hud.method_338(), 0.0F, 14F);
   }
}
