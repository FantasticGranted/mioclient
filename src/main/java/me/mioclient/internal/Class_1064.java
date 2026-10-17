package me.mioclient.internal;

import me.mioclient.baritone.api.process.IBaritoneProcess;
import me.mioclient.baritone.api.process.PathingCommand;
import me.mioclient.baritone.api.process.PathingCommandType;

public class Class_1064 implements IBaritoneProcess {
   public final Class_0473 field_3272;

   public Class_1064(Class_0473 var1) {
      super();
      this.field_3272 = var1;
   }

   public boolean isActive() {
      return !this.field_3272.field_1512.isEmpty();
   }

   public PathingCommand onTick(boolean var1, boolean var2) {
      return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
   }

   public boolean isTemporary() {
      return true;
   }

   public void onLostControl() {
   }

   public String displayName0() {
      return "Mio pause service";
   }

   public double priority() {
      return 999.0;
   }
}
