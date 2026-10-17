package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.ScreenshotAction;
import me.mioclient.module.misc.ExtraScreenshotModule;

public class Class_0796 implements Predicate {
   public ExtraScreenshotModule extraScreenshot;

   public Class_0796(ExtraScreenshotModule var1) {
      super();
      this.extraScreenshot = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.extraScreenshot.mode.getValue() != ScreenshotAction.NONE;
   }
}
