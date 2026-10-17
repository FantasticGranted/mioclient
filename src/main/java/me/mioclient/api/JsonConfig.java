package me.mioclient.api;

import com.google.gson.JsonElement;

/** Something serializable to and from JSON with an optional config name. */
public interface JsonConfig {
   JsonElement toJson();

   void fromJson(JsonElement var1);

   default String getConfigName() {
      return "";
   }
}
