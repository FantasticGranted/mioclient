package me.mioclient.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import me.mioclient.api.JsonConfig;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.record.ItemMatcher;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class Class_0447 extends NamedRegistry<ItemMatcherProfile> implements MinecraftAccess, JsonConfig {
   public String field_1435;

   public Class_0447() {
      super();
   }

   @Override
   public JsonElement toJson() {
      JsonObject var1 = new JsonObject();
      if (this.field_1435 != null) {
         var1.addProperty("current", this.field_1435);
      }

      for (ItemMatcherProfile var3 : (Iterable<ItemMatcherProfile>)(Iterable<?>)((List)this.getRegistry())) {
         JsonArray var4 = new JsonArray();
         var3.method_906().forEach((var1x, var2) -> {
            JsonObject var3x = new JsonObject();
            var3x.addProperty("id", var2.getRawId());
            if (var2.getCustomName() != null) {
               var3x.addProperty("data", var2.getCustomName());
            }

            JsonObject var4x = new JsonObject();
            var4x.addProperty("slot", var1x);
            var4x.add("item", var3x);
            var4.add(var4x);
         });
         var1.add(var3.getName(), var4);
      }

      return var1;
   }

   @Override
   public void fromJson(JsonElement var1) {
      if (var1.isJsonObject()) {
         JsonObject var2 = var1.getAsJsonObject();
         if (var2.has("current")) {
            this.method_27(var2.get("current").getAsString());
         }

         for (Entry var4 : var2.entrySet()) {
            if (((JsonElement)var4.getValue()).isJsonArray()) {
               ItemMatcherProfile var5 = new ItemMatcherProfile((String)var4.getKey());

               for (JsonElement var7 : ((JsonElement)var4.getValue()).getAsJsonArray()) {
                  if (var7.isJsonObject()) {
                     JsonObject var8 = var7.getAsJsonObject();
                     if (var8.has("slot") && var8.has("item")) {
                        ItemMatcher var9 = null;
                        JsonElement var11 = var8.get("item");
                        if (var11 instanceof JsonObject) {
                           JsonObject var10 = (JsonObject)var11;
                           if (var10.has("id")) {
                              int var14 = var10.get("id").getAsInt();
                              String var12 = null;
                              if (var10.has("data")) {
                                 var12 = var10.get("data").getAsString();
                              }

                              var9 = new ItemMatcher(var14, var12);
                           }
                        }

                        var11 = var8.get("item");
                        if (var11 instanceof JsonPrimitive) {
                           JsonPrimitive var13 = (JsonPrimitive)var11;
                           if (var13.isNumber()) {
                              var9 = new ItemMatcher(var13.getAsInt(), null);
                           }
                        }

                        if (var9 != null) {
                           var5.method_906().put(var8.get("slot").getAsInt(), var9);
                        }
                     }
                  }
               }

               this.method_2(var5);
            }
         }
      }
   }

   @Override
   public String getConfigName() {
      return "kits.json";
   }

   public ItemMatcherProfile method_487() {
      return this.field_1435 == null ? null : this.method_2(var1 -> var1.getName().equalsIgnoreCase(this.field_1435)).orElse(null);
   }

   public void method_27(String var1) {
      this.field_1435 = var1;
   }

   public void method_468(String var1) {
      this.getRegistry().removeIf(var1x -> var1x.getName().equalsIgnoreCase(var1));
      ItemMatcherProfile var2 = new ItemMatcherProfile(var1);
      int var3 = 0;

      for (ItemStack var5 : mc.player.getInventory().items) {
         var2.method_906().put(var3, new ItemMatcher(Item.getId(var5.getItem()), method_31(var5)));
         var3++;
      }

      ((List)this.getRegistry()).add(var2);
      this.method_27(var1);
   }

   public static String method_31(ItemStack var0) {
      PotionContents var1 = (PotionContents)var0.get(DataComponents.POTION_CONTENTS);
      if (var1 != null) {
         ArrayList var2 = new ArrayList();
         var1.forEachEffect(
            var1x -> var2.add(new PlaceholderText().method_2(var1x.getAmplifier() + 1).method_2(var1x.getDescriptionId()).method_9("\u0001 \u0001")),
            1.0F
         );
         var2.sort(Comparator.comparing(var0x -> (String)var0x));
         return String.join("", var2);
      } else {
         return null;
      }
   }
}
