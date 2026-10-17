package me.mioclient.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Axis;
import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.BufferSource;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import com.mojang.blaze3d.opengl.GlStateManager;

public class SkeletonModule extends Module {
   public static AnimationsModule animations = Hub.field_2595.method_78(AnimationsModule.class);
   public Setting<Color> color;

   public SkeletonModule() {
      super("Skeleton", "Draws skeletons for players.", Category.RENDER);
      Settings.initialize(this);
      this.setDrawn(false);
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      PoseStack var2 = var1.method_10();
      float var3 = var1.method_880();

      GlStateManager._enableBlend();
      GlStateManager._blendFuncSeparate(770, 771, 1, 771);
      GlStateManager._disableDepthTest();
      GlStateManager._depthMask(Minecraft.getInstance().options.graphicsPreset().get() != net.minecraft.client.GraphicsPreset.FAST);
      GlStateManager._enableCull();
      Tesselator var4 = Tesselator.getInstance();
      BufferBuilder var5 = var4.begin(Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
      var2.pushPose();

      for (AbstractClientPlayer var7 : mc.level.players()) {
         if (!var7.isDeadOrDying() && var7 != mc.player) {
            Color var8 = this.color.getValue();
            Vec3 var9 = this.method_9(var7, (double)var3);
            AvatarRenderer var10 = (AvatarRenderer)mc.getEntityRenderDispatcher().getRenderer(var7);
            PlayerModel var11 = (PlayerModel)var10.getModel();
            float var13 = Mth.rotLerp(var3, var7.yBodyRotO, var7.yBodyRot);
            float var14 = Mth.lerp(var3, var7.xRotO, var7.getXRot());
            boolean var24 = var7.isVisuallySwimming();
            boolean var25 = var7.isShiftKeyDown() || animations.method_130();
            boolean var26 = var7.isFallFlying();
            ModelPart var27 = var11.head;
            ModelPart var28 = var11.leftArm;
            ModelPart var29 = var11.rightArm;
            ModelPart var21 = var11.leftLeg;
            ModelPart var22 = var11.rightLeg;
            var2.translate(var9.x, var9.y, var9.z);
            if (var24) {
               var2.translate(0.0F, 0.35F, 0.0F);
            }

            var2.mulPose(
               new Quaternionf()
                  .setAngleAxis(
                     (double)(var13 + 1.8e+02F) * Class_0245.field_687 / 1.8e+02,
                     0.0,
                     -1.0,
                     0.0
                  )
            );
            if (var24 || var26) {
               var2.mulPose(
                  new Quaternionf()
                     .setAngleAxis(
                        (double)((float)Class_0245.field_685 + var14) * Class_0245.field_687 / 1.8e+02,
                        -1.0,
                        0.0,
                        0.0
                     )
               );
            }

            if (var24) {
               var2.translate(0.0F, -0.95F, 0.0F);
            }

            if (animations.method_128()) {
               var2.scale(animations.playerScale.getValue(), animations.playerScale.getValue(), animations.playerScale.getValue());
            }

            Matrix4f var23 = var2.last().pose();
            var5.addVertex(
                  var23, 0.0F, var25 ? 0.6F : 0.7F, var25 ? 0.23F : 0.0F
               )
               .setColor(var8.getRGB());
            var5.addVertex(var23, 0.0F, var25 ? 1.05F : 1.4F, 0.0F).setColor(var8.getRGB());
            var5.addVertex(var23, -0.37F, var25 ? 1.05F : 1.35F, 0.0F)
               .setColor(var8.getRGB());
            var5.addVertex(var23, 0.37F, var25 ? 1.05F : 1.35F, 0.0F)
               .setColor(var8.getRGB());
            var5.addVertex(
                  var23,
                  -0.15F,
                  var25 ? 0.6F : 0.7F,
                  var25 ? 0.23F : 0.0F
               )
               .setColor(var8.getRGB());
            var5.addVertex(
                  var23,
                  0.15F,
                  var25 ? 0.6F : 0.7F,
                  var25 ? 0.23F : 0.0F
               )
               .setColor(var8.getRGB());
            var2.pushPose();
            var2.translate(0.0F, var25 ? 1.05F : 1.4F, 0.0F);
            this.method_2(var2, var27);
            var23 = var2.last().pose();
            var5.addVertex(var23, 0.0F, 0.0F, 0.0F).setColor(var8.getRGB());
            var5.addVertex(var23, 0.0F, 0.15F, 0.0F).setColor(var8.getRGB());
            var2.popPose();
            var2.pushPose();
            var2.translate(
               0.15F,
               var25 ? 0.6F : 0.7F,
               var25 ? 0.23F : 0.0F
            );
            this.method_2(var2, var22);
            var23 = var2.last().pose();
            var5.addVertex(var23, 0.0F, 0.0F, 0.0F).setColor(var8.getRGB());
            var5.addVertex(var23, 0.0F, -0.6F, 0.0F).setColor(var8.getRGB());
            var2.popPose();
            var2.pushPose();
            var2.translate(
               -0.15F,
               var25 ? 0.6F : 0.7F,
               var25 ? 0.23F : 0.0F
            );
            this.method_2(var2, var21);
            var23 = var2.last().pose();
            var5.addVertex(var23, 0.0F, 0.0F, 0.0F).setColor(var8.getRGB());
            var5.addVertex(var23, 0.0F, -0.6F, 0.0F).setColor(var8.getRGB());
            var2.popPose();
            var2.pushPose();
            var2.translate(0.37F, var25 ? 1.05F : 1.35F, 0.0F);
            this.method_2(var2, var29);
            var23 = var2.last().pose();
            var5.addVertex(var23, 0.0F, 0.0F, 0.0F).setColor(var8.getRGB());
            var5.addVertex(var23, 0.0F, -0.55F, 0.0F).setColor(var8.getRGB());
            var2.popPose();
            var2.pushPose();
            var2.translate(-0.37F, var25 ? 1.05F : 1.35F, 0.0F);
            this.method_2(var2, var28);
            var23 = var2.last().pose();
            var5.addVertex(var23, 0.0F, 0.0F, 0.0F).setColor(var8.getRGB());
            var5.addVertex(var23, 0.0F, -0.55F, 0.0F).setColor(var8.getRGB());
            var2.popPose();
            if (var24) {
               var2.translate(0.0F, 0.95F, 0.0F);
            }

            if (var24 || var26) {
               var2.mulPose(
                  new Quaternionf()
                     .setAngleAxis(
                        (double)((float)Class_0245.field_685 + var14) * Class_0245.field_687 / 1.8e+02,
                        1.0,
                        0.0,
                        0.0
                     )
               );
            }

            if (var24) {
               var2.translate(0.0F, -0.35F, 0.0F);
            }

            var2.mulPose(
               new Quaternionf()
                  .setAngleAxis(
                     (double)(var13 + 1.8e+02F) * Class_0245.field_687 / 1.8e+02,
                     0.0,
                     1.0,
                     0.0
                  )
            );
            var2.translate(-var9.x, -var9.y, -var9.z);
         }
      }

      var2.popPose();
      BufferSource.method_2(var5);
      GlStateManager._disableCull();
      GlStateManager._disableBlend();
      GlStateManager._enableDepthTest();
      GlStateManager._depthMask(true);

   }

   public void method_2(PoseStack var1, ModelPart var2) {
      if (var2.zRot != 0.0F) {
         var1.mulPose(Axis.ZP.rotation(var2.zRot));
      }

      if (var2.yRot != 0.0F) {
         var1.mulPose(Axis.YN.rotation(var2.yRot));
      }

      if (var2.xRot != 0.0F) {
         var1.mulPose(Axis.XN.rotation(var2.xRot));
      }
   }

   public Vec3 method_9(Entity var1, double var2) {
      double var4 = var1.xo + (var1.getX() - var1.xo) * var2 - mc.getEntityRenderDispatcher().camera.position().x;
      double var6 = var1.yo + (var1.getY() - var1.yo) * var2 - mc.getEntityRenderDispatcher().camera.position().y;
      double var8 = var1.zo + (var1.getZ() - var1.zo) * var2 - mc.getEntityRenderDispatcher().camera.position().z;
      return new Vec3(var4, var6, var8);
   }
}
