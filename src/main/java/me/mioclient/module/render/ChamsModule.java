package me.mioclient.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import me.mioclient.Hub;
import me.mioclient.api.AttackTrackingAccess;
import me.mioclient.api.ForceSettable;
import me.mioclient.api.ChamsRenderer;
import me.mioclient.enum_.ChamsMode;
import me.mioclient.enum_.Class_1176;
import me.mioclient.enum_.WireframeMode;
import me.mioclient.enum_.SettingMode;
import me.mioclient.event.Event_12;
import me.mioclient.event.Event_13;
import me.mioclient.event.Event_14;
import me.mioclient.event.Event_15;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.EntityRenderHooks;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BufferSource;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.Class_0878;
import me.mioclient.internal.Colors;
import me.mioclient.internal.EntityCullingBridge;
import me.mioclient.internal.RenderManager;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.combat.AutoCrystalModule;
import me.mioclient.module.player.FreecamModule;
import me.mioclient.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.CameraType;
import net.minecraft.client.renderer.rendertype.RenderType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import com.mojang.blaze3d.opengl.GlStateManager;

public class ChamsModule extends Module {
   public static final Identifier field_218 = Identifier.fromNamespaceAndPath("mio-mount", "textures/shine.png");
   public static AutoCrystalModule autoCrystal = Hub.field_2595.method_78(AutoCrystalModule.class);
   public static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   public static AnimationsModule animations = Hub.field_2595.method_78(AnimationsModule.class);
   public static final FreecamModule field_220 = Hub.field_2595.method_78(FreecamModule.class);
   public Setting<Integer> range;
   public Setting<Boolean> fade;
   public Setting<Integer> fadeRadius;
   public Setting<Float> lineWidth;
   public Setting<Boolean> extraLayer;
   public Setting<Boolean> model;
   public Setting<Boolean> xQZ;
   public Setting<Integer> opacity;
   public Setting<Boolean> shine;
   public Setting<Float> speed;
   public Setting<Float> strength;
   public Setting<Integer> progress;
   public Setting<Boolean> targets;
   public Setting<ChamsMode> animals;
   public Setting<ChamsMode> hostiles;
   public Setting<ChamsMode> players;
   public Setting<ChamsMode> self;
   public Setting<ChamsMode> crystals;
   public Setting<Boolean> pop;
   public Setting<Boolean> death;
   public Setting<WireframeMode> wireframe;
   public Setting<Boolean> animate;
   public Setting<Boolean> boost;
   public Setting<Float> time;
   public Setting<Float> motion;
   public Setting<Boolean> colors;
   public Setting<Boolean> friends;
   public Setting<Color> fill;
   public Setting<Color> outline;
   public Setting<Color> shine2;
   public Setting<Color> popFill;
   public Setting<Color> popLine;
   public static boolean field_253 = false;
   public static boolean field_254 = false;
   public final BufferSource field_255;
   public final Map<Class_0878, Long> field_256;
   public boolean field_257;

   public ChamsModule() {
      super("Chams", "Wallhack on entities.", Category.RENDER);
      Settings.initialize(this);
      this.field_255 = BufferSource.method_740();
      this.field_256 = new HashMap<>();
      this.wireframe.method_31("PopMode");
      this.shine2.method_31("ShineColor");
      this.speed.method_2("Static", SettingMode.MIN);
   }

   @Subscribe(
      method_800 = 200
   )
   public void method_5(Event_12 var1) {
      if (this.shine.getValue()) {
         this.field_255.method_9(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      }

      boolean var2 = (Integer)mc.options.fov().get() > 115 && this.method_102();
      float var3 = var2 ? 2.3F : 1F;
      EntityCullingBridge.method_1013();
      GlStateManager._enablePolygonOffset();
      GlStateManager._polygonOffset(1F, -3.5e+06F * var3);

      for (Entity var5 : mc.level.entitiesForRendering()) {
         if (this.method_98(var5)) {
            if (this.model.getValue() && this.xQZ.getValue() && !norender.method_230(var5) && this.opacity.getValue() > 0) {
               this.field_257 = true;
               




 
               RenderUtil.field_2672.method_2(var5, var1.method_880(), var1.method_10(), mc.renderBuffers().bufferSource());
               this.field_257 = false;
            }

            if (this.shine.getValue()) {
               field_253 = true;
               

 
               var1.method_10().pushPose();
               RenderUtil.method_9(var1.method_10());
               RenderUtil.field_2672.method_2(var5, var1.method_880(), var1.method_10(), mc.renderBuffers().bufferSource());
               var1.method_10().popPose();
               field_253 = false;
            }
         }
      }

      if (this.model.getValue() && this.xQZ.getValue()) {
         




 
         mc.renderBuffers().bufferSource().endBatch();
         

 
      }

      GlStateManager._disablePolygonOffset();
      GlStateManager._polygonOffset(1F, 3.5e+06F * var3);
      EntityCullingBridge.method_1014();
   }

   @Subscribe(
      method_800 = -1000
   )
   public void method_7(Event_3 var1) {
      EntityRenderHooks.method_5((double)this.lineWidth.getValue().floatValue());

      for (Entity var3 : mc.level.entitiesForRendering()) {
         if (this.method_98(var3) && this.method_105(var3).getValue().method_670()) {
            this.method_105(var3).getValue().method_2(this, var3, var1.method_10());
         }
      }

      if (this.pop.getValue()) {
         synchronized (this.field_256) {
            this.field_256
               .entrySet()
               .removeIf(
                  var1x -> (float)var1x.getValue().longValue() + this.time.getValue() * 1e+03F
                        < (float)System.currentTimeMillis()
               );
            this.field_256
               .forEach(
                  (var2, var3x) -> {
                     float var4x = 1F
                        - Mth.clamp(
                           (float)(System.currentTimeMillis() - var3x) / (this.time.getValue() * 1e+03F),
                           0.0F,
                           1F
                        );
                     var2.setPos(
                        var2.getX(), var2.method_805().y + (double)((1F - var4x) * this.motion.getValue()), var2.getZ()
                     );
                     Color[] var5 = this.wireframe.getValue().method_2(this);
                     EntityRenderHooks.method_2(
                        Colors.method_2(var5[0], (float)var5[0].getAlpha() / 255F * var4x),
                        Colors.method_2(var5[1], (float)var5[1].getAlpha() / 255F * var4x)
                     );
                     EntityRenderHooks.method_2(var1.method_10(), var2);
                  }
               );
         }
      }

      if (this.xQZ.getValue()) {
         mc.renderBuffers().bufferSource().endBatch();
      }

      if (this.shine.getValue()) {
         double var9 = (Double)mc.options.glintSpeed().get();
         double var4 = (Double)mc.options.glintStrength().get();
         ((ForceSettable)(Object)mc.options.glintSpeed()).forceSetValue(this.speed.getValue().doubleValue());
         mc.options.glintStrength().set(this.strength.getValue().doubleValue());
         Color var6 = this.shine2.getValue();
         GlStateManager._enableBlend();
         field_254 = true;
         // TODO26.1 RenderType.getArmorEntityGlint().startDrawing();
         // TODO26.1 Minecraft.getInstance().getTextureManager().getTexture(field_218).setFilter(true, false);
         // TODO26.1 RenderSystem.setShaderTexture(0, field_218);
         




 
         GlStateManager._disableDepthTest();
         this.field_255.method_529();
         GlStateManager._enableDepthTest();
         // TODO26.1 RenderType.getArmorEntityGlint().endDrawing();
         

 
         field_254 = false;
         mc.options.glintSpeed().set(var9);
         mc.options.glintStrength().set(var4);
      }
   }

   @Subscribe
   public void method_9(Event_13 var1) {
      if (!RenderManager.field_4422) {
         if (!this.model.getValue() && this.method_98(var1.method_11()) && !this.field_257) {
            var1.method_463();
         }
      }
   }

   @Subscribe
   public void method_2(Event_14 var1) {
      if (this.method_98(var1.method_11())) {
         var1.method_294(15728880);
      }
   }

   @Subscribe
   public void method_9(Event_15 var1) {
      if (this.pop.getValue() && (var1.method_1050() != Class_1176.DEATH || this.death.getValue())) {
         Player var2 = var1.method_1048();
         if (var2 != mc.player) {
            Class_0878 var3 = new Class_0878(mc.level);
            var3.method_78(var2);
            var3.walkAnimation.position = var3.walkAnimation.position * 2F;
            if (!this.animate.getValue()) {
               var3.walkAnimation.position = 0.0F;
               var3.walkAnimation.speed = 0.0F;
               var3.walkAnimation.speedOld = 0.0F;
            } else if (this.boost.getValue()) {
               var3.method_14(true);
               var3.swingTime = 0;
                var3.swinging = false;
               var3.walkAnimation.speed = (float)(Math.random() * 0.800000011920929 + 0.20000000298023224);
               var3.walkAnimation.position = 0.0F;
            }

            synchronized (this.field_256) {
               this.field_256.putIfAbsent(var3, System.currentTimeMillis());
            }
         }
      }
   }

   public boolean method_98(Entity var1) {
      if (LogoutSpotsModule.method_708()) {
         return false;
      } else {
         boolean var2;
         try {
            var2 = !RotationManager.method_4(var1.getBoundingBox())
               || mc.gameRenderer.getMainCamera().position().distanceTo(var1.position()) > (double)this.range.getValue().intValue();
         } catch (Exception var4) {
            return false;
         }

         if (var2) {
            return false;
         } else if (EntityUtil.method_7(var1)) {
            return false;
         } else if (var1 instanceof EndCrystal var5) {
            return System.currentTimeMillis() - ((AttackTrackingAccess)var5).getSpawnTime() > (long)Math.max(Hub.field_2602.method_983(), 50)
                  && var5.tickCount < 10
                  && autoCrystal.isToggled()
                  && autoCrystal.inhibit.getValue()
                  && ((AttackTrackingAccess)var5).isMioAttacked()
               ? false
               : this.crystals.getValue().method_670();
         } else if (var1 instanceof Player) {
            boolean var3 = field_220.isToggled() || mc.options.getCameraType() != CameraType.FIRST_PERSON;
            return var1 != mc.player ? this.players.getValue().method_670() : this.self.getValue().method_670() && var3;
         } else {
            return var1 instanceof AgeableMob && this.animals.getValue().method_670() || var1 instanceof Enemy && this.hostiles.getValue().method_670();
         }
      }
   }

   public float method_99(Entity var1) {
      float var2 = (float)this.fadeRadius.getValue().intValue() / 1e+02F;
      double var3 = mc.gameRenderer.getMainCamera().position().distanceTo(var1.position()) / (double)this.range.getValue().intValue();
      var3 -= (double)var2;
      return (float)(
         1.0
            - Mth.clamp(var3 / (double)(1F - var2), 0.0, 1.0)
      );
   }

   public boolean method_100(Entity var1) {
      return this.isToggled() && this.xQZ.getValue() ? this.method_98(var1) : false;
   }

   public boolean method_101(Entity var1) {
      boolean var2 = this.method_105(var1).getValue().method_670() || var1 == mc.player;
      return this.isToggled() && var2 && this.shine.getValue() && field_253;
   }

   public boolean method_102() {
      float var1 = 0.3F;
      Vec3 var2 = mc.player.getEyePosition();
      BlockPos var3 = BlockPos.containing(var2);
      AABB var4 = AABB.ofSize(var2, (double)var1, (double)var1, (double)var1);
      return !mc.level.noCollision(mc.player, var4)
         && mc.level.getBlockState(var3).getCollisionShape(mc.level, var3).isEmpty();
   }

   public boolean method_103() {
      return animations.method_129();
   }

   public BufferSource method_104() {
      return this.field_255;
   }

   public Setting<? extends ChamsRenderer> method_105(Entity var1) {
      if (var1 == mc.player) {
         return this.self;
      } else if (var1 instanceof EndCrystal) {
         return this.crystals;
      } else if (var1 instanceof Player) {
         return this.players;
      } else {
         return var1 instanceof AgeableMob ? this.animals : this.hostiles;
      }
   }
}
