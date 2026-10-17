package me.mioclient.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import me.mioclient.Hub;
import me.mioclient.api.JsonConfig;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.misc.ChatFilterModule;
import me.mioclient.record.StringPair;
import net.minecraft.ChatFormatting;

public class Class_0295 implements MinecraftAccess, JsonConfig {
   public static ChatFilterModule field_302 = Hub.field_2595.method_78(ChatFilterModule.class);
   public final List<StringPair> field_948 = Collections.synchronizedList(new ArrayList<>());

   public Class_0295() {
      super();
      lookups.method_14(this);
   }

   public boolean method_334(String var1) {
      synchronized (this.field_948) {
         String var3 = var1.toLowerCase();

         for (StringPair var5 : this.field_948) {
            if (var5.getFirst().toLowerCase().equals(var3)) {
               return true;
            }
         }

         return false;
      }
   }

   public void method_38(String var1, String var2) {
      synchronized (this.field_948) {
         StringPair var4 = new StringPair(var1, var2);
         this.field_948.add(var4);
      }
   }

   public boolean method_82(String var1) {
      if (!field_302.caseSensitive.getValue()) {
         var1 = var1.toLowerCase();
      }

      synchronized (this.field_948) {
         for (StringPair var4 : this.field_948) {
            String var5 = var4.getSecond();
            if (!field_302.caseSensitive.getValue()) {
               var5 = var5.toLowerCase();
            }

            if (var1.contains(var5)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isEmpty() {
      synchronized (this.field_948) {
         return this.field_948.isEmpty();
      }
   }

   public Stream<String> method_335() {
      synchronized (this.field_948) {
         return this.field_948.stream().map(StringPair::getFirst);
      }
   }

   public Optional<StringPair> method_310(String var1) {
      synchronized (this.field_948) {
         return this.field_948.stream().filter(var1x -> var1x.getFirst().equalsIgnoreCase(var1)).findAny();
      }
   }

   public void method_2(StringPair var1) {
      synchronized (this.field_948) {
         this.field_948.remove(var1);
      }
   }

   public int method_336() {
      synchronized (this.field_948) {
         return this.field_948.size();
      }
   }

   public List<String> method_337() {
      synchronized (this.field_948) {
         ArrayList var2 = new ArrayList();

         for (StringPair var4 : this.field_948) {
            String var5 = "%s%s%s: \"%s\"".formatted(ChatFormatting.GRAY, var4.getFirst(), ChatFormatting.RESET, var4.getSecond());
            var2.add(var5);
         }

         return var2;
      }
   }

   @Override
   public JsonElement toJson() {
      JsonObject var1 = new JsonObject();
      JsonArray var2 = new JsonArray();
      synchronized (this.field_948) {
         for (StringPair var5 : this.field_948) {
            JsonObject var6 = new JsonObject();
            var6.addProperty("id", var5.getFirst());
            var6.addProperty("filter", var5.getSecond());
            var2.add(var6);
         }
      }

      var1.add("filters", var2);
      return var1;
   }

   @Override
   public void fromJson(JsonElement var1) {
      if (var1 instanceof JsonObject var2 && var2.has("filters") && var2.get("filters") instanceof JsonArray var4) {
         for (int var5 = 0; var5 < var4.size(); var5++) {
            JsonObject var6 = var4.get(var5).getAsJsonObject();
            if (var6.has("id") && var6.has("filter")) {
               String var7 = var6.get("id").getAsString();
               String var8 = var6.get("filter").getAsString();
               if (!this.method_334(var7)) {
                  this.method_38(var7, var8);
               }
            }
         }
      }
   }

   @Override
   public String getConfigName() {
      return "chatfilter.json";
   }
}
