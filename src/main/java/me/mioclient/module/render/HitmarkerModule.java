package me.mioclient.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.enum_.InteractType;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.Event_26;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InteractPacketReader;
import me.mioclient.internal.SoundId;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.Colors;
import me.mioclient.mixin.ducks.DuckPlayerInteractEntityC2SPacket;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import me.mioclient.nick.Settings;
import org.joml.Matrix4f;

public class HitmarkerModule extends Module {
   public Setting<Boolean> draw;
   public Setting<Float> length;
   public Setting<Float> time;
   public Setting<Float> fadeTime;
   public Setting<Color> color;
   public Setting<Color> outlineColor;
   public Setting<Boolean> sound;
   public Setting<SoundId> sound2;
   public Setting<Float> volume;
   public Setting<Boolean> targets;
   public Setting<Boolean> animals;
   public Setting<Boolean> hostiles;
   public Setting<Boolean> players;
   public Setting<Boolean> crystals;
   public static CrosshairModule field_1609 = Hub.field_2595.method_78(CrosshairModule.class);
   public long field_1610;
   public int field_1611;
   public boolean field_1612;

   public HitmarkerModule() {
      super("Hitmarker", "Marks your screen when you hit entities.", Category.RENDER);
      Settings.initialize(this);
      this.field_1610 = -1L;
      this.field_1611 = 255;
      this.field_1612 = false;
      this.setDrawn(false);
      this.sound2.method_31("Type");
   }

   @Override
   public void onToggle() {
      this.field_1610 = -1L;
      this.field_1611 = 255;
      this.field_1612 = false;
   }

   @Subscribe
   public void method_2(Event_26 var1) {
      if (!this.method_535()) {
         if (this.field_1610 != -1L) {
            if (this.field_1612) {
               this.field_1612 = false;
               if (this.sound.getValue()) {
                  Hub.field_2606.method_2(this.sound2.getValue(), this.volume.getValue());
               }
            }

            long var2 = System.currentTimeMillis() - this.field_1610;
            long var4 = (long)(this.time.getValue() * 1e+03F);
            if (var2 > var4) {
               this.field_1610 = -1L;
               return;
            }

            this.method_38(var2);
            if (this.field_1611 > 0 && this.draw.getValue()) {
               float var6 = (float)mc.getWindow().getGuiScaledWidth() / 2F;
               float var7 = (float)mc.getWindow().getGuiScaledHeight() / 2F - 0.5F;
               if (!field_1609.isToggled()) {
                  var6 = (float)((double)var6 - 0.5);
               } else {
                  var7 = (float)((double)var7 + 0.5);
               }

               Color var8 = Colors.method_9(this.outlineColor.getValue(), this.field_1611);
               Color var9 = Colors.method_9(this.color.getValue(), this.field_1611);
               float var10 = 2F + this.length.getValue();

               for (int var11 = 0; var11 < 2; var11++) {
                  Color var12 = var11 == 0 ? var8 : var9;
                  var1.method_10().pushPose();
                  if (field_1609.isToggled()) {
                     var1.method_10().translate(-0.6, 0.0, 0.0);
                  }

                  method_2(
                     var1.method_10(), var12, var6 - var10, var7 - var10, var6 - 2F, var7 - 2F
                  );
                  method_2(
                     var1.method_10(), var12, var6 - var10, var7 + var10, var6 - 2F, var7 + 2F
                  );
                  method_2(
                     var1.method_10(), var12, var6 + 2F, var7 - 2F, var6 + var10, var7 - var10
                  );
                  method_2(
                     var1.method_10(), var12, var6 + 2F, var7 + 2F, var6 + var10, var7 + var10
                  );
                  var1.method_10().popPose();
               }
            }
         }
      }
   }

   @Subscribe(
      method_800 = -100
   )
   public void method_7(PacketSendEvent var1) {
      if (!this.method_535() && !var1.method_464()) {
         if (var1.method_127() instanceof net.minecraft.network.protocol.game.ServerboundAttackPacket var2) {
            Entity var4 = mc.level.getEntity(var2.entityId());
            if (var4 != null && var4.level() != null && var4 != mc.player && var4.level().isClientSide() && this.method_221(var4)) {
               this.field_1610 = System.currentTimeMillis();
               this.field_1611 = 255;
               this.field_1612 = true;
            }
         }
      }
   }

   public void method_38(long var1) {
      long var3 = (long)(this.time.getValue() * 1e+03F);
      long var5 = (long)(this.fadeTime.getValue() * 1e+03F);
      if (var1 < var3 - var5) {
         this.field_1611 = 255;
      } else {
         float var7 = 255F / this.fadeTime.getValue();
         this.field_1611 = (int)((float)this.field_1611 - var7 * Hub.field_2601.method_154(1F));
         this.field_1611 = Mth.clamp(this.field_1611, 0, 255);
      }
   }

   public boolean method_221(Entity var1) {
      return this.animals.getValue() && var1 instanceof AgeableMob
         || this.hostiles.getValue() && var1 instanceof Enemy
         || this.players.getValue() && var1 instanceof Player
         || this.crystals.getValue() && var1 instanceof EndCrystal;
   }

   public static void method_2(PoseStack var0, Color var1, float var2, float var3, float var4, float var5) {
      float[] var6 = new float[]{
         (float)var1.getRed() / 255F,
         (float)var1.getGreen() / 255F,
         (float)var1.getBlue() / 255F,
         (float)var1.getAlpha() / 255F
      };
      Matrix4f var7 = var0.last().pose();
      BufferBuilder var8 = Tesselator.getInstance().begin(Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
      var8.addVertex(var7, var2, var3, 0.0F).setColor(var6[0], var6[1], var6[2], var6[3]);
      var8.addVertex(var7, var4, var5, 0.0F).setColor(var6[0], var6[1], var6[2], var6[3]);
      RenderUtil.field_2672.method_779();

      net.minecraft.client.renderer.rendertype.RenderTypes.debugQuads().draw(var8.buildOrThrow());
      RenderUtil.field_2672.method_781();
   }
}
