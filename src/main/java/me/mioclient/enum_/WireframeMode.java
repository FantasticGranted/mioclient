package me.mioclient.enum_;

import java.awt.Color;
import me.mioclient.api.Nameable;
import me.mioclient.api.ChamsRenderer;
import me.mioclient.module.render.ChamsModule;

/** Chams wireframe mode. */
public enum WireframeMode implements Nameable, ChamsRenderer {
   BOTH("Both") {
      @Override
      public Color[] method_2(ChamsModule var1) {
         return new Color[]{var1.popLine.getValue(), var1.popFill.getValue()};
      }
   },
   FILL("Fill") {
      @Override
      public Color[] method_2(ChamsModule var1) {
         return new Color[]{field_2973, var1.popFill.getValue()};
      }
   },
   LINE("Line") {
      @Override
      public Color[] method_2(ChamsModule var1) {
         return new Color[]{var1.popLine.getValue(), field_2973};
      }
   };

   public final String field_4163;

    WireframeMode(String var3) {
      this.field_4163 = var3;
   }

   @Override
   public String getName() {
      return this.field_4163;
   }
}
