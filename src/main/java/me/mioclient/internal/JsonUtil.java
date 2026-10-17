package me.mioclient.internal;

import com.google.gson.JsonObject;
import java.util.Map.Entry;

/** Small JsonObject helpers (key presence checks, etc.). */
public class JsonUtil {
   public JsonUtil() {
      super();
   }

   public static boolean method_2(JsonObject var0, String... var1) {
      for (String var5 : var1) {
         if (!var0.has(var5)) {
            return false;
         }
      }

      return true;
   }

   public static boolean method_2(JsonObject var0) {
      for (Entry var2 : var0.entrySet()) {
         if (!(var2.getValue() instanceof JsonObject) || !method_2((JsonObject)var2.getValue())) {
            return false;
         }
      }

      return true;
   }
}
