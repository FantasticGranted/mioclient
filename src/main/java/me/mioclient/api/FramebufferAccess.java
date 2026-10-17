package me.mioclient.api;

import com.mojang.blaze3d.pipeline.RenderTarget;

/** Mixin accessor for a stored Framebuffer. */
public interface FramebufferAccess {
   RenderTarget getFramebuffer();

   void setFramebuffer(RenderTarget var1);
}
