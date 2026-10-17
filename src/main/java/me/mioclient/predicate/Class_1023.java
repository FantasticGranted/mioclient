package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.FastWebMode;
import me.mioclient.module.movement.FastWebModule;

public class Class_1023 implements Predicate {
   public FastWebModule fastWeb;

   public Class_1023(FastWebModule var1) {
      super();
      this.fastWeb = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.fastWeb.field_2450.getValue() != FastWebMode.GRIM;
   }
}
