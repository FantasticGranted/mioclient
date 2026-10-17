package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.mixin.ducks.DuckKeyBinding;
import me.mioclient.module.movement.NoSlowModule;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.ClientInput;
import org.lwjgl.glfw.GLFW;
import net.minecraft.world.phys.Vec2;

public class Class_0411 extends ClientInput implements MinecraftAccess {
   public static NoSlowModule noslow = Hub.field_2595.method_78(NoSlowModule.class);

   public Class_0411() {
      super();
   }

   public void tick(boolean slowDown, float slowDownFactor) {
      this.moveVector = new Vec2(
         this.method_2(this.method_2(mc.options.keyLeft), this.method_2(mc.options.keyRight)),
         this.method_2(this.method_2(mc.options.keyUp), this.method_2(mc.options.keyDown))
      );
   }

   public float method_2(boolean var1, boolean var2) {
      if (var1 == var2) {
         return 0.0F;
      } else {
         return var1 ? 1F : -1F;
      }
   }

   public boolean method_2(KeyMapping var1) {
      return noslow.isToggled() && noslow.method_569() && noslow.guiMove.getValue()
         ? GLFW.glfwGetKey(Minecraft.getInstance().getWindow().handle(), ((DuckKeyBinding)(Object)var1).getKey().getValue()) == 1
         : var1.isDown();
   }
}
