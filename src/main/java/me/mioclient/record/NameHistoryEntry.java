package me.mioclient.record;

import com.google.gson.annotations.SerializedName;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/** One entry of a player's name history (username, change timestamp, hidden flag), deserialized from JSON. */
public final class NameHistoryEntry {
   @SerializedName("username")
   public final String username;
   @SerializedName("changed_at")
   public final String changedAt;
   @SerializedName("hidden")
   public final boolean hidden;
   public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("d MMM yyyy HH:mm:ss");

   public NameHistoryEntry(String var1, String var2, boolean var3) {
      super();
      this.username = var1;
      this.changedAt = var2;
      this.hidden = var3;
   }

   public String getFormattedDate() {
      if (this.changedAt == null) {
         return "first name";
      } else {
         String var1 = this.changedAt.split("\\+")[0];

         try {
            LocalDateTime var2 = LocalDateTime.parse(var1);
            return DATE_FORMAT.format(Date.from(var2.atZone(ZoneId.systemDefault()).toInstant()));
         } catch (Exception var3) {
            return var1;
         }
      }
   }

   public String getDisplayName() {
      return this.hidden ? "HIDDEN" : this.username;
   }

   @SerializedName("changed_at")
   public String getChangedAt() {
      return this.changedAt;
   }

   @SerializedName("hidden")
   public boolean isHidden() {
      return this.hidden;
   }
}
