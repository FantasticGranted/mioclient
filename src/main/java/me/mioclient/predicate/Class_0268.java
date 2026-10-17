package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.combat.PusherModule;

public class Class_0268 implements Predicate {
   public PusherModule pusher;

   public Class_0268(PusherModule var1) {
      super();
      this.pusher = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.pusher.render.method_194();
   }
}
