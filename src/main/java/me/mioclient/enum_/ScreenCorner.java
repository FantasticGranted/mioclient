package me.mioclient.enum_;

import java.util.function.Supplier;
import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.record.Rect;
import net.minecraft.util.Mth;

/** A screen anchor position with an id and a bounds supplier; can hit-test a point. */
public enum ScreenCorner implements MinecraftAccess, Nameable {
   TOP_LEFT("top_left", () -> Rect.method_9(0.0F, 0.0F, 10.0F, 10.0F)),
   TOP_RIGHT("top_right", () -> Rect.method_9((float)(mc.getWindow().getGuiScaledWidth() - 10), 0.0F, 10.0F, 10.0F)),
   BOTTOM_LEFT("bottom_left", () -> Rect.method_9(0.0F, (float)(mc.getWindow().getGuiScaledHeight() - 10), 10.0F, 10.0F)),
   BOTTOM_RIGHT(
      "bottom_right",
      () -> Rect.method_9((float)(mc.getWindow().getGuiScaledWidth() - 10), (float)(mc.getWindow().getGuiScaledHeight() - 10), 10.0F, 10.0F)
   ),
   TOP_CENTER("top_center", () -> Rect.method_9((float)mc.getWindow().getGuiScaledWidth() / 2.0F - 10.0F, 0.0F, 10.0F, 10.0F)),
   NONE("none", () -> Rect.method_9(0.0F, 0.0F, (float)mc.getWindow().getGuiScaledWidth(), (float)mc.getWindow().getGuiScaledHeight()));

   public final String id;
   public final Supplier<Rect> bounds;

    ScreenCorner(String var3, Supplier<Rect> var4) {
      this.id = var3;
      this.bounds = var4;
   }

   @Override
   public String getName() {
      return this.id;
   }

   public boolean method_5(double var1, double var3) {
      return this.bounds
         .get()
         .method_5(
            Mth.clamp(var1, 0.0, (double)mc.getWindow().getGuiScaledWidth()),
            Mth.clamp(var3, 0.0, (double)mc.getWindow().getGuiScaledHeight())
         );
   }

   public Rect method_198() {
      return this.bounds.get();
   }
}
