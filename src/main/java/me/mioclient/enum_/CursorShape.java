package me.mioclient.enum_;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

/** A GLFW mouse cursor shape, lazily created and cached by its handle. */
public enum CursorShape {
   STANDARD(() -> GLFW.glfwCreateStandardCursor(221185)),
   POINTER(() -> GLFW.glfwCreateStandardCursor(221188)),
   INPUT(() -> GLFW.glfwCreateStandardCursor(221186));

   public static CursorShape active = STANDARD;
   public final Supplier<Long> cursorSupplier;
   public long handle = -1L;

    CursorShape(Supplier<Long> var3) {
      this.cursorSupplier = var3;
   }

   public long getHandle() {
      if (this.handle == -1L) {
         this.handle = this.cursorSupplier.get();
      }

      return this.handle;
   }

   public void method_580() {
      if (this != active) {
         GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().handle(), this.getHandle());
         active = this;
      }
   }
}
