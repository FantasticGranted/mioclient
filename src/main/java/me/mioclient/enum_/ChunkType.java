package me.mioclient.enum_;

import java.awt.Color;
import me.mioclient.api.Nameable;
import me.mioclient.module.exploit.NewChunksModule;

/** Classification of a chunk for the New Chunks overlay; each type supplies its fill/outline colors from the module. */
public enum ChunkType implements Nameable {
   NEW("New"),
   OLD("Old") {
      @Override
      public Color[] method_2(NewChunksModule var1) {
         return new Color[]{var1.fill2.getValue(), var1.line.getValue()};
      }
   },
   BLOCKS("Blocks") {
      @Override
      public Color[] method_2(NewChunksModule var1) {
         return new Color[]{var1.fill3.getValue(), var1.line2.getValue()};
      }
   },
   OVERFLOW("Overflow") {
      @Override
      public Color[] method_2(NewChunksModule var1) {
         return new Color[]{var1.fill4.getValue(), var1.line3.getValue()};
      }
   },
   PLACEHOLDER("Placeholder") {
      @Override
      public Color[] method_2(NewChunksModule var1) {
         return null;
      }
   };

   public final String displayName;

    ChunkType(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public Color[] method_2(NewChunksModule var1) {
      return new Color[]{var1.fill.getValue(), var1.outline.getValue()};
   }
}
