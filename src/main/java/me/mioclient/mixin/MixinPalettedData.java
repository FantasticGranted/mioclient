package me.mioclient.mixin;

import me.mioclient.api.PaletteAccess;
import net.minecraft.util.BitStorage;
import net.minecraft.world.level.chunk.Palette;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(
   targets = {"net.minecraft.world.level.chunk.PalettedContainer$Data"}
)
public class MixinPalettedData<T> implements PaletteAccess<T> {
   @Shadow
   @Final
   BitStorage storage;
   @Shadow
   @Final
   Palette<T> palette;

   public MixinPalettedData() {
      super();
   }

   @Override
   public BitStorage mio$storage() {
      return this.storage;
   }

   @Override
   public Palette<T> mio$palette() {
      return this.palette;
   }
}
