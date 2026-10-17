package me.mioclient.mixin;

import me.mioclient.api.PaletteAccess;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainer.Data;
import me.mioclient.api.DataHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({PalettedContainer.class})
public class MixinPalettedContainer<T> implements DataHolder<T> {
   @Shadow
   private volatile Data<T> data;

   public MixinPalettedContainer() {
      super();
   }

   @Override
   @SuppressWarnings({"rawtypes", "unchecked"})
   public PaletteAccess<T> mio$data() {
      return (PaletteAccess<T>)(Object)this.data;
   }
}
