package me.mioclient.internal;

import me.mioclient.api.LookupFactory;

/** Pairs a package/name string with its MethodHandles lookup factory. */
public class LookupEntry {
   public final String field_3884;
   public final LookupFactory field_3885;

   public LookupEntry(String var1, LookupFactory var2) {
      super();
      this.field_3884 = var1;
      this.field_3885 = var2;
   }
}
