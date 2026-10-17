package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.NameTagsModule;

public class Class_0223 implements Predicate {
   public NameTagsModule nameTags;

   public Class_0223(NameTagsModule var1) {
      super();
      this.nameTags = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.nameTags.info.method_194();
   }
}
