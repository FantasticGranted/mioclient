package me.mioclient.internal;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.mixin.ducks.DuckMinecraftClient;
import org.lwjgl.glfw.GLFW;

public final class Class_0142 implements BooleanConsumer, MinecraftAccess {
   public Class_0142() {
      super();
   }

   public void accept(boolean var1) {
      if (!((DuckMinecraftClient)(Object)mc).mio$isDisconnecting()) {
         mc.setScreen(null);
      }

      if (var1) {
         mc.destroy();
      } else {
         GLFW.glfwSetWindowShouldClose(mc.getWindow().handle(), false);
      }
   }
}
