package me.mioclient.internal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Path;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;

public final class Class_0950 implements MinecraftAccess {
   public Class_0950() {
      super();
   }

   public static void run() throws java.io.IOException {
      Path var0 = FileManager.field_4281.resolve("friends.json");
      Path var1 = FileManager.field_4281.resolve("socials.json");
      if (var0.toFile().exists() && !var1.toFile().exists()) {
         String var2 = FileUtil.method_9(var0);
         JsonObject var3 = JsonParser.parseString(var2).getAsJsonObject();

         for (JsonElement var5 : var3.getAsJsonArray("friends")) {
            Hub.field_2603.method_632(var5.getAsString());
         }

         FileUtil.method_2(var1, GSON.toJson(Hub.field_2603.toJson()));
         var0.toFile().delete();
      }
   }
}
