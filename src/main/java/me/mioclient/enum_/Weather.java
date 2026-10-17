package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import net.minecraft.world.level.biome.Biome.Precipitation;

/** Weather type with a display name and vanilla precipitation. */
public enum Weather implements Nameable {
   CLEAR("Clear", Precipitation.NONE),
   SNOW("Snow", Precipitation.SNOW),
   RAIN("Rain", Precipitation.RAIN),
   DUSTY("Dusty", Precipitation.NONE);

   public final String displayName;
   public final Precipitation precipitation;

    Weather(String var3, Precipitation var4) {
      this.displayName = var3;
      this.precipitation = var4;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public Precipitation getPrecipitation() {
      return this.precipitation;
   }
}
