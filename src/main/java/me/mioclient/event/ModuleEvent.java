package me.mioclient.event;

import me.mioclient.module.Module;

/** Fired when a module is toggled (carries the module). */
public final class ModuleEvent {
   public final Module field_837;

   public ModuleEvent(Module var1) {
      super();
      this.field_837 = var1;
   }

   public Module method_65() {
      return this.field_837;
   }
}
