package me.mioclient.mixin;

import me.mioclient.api.InitialAlphaAccess;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO26.1: alpha/setAlpha moved from Particle to SingleQuadParticle; the InitialAlpha duck
// therefore only applies to quad particles (all ash/smoke particles are quad-based).
@Mixin({SingleQuadParticle.class})
public class MixinParticle implements InitialAlphaAccess {
   @Shadow
   protected float alpha;
   @Unique
   private float mio$initialAlpha;

   public MixinParticle() {
      super();
   }

   @Inject(
      method = {"<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDLnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"},
      at = {@At("TAIL")}
   )
   private void init(ClientLevel var1, double var2, double var4, double var6, TextureAtlasSprite var8, CallbackInfo var9) {
      this.mio$initialAlpha = this.alpha;
   }

   @Inject(
      method = {"<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"},
      at = {@At("TAIL")}
   )
   private void init2(ClientLevel var1, double var2, double var4, double var6, double var8, double var10, double var12, TextureAtlasSprite var14, CallbackInfo var15) {
      this.mio$initialAlpha = this.alpha;
   }

   @ModifyVariable(
      method = {"setAlpha"},
      at = @At("HEAD"),
      ordinal = 0,
      argsOnly = true
   )
   private float setAlpha(float var1) {
      return var1 * this.mio$initialAlpha;
   }

   @Override
   public void mio$setInitialAlpha(float var1) {
      this.mio$initialAlpha = var1;
      this.alpha = var1;
   }

   @Override
   public float mio$getInitialAlpha() {
      return this.mio$initialAlpha;
   }
}
