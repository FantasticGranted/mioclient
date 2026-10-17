package me.mioclient.enum_;

import java.util.function.Supplier;
import me.mioclient.api.Nameable;
import me.mioclient.internal.EntityOutlineShader;
import me.mioclient.internal.RenderManager;

/** How a color is produced: solid, rainbow, gradient, or bloom, via a color supplier. */
public enum ColorMode implements Nameable {
   SOLID("Solid", () -> RenderManager.field_4416),
   RAINBOW("Rainbow", () -> RenderManager.field_4417),
   GRADIENT("Gradient", () -> RenderManager.field_4418),
   BLOOM("Bloom", () -> RenderManager.field_4419);

   public final String displayName;
   public final Supplier<EntityOutlineShader> colorSupplier;

    ColorMode(String var3, Supplier<EntityOutlineShader> var4) {
      this.displayName = var3;
      this.colorSupplier = var4;
   }

   public EntityOutlineShader getColor() {
      return this.colorSupplier.get();
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
