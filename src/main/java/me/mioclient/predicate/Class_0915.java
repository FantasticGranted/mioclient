package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.client.IRCModule;

public class Class_0915 implements Predicate {
   public IRCModule iRC;

   public Class_0915(IRCModule var1) {
      super();
      this.iRC = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.iRC.chat.method_194() && this.iRC.chatSound.method_194();
   }
}
