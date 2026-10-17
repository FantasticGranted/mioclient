package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.player.AutoCraftModule;

public class Class_1121 implements Predicate {
   public AutoCraftModule autoCraft;

   public Class_1121(AutoCraftModule var1) {
      super();
      this.autoCraft = var1;
   }

   @Override
   public boolean test(Object var1) {
      return !this.autoCraft.craftAll.getValue();
   }
}
