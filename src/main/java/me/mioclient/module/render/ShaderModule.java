package me.mioclient.module.render;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.awt.Color;
import java.util.Comparator;
import me.mioclient.Hub;
import me.mioclient.enum_.ColorMode;
import me.mioclient.enum_.ShaderPattern;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.EntityOutlineShader;
import me.mioclient.internal.HudRenderEvent;
import me.mioclient.internal.FramebufferUtil;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.Colors;
import me.mioclient.internal.RenderManager;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.player.FreecamModule;
import me.mioclient.setting.Setting;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class ShaderModule extends Module {
   public static FreecamModule freecam = Hub.field_2595.method_78(FreecamModule.class);
   public static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   public Setting<ColorMode> shader;
   public Setting<Integer> range;
   public Setting<Integer> lineWidth;
   public Setting<Boolean> fastLines;
   public Setting<Float> glow;
   public Setting<Integer> quality;
   public Setting<Float> step;
   public Setting<Float> speed;
   public Setting<Float> fillSpeed;
   public Setting<Float> outlineSpeed;
   public Setting<Float> fillStrength;
   public Setting<Float> outlineStrength;
   public Setting<Boolean> image;
   public Setting<Float> overlayAlpha;
   public Setting<Boolean> decorator;
   public Setting<ShaderPattern> type;
   public Setting<Integer> radius;
   public Setting<Float> alpha;
   public Setting<Boolean> colors;
   public Setting<Color> fill;
   public Setting<Color> fillSecond;
   public Setting<Color> outline;
   public Setting<Color> outlineSecond;
   public Setting<Boolean> targets;
   public Setting<Boolean> hands;
   public Setting<Boolean> fill2;
   public Setting<Boolean> outline2;
   public Setting<Boolean> crystals;
   public Setting<Integer> range2;
   public Setting<Boolean> items;
   public Setting<Integer> range3;
   public Setting<Boolean> animals;
   public Setting<Boolean> hostiles;
   public Setting<Boolean> players;
   public Setting<Boolean> self;
   public Setting<Boolean> exp;
   public Setting<Boolean> pearls;
   public Setting<Boolean> minecarts;
   public Setting<Boolean> dead;
   public final ObjectArrayList<Entity> field_2056;
   public float field_765;

   public ShaderModule() {
      super("Shader", "Advanced ESP using shaders.", Category.RENDER);
      Settings.initialize(this);
      this.field_2056 = new ObjectArrayList();
      this.field_765 = 1F;
      this.radius.method_31("DecoratorRadius");
      this.alpha.method_31("DecoratorAlpha");
      this.fill2.method_31("HandsFill");
      this.outline2.method_31("HandsOutline");
      this.range3.method_31("ItemRange");
      this.range2.method_31("CrystalRange");
   }

   @Override
   public String getInfo() {
      return FontRenderer.method_3(this.shader.getValue());
   }

   @Subscribe(
      method_800 = -99999999
   )
   public void method_9(Event_3 var1) {
      EntityOutlineShader var2 = this.shader.getValue().getColor();
      this.field_2056.clear();

      for (Entity var4 : mc.level.entitiesForRendering()) {
         if (this.method_98(var4)) {
            this.field_2056.add(var4);
         }
      }

      this.field_2056.sort(Comparator.comparing(var0 -> mc.gameRenderer.getMainCamera().position().distanceToSqr(var0.position())));
      FramebufferUtil.method_2(var2, false, () -> {
         var1.method_10().pushPose();
         this.field_2056.forEach(var3 -> this.method_2(var1, var3, var2));
         var1.method_10().popPose();
      });
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      this.field_765 = this.method_186(this.field_2056.stream().min(Comparator.comparing(this::method_186)).orElse(null));
   }

   public void method_2(HudRenderEvent var1, Entity var2, EntityOutlineShader var3) {
      if (var2.isAlive() || this.dead.getValue()) {
         Vec3 var4 = mc.gameRenderer.getMainCamera().position();
         float var5 = var1.method_880();
         double var6 = Mth.lerp((double)var5, var2.xOld, var2.getX()) - var4.x;
         double var8 = Mth.lerp((double)var5, var2.yOld, var2.getY()) - var4.y;
         double var10 = Mth.lerp((double)var5, var2.zOld, var2.getZ()) - var4.z;
         float var12 = Mth.lerp(var5, var2.yRotO, var2.getYRot());
         var3.field_2301.setColor((255 << 24 | 255 << 16 | 255 << 8 | 255));
         // TODO26.1: EntityRenderDispatcher.shouldRender(E,Frustum,dx,dy,dz) is culling-only now and no longer submits a render;
         // the outline-shader entity pass needs the SubmitNodeCollector pipeline instead.
      }
   }

   public float method_186(Entity var1) {
      if (var1 != null && norender.isToggled() && norender.noCluster.getValue()) {
         double var2 = mc.player.position().distanceTo(var1.position());
         float var4 = var1.getBbWidth();
         if (var2 <= (double)var4 && var1 != mc.player) {
            float var5 = (float)(var2 / (double)var4);
            float var6 = (float)norender.clusterAlpha.getValue().intValue() / 255F;
            return var6 + var5 * (1F - var6);
         }
      }

      return 1F;
   }

   public Color getId(Color var1) {
      float var2 = this.field_765;
      if (RenderManager.field_2003) {
         var2 = 1F;
      }

      return Colors.method_9(var1, (int)(var2 * (float)var1.getAlpha()));
   }

   public float method_650() {
      return RenderManager.field_2003 ? this.overlayAlpha.getValue() : this.overlayAlpha.getValue() * this.field_765;
   }

   public boolean method_98(Entity var1) {
      float var2 = (float)this.range.getValue().intValue();
      if (var1 instanceof EndCrystal) {
         var2 = (float)this.range2.getValue().intValue();
      }

      if (var1 instanceof ItemEntity) {
         var2 = (float)this.range3.getValue().intValue();
      }

      boolean var3 = !RotationManager.method_4(var1.getBoundingBox()) || mc.player.distanceTo(var1) > var2;
      if (var1 instanceof Player var4 && var4.isDeadOrDying()) {
         return false;
      }

      if (var3) {
         return false;
      } else if (EntityUtil.method_7(var1)) {
         return false;
      } else {
         boolean var5 = mc.gameRenderer.getMainCamera().isDetached() || freecam.isToggled();
         if (var1 instanceof Player) {
            return var5 && this.self.getValue() && var1 == mc.player ? true : this.players.getValue() && var1 != mc.player;
         } else {
            return var1 instanceof ItemEntity && this.items.getValue()
               || var1 instanceof ThrownExperienceBottle && this.exp.getValue()
               || (var1 instanceof AgeableMob || var1 instanceof AbstractFish || var1 instanceof Squid) && this.animals.getValue()
               || var1 instanceof Enemy && this.hostiles.getValue()
               || var1 instanceof ThrownEnderpearl && this.pearls.getValue()
               || var1 instanceof EndCrystal && this.crystals.getValue()
               || var1 instanceof AbstractMinecart && this.minecarts.getValue();
         }
      }
   }

   public boolean method_651() {
      return RenderManager.field_2003 && !this.fill2.getValue();
   }

   public boolean method_652() {
      return RenderManager.field_2003 && !this.outline2.getValue();
   }
}
