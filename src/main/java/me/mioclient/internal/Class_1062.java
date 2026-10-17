package me.mioclient.internal;

import me.mioclient.record.JsonProfile;

public final class Class_1062 extends ButtonComponent {
   public final TabbedClickGui field_3270;
   public final JsonProfile field_1636;

   public Class_1062(TabbedClickGui var1, ClickGuiPanel var2, JsonProfile var3) {
      super(var2, var3.getName(), () -> {
         var1.method_368().method_9(var3);
         var1.method_368().method_142();
      });
      this.field_1636 = var3;
      this.field_3270 = var1;
   }

   @Override
   public boolean method_949() {
      return this.field_1636.equals(this.field_3270.method_368().method_550());
   }
}
