package me.mioclient.record;

import me.mioclient.api.Nameable;
import me.mioclient.enum_.RelationType;

public final class SocialEntry implements Nameable {
   public final String name;
   public final RelationType relation;

   public SocialEntry(String var1, RelationType var2) {
      super();
      this.name = var1;
      this.relation = var2;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public String method_228() {
      return this.name;
   }

   public RelationType getRelation() {
      return this.relation;
   }
}
