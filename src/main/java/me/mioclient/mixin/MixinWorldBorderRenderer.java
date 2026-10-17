package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.renderer.WorldBorderRenderer;
import net.minecraft.client.renderer.state.level.WorldBorderRenderState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WorldBorderRenderer.class})
public abstract class MixinWorldBorderRenderer implements MinecraftAccess {
   private static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);

   public MixinWorldBorderRenderer() {
      super();
   }

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderWorldBorderHook(WorldBorderRenderState var1, Vec3 var2, double var3, double var5, CallbackInfo var7) {
      if (norender.isToggled() && norender.worldBorder.getValue()) {
         var7.cancel();
      }
   }
}
