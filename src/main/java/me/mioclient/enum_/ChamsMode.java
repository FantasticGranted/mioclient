package me.mioclient.enum_;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import me.mioclient.api.Nameable;
import me.mioclient.api.ChamsRenderer;
import me.mioclient.module.render.ChamsModule;
import net.minecraft.world.entity.Entity;

/** Chams render mode: both or fill. */
public enum ChamsMode implements Nameable, ChamsRenderer {
   BOTH("Both"),
   FILL("Fill") {
      @Override
      public Color[] method_2(ChamsModule var1) {
         return new Color[]{field_2973, var1.fill.getValue()};
      }
   },
   LINE("Line") {
      @Override
      public Color[] method_2(ChamsModule var1) {
         return new Color[]{var1.outline.getValue(), field_2973};
      }
   },
   PLAIN("Plain") {
      @Override
      public void method_2(ChamsModule var1, Entity var2, PoseStack var3) {
      }
   },
   OFF("Off");

   public final String field_2152;

    ChamsMode(String var3) {
      this.field_2152 = var3;
   }

   @Override
   public String getName() {
      return this.field_2152;
   }

   @Override
   public boolean method_670() {
      return this != OFF;
   }
}
