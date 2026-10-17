package me.mioclient.mixin;

import com.mojang.authlib.GameProfile;
import me.mioclient.Hub;
import me.mioclient.module.client.IRCModule;
import me.mioclient.module.player.NameProtectModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AbstractClientPlayer.class})
public abstract class MixinAbstractClientPlayerEntity extends Player {
   private static Identifier PEPSI = Identifier.fromNamespaceAndPath("mio", "capes/pepsi.png");
   private static Identifier TETRIS = Identifier.fromNamespaceAndPath("mio", "capes/tetris.png");
   private static Identifier HEROBRINE = Identifier.fromNamespaceAndPath("mio-mount", "textures/skin_protect.png");
   private static final IRCModule irc = Hub.field_2595.method_78(IRCModule.class);
   private static final NameProtectModule nameprotect = Hub.field_2595.method_78(NameProtectModule.class);

   public MixinAbstractClientPlayerEntity(ClientLevel var1, GameProfile var4) {
      super(var1, var4);
   }

   @Inject(
      method = {"getSkin"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void getCape(CallbackInfoReturnable<PlayerSkin> var1) {
      PlayerSkin var2 = (PlayerSkin)var1.getReturnValue();
      ClientAsset.Texture var3 = var2.body();
      PlayerModelType var4 = var2.model();
      if (nameprotect.isToggled() && nameprotect.skin.getValue() && Minecraft.getInstance().player.equals(this)) {
         var3 = new ClientAsset.ResourceTexture(HEROBRINE);
         var4 = PlayerModelType.WIDE;
         if (nameprotect.slim.getValue()) {
            var4 = PlayerModelType.SLIM;
         }

         var1.setReturnValue(new PlayerSkin(var3, var2.cape(), var2.elytra(), var4, var2.secure()));
      }

      if ("cattyyyn".equals(this.getGameProfile().name())) {
         var1.setReturnValue(new PlayerSkin(var3, new ClientAsset.ResourceTexture(PEPSI), var2.elytra(), var4, var2.secure()));
      }

      if ("u3o".equals(this.getGameProfile().name())) {
         var1.setReturnValue(new PlayerSkin(var3, new ClientAsset.ResourceTexture(TETRIS), var2.elytra(), var4, var2.secure()));
      }

      if (Hub.field_2610.method_326() && irc.capes.getValue()) {
         Identifier var5 = Hub.field_2610.method_68(this.getGameProfile().name());
         if (var5 != null) {
            var1.setReturnValue(new PlayerSkin(var3, new ClientAsset.ResourceTexture(var5), var2.elytra(), var4, var2.secure()));
            var1.cancel();
         }
      }
   }
}
