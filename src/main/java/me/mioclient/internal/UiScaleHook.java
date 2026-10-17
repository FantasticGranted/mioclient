package me.mioclient.internal;

import me.mioclient.api.MinecraftAccess;
import net.fabricmc.loader.api.FabricLoader;

/** Reads the advanced-ui-scale mod's scale factor when that mod is present. */
public class UiScaleHook implements MinecraftAccess {
   public static double field_3274;
   public static boolean field_3275 = FabricLoader.getInstance().isModLoaded("advanced-ui-scale");

   public UiScaleHook() {
      super();
   }

   public static void method_669() {
      method_38(1F);
   }

   public static void method_38(float var0) {
      if (!method_951()) {
         field_3274 = mc.getWindow().getGuiScale();
         mc.getWindow().setGuiScale((int)Math.round((double)(method_950() * var0)));
      }
   }

   public static void method_159() {
      if (!method_951()) {
         if (field_3274 == -1.0) {
            throw new UnsupportedOperationException();
         } else {
            mc.getWindow().setGuiScale((int)field_3274);
            field_3274 = -1.0;
         }
      }
   }

   public static float method_950() {
      return (float)mc.getWindow().calculateScale(2, mc.isEnforceUnicode());
   }

   public static double method_78(float var0) {
      return method_951() ? 1.0 : (double)method_950() / mc.getWindow().getGuiScale() * (double)var0;
   }

   public static boolean method_951() {
      return field_3275;
   }
}
