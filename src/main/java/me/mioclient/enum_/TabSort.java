package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** ExtraTab player sorting: alphabet, socials, latency, or length. */
public enum TabSort implements Nameable {
   ALPHABET("Alphabet"),
   SOCIALS("Socials"),
   LATENCY("Latency"),
   LENGTH("Length");

   public final String displayName;

    TabSort(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
