package me.mioclient.enum_;

import me.mioclient.api.Nameable;

public enum RelationType implements Nameable {
   FRIEND("friend", "friends"),
   ENEMY("enemy", "enemies");

   public final String name;
   public final String plural;

    RelationType(String var3, String var4) {
      this.name = var3;
      this.plural = var4;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public String getPlural() {
      return this.plural;
   }
}
