package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.CombatVersion;
import me.mioclient.internal.Class_0860;
import me.mioclient.internal.RenderManager;
import me.mioclient.module.misc.SwingModule;
import me.mioclient.module.render.ShaderModule;
import me.mioclient.module.render.ViewModelModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(
   value = {ItemInHandRenderer.class},
   priority = 9999
)
public abstract class MixinHeldItemRenderer {
   private static ShaderModule shader = Hub.field_2595.method_78(ShaderModule.class);
   private static ViewModelModule viewmodel = Hub.field_2595.method_78(ViewModelModule.class);
   private static SwingModule swing = Hub.field_2595.method_78(SwingModule.class);
   @Shadow
   private ItemStack mainHandItem;
   @Shadow
   private float mainHandHeight;
   @Shadow
   @Final
   private Minecraft minecraft;

   public MixinHeldItemRenderer() {
      super();
   }

    @Shadow
    protected abstract void renderArmWithItem(
       AbstractClientPlayer var1,
       float var2,
       float var3,
       InteractionHand var4,
       float var5,
       ItemStack var6,
       float var7,
       PoseStack var8,
       SubmitNodeCollector var9,
       int var10
    );

    @Inject(
       method = {"renderArmWithItem"},
       at = {@At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V"
       )}
    )
    private void onRenderItem(
       AbstractClientPlayer var1,
       float var2,
       float var3,
       InteractionHand var4,
       float var5,
       ItemStack var6,
       float var7,
       PoseStack var8,
       SubmitNodeCollector var9,
       int var10,
       CallbackInfo var11
    ) {
      if (viewmodel.isToggled()) {
         boolean var12 = var6.has(DataComponents.FOOD) || var6.getItem() instanceof PotionItem;
         boolean var13 = MinecraftAccess.mc.player.getUseItem().getItem() == var6.getItem();
         if (var12 && MinecraftAccess.mc.player.getUseItem() == var6 && !viewmodel.eating.getValue()) {
            return;
         }

         if (var4 == InteractionHand.MAIN_HAND) {
            float var14 = var12 && var13 ? 0.0F : -viewmodel.mainX.getValue();
            float var15 = var12 && var13 ? 0.0F : viewmodel.mainZ.getValue();
            if (MinecraftAccess.mc.player.getMainArm() == HumanoidArm.LEFT) {
               var14 = -var14;
            }

            var8.translate(var14, viewmodel.mainY.getValue(), var15);
            var8.scale(viewmodel.mainScaleX.getValue(), viewmodel.mainScaleY.getValue(), viewmodel.mainScaleZ.getValue());
            var8.mulPose(Axis.XN.rotationDegrees(viewmodel.mainRotateX.getValue()));
            var8.mulPose(Axis.YP.rotationDegrees(viewmodel.mainRotateY.getValue()));
            var8.mulPose(Axis.ZP.rotationDegrees(viewmodel.mainRotateZ.getValue()));
         } else {
            float var16 = var12 && var13 ? 0.0F : viewmodel.offX.getValue();
            float var17 = var12 && var13 ? 0.0F : viewmodel.offZ.getValue();
            if (MinecraftAccess.mc.player.getMainArm() == HumanoidArm.LEFT) {
               var16 = -var16;
            }

            var8.translate(var16, viewmodel.offY.getValue(), var17);
            var8.scale(viewmodel.offScaleX.getValue(), viewmodel.offScaleY.getValue(), viewmodel.offScaleZ.getValue());
            var8.mulPose(Axis.XN.rotationDegrees(viewmodel.offRotateX.getValue()));
            var8.mulPose(Axis.YN.rotationDegrees(viewmodel.offRotateY.getValue()));
            var8.mulPose(Axis.ZN.rotationDegrees(viewmodel.offRotateZ.getValue()));
         }
      }
   }

   @Inject(
      method = {"renderPlayerArm"},
      at = {@At("HEAD")}
   )
    private void renderArmHook(PoseStack var1, SubmitNodeCollector var2, int var3, float var4, float var5, HumanoidArm var6, CallbackInfo var7) {
      if (viewmodel.isToggled() && viewmodel.arm.getValue()) {
         float var8 = -viewmodel.mainX.getValue();
         if (MinecraftAccess.mc.player.getMainArm() == HumanoidArm.LEFT) {
            var8 = -var8;
         }

         var1.translate(var8, viewmodel.mainY.getValue(), viewmodel.mainZ.getValue());
         var1.scale(viewmodel.mainScaleX.getValue(), viewmodel.mainScaleY.getValue(), viewmodel.mainScaleZ.getValue());
      }
   }

    @Redirect(
       method = {"renderHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V"},
       at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V"
       ),
       require = 0
    )
    private void modifySwing(
       ItemInHandRenderer var1,
       AbstractClientPlayer var2,
       float var3,
       float var4,
       InteractionHand var5,
       float var6,
       ItemStack var7,
       float var8,
       PoseStack var9,
       SubmitNodeCollector var10,
       int var11
    ) {
       this.renderArmWithItem(var2, var3, var4, var5, viewmodel.method_2(var5, var6), var7, var8, var9, var10, var11);
    }

    @ModifyVariable(
       method = {"renderArmWithItem"},
       at = @At("HEAD"),
       argsOnly = true,
       require = 0
    )
    private SubmitNodeCollector renderFirstPersonItemHook(SubmitNodeCollector var1) {
       return var1;
    }

    @Redirect(
       method = {"renderHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V"},
       at = @At(
          value = "INVOKE",
          target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V"
       )
    )
    private void multiplyHook(PoseStack var1, Quaternionfc var2) {
      MinecraftAccess.lookups.method_36(new Class_0860());
      if (!viewmodel.isToggled() || !viewmodel.noSway.getValue()) {
         var1.mulPose(var2);
      }
    }

   @ModifyExpressionValue(
      method = {"tick"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/util/Mth;clamp(FFF)F",
         ordinal = 2
      )}
   )
   private float updateHeldItemsHook(float var1) {
      if (swing.isToggled() && swing.type.getValue() != CombatVersion.VANILLA) {
         boolean var2 = swing.type.getValue() == CombatVersion.ONE_EIGHT;
         float var3 = this.minecraft.player.getAttackStrengthScale(1.0F);
         if (var2) {
            var3 = 1.0F;
         }

         boolean var4 = !ItemStack.isSameItemSameComponents(this.mainHandItem, MinecraftAccess.mc.player.getMainHandItem());
         float var5 = var2 ? -0.6F : -0.4F;
         return Mth.clamp((var4 ? 0.0F : var3 * var3 * var3) - this.mainHandHeight, var5, 0.4F);
      } else {
         return var1;
      }
   }

   @ModifyExpressionValue(
      method = {"renderArmWithItem"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/AbstractClientPlayer;isAutoSpinAttack()Z"
      )}
   )
   private boolean isUsingRiptideHook(boolean var1) {
      return viewmodel.isToggled() && viewmodel.noTridentAnim.getValue() ? false : var1;
   }

   @ModifyArgs(
      method = {"applyItemArmTransform"},
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"
      )
   )
   private void applyEquipOffsetHook(Args var1) {
      if (viewmodel.isToggled() && viewmodel.instantSwap.getValue()) {
         var1.set(1, -0.52F);
      }
   }

   @ModifyArgs(
      method = {"applyEatTransform"},
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",
         ordinal = 0
      )
   )
   private void applyEatOrDrinkTransformationHook(Args var1) {
      if (viewmodel.isToggled()) {
         var1.set(1, (Float)var1.get(1) * viewmodel.eatMultiplier.getValue());
      }
   }
}
