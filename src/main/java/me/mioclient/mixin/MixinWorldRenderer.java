package me.mioclient.mixin;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.mioclient.Hub;
import me.mioclient.api.FramebufferAccess;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.FullbrightMode;
import me.mioclient.event.BlockRenderEvent;
import me.mioclient.event.Event_12;
import me.mioclient.event.Event_3;
import me.mioclient.internal.Class_0500;
import me.mioclient.internal.Class_0947;
import me.mioclient.internal.RenderManager;
import me.mioclient.internal.RenderUtil;
import me.mioclient.module.player.FreecamModule;
import me.mioclient.module.render.AmbienceModule;
import me.mioclient.module.render.ChamsModule;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4fc;
import org.joml.Vector4f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LevelRenderer.class})
public abstract class MixinWorldRenderer implements MinecraftAccess, FramebufferAccess {
   private static AmbienceModule ambience = Hub.field_2595.method_78(AmbienceModule.class);
   private static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   private static FreecamModule freecam = Hub.field_2595.method_78(FreecamModule.class);
   private static ChamsModule chams = Hub.field_2595.method_78(ChamsModule.class);
   @Unique
   private PoseStack mio$stack;
   @Shadow
   private RenderTarget entityOutlineTarget;
   @Shadow
   @Nullable
   private ClientLevel level;

   public MixinWorldRenderer() {
      super();
   }

   @Shadow
   private void renderHitOutline(PoseStack var1, VertexConsumer var2, double var3, double var5, double var7, BlockOutlineRenderState var9, int var10, float var11) {
   }

   @Inject(
      method = {"renderLevel"},
      at = {@At("RETURN")}
   )
   private void render(
      GraphicsResourceAllocator var1,
      DeltaTracker var2,
      boolean var3,
      CameraRenderState var4,
      Matrix4fc var5,
      GpuBufferSlice var6,
      Vector4f var7,
      boolean var8,
      ChunkSectionsToRender var9,
      CallbackInfo var10
   ) {
      PoseStack var11 = new PoseStack();
      var11.pushPose();
      RenderUtil.method_9(var11);
      RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(mc.getMainRenderTarget().getDepthTexture(), 1.0);

      Event_3 var12 = Event_3.method_9(var11, RenderUtil.method_776());
      RenderUtil.field_2672.method_38(() -> lookups.method_36(var12));
      Class_0947 var13 = Class_0947.method_2(var11, RenderUtil.method_776());
      lookups.method_36(var13);
      var11.popPose();
   }

   @Redirect(
      method = {"renderBlockOutline"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/LevelRenderer;renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDLnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;IF)V"
      )
   )
   private void render_drawBlockOutline(
      LevelRenderer var1, PoseStack var2, VertexConsumer var3, double var4, double var6, double var8, BlockOutlineRenderState var10, int var11, float var12
   ) {
      if (this.level == null) {
         this.renderHitOutline(var2, var3, var4, var6, var8, var10, var11, var12);
         return;
      }
      BlockPos var13 = var10.pos();
      BlockState var14 = this.level.getBlockState(var13);
      BlockRenderEvent var15 = new BlockRenderEvent(var2, var3, var13, var14);
      lookups.method_36(var15);
      if (!var15.method_464()) {
         BlockOutlineRenderState var16 = var10;
         BlockPos var17 = var15.method_111();
         if (!var17.equals(var13)) {
            var16 = new BlockOutlineRenderState(var17, var10.isTranslucent(), var10.highContrast(), var10.shape(), var10.collisionShape(), var10.occlusionShape(), var10.interactionShape());
         }
         this.renderHitOutline(var15.method_1089(), var15.method_1090(), var4, var6, var8, var16, var11, var12);
      }
   }

   @Redirect(
      method = {"extractVisibleEntities"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z"
      )
   )
   private boolean renderHook(EntityRenderDispatcher var1, Entity var2, Frustum var3, double var4, double var6, double var8) {
      if (norender.entities2.getValue() && norender.method_230(var2) || chams.method_100(var2)) {
         return false;
      } else {
         return var1.shouldRender(var2, var3, var4, var6, var8);
      }
   }

   @Redirect(
      method = {"extractVisibleEntities"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/Camera;isDetached()Z"
      )
   )
   private boolean renderHook2(Camera var1) {
      return freecam.isToggled() ? true : var1.isDetached();
   }

   @Inject(
      method = {"<init>"},
      at = {@At("RETURN")}
   )
   private void initHook(Minecraft var1, EntityRenderDispatcher var2, BlockEntityRenderDispatcher var3, RenderBuffers var4, GameRenderState var5, FeatureRenderDispatcher var6, CallbackInfo var7) {
      Class_0500.init();
      RenderManager.init();
   }

   @Inject(
      method = {"renderLevel"},
      at = {@At("HEAD")}
   )
   private void onRenderHead(
      GraphicsResourceAllocator var1, DeltaTracker var2, boolean var3, CameraRenderState var4, Matrix4fc var5, GpuBufferSlice var6, Vector4f var7, boolean var8, ChunkSectionsToRender var9, CallbackInfo var10
   ) {
      RenderUtil.method_39(var2.getGameTimeDeltaPartialTick(false));
      this.mio$stack = new PoseStack();
      RenderUtil.method_2(this.mio$stack);
      RenderManager.method_528();
      Event_12 var11 = Event_12.method_5(this.mio$stack, RenderUtil.method_776());
      lookups.method_36(var11);
   }

   @Inject(
      method = {"renderLevel"},
      at = {@At("TAIL")}
   )
   private void onRenderTail(
      GraphicsResourceAllocator var1, DeltaTracker var2, boolean var3, CameraRenderState var4, Matrix4fc var5, GpuBufferSlice var6, Vector4f var7, boolean var8, ChunkSectionsToRender var9, CallbackInfo var10
   ) {
      RenderManager.method_434();
   }

   @Inject(
      method = {"resize"},
      at = {@At("HEAD")}
   )
   private void onResized(int var1, int var2, CallbackInfo var3) {
      RenderManager.method_39(var1, var2);
   }

   @Override
   public RenderTarget getFramebuffer() {
      return this.entityOutlineTarget;
   }

   @Override
   public void setFramebuffer(RenderTarget var1) {
      this.entityOutlineTarget = var1;
   }

   @ModifyVariable(
      method = {"getLightCoords(Lnet/minecraft/client/renderer/LevelRenderer$BrightnessGetter;Lnet/minecraft/world/level/BlockAndLightGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)I"},
      at = @At("STORE"),
      ordinal = 0
   )
   private static int getLightmapCoordinates(int var0) {
      return ambience.isToggled() && ambience.brightness.getValue() == FullbrightMode.SKY ? Math.max(ambience.lightLevel.getValue(), var0) : Math.max(0, var0);
   }
}
