package me.mioclient.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import java.util.List;
import me.mioclient.api.PostEffectAccess;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({PostChain.class})
public class ShaderEffectMixin implements PostEffectAccess {
   public ShaderEffectMixin() {
      super();
   }

   @Override
   public List<PostPass> getPasses() {
      return List.of();
   }

   @Override
   public void addFakeTarget(String var1, RenderTarget var2) {
      // TODO26.1: PostChain custom render targets removed; PostPass no longer exposes in/out targets.
   }
}
