package me.mioclient.module.abstract_;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.enum_.Class_1087;
import me.mioclient.internal.ScissorStack;
import me.mioclient.internal.ResizableHudElement;
import me.mioclient.internal.Render2D;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.Colors;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.exploit.NewChunksModule;
import me.mioclient.record.TrackedChunk;
import me.mioclient.setting.ColorSetting;
import me.mioclient.setting.CustomSetting;
import me.mioclient.setting.CustomSetting3;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;

public class AbstractModule_4 extends AbstractModule_26 {
   public static final NewChunksModule field_2958 = Hub.field_2595.method_78(NewChunksModule.class);
   public static final Identifier field_2959 = Identifier.fromNamespaceAndPath("mio", "textures/nav.png");
   public static final float field_2960 = 16F;
   public static final float field_2961 = 8F;
   public final Setting<Class_1087> pointer = this.add(new CustomSetting<>("Pointer", Class_1087.ARROW));
   public final Setting<Float> width = this.add(
      new CustomSetting3<>("Width", 76F, 5e+01F, 5e+02F)
   );
   public final Setting<Float> height = this.add(
      new CustomSetting3<>("Height", 76F, 5e+01F, 2.5e+02F)
   );
   public final Setting<Color> pointer2 = this.add(new ColorSetting("Pointer", Color.white));
   public final Setting<Color> background = this.add(new ColorSetting("Background", new Color(10, 10, 10, 50)));
   public final Setting<Color> outline = this.add(new ColorSetting("Outline", new Color(10, 10, 10, 100)));

   public AbstractModule_4() {
      super("Map", "minimap");
      this.pointer2.method_31("PointerColor");
      this.pointer2.method_8();
      this.setDescription(new PlaceholderText().method_2(String.valueOf(ChatFormatting.YELLOW)).method_9("\u0001Primarily used for NewChunks"));
      ResizableHudElement var1 = new ResizableHudElement(this);
      var1.method_2(this);
      this.method_2(var1);
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1) {
      float[] var2 = this.field_1844.method_193();
      if (var2 != null) {
         float var3 = 1.0F;
         // TODO26.1 RenderSystem.setShaderColor(
         // TODO26.1 1F, 1F, 1F, 1F
         // TODO26.1 );
         Render2D.method_474();

         RenderUtil.field_2672
            .method_9(
               RenderUtil.mioPose(var1.pose()),
               -1F,
               -1F,
               this.width.getValue(),
               this.height.getValue(),
               this.background.getValue()
            );
         RenderUtil.field_2672
            .method_2(
               RenderUtil.mioPose(var1.pose()),
               -2F,
               -2F,
               this.width.getValue(),
               this.height.getValue(),
               this.outline.getValue()
            );
         float var4 = this.width.getValue() / 2F;
         float var5 = this.height.getValue() / 2F;
         var1.pose().pushMatrix();
         var1.pose().translate(var4, var5);

         for (BlockPos var7 : field_2958.method_973()) {
            ChunkPos var8 = new ChunkPos(var7.getX(), var7.getZ());
            if (this.method_2(var8)) {
               Color var9 = Colors.method_2(field_2958.fill5.getValue(), 0.4F);
               this.method_2(var1, var8, var9);
            }
         }

         for (TrackedChunk var11 : field_2958.method_972()) {
            if (this.method_2(var11.getChunkPos()) && var11.getType().method_2(field_2958) != null) {
               Color var12 = var11.getType().method_2(field_2958)[0];
               var12 = Colors.method_2(var12, 0.25F);
               ChunkPos var14 = var11.getChunkPos();
               this.method_2(var1, var14, var12);
            }
         }

         var1.pose().popMatrix();
         ScissorStack.method_9((int)this.field_1844.method_59(), (int)this.field_1844.method_60(), (int)var2[0], (int)var2[1]);
         Render2D.method_474();
         ScissorStack.method_57();
         this.method_2(var1, var4, var5);
      }
   }

   @Override
   public float[] method_31() {
      return new float[]{this.width.getValue(), this.height.getValue()};
   }

   public boolean method_2(ChunkPos var1) {
      int var2 = (int)Math.ceil(
         Math.hypot((double)this.width.getValue().floatValue(), (double)this.height.getValue().floatValue())
            / 16.0
      );
      return var1.distanceSquared(mc.player.chunkPosition()) <= var2 * var2;
   }

   public void method_2(GuiGraphicsExtractor var1, ChunkPos var2, Color var3) {
      Vec3 var4 = mc.gameRenderer.getMainCamera().position();
      float var5 = (float)(var4.x - (double)var2.getMinBlockX()) / 2F - 8F;
      float var6 = (float)(var4.z - (double)var2.getMinBlockZ()) / 2F - 8F;
      var1.pose().pushMatrix();
      var1.pose().translate(var5, var6);
      Render2D.method_4(RenderUtil.mioPose(var1.pose()), 0.0F, 0.0F, 8F, 8F, var3);
      var1.pose().popMatrix();
   }

   public void method_2(GuiGraphicsExtractor var1, float var2, float var3) {
      var1.pose().pushMatrix();
      var1.pose().translate(var2, var3);
      if (this.pointer.getValue() == Class_1087.DOT) {
         var1.fill(-1, -1, 1, 1, this.pointer2.getValue().hashCode());
      } else if (this.pointer.getValue() == Class_1087.ARROW) {
         var1.pose().scale(0.05F, 0.05F);
         var1.pose().rotate((float)Math.toRadians(45F + mc.gameRenderer.getMainCamera().yaw()));
         GlStateManager._texParameter(3553, 10240, 9729);
         // TODO26.1 RenderSystem.setShaderColor(
         // TODO26.1 (float)this.pointer2.getValue().getRed() / 255F,
         // TODO26.1 (float)this.pointer2.getValue().getGreen() / 255F,
         // TODO26.1 (float)this.pointer2.getValue().getBlue() / 255F,
         // TODO26.1 1F
         // TODO26.1 );
         var1.blit(field_2959, -128, -128, 256, 256, 0.0F, 0.0F, 256.0F, 256.0F);
         // TODO26.1 RenderSystem.setShaderColor(
         // TODO26.1 1F, 1F, 1F, 1F
         // TODO26.1 );
         GlStateManager._texParameter(3553, 10240, 9728);
      }

      var1.pose().popMatrix();
   }
}
