package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import me.mioclient.Hub;
import me.mioclient.internal.LockUtil;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.FramebufferUtil;
import me.mioclient.internal.VectorUtil;
import me.mioclient.internal.RenderManager;
import me.mioclient.module.exploit.ReachModule;
import me.mioclient.module.player.FreecamModule;
import me.mioclient.module.render.FreeLookModule;
import me.mioclient.module.render.NoBobModule;
import me.mioclient.module.render.NoRenderModule;
import me.mioclient.module.render.ShaderModule;
import me.mioclient.module.render.ViewModelModule;
import me.mioclient.module.render.ZoomModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({GameRenderer.class})
public abstract class MixinGameRenderer {
   private static ZoomModule zoom = Hub.field_2595.method_78(ZoomModule.class);
   private static ReachModule reach = Hub.field_2595.method_78(ReachModule.class);
   private static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   private static ShaderModule shader = Hub.field_2595.method_78(ShaderModule.class);
   private static FreecamModule freecam = Hub.field_2595.method_78(FreecamModule.class);
   private static FreeLookModule freelook = Hub.field_2595.method_78(FreeLookModule.class);
   private static ViewModelModule viewmodel = Hub.field_2595.method_78(ViewModelModule.class);
   private static NoBobModule nobob = Hub.field_2595.method_78(NoBobModule.class);
   @Final
   @Shadow
   Minecraft minecraft;
   @Final
   @Shadow
   private Camera mainCamera;
   @Shadow
   @Final
   public ItemInHandRenderer itemInHandRenderer;
   @Shadow
   @Final
   private RenderBuffers renderBuffers;
   @Unique
   private boolean bobbing;
   @Unique
   private boolean prevBobbing;
   private boolean freecamSet = false;

   public MixinGameRenderer() {
      super();
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"renderLevel"}
   )
   private void renderWorldPreHook(DeltaTracker var1, CallbackInfo var2) {
      this.prevBobbing = true;
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"bobView"},
      cancellable = true
   )
   private void bobViewHook(CameraRenderState var1, PoseStack var2, CallbackInfo var3) {
      if ((Boolean)this.minecraft.options.bobView().get() && this.prevBobbing) {
         this.bobbing = true;
         this.minecraft.options.bobView().set(false);
         this.prevBobbing = false;
         var3.cancel();
      }
   }

   @Inject(
      method = {"bobHurt"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void bobViewWhenHurtHook(CameraRenderState var1, PoseStack var2, CallbackInfo var3) {
      if (norender.isToggled() && norender.hurtCam.getValue()) {
         var3.cancel();
      }
   }

   @Inject(
      method = {"displayItemActivation"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void showFloatingItemHook(ItemStack var1, CallbackInfo var2) {
      if (var1.getItem() == Items.TOTEM_OF_UNDYING && norender.isToggled() && norender.totemOverlay.getValue()) {
         var2.cancel();
      }
   }

   @WrapOperation(
      method = {"renderLevel"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/util/Mth;lerp(FFF)F"
      )},
      require = 0
   )
   private float applyCameraTransformationsMathHelperLerpProxy(float var1, float var2, float var3, Operation<Float> var4) {
      return norender.isToggled() && norender.blindness.getValue() ? 0.0F : Mth.lerp(var1, var2, var3);
   }

   @WrapWithCondition(
      method = {"renderItemInHand"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V"
      )}
   )
   private boolean renderHand(ItemInHandRenderer var1, float var2, PoseStack var3, SubmitNodeCollector var4, LocalPlayer var5, int var6) {
      if (!RenderManager.field_2003) {
         return true;
      } else {
         FramebufferUtil.method_2(shader.shader.getValue().getColor(), true, () -> var1.renderHandsWithItems(var2, var3, var4, var5, var6));
         return false;
      }
   }

   @Redirect(
      method = {"bobView"},
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"
      )
   )
   private void mio$redirectTranslate(PoseStack var1, float var2, float var3, float var4) {
      float var5 = nobob.multiplier.getValue();
      if (var5 <= 0.0F || !nobob.isToggled()) {
         var5 = 1.0F;
      }

      if ((Boolean)this.minecraft.options.bobView().get()) {
         var1.translate(var2 * var5, var3 * var5, var4 * var5);
      }
   }

   @Inject(
      method = {"renderItemInHand"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderHandHook(CameraRenderState var1, float var2, Matrix4fc var3, CallbackInfo var4) {
      if (viewmodel.isToggled() && !viewmodel.shadow.getValue()) {
         float var5 = var1.xRot * Class_0245.field_690;
         float var6 = -(var1.yRot - 45.0F) * Class_0245.field_690;
         float var7 = Mth.cos(var6);
         float var8 = Mth.sin(var6);
         float var9 = Mth.cos(var5);
         float var10 = Mth.sin(var5);
         Vector3f var11 = new Vector3f(var8 * var9, -var10, var7 * var9);
      }

      if (freecam.method_279() || norender.method_279()) {
         var4.cancel();
      }
   }

   @Inject(
      method = {"render"},
      at = {@At("HEAD")}
   )
   private void render(DeltaTracker var1, boolean var2, CallbackInfo var3) {
      Hub.field_2601.method_813();
      if (this.bobbing) {
         this.minecraft.options.bobView().set(true);
         this.bobbing = false;
      }
   }
}
