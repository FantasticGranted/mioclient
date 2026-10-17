package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.player.NameProtectModule;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringDecomposer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({StringDecomposer.class})
public class MixinTextVisitFactory {
   private static NameProtectModule nameprotect;

   public MixinTextVisitFactory() {
      super();
   }

   @ModifyArg(
      method = {"iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/util/StringDecomposer;iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z",
         ordinal = 0
      ),
      index = 0
   )
   private static String adjustText(String var0) {
      if (nameprotect == null) {
         if (Hub.field_2595 == null) return var0;
         nameprotect = Hub.field_2595.method_78(NameProtectModule.class);
         if (nameprotect == null) return var0;
      }
      if (nameprotect.isToggled() && Minecraft.getInstance().level != null) {
         var0 = var0.replace(Minecraft.getInstance().getUser().getName(), nameprotect.name.getValue());
      }

      return var0;
   }
}
