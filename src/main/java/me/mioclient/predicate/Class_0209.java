package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.misc.BetterChatModule;

public class Class_0209 implements Predicate {
   public BetterChatModule betterChat;

   public Class_0209(BetterChatModule var1) {
      super();
      this.betterChat = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.betterChat.timestamps.method_194();
   }
}
