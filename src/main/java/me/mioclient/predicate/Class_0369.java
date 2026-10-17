package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.ColorMode;
import me.mioclient.module.render.ShaderModule;

public class Class_0369 implements Predicate {
   public ShaderModule shader;

   public Class_0369(ShaderModule var1) {
      super();
      this.shader = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.shader.shader.getValue() == ColorMode.BLOOM;
   }
}
