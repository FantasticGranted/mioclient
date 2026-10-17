package me.mioclient.internal;

import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;

public class Class_1337 extends ModuleAnimationTracker {
   public Class_1337() {
      super();
   }

   @Subscribe(
      method_800 = -200
   )
   public void method_9(PlayerTickEvent var1) {
      if (this.method_537() != 1F) {
         for (int var2 = 1; (float)var2 <= this.method_537() - 1F; var2++) {
            Class_0654.method_633();
         }
      }
   }
}
