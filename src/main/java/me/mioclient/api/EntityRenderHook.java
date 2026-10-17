package me.mioclient.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;

/** Hook to custom-render an entity with a tick delta and matrix stack. */
public interface EntityRenderHook<T extends Entity> {
   void method_2(T var1, float var2, PoseStack var3);
}
