package me.mioclient.record;

import com.google.gson.JsonObject;
import java.nio.file.Path;
import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.FileUtil;
import me.mioclient.internal.PlaceholderText;

/** A named JSON document that can be serialized to "<name>.json" on disk. */
public final class JsonProfile implements MinecraftAccess, Nameable {
   public final String name;
   public final JsonObject json;

   public JsonProfile(String var1, JsonObject var2) {
      super();
      this.name = var1;
      this.json = var2;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public void method_5(Path var1) throws java.io.IOException {
      String var10001 = this.name;
      FileUtil.method_2(var1.resolve(new PlaceholderText().method_2((Object)var10001).method_9("\u0001.json")), GSON.toJson(this.getJson()));
   }

   public String method_228() {
      return this.name;
   }

   public JsonObject getJson() {
      return this.json;
   }
}
