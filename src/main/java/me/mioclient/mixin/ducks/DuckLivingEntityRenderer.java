package me.mioclient.mixin.ducks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({LivingEntityRenderer.class})
public interface DuckLivingEntityRenderer<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
   @Invoker("setupRotations")
   void mio$setupRotations(S var1, PoseStack var2, float var3, float var4);

   @Invoker("scale")
   void mio$scale(S var1, PoseStack var2);
}
