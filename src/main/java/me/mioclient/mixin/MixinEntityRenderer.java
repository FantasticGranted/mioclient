package me.mioclient.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.FullbrightMode;
import me.mioclient.event.EntityRenderEvent;
import me.mioclient.internal.RenderManager;
import me.mioclient.module.render.AmbienceModule;
import me.mioclient.module.render.NameTagsModule;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntityRenderer.class})
public abstract class MixinEntityRenderer<T extends Entity> {
   private static NameTagsModule nametags = Hub.field_2595.method_78(NameTagsModule.class);
   private static AmbienceModule ambience = Hub.field_2595.method_78(AmbienceModule.class);

   public MixinEntityRenderer() {
      super();
   }

   @Inject(
      method = {"shouldShowName"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hasLabelHook(T var1, double var2, CallbackInfoReturnable<Boolean> var3) {
      if (RenderManager.field_4422) {
         var3.setReturnValue(false);
         return;
      }

      EntityRenderEvent var4 = new EntityRenderEvent(null, var1);
      MinecraftAccess.lookups.method_36(var4);
      if (var4.method_464()) {
         var3.setReturnValue(false);
         return;
      }

      if (var1 instanceof Player && nametags.isToggled()) {
         var3.setReturnValue(var1.hasCustomName());
      }
   }

   @Inject(
      method = {"getSkyLightLevel"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void getSkyLightHook(CallbackInfoReturnable<Integer> var1) {
      if (ambience.isToggled() && ambience.brightness.getValue() == FullbrightMode.SKY) {
         var1.setReturnValue(Math.max(ambience.lightLevel.getValue(), var1.getReturnValueI()));
      }
   }
}
