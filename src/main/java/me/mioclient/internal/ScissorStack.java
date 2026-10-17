package me.mioclient.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.LinkedList;
import me.mioclient.api.MinecraftAccess;
import org.joml.Vector4i;
import com.mojang.blaze3d.opengl.GlStateManager;

/** Stack of GL scissor rectangles (Vector4i) for nested clip regions. */
public class ScissorStack implements MinecraftAccess {
   public static final LinkedList<Vector4i> field_81 = new LinkedList<>();

   public ScissorStack() {
      super();
   }

   public static void method_9(int var0, int var1, int var2, int var3) {
      double var4 = mc.getWindow().getGuiScale();
      Vector4i var6 = new Vector4i(
         (int)((double)var0 * var4),
         (int)((double)(mc.getWindow().getGuiScaledHeight() - (var1 + var3)) * var4),
         (int)((double)var2 * var4),
         (int)((double)var3 * var4)
      );
      GlStateManager._scissorBox(var6.x, var6.y, var6.z, var6.w); GlStateManager._enableScissorTest();
      field_81.addLast(var6);
   }

   public static void method_57() {
      GlStateManager._disableScissorTest();;
      field_81.removeLast();
      if (!field_81.isEmpty()) {
         Vector4i var0 = field_81.getLast();
         GlStateManager._scissorBox(var0.x, var0.y, var0.z, var0.w); GlStateManager._enableScissorTest();
      }
   }
}
