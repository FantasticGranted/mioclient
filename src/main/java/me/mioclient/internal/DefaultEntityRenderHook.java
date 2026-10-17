/*
 * Decompiled with CFR 0.152.
 */
package me.mioclient.internal;

import java.lang.invoke.LambdaMetafactory;
import java.util.function.Consumer;
import me.mioclient.Hub;
import me.mioclient.api.EntityRenderHook;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.RenderMode;
import me.mioclient.internal.EntityRenderHooks;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.Class_0878;
import me.mioclient.mixin.ducks.DuckLivingEntityRenderer;
import me.mioclient.module.render.AnimationsModule;
import me.mioclient.record.RenderContext;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.HumanoidModel;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.player.PlayerModel;

import net.minecraft.client.model.npc.VillagerModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

/** Default EntityRenderHook: renders an entity's model/box according to the active RenderMode. */
public class DefaultEntityRenderHook
implements MinecraftAccess,
EntityRenderHook<LivingEntity> {
    public static AnimationsModule animations = Hub.field_2595.method_78(AnimationsModule.class);
    public boolean field_2003;
    public boolean field_2004 = false;
    public boolean field_2005 = false;

    @Override
    public void method_2(LivingEntity livingEntity, float f, PoseStack matrixStack) {
        VillagerModel villagerResemblingModel;
        Direction direction;
        float f2;
        Entity entity;
        Class_0878 class_0878;
        this.field_2003 = DefaultEntityRenderHook.mc.player == livingEntity;
        this.field_2004 = livingEntity instanceof Class_0878;
        if (this.field_2004) {
            f = 1.0f;
        }
        if (livingEntity instanceof Class_0878) {
            class_0878 = (Class_0878)livingEntity;
            this.field_2005 = class_0878.method_806();
        } else {
            this.field_2005 = false;
        }
        Object renderer = mc.getEntityRenderDispatcher().getRenderer((Entity)livingEntity);
        if (!(renderer instanceof LivingEntityRenderer)) {
            return;
        }
        DuckLivingEntityRenderer duckLivingEntityRenderer = (DuckLivingEntityRenderer)renderer;
        EntityModel entityModel = ((LivingEntityRenderer)renderer).getModel();
        matrixStack.pushPose();
        // TODO(port): EntityModel MODEL_Y_OFFSET/riding/child fields removed in 26.1 (state-driven)
        // TODO(port): entityModel.riding = livingEntity.isPassenger();
        // TODO(port): entityModel.child = livingEntity.isBaby();
        float f3 = Mth.rotLerp((float)f, (float)livingEntity.yBodyRotO, (float)livingEntity.yBodyRot);
        float f4 = Mth.rotLerp((float)f, (float)livingEntity.yHeadRotO, (float)livingEntity.yHeadRot);
        if (this.field_2003) {
            f3 = Mth.rotLerp((float)f, (float)DefaultEntityRenderHook.rotations().method_520(), (float)DefaultEntityRenderHook.rotations().method_517());
            f4 = Mth.rotLerp((float)f, (float)DefaultEntityRenderHook.rotations().method_521(), (float)DefaultEntityRenderHook.rotations().method_522());
        }
        float f5 = f4 - f3;
        if (livingEntity.isPassenger() && (entity = livingEntity.getVehicle()) instanceof LivingEntity) {
            LivingEntity livingEntity2 = (LivingEntity)entity;
            f3 = this.field_2003 ? Mth.rotLerp((float)f, (float)DefaultEntityRenderHook.rotations().method_520(), (float)DefaultEntityRenderHook.rotations().method_517()) : Mth.rotLerp((float)f, (float)livingEntity2.yBodyRotO, (float)livingEntity2.yBodyRot);
            f5 = f4 - f3;
            f2 = Mth.clamp((float)Mth.wrapDegrees((float)f5), (float)-85.0f, (float)85.0f);
            f3 = f4 - f2;
            if (f2 * f2 > 2500.0f) {
                f3 += f2 * 0.2f;
            }
            f5 = f4 - f3;
        }
        float f6 = Mth.lerp((float)f, (float)livingEntity.xRotO, (float)livingEntity.getXRot());
        if (this.field_2003) {
            f6 = Mth.lerp((float)f, (float)DefaultEntityRenderHook.rotations().method_519(), (float)DefaultEntityRenderHook.rotations().method_516());
        }
        if (((LivingEntityRenderer)renderer).isEntityUpsideDown(livingEntity)) {
            f6 *= -1.0f;
            f5 *= -1.0f;
        }
        if (livingEntity.isSleeping() && (direction = livingEntity.getBedOrientation()) != null) {
            float f7 = livingEntity.getEyeHeight(Pose.STANDING) - 0.1f;
            matrixStack.translate((float)(-direction.getStepX()) * f7, 0.0f, (float)(-direction.getStepZ()) * f7);
        }
        // TODO(port): HumanoidModel.sneaking removed in 26.1 (state-driven)
        // {
        //    Player playerEntity;
        //    biped.sneaking = ...
        // }
        LivingEntityRenderState mioState = (LivingEntityRenderState)((LivingEntityRenderer)renderer).createRenderState(livingEntity, f);
        mioState.bodyRot = f3;
        mioState.yRot = f4;
        mioState.xRot = f6;
        float f8 = livingEntity.getScale();
        mioState.scale = f8;
        matrixStack.scale(f8, f8, f8);
        duckLivingEntityRenderer.mio$setupRotations(mioState, matrixStack, f3, f8);
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        duckLivingEntityRenderer.mio$scale(mioState, matrixStack);
        matrixStack.translate(0.0f, -1.501f, 0.0f);
        float f9 = 0.0f;
        float f10 = 0.0f;
        if (!livingEntity.isPassenger() && livingEntity.isAlive()) {
            if (livingEntity instanceof Class_0878) {
                f9 = Math.min(livingEntity.walkAnimation.speed(), 1.0f);
                f10 = livingEntity.walkAnimation.position();
            } else {
                f9 = Math.min(livingEntity.walkAnimation.speed(f), 1.0f);
                f10 = livingEntity.walkAnimation.position(f);
            }
            if (livingEntity.isBaby()) {
                f10 *= 3.0f;
            }
        }
        Object mioRenderState = mioState;
        entityModel.setupAnim(mioRenderState);
        RenderContext class_0661 = new RenderContext(matrixStack, RenderMode.BOTH);
        if (entityModel instanceof VillagerModel) {
            villagerResemblingModel = (VillagerModel)entityModel;
            villagerResemblingModel.getHead().getChild("hat").getChild("hat_rim").visible = false;
        }
        if (entityModel instanceof HumanoidModel) {
            this.method_2((HumanoidModel)entityModel);
        }
        EntityRenderHooks.method_2(class_0661, entityModel.root());
        matrixStack.popPose();
    }

    public void method_2(RenderContext class_0661, EntityModel<?> entityModel) {
    }

    public void method_2(HumanoidModel<?> bipedEntityModel) {
        boolean bl;
        bipedEntityModel.hat.visible = bl = EntityRenderHooks.method_505() && !this.field_2004;
        if (bipedEntityModel instanceof PlayerModel) {
            PlayerModel playerEntityModel = (PlayerModel)bipedEntityModel;
            playerEntityModel.leftPants.visible = bl;
            playerEntityModel.rightPants.visible = bl;
            playerEntityModel.leftSleeve.visible = bl;
            playerEntityModel.rightSleeve.visible = bl;
            playerEntityModel.jacket.visible = bl;
            if (this.field_2005) {
                // TODO26.1 playerEntityModel.rightArmPose = HumanoidModel.ArmPose.EMPTY;
                // TODO26.1 playerEntityModel.leftArmPose = HumanoidModel.ArmPose.EMPTY;
            }
        }
    }

    public <T> T method_2(T t, T t2) {
        if (this.field_2003) {
            return t2;
        }
        return t;
    }

    public static RotationManager rotations() {
        return Hub.field_2598;
    }
}

