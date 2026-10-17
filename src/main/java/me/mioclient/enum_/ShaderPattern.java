package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Shader overlay pattern: none, dots, or grid. */
public enum ShaderPattern implements Nameable {
   NONE("None"),
   DOTS("Dots"),
   GRID("Grid");

   public final String displayName;

    ShaderPattern(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
