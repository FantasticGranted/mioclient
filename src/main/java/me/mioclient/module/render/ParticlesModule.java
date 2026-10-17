package me.mioclient.module.render;

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import me.mioclient.api.InitialAlphaAccess;
import net.minecraft.client.particle.SingleQuadParticle;
import me.mioclient.event.ParticleEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Colors;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.particle.AshParticle;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.CritParticle;
import net.minecraft.client.particle.FireworkParticles.SparkParticle;
import net.minecraft.client.particle.FireworkParticles.Starter;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.TotemParticle;
import net.minecraft.client.particle.WhiteAshParticle;
import me.mioclient.nick.Settings;

public class ParticlesModule extends Module {
   public final Random field_2712 = new Random();
   public Setting<Boolean> totems;
   public Setting<Float> totemsScale;
   public Setting<Float> totemsVelocity;
   public Setting<Color> totemsColor1;
   public Setting<Color> totemsColor2;
   public Setting<Boolean> ignoreSelf;
   public Setting<Boolean> rockets;
   public Setting<Float> rocketScale;
   public Setting<Color> rocketColor;
   public Setting<Boolean> damage;
   public Setting<Float> damageScale;
   public Setting<Float> damageVelocity;
   public Setting<Color> damageColor;
   public Setting<Boolean> portal;
   public Setting<Float> portalScale;
   public Setting<Color> portalColor;
   public Setting<Boolean> dust;
   public Setting<Color> from;
   public Setting<Color> to;

   public ParticlesModule() {
      super("Particles", "Allows you to adjust certain particles.", Category.RENDER);
      Settings.initialize(this);
      this.setDrawn(false);
   }

   @Subscribe
   public void method_9(ParticleEvent var1) {
      if (var1.method_853() instanceof TotemParticle var2) {
         if (this.ignoreSelf.getValue() && mc.player.getBoundingBox().intersects(var2.getBoundingBox())) {
            var1.method_463();
         }

         if (!this.totems.getValue()) {
            return;
         }

         if (this.field_2712.nextInt(4) == 0) {
            this.method_2(var2, this.totemsColor1.getValue().hashCode());
         } else {
            this.method_2(var2, this.totemsColor2.getValue().hashCode());
         }

         var2.scale(this.totemsScale.getValue());
         var2.setPower(this.totemsVelocity.getValue());
      }

      if (this.rockets.getValue() && var1.method_853() instanceof Starter var5) {
         this.method_2(var5, this.rocketColor.getValue().hashCode());
         var5.scale(this.rocketScale.getValue());
      }

      if (this.rockets.getValue() && var1.method_853() instanceof SparkParticle var6) {
         this.method_2(var6, this.rocketColor.getValue().hashCode());
         var6.scale(this.rocketScale.getValue());
      }

      if (this.damage.getValue() && var1.method_853() instanceof CritParticle var7) {
         this.method_2(var7, this.damageColor.getValue().hashCode());
         var7.scale(this.damageScale.getValue());
         var7.setPower(this.damageVelocity.getValue());
      }

      if (this.portal.getValue() && var1.method_853() instanceof PortalParticle var8) {
         this.method_2(var8, this.portalColor.getValue().hashCode());
         var8.scale(this.portalScale.getValue());
      }

      if (this.dust.getValue() && var1.method_853() instanceof BaseAshSmokeParticle var9) {
         if (var9 instanceof WhiteAshParticle) {
            this.method_2(var9, this.to.getValue().brighter().hashCode());
         }

         if (var9 instanceof AshParticle) {
            ThreadLocalRandom var15 = ThreadLocalRandom.current();
            Color var4 = Colors.method_2(this.from.getValue(), this.to.getValue(), var15.nextFloat());
            this.method_2(var9, var4.hashCode());
         }
      }
   }

   public void method_2(Particle var1, int var2) {
      float var3 = (float)(var2 >> 24) / 255F;
      float var4 = (float)((var2 & 0xFF0000) >> 16) / 255F;
      float var5 = (float)((var2 & 0xFF00) >> 8) / 255F;
      float var6 = (float)(var2 & 0xFF) / 255F;
      if (var1 instanceof SingleQuadParticle var1q) {
         var1q.setColor(var4, var5, var6);
         ((InitialAlphaAccess)var1q).mio$setInitialAlpha(var3);
      }
   }
}
