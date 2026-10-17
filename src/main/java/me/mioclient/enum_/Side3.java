package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.mixin.ducks.DuckMinecraftClient;
import net.minecraft.client.KeyMapping;

public enum Side3 implements Nameable {
   LEFT("Left"),
   RIGHT("Right");

   public final String field_2273;

    Side3(String var3) {
      this.field_2273 = var3;
   }

   public void method_687() {
      if (this == LEFT) {
         ((DuckMinecraftClient)(Object)MinecraftAccess.mc).attack();
      } else {
         ((DuckMinecraftClient)(Object)MinecraftAccess.mc).interact();
      }
   }

   public KeyMapping method_688() {
      return this == LEFT ? MinecraftAccess.mc.options.keyAttack : MinecraftAccess.mc.options.keyUse;
   }

   @Override
   public String getName() {
      return this.field_2273;
   }
}
