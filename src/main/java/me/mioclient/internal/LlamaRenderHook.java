/*
 * Decompiled with CFR 0.152.
 */
package me.mioclient.internal;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import java.lang.invoke.LambdaMetafactory;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import me.mioclient.internal.EntityRenderHooks;
import me.mioclient.internal.DefaultEntityRenderHook;
import me.mioclient.record.RenderContext;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.animal.llama.BabyLlamaModel;
import net.minecraft.client.model.animal.llama.LlamaModel;
import net.minecraft.client.model.geom.ModelPart;

/** EntityRenderHook for llamas. */
public class LlamaRenderHook
extends DefaultEntityRenderHook {
    private static ModelPart getLlamaPart(LlamaModel model, String name) {
        try {
            Field f = LlamaModel.class.getDeclaredField(name);
            f.setAccessible(true);
            return (ModelPart) f.get(model);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void method_2(RenderContext class_0661, EntityModel<?> entityModel) {
        PoseStack matrixStack = class_0661.getMatrices();
        if (entityModel instanceof LlamaModel) {
            LlamaModel llamaEntityModel = (LlamaModel)entityModel;
            if (llamaEntityModel instanceof BabyLlamaModel) {
                matrixStack.pushPose();
                matrixStack.scale(0.71428573f, 0.64935064f, 0.7936508f);
                matrixStack.translate(0.0f, 1.3125f, 0.22f);
                EntityRenderHooks.method_2(class_0661, getLlamaPart(llamaEntityModel, "head"));
                matrixStack.popPose();
                matrixStack.pushPose();
                matrixStack.scale(0.625f, 0.45454544f, 0.45454544f);
                matrixStack.translate(0.0f, 2.0625f, 0.0f);
                EntityRenderHooks.method_2(class_0661, getLlamaPart(llamaEntityModel, "rightChest"));
                EntityRenderHooks.method_2(class_0661, getLlamaPart(llamaEntityModel, "leftChest"));
                matrixStack.popPose();
                matrixStack.pushPose();
                matrixStack.scale(0.45454544f, 0.41322312f, 0.45454544f);
                matrixStack.translate(0.0f, 2.0625f, 0.0f);
        /* removed indy */ if (false) throw new UnsupportedOperationException("indy decompile failed - method removed by deobfuscator");
                matrixStack.popPose();
            } else {
        /* removed indy */ if (false) throw new UnsupportedOperationException("indy decompile failed - method removed by deobfuscator");
            }
        }
    }
}

