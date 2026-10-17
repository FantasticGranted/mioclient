package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.Event_13;
import me.mioclient.internal.Class_0483;
import me.mioclient.internal.RenderManager;
import me.mioclient.module.render.ChamsModule;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntityRenderDispatcher.class})
public class MixinEntityRenderDispatcher {
   private static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   private static ChamsModule chams = Hub.field_2595.method_78(ChamsModule.class);

   public MixinEntityRenderDispatcher() {
      super();
   }

   @Inject(
      method = {"shouldRender"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public <E extends Entity> void onRenderPre(
      E var1, Frustum var2, double var3, double var5, double var7, CallbackInfoReturnable<Boolean> var9
   ) {
      Event_13 var10 = Event_13.method_9(var1, null, null);
      MinecraftAccess.lookups.method_36(var10);
      if (var10.method_464() || var1 == null) {
         var9.setReturnValue(false);
         var9.cancel();
      }
   }

   @Inject(
      method = {"shouldRender"},
      at = {@At("RETURN")}
   )
   public <E extends Entity> void onRenderPost(
      E var1, Frustum var2, double var3, double var5, double var7, CallbackInfoReturnable<Boolean> var9
   ) {
      Class_0483 var10 = Class_0483.method_2(var1, null, null);
      MinecraftAccess.lookups.method_36(var10);
   }

   @WrapOperation(
      method = {"submit"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitFlame(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lorg/joml/Quaternionf;)V"
      )}
   )
   private void renderFireHook(SubmitNodeCollector var1, PoseStack var2, EntityRenderState var3, Quaternionf var4, Operation<Void> var5) {
      if (norender.isToggled()
         && norender.fire.getValue()
         && norender.others.getValue()) {
         return;
      }

      var5.call(var1, var2, var3, var4);
   }

   @WrapOperation(
      method = {"submit"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitShadow(Lcom/mojang/blaze3d/vertex/PoseStack;FLjava/util/List;)V"
      )}
   )
   private void onRenderShadow(SubmitNodeCollector var1, PoseStack var2, float var3, java.util.List var4, Operation<Void> var5) {
      if (RenderManager.field_4422) {
         return;
      }

      var5.call(var1, var2, var3, var4);
   }
}
