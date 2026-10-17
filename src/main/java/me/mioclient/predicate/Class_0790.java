package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.NameTagsModule;

public class Class_0790 implements Predicate {
   public NameTagsModule nameTags;

   public Class_0790(NameTagsModule var1) {
      super();
      this.nameTags = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.nameTags.colors.method_194();
   }
}
