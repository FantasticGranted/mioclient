package me.mioclient.record;

import java.util.Map.Entry;
import me.mioclient.internal.PlaceholderText;
import net.minecraft.ChatFormatting;

/** A name paired with an integer count (e.g. an aggregated tally). */
public final class NameCount {
   public final String name;
   public final int count;

   public NameCount(String var1, int var2) {
      super();
      this.name = var1;
      this.count = var2;
   }

   public static NameCount of(Entry<String, Integer> var0) {
      return new NameCount((String)var0.getKey(), (Integer)var0.getValue());
   }

   public String method_2(ChatFormatting var1) {
      String var10000 = this.name;
      int var10001 = this.count;
      return new PlaceholderText().method_2(var10001).method_2((Object)var10000).method_9("\u0001 x\u0001");
   }

   public String method_228() {
      return this.name;
   }

   public int getCount() {
      return this.count;
   }
}
