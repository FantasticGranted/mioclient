package me.mioclient.mixin;

import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({Options.class})
public class MixinGameOptions {
   public MixinGameOptions() {
      super();
   }

   @ModifyConstant(
      method = {"<init>"},
      constant = {@Constant(
         intValue = 110
      )}
   )
   private int bullet(int var1) {
      return 150;
   }
}
