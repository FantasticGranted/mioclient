package me.mioclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Predicate;
import me.mioclient.internal.JsonUtil;
import me.mioclient.internal.SoundId;

public final class Class0211Setting extends Setting<SoundId> {
   public Class0211Setting(String var1, SoundId var2) {
      super(var1, var2);
   }

   public Class0211Setting(String var1, SoundId var2, Predicate<SoundId> var3) {
      super(var1, var2, var3);
   }

   @Override
   public void method_78(String var1) {
      String[] var2 = var1.split(":");
      if (var2.length == 1) {
         this.method_78(new SoundId(var1));
      } else if (var2.length == 2) {
         this.method_78(new SoundId(var2[0], var2[1]));
      }
   }

   @Override
   public JsonElement toJson() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("name", ((SoundId)this.getValue()).getName());
      var1.addProperty("category", ((SoundId)this.getValue()).method_243());
      return var1;
   }

   @Override
   public void fromJson(JsonElement var1) {
      JsonObject var2 = var1.getAsJsonObject();
      if (JsonUtil.method_2(var2, "name", "category")) {
         String var3 = var2.get("name").getAsString();
         String var4 = var2.get("category").getAsString();
         this.method_78(new SoundId(var4, var3));
      }
   }
}
