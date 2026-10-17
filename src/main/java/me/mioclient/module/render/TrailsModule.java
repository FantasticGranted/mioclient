package me.mioclient.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import me.mioclient.event.Event_12;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.TimedPosition;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.Colors;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
import me.mioclient.nick.Settings;
import org.lwjgl.opengl.GL32C;
import com.mojang.blaze3d.opengl.GlStateManager;
import org.lwjgl.opengl.GL11;

public class TrailsModule extends Module {
   public final HashMap<Integer, List<TimedPosition>> field_3546 = new HashMap<>();
   public Setting<Boolean> render;
   public Setting<Float> delay;
   public Setting<Boolean> targets;
   public Setting<Boolean> self;
   public Setting<Boolean> players;
   public Setting<Boolean> pearls;
   public Setting<Boolean> arrows;
   public Setting<Boolean> exp;
   public Setting<Float> lineWidth;
   public Setting<Boolean> fade;
   public Setting<Float> fadeDelay;
   public Setting<Float> fadeDuration;
   public Setting<Color> color;
   public Setting<Boolean> rainbow;
   public Setting<Double> threshold;
   public final Timer field_3562;

   public TrailsModule() {
      super("Trails", "Draws trails behind certain entities.", Category.RENDER);
      Settings.initialize(this);
      this.field_3562 = new Timer();
      this.setDrawn(false);
   }

   @Override
   public void onToggle() {
      this.field_3546.clear();
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      if (!this.method_535() && this.render.getValue()) {
         for (Entry var3 : this.field_3546.entrySet()) {
            if (((List)var3.getValue()).size() > 2) {
               this.method_2(var1.method_10(), (List<TimedPosition>)var3.getValue());
            }
         }
      }
   }

   @Subscribe(
      method_800 = -100
   )
   public void method_9(Event_12 var1) {
      if (!this.method_535()) {
         if (this.delay.getValue() == 0.0F || this.field_3562.method_2((double)this.delay.getValue().floatValue(), TimeUnit.SECONDS)) {
            this.field_3562.reset();
            synchronized (this.field_3546) {
               if (mc.player.tickCount % 4 == 0) {
                  int var3 = 0;

                  for (Integer var5 : this.field_3546.keySet()) {
                     List<TimedPosition> var6 = this.field_3546.get(var5);
                     var6.removeIf(
                        var1x -> this.fade.getValue() && System.currentTimeMillis() - var1x.field_1103 > this.method_1022() + this.method_1023()
                     );
                     if (var6.size() == 0) {
                        var3 = var5;
                        break;
                     }
                  }

                  if (var3 != 0) {
                     this.field_3546.remove(var3);
                  }
               }

               for (Entity var10 : mc.level.entitiesForRendering()) {
                  if (this.method_98(var10)) {
                     this.field_3546.compute(var10.getId(), (var1x, var2) -> {
                        TimedPosition var3x = new TimedPosition(var10.getPosition(RenderUtil.method_776()));
                        if (var2 == null) {
                           return new ArrayList<>(Collections.singleton(var3x));
                        } else {
                           var2.add(var3x);
                           return var2;
                        }
                     });
                  }
               }
            }
         }
      }
   }

   public void method_2(PoseStack var1, List<TimedPosition> var2) {
      Tesselator var3 = Tesselator.getInstance();
      BufferBuilder var4 = var3.begin(Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
      RenderUtil.field_2672.method_780();

      GlStateManager._enableBlend();
      GL32C.glLineWidth(this.lineWidth.getValue());
      GL11.glLineWidth(this.lineWidth.getValue());
      float[] var5 = Color.RGBtoHSB(this.color.getValue().getRed(), this.color.getValue().getGreen(), this.color.getValue().getBlue(), null);

      for (int var6 = 0; var6 < var2.size() - 2; var6++) {
         RenderUtil.field_2672
            .method_2(
               var1,
               var4,
               ((TimedPosition)var2.get(var6)).method_384(),
               ((TimedPosition)var2.get(var6 + 1)).method_384(),
               this.method_2(var5, (TimedPosition)var2.get(var6)).hashCode(),
               this.method_2(var5, (TimedPosition)var2.get(var6 + 1)).hashCode()
            );
      }

      net.minecraft.client.renderer.rendertype.RenderTypes.debugQuads().draw(var4.buildOrThrow());
      RenderUtil.field_2672.method_782();
      GL32C.glLineWidth(1F);
   }

   public Color method_2(float[] var1, TimedPosition var2) {
      Color var3 = this.rainbow.getValue()
         ? Color.getHSBColor(
            (float)(
               Math.ceil((double)var2.method_383() / (2e+01 * this.threshold.getValue()))
                  % (double)Class_0245.field_686
                  / (double)Class_0245.field_686
            ),
            var1[1],
            var1[2]
         )
         : this.color.getValue();
      if (this.fade.getValue() && System.currentTimeMillis() - var2.method_383() >= this.method_1022()) {
         float var4 = 1F
            - (float)(System.currentTimeMillis() - var2.method_383() - this.method_1022()) / (float)this.method_1023();
         return Colors.method_9(var3, (int)Mth.clamp(var4 * (float)this.color.getValue().getAlpha(), 0.0F, 255F));
      } else {
         return Colors.method_9(var3, this.color.getValue().getAlpha());
      }
   }

   public boolean method_98(Entity var1) {
      return var1 == mc.player && this.self.getValue()
         || var1 instanceof Player && this.players.getValue() && var1 != mc.player
         || var1 instanceof ThrownEnderpearl && this.pearls.getValue()
         || var1 instanceof ThrownExperienceBottle && this.exp.getValue()
         || var1 instanceof Arrow && this.arrows.getValue();
   }

   public long method_1022() {
      return (long)(this.fadeDelay.getValue() * 1e+03F);
   }

   public long method_1023() {
      return (long)(this.fadeDuration.getValue() * 1e+03F);
   }
}
