package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.RenderUtil;
import me.mioclient.module.abstract_.AbstractModule_24;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({InventoryScreen.class})
public class MixinInventoryScreen {
   private static AbstractModule_24 playermodel = Hub.field_2595.method_78(AbstractModule_24.class);

   public MixinInventoryScreen() {
      super();
   }

   @Inject(
      method = {"extractEntityInInventoryFollowsMouse(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIIIIFFFLnet/minecraft/world/entity/LivingEntity;)V"},
      at = {@At("HEAD")}
   )
   private static void drawEntityHook(
      GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, LivingEntity var9, CallbackInfo var10
   ) {
      RotationManager.field_1538 = true;
   }

   @Inject(
      method = {"extractEntityInInventoryFollowsMouse(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIIIIFFFLnet/minecraft/world/entity/LivingEntity;)V"},
      at = {@At("RETURN")}
   )
   private static void drawEntityHook2(
      GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, LivingEntity var9, CallbackInfo var10
   ) {
      RotationManager.field_1538 = false;
   }

   @Redirect(
      method = {"extractRenderState(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/client/renderer/entity/state/EntityRenderState;"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;createRenderState(Lnet/minecraft/world/entity/Entity;F)Lnet/minecraft/client/renderer/entity/state/EntityRenderState;"
      )
   )
    private static net.minecraft.client.renderer.entity.state.EntityRenderState lambdaHook(
       net.minecraft.client.renderer.entity.EntityRenderer var0, net.minecraft.world.entity.Entity var1, float var2
    ) {
      return (net.minecraft.client.renderer.entity.state.EntityRenderState)var0.createRenderState(
         var1, AbstractModule_24.field_1777 ? RenderUtil.method_776() : var2
      );
   }
}
