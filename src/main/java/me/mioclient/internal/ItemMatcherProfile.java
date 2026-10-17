package me.mioclient.internal;

import java.util.HashMap;
import java.util.Map;
import me.mioclient.api.Nameable;
import me.mioclient.record.ItemMatcher;

/** Named set of slot-index -> ItemMatcher rules (an inventory/item match profile). */
public final class ItemMatcherProfile implements Nameable {
   public final Map<Integer, ItemMatcher> field_3110 = new HashMap<>();
   public final String field_3111;

   public ItemMatcherProfile(String var1) {
      super();
      this.field_3111 = var1;
   }

   @Override
   public String getName() {
      return this.field_3111;
   }

   public Map<Integer, ItemMatcher> method_906() {
      return this.field_3110;
   }
}
