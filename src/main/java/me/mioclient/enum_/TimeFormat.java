package me.mioclient.enum_;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import me.mioclient.api.Nameable;

/** Clock display precision (hours / minutes / seconds) with its date format and label. */
public enum TimeFormat implements Nameable {
   HH(new SimpleDateFormat("HH"), "Hours"),
   HH_MM(new SimpleDateFormat("HH:mm"), "Minutes"),
   HH_MM_SS(new SimpleDateFormat("HH:mm:ss"), "Seconds");

   public final DateFormat format;
   public final String displayName;

    TimeFormat(DateFormat var3, String var4) {
      this.format = var3;
      this.displayName = var4;
   }

   public DateFormat getFormat() {
      return this.format;
   }

   public String method_2(Date var1) {
      return this.format.format(var1);
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
