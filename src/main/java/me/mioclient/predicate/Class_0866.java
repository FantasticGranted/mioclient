package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.misc.KillEffectsModule;

public class Class_0866 implements Predicate {
   public KillEffectsModule killEffects;

   public Class_0866(KillEffectsModule var1) {
      super();
      this.killEffects = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.killEffects.killSound.method_194();
   }
}
