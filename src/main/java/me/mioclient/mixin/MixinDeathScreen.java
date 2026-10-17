package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.misc.CustomDeathTextModule;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({DeathScreen.class})
public class MixinDeathScreen {
   private static CustomDeathTextModule customdeath = Hub.field_2595.method_78(CustomDeathTextModule.class);
   @Mutable
   @Shadow
   @Final
   private Component causeOfDeath;

   public MixinDeathScreen() {
      super();
   }

   @Inject(
      method = {"<init>"},
      at = {@At("RETURN")}
   )
   public void mio$init(Component var1, boolean var2, net.minecraft.client.player.LocalPlayer var3, CallbackInfo var4) {
      if (customdeath != null && customdeath.isToggled()) {
         this.causeOfDeath = customdeath.method_210();
      }
   }
}
