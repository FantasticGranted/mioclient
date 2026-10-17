package me.mioclient.enum_;

import java.awt.Color;
import me.mioclient.module.render.PhaseESPModule;

public enum BlockSafety {
   SAFE {
      @Override
      public Color method_2(PhaseESPModule var1) {
         return var1.safe.getValue();
      }
   },
   SEMI {
      @Override
      public Color method_2(PhaseESPModule var1) {
         return var1.semiSafe.getValue();
      }
   },
   UNSAFE {
      @Override
      public Color method_2(PhaseESPModule var1) {
         return var1.unsafe.getValue();
      }
   };

   public Color method_2(PhaseESPModule var1) {
      return null;
   }
}
