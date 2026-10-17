package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.mioclient.Hub;
import me.mioclient.enum_.ElytraFlyMode;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.MioRenderLayers;
import me.mioclient.internal.RenderManager;
import me.mioclient.module.movement.ElytraFlyModule;
import me.mioclient.module.render.AnimationsModule;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.model.player.PlayerModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.Identifier;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({AvatarRenderer.class})
public abstract class MixinPlayerEntityRenderer extends LivingEntityRenderer<net.minecraft.world.entity.Avatar, AvatarRenderState, PlayerModel> {
   private static AnimationsModule animations = Hub.field_2595.method_78(AnimationsModule.class);
   private static final NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   private static final ElytraFlyModule elytrafly = Hub.field_2595.method_78(ElytraFlyModule.class);
   @Unique
   AvatarRenderState rendered;

   public MixinPlayerEntityRenderer(Context var1, PlayerModel var2, float var3) {
      super(var1, var2, var3);
   }

   @Inject(
      method = {"getRenderOffset(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)Lnet/minecraft/world/phys/Vec3;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void mio$getPositionOffset(AvatarRenderState var1, CallbackInfoReturnable<Vec3> var2) {
      if (animations.method_130() && var1.id != Minecraft.getInstance().player.getId()) {
         var2.setReturnValue(new Vec3(0.0, (double)(var1.scale * -2.0F) / 16.0, 0.0));
      }
   }

   @Inject(
      method = {"scale(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V"},
      at = {@At("HEAD")}
   )
   private void scalePre(AvatarRenderState var1, PoseStack var2, CallbackInfo var3) {
      this.rendered = var1;
   }

   @ModifyArgs(
      method = {"scale(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V"},
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"
      )
   )
   private void scaleHook(Args var1) {
      if (animations.isToggled() && animations.method_128() && this.rendered != null && this.rendered.id != Minecraft.getInstance().player.getId()) {
         var1.set(0, (Float)var1.get(0) * animations.playerScale.getValue());
         var1.set(1, (Float)var1.get(1) * animations.playerScale.getValue());
         var1.set(2, (Float)var1.get(2) * animations.playerScale.getValue());
      }
   }

   @ModifyArgs(
      method = {"setupRotations(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V"},
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V"
      )
   )
   private void setupTransformsHook(Args var1) {
      if (elytrafly.isToggled()) {
         if (elytrafly.mode.getValue() == ElytraFlyMode.BOUNCE
            && elytrafly.method_41()
            && elytrafly.pitchLock.getValue()
            && elytrafly.silent.getValue()) {
            var1.set(0, Axis.XP.rotationDegrees((float)(-Class_0245.field_684)));
         }
      }
   }

   @ModifyExpressionValue(
      method = {"renderHand"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;entityTranslucent(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;"
      )}
   )
   private RenderType renderArmHook(RenderType var1, @Local Identifier var2) {
      return norender.isToggled() && norender.hands.getValue() && RenderManager.field_4422 ? RenderTypes.entitySolid(var2) : var1;
   }

    @ModifyExpressionValue(
       method = {"renderHand"},
       at = {@At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;entityTranslucent(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;"
       )}
    )
    private RenderType renderArm(RenderType var1, @Local Identifier var2) {
       return norender.method_282() != 1.0F ? MioRenderLayers.method_9(var2) : var1;
    }
}
