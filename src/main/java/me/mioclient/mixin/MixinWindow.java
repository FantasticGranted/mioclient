package me.mioclient.mixin;

import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.platform.WindowEventHandler;
import com.mojang.blaze3d.systems.GpuBackend;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Window.class})
public class MixinWindow {
   public MixinWindow() {
      super();
   }

   @Inject(
      method = {"createGlfwWindow(IILjava/lang/String;JLcom/mojang/blaze3d/systems/GpuBackend;)J"},
      at = {@At(
         value = "INVOKE_ASSIGN",
         target = "Lorg/lwjgl/glfw/GLFW;glfwCreateWindow(IILjava/lang/CharSequence;JJ)J",
         shift = Shift.BEFORE,
         remap = false
      )}
   )
   private static void init(int var1, int var2, String var3, long var4, GpuBackend var6, CallbackInfoReturnable<Long> var7) {
      GLFW.glfwWindowHint(139270, 0);
   }
}
