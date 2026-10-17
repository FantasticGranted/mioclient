package me.mioclient.api;

import com.mojang.blaze3d.pipeline.RenderTarget;
import java.util.List;
import net.minecraft.client.renderer.PostPass;

/** Mixin accessor for post-effect passes and fake render targets. */
public interface PostEffectAccess {
   void addFakeTarget(String var1, RenderTarget var2);

   List<PostPass> getPasses();
}
