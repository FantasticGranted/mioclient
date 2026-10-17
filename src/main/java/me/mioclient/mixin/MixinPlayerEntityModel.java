package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.mioclient.Hub;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({HumanoidModel.class})
public class MixinPlayerEntityModel {
   private static final NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);

   public MixinPlayerEntityModel() {
      super();
   }

   @ModifyExpressionValue(
      method = {"poseRightArm", "poseLeftArm"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;getUseItemStackForArm(Lnet/minecraft/world/entity/HumanoidArm;)Lnet/minecraft/world/item/ItemStack;"
      )}
   )
   private ItemStack setAnglesHook(ItemStack var1, @Local(argsOnly = true) HumanoidRenderState var2) {
      if (var2 instanceof AvatarRenderState && norender.method_283() == 0.0F) {
         return ItemStack.EMPTY;
      } else {
         return var1;
      }
   }
}
