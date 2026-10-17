package me.mioclient.record;

import com.google.gson.annotations.SerializedName;
import me.mioclient.api.Nameable;

/** A git commit summary (sha, message, author) deserialized from the GitHub API. */
public final class CommitInfo implements Nameable {
   @SerializedName("sha")
   public final String sha;
   @SerializedName("message")
   public final String message;
   @SerializedName("author")
   public final String author;

   public CommitInfo(String var1, String var2, String var3) {
      super();
      this.sha = var1;
      this.message = var2;
      this.author = var3;
   }

   @Override
   public String getName() {
      return this.message;
   }

   public String getShortSha() {
      return this.sha.substring(0, 6);
   }

   @SerializedName("sha")
   public String getSha() {
      return this.sha;
   }

   @SerializedName("message")
   public String getMessage() {
      return this.message;
   }

   @SerializedName("author")
   public String getAuthor() {
      return this.author;
   }
}
