package me.mioclient.mixin;

import me.mioclient.api.ForceSettable;
import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({OptionInstance.class})
public class MixinSimpleOption<T> implements ForceSettable<T> {
   @Shadow
   T value;

   public MixinSimpleOption() {
      super();
   }

   @Override
   public void forceSetValue(T var1) {
      this.value = (T)var1;
   }
}
