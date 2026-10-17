package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.misc.KillEffectsModule;

public class Class_0418 implements Predicate {
   public KillEffectsModule killEffects;

   public Class_0418(KillEffectsModule var1) {
      super();
      this.killEffects = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.killEffects.killSound.method_194() && this.killEffects.players.method_194();
   }
}
