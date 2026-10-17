package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.Event_14;
import me.mioclient.internal.Colors;
import me.mioclient.internal.RenderManager;
import me.mioclient.internal.RotationManager;
import me.mioclient.module.render.ChamsModule;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   value = {LivingEntityRenderer.class},
   priority = 9999
)
public abstract class MixinLivingEntityRenderer implements MinecraftAccess {
   private static final ChamsModule chams = Hub.field_2595.method_78(ChamsModule.class);
   private static final NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   @Unique
   private final Map mio$stateEntities = new WeakHashMap();

   public MixinLivingEntityRenderer() {
      super();
   }

   @Redirect(
      method = {"submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"
      )
   )
   private void onRenderModel(SubmitNodeCollector var1, Model var2, Object var3, PoseStack var4, RenderType var5, int var6, int var7, int var8, TextureAtlasSprite var9, int var10, ModelFeatureRenderer.CrumblingOverlay var11) {
      if (RenderManager.field_4422) {
         var1.submitModel(var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
      } else {
         LivingEntityRenderState var12 = (LivingEntityRenderState)var3;
         LivingEntity var13 = (LivingEntity)this.mio$stateEntities.get(var12);
         if (var13 == null) {
            var1.submitModel(var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
         } else {
            float var14 = (float)ARGB.red(var8) / 255.0F;
            float var15 = (float)ARGB.green(var8) / 255.0F;
            float var16 = (float)ARGB.blue(var8) / 255.0F;
            float var17 = (float)ARGB.alpha(var8) / 255.0F;
            Event_14 var18 = Event_14.method_2(var13, var6, var14, var15, var16, var17);
            lookups.method_36(var18);
            int var19 = Colors.method_2(var18.method_934(), var18.method_935(), var18.method_936(), var18.method_937());
            if (ChamsModule.field_253) {
               var1.submitModel(var2, var3, var4, var5, var18.method_938(), var7, -1, var9, var10, var11);
            } else if (!var18.method_464()) {
               var1.submitModel(var2, var3, var4, var5, var18.method_938(), var7, var19, var9, var10, var11);
            }
         }
      }
   }

   @ModifyExpressionValue(
      method = {"submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;layers:Ljava/util/List;"
      )}
   )
   private List render2(List var1) {
      return ChamsModule.field_253 ? Collections.emptyList() : var1;
   }

   @Redirect(
      method = {"solveBodyRot(Lnet/minecraft/world/entity/LivingEntity;FF)F"},
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/world/entity/LivingEntity;yBodyRotO:F"
      )
   )
   private static float hook1(LivingEntity var1) {
      return var1 == mc.player && !RotationManager.field_1538 ? Hub.field_2598.method_520() : var1.yBodyRotO;
   }

   @Redirect(
      method = {"extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V"},
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/world/entity/LivingEntity;yHeadRotO:F"
      )
   )
   private float hook2(LivingEntity var1) {
      return var1 == mc.player && !RotationManager.field_1538 ? Hub.field_2598.method_521() : var1.yHeadRotO;
   }

   @Redirect(
      method = {"solveBodyRot(Lnet/minecraft/world/entity/LivingEntity;FF)F"},
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/world/entity/LivingEntity;yBodyRot:F"
      )
   )
   private static float hook3(LivingEntity var1) {
      return var1 == mc.player && !RotationManager.field_1538 ? Hub.field_2598.method_517() : var1.yBodyRot;
   }

   @Redirect(
      method = {"extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V"},
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/world/entity/LivingEntity;yHeadRot:F"
      )
   )
   private float hook4(LivingEntity var1) {
      return var1 == mc.player && !RotationManager.field_1538 ? Hub.field_2598.method_522() : var1.yHeadRot;
   }

   @Redirect(
      method = {"extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;getXRot(F)F"
      )
   )
   private float hook5(LivingEntity var1, float var2) {
      return var1 == mc.player && !RotationManager.field_1538 ? Hub.field_2598.method_519() : var1.getXRot(var2);
   }

   @Inject(
      method = {"extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V"},
      at = {@At("HEAD")}
   )
   private void hook7(LivingEntity var1, LivingEntityRenderState var2, float var3, CallbackInfo var4) {
      this.mio$stateEntities.put(var2, var1);
   }

   @Inject(
      method = {"submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void render(LivingEntityRenderState var1, PoseStack var2, SubmitNodeCollector var3, CameraRenderState var4, CallbackInfo var5) {
      int var6 = norender.wardenDistance.getValue();
      if (var1.entityType == EntityType.WARDEN
         && norender.isToggled()
         && norender.wardens.getValue()
         && var1.distanceToCameraSq > (double)(var6 * var6)) {
         var5.cancel();
      }
   }

   @ModifyExpressionValue(
      method = {"getOverlayCoords(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)I"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;v(Z)I"
      )}
   )
   private static int getV(int var0) {
      return norender.isToggled() && norender.hurt.getValue() ? 10 : var0;
   }
}
