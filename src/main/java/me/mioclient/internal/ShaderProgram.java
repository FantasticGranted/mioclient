package me.mioclient.internal;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import me.mioclient.api.MinecraftAccess;
import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL32C;

/** GLSL program wrapper for the ESP shaders (currently stubbed: shader system disabled). */
public class ShaderProgram implements MinecraftAccess {
    public static ShaderProgram field_3472;
    public final int field_3473;
    public final Object2IntMap<String> field_3474 = new Object2IntOpenHashMap();

    public ShaderProgram(String var1, String var2) {
        super();
        // 直接给一个默认值，跳过所有着色器编译
        this.field_3473 = -1;
        System.err.println("Shader system disabled (ShaderProgram)");
    }

    public String method_186(String var1) {
        return "";
    }

    public void method_1004() {
        // 如果着色器无效，什么都不做
        if (this.field_3473 == -1) return;
        GlStateManager._glUseProgram(this.field_3473);
        field_3472 = this;
    }

    public int method_98(String var1) {
        // 如果着色器无效，返回 0
        if (this.field_3473 == -1) return 0;
        if (this.field_3474.containsKey(var1)) {
            return this.field_3474.getInt(var1);
        } else {
            int var2 = GlUtil.method_9(this.field_3473, var1);
            this.field_3474.put(var1, var2);
            return var2;
        }
    }

    public void method_2(String var1, boolean var2) {
        if (this.field_3473 == -1) return;
        GlStateManager._glUniform1i(this.method_98(var1), var2 ? 1 : 0);
    }

    public void method_2(String var1, int var2) {
        if (this.field_3473 == -1) return;
        GlStateManager._glUniform1i(this.method_98(var1), var2);
    }

    public void method_2(String var1, double var2) {
        if (this.field_3473 == -1) return;
        GL32C.glUniform1f(this.method_98(var1), (float)var2);
    }

    public void method_2(String var1, double var2, double var4) {
        if (this.field_3473 == -1) return;
        GL32C.glUniform2f(this.method_98(var1), (float)var2, (float)var4);
    }

    public void method_2(String var1, Color var2) {
        if (this.field_3473 == -1) return;
        GL32C.glUniform4f(
                this.method_98(var1),
                (float)var2.getRed() / 255F,
                (float)var2.getGreen() / 255F,
                (float)var2.getBlue() / 255F,
                (float)var2.getAlpha() / 255F
        );
    }

    public void method_2(String var1, Matrix4f var2) {
        if (this.field_3473 == -1) return;
        GlUtil.method_2(this.method_98(var1), var2);
    }

    public void method_1005() {
        if (this.field_3473 == -1) return;
        this.method_2("u_Proj", RenderSystem.getModelViewMatrix());
        this.method_2("u_ModelView", RenderSystem.getModelViewStack());
    }
}