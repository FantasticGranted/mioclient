package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.misc.BetterChatModule;

public class Class_0390 implements Predicate {
   public BetterChatModule betterChat;

   public Class_0390(BetterChatModule var1) {
      super();
      this.betterChat = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.betterChat.customSuffix.method_194();
   }
}
