package me.mioclient.module.abstract_;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import java.util.ArrayList;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.ResizableHudElement;
import me.mioclient.internal.Colors;
import me.mioclient.setting.CustomSetting3;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.opengl.GlStateManager;

public class AbstractModule_5 extends AbstractModule_26 {
   public final Setting<Float> ceil = this.add(
      new CustomSetting3<>("Ceil", 26F, 1e+01F, 1e+02F)
   );
   public final Setting<Integer> height = this.add(new CustomSetting3<>("Height", 30, 10, 40));
   public final Setting<Integer> width = this.add(new CustomSetting3<>("Width", 100, 50, 150));
   public final List<Double> field_2659 = new ArrayList<>();

   public AbstractModule_5() {
      super("Graph");
      this.method_2(new ResizableHudElement(this));
   }

   @Override
   public void onEnable() {
      this.field_2659.clear();

      for (int var1 = 0; var1 < 150; var1++) {
         this.field_2659.add(0.0);
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      this.field_2659.add(Hub.field_2614.method_875());

      while (this.field_2659.size() > this.width.getValue()) {
         this.field_2659.remove(0);
      }
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1) {
      Tesselator var2 = Tesselator.getInstance();

      GlStateManager._enableBlend();
      GlStateManager._blendFuncSeparate(770, 771, 1, 771);
      BufferBuilder var3 = var2.begin(Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
      int var4 = 0;

      for (double var6 : this.field_2659) {
         float var8 = 1F
            + (float)(
               (double)(this.height.getValue() - 2)
                  * (
                     1.0
                        - Math.min(var6, (double)this.ceil.getValue().floatValue()) / (double)this.ceil.getValue().floatValue()
                  )
            );
         var3.addVertex(new org.joml.Matrix4f().mul(var1.pose()), (float)var4, var8, 0.0F)
            .setColor(Colors.method_9(this.method_9(var8 + this.field_1844.method_60()), this.method_310(var4)).hashCode());
         var4++;
      }

      net.minecraft.client.renderer.rendertype.RenderTypes.debugQuads().draw(var3.buildOrThrow());
      GlStateManager._disableBlend();
   }

   public int method_310(int var1) {
      if (var1 <= 10) {
         return (int)Mth.clamp((float)var1 / 1e+01F * 255F, 0.0F, 255F);
      } else {
         return var1 >= this.width.getValue() - 10
            ? (int)Mth.clamp(
               (1F - (float)(var1 - this.width.getValue() + 10) / 1e+01F)
                  * 255F,
               0.0F,
               255F
            )
            : 255;
      }
   }

   @Override
   public float[] method_31() {
      return new float[]{(float)this.width.getValue().intValue(), (float)this.height.getValue().intValue()};
   }
}
