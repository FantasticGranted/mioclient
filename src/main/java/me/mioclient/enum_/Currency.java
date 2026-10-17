package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Supported currency codes. */
public enum Currency implements Nameable {
   USD("USD"),
   EUR("EUR"),
   RUB("RUB"),
   CNY("CNY"),
   TRY("TRY"),
   JPY("JPY"),
   PLN("PLN"),
   BRL("BRL");

   public final String code;

    Currency(String var3) {
      this.code = var3;
   }

   @Override
   public String getName() {
      return this.code;
   }
}
