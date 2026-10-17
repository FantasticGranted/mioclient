package me.mioclient.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import me.mioclient.api.JsonConfig;
import me.mioclient.api.MinecraftAccess;

public final class Class_0314 extends CollectionRegistry<NamedPosition, List<NamedPosition>> implements MinecraftAccess, JsonConfig {
   public Class_0314() {
      super(new ArrayList<>());
      lookups.method_14(this);
   }

   public List<NamedPosition> getVisible() {
      return this.method_535()
         ? Collections.emptyList()
         : this.field_3243
            .stream()
            .filter(
               var0 -> var0.method_106()
                     .equalsIgnoreCase(
                        mc.player.connection.getServerData() == null ? "singleplayer" : mc.player.connection.getServerData().ip
                     )
            )
            .sorted(Comparator.comparing(var0 -> mc.player.distanceToSqr(var0.method_380(), var0.method_395(), var0.method_396())))
            .toList();
   }

   @Override
   public JsonElement toJson() {
      JsonArray var1 = new JsonArray();
      JsonObject var2 = new JsonObject();

      for (NamedPosition var4 : this.field_3243) {
         var1.add(GSON.toJsonTree(var4));
      }

      var2.add("waypoints", var1);
      return var2;
   }

   @Override
   public void fromJson(JsonElement var1) {
      if (var1.isJsonObject() && var1.getAsJsonObject().has("waypoints")) {
         for (JsonElement var4 : var1.getAsJsonObject().getAsJsonArray("waypoints")) {
            try {
               this.field_3243.add((NamedPosition)GSON.fromJson(var4, NamedPosition.class));
            } catch (Exception var6) {
               var6.printStackTrace();
            }
         }
      }
   }

   @Override
   public String getConfigName() {
      return "waypoints.json";
   }

   @Override public boolean register(NamedPosition var1)   { return method_9(var1); }
   @Override public boolean unregister(NamedPosition var1) { return method_5(var1); }

   public boolean method_9(NamedPosition var1) {
      for (NamedPosition var3 : (Iterable<NamedPosition>)(Iterable<?>)((List)this.getRegistry())) {
         if (var3.getName().equalsIgnoreCase(var1.getName()) && var3.method_106().equalsIgnoreCase(var1.method_106())) {
            return false;
         }
      }

      return ((List)this.getRegistry()).add(var1);
   }

   public boolean method_5(NamedPosition var1) {
      return ((List<NamedPosition>)this.getRegistry())
         .removeIf(var1x -> var1x.getName().equalsIgnoreCase(var1.getName()) && var1x.method_106().equalsIgnoreCase(var1.getName()));
   }
}
