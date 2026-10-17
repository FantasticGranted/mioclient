package me.mioclient.enum_;

import me.mioclient.Hub;
import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.abstract_.AbstractModule_40;
import net.minecraft.network.chat.Component;

public enum Class_0602 implements Nameable {
   NAME("Name") {
      @Override
      public Component method_2(AbstractModule_40 var1) {
         return Component.literal("Hello " + MinecraftAccess.mc.player.getName().getString() + " :')");
      }
   },
   UID("UID") {
      @Override
      public Component method_2(AbstractModule_40 var1) {
         return Component.literal("Hello uid" + Hub.field_2609.method_802() + " :')");
      }
   },
   CUSTOM("Custom") {
      @Override
      public Component method_2(AbstractModule_40 var1) {
         return Component.literal(var1.text.getValue());
      }
   };

   public final String field_1874;

    Class_0602(String var3) {
      this.field_1874 = var3;
   }

   @Override
   public String getName() {
      return this.field_1874;
   }

   public Component method_2(AbstractModule_40 var1) {
      return null;
   }
}
