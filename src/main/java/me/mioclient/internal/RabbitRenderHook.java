/*
 * Decompiled with CFR 0.152.
 */
package me.mioclient.internal;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import java.lang.invoke.LambdaMetafactory;
import java.util.function.Consumer;
import me.mioclient.internal.DefaultEntityRenderHook;
import me.mioclient.record.RenderContext;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.animal.rabbit.BabyRabbitModel;
import net.minecraft.client.model.animal.rabbit.RabbitModel;

/** EntityRenderHook for rabbits. */
public class RabbitRenderHook
extends DefaultEntityRenderHook {
    @Override
    public void method_2(RenderContext class_0661, EntityModel<?> entityModel) {
        PoseStack matrixStack = class_0661.getMatrices();
        if (entityModel instanceof RabbitModel) {
            RabbitModel rabbitEntityModel = (RabbitModel)entityModel;
            if (rabbitEntityModel instanceof BabyRabbitModel) {
                matrixStack.pushPose();
                matrixStack.scale(0.56666666f, 0.56666666f, 0.56666666f);
                matrixStack.translate(0.0f, 1.375f, 0.125f);
        /* removed indy */ if (false) throw new UnsupportedOperationException("indy decompile failed - method removed by deobfuscator");
                matrixStack.popPose();
                matrixStack.pushPose();
                matrixStack.scale(0.4f, 0.4f, 0.4f);
                matrixStack.translate(0.0f, 2.25f, 0.0f);
        /* removed indy */ if (false) throw new UnsupportedOperationException("indy decompile failed - method removed by deobfuscator");
                matrixStack.popPose();
            } else {
                matrixStack.pushPose();
                matrixStack.scale(0.6f, 0.6f, 0.6f);
                matrixStack.translate(0.0f, 1.0f, 0.0f);
        /* removed indy */ if (false) throw new UnsupportedOperationException("indy decompile failed - method removed by deobfuscator");
                matrixStack.popPose();
            }
        }
    }
}

