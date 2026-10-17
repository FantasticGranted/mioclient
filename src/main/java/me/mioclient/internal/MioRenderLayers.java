package me.mioclient.internal;

import java.util.function.Function;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

/** Factory of custom RenderType definitions (lines, quads, textured) used by the client. */
public final class MioRenderLayers {
   public static final Function<Identifier, RenderType> field_1265 = Util.memoize(var0 -> method_2("armor_cutout_no_cull", var0, false));
   public static final Function<Identifier, RenderType> field_1266 = Util.memoize(var0 -> RenderTypes.entitySolid(var0));
   public static final Function<Identifier, RenderType> field_1267 = Util.memoize(var0 -> RenderTypes.entityCutout(var0));

   public MioRenderLayers() {
      super();
   }

   public static RenderType method_2(String var0, Identifier var1, boolean var2) {
      // TODO(port): equal-depth variant and view-offset layering dropped in 26.1 (pipeline-based)
      return RenderTypes.armorCutoutNoCull(var1);
   }

   public static RenderType method_2(Identifier var0) {
      return field_1265.apply(var0);
   }

   public static RenderType method_9(Identifier var0) {
      return field_1266.apply(var0);
   }

   public static RenderType method_5(Identifier var0) {
      return field_1267.apply(var0);
   }
}
