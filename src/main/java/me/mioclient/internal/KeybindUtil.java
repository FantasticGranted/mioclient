package me.mioclient.internal;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;
import com.mojang.blaze3d.platform.InputConstants.Type;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.mixin.ducks.DuckKeyBinding;
import me.mioclient.record.Keybind;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

/** Keybind helpers: format a key/mouse binding as a display name and test whether it is pressed. */
public class KeybindUtil implements MinecraftAccess {
   public KeybindUtil() {
      super();
   }

   public static String method_2(Keybind var0) {
      return method_2(var0.method_38(), var0.method_39());
   }

   public static String method_2(int var0, boolean var1) {
      if (var0 < 0) {
         return "NONE";
      } else {
         return var1
            ? new PlaceholderText().method_2(var0).method_9("MOUSE\u0001")
            : InputConstants.Type.KEYSYM.getOrCreate(var0).getName().replaceAll("key.keyboard.", "").replace(".", "_").toUpperCase();
      }
   }

   public static boolean method_9(Keybind var0) {
      return var0.method_39() ? method_3(var0.method_38()) : method_22(var0.method_38());
   }

   public static boolean method_2(KeyMapping var0) {
      Key var1 = ((DuckKeyBinding)(Object)var0).getKey();
      return var1.getType() == Type.MOUSE ? method_3(var1.getValue()) : method_22(var1.getValue());
   }

   public static boolean method_22(int var0) {
      return GLFW.glfwGetKey(mc.getWindow().handle(), var0) == 1;
   }

   public static boolean method_3(int var0) {
      return GLFW.glfwGetMouseButton(mc.getWindow().handle(), var0) == 1;
   }

   public static boolean method_23() {
      return GLFW.glfwGetKey(mc.getWindow().handle(), 341) == 1;
   }

   public static boolean method_24() {
      return GLFW.glfwGetKey(mc.getWindow().handle(), 340) == 1;
   }

   public static boolean method_25() {
      return GLFW.glfwGetKey(mc.getWindow().handle(), 342) == 1;
   }
}
