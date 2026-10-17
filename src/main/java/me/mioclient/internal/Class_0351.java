package me.mioclient.internal;

import me.mioclient.api.SafetyBoxPredicate;
import me.mioclient.module.abstract_.AbstractModule_42;
import me.mioclient.record.SafetyBox;

public class Class_0351 implements SafetyBoxPredicate {
   public final AbstractModule_42 field_1150;

   public Class_0351(AbstractModule_42 var1) {
      super();
      this.field_1150 = var1;
   }

   @Override
   public boolean method_2(SafetyBox var1) {
      return (
            var1.getBox().getXsize() == 1.0
                  && var1.getBox().getZsize() == 1.0
               || this.field_1150.doubles.getValue()
         )
         && mc.player.getEyePosition().distanceTo(var1.getPos().getCenter()) <= (double)this.field_1150.range.getValue().floatValue()
         && Math.abs(mc.player.getEyePosition().y - var1.getPos().getCenter().y) <= (double)this.field_1150.verticalRange.getValue().floatValue();
   }
}
