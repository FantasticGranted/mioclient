package me.mioclient.internal;

import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_23;
import net.minecraft.network.chat.Component;

public class Class_0747 extends HudTextElement {
   public final Module field_2385;

   public Class_0747(AbstractModule_23 var1, Module var2) {
      super(
         () -> Component.literal(var2.getInfoString()), () -> var2.isToggled() && var2.isDrawn() && (!var2.getKeybind().method_29() || !var1.onlyBound.getValue())
      );
      this.field_2385 = var2;
   }

   public Module method_65() {
      return this.field_2385;
   }
}
