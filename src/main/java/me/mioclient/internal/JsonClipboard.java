package me.mioclient.internal;

import com.google.gson.JsonElement;
import me.mioclient.enum_.Class_0836;

/** Holds a JSON element for copy/paste of module config. */
public final class JsonClipboard {
   public JsonElement field_994;

   public JsonClipboard() {
      super();
   }

   public void method_357() {
      this.field_994 = Class_0836.ALL.toJson();
   }

   public JsonElement method_358() {
      JsonElement var1 = this.field_994;
      this.field_994 = null;
      return var1;
   }
}
