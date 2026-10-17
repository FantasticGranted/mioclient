package me.mioclient.module.render;

import java.util.Set;
import me.mioclient.enum_.RemovalMode;
import me.mioclient.enum_.Dimension;
import me.mioclient.enum_.SelfFillMode;
import me.mioclient.event.Event_13;
import me.mioclient.event.Event_14;
import me.mioclient.event.EntityRenderEvent;
import me.mioclient.event.ParticleEvent;
import me.mioclient.event.StatusEffectEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.RenderManager;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.particle.ExplodeParticle;
import net.minecraft.client.particle.HugeExplosionParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import me.mioclient.nick.Settings;

public class NoRenderModule extends Module {
   public Setting<Boolean> self;
   public Setting<Boolean> hurtCam;
   public Setting<Boolean> potions;
   public Setting<Boolean> darkness;
   public Setting<Boolean> blindness;
   public Setting<Boolean> newSneaking;
   public Setting<Boolean> totemOverlay;
   public Setting<Boolean> hands;
   public Setting<Float> opacity;
   public Setting<Boolean> uI;
   public Setting<Boolean> messageIndicator;
   public Setting<Boolean> unsecureServer;
   public Setting<Boolean> tutorialToast;
   public Setting<Boolean> textShadow;
   public Setting<Boolean> bossBars;
   public Setting<Boolean> scoreBoard;
   public Setting<Boolean> tabIcons;
   public Setting<Boolean> heldTooltips;
   public Setting<Boolean> tint;
   public Setting<Boolean> world;
   public Setting<Boolean> explosions;
   public Setting<Boolean> worldBorder;
   public Setting<Boolean> skyLight;
   public Setting<Boolean> fog;
   public Setting<Float> range;
   public Setting<Boolean> sky;
   public Setting<Boolean> particles;
   public Setting<Set<ParticleType<?>>> particleTypes;
   public Setting<SelfFillMode> selection;
   public Setting<Boolean> noBlockBreaking;
   public Setting<Boolean> entities;
   public Setting<Boolean> hurt;
   public Setting<Boolean> nameTags;
   public Setting<Boolean> interpolation;
   public Setting<Boolean> fire;
   public Setting<Boolean> others;
   public Setting<Boolean> armor;
   public Setting<Float> opacity2;
   public Setting<Boolean> noCluster;
   public Setting<Integer> clusterAlpha;
   public Setting<Boolean> tileEntities;
   public Setting<Integer> tileDistance;
   public Setting<Boolean> ignoreESP;
   public Setting<Boolean> wardens;
   public Setting<Integer> wardenDistance;
   public Setting<Boolean> entities2;
   public Setting<Set<EntityType<?>>> entityTypes;
   public Setting<RemovalMode> removal;
   public Setting<SelfFillMode> selection2;
   public float field_765;
   public BlockPos field_355;

   public NoRenderModule() {
      super("NoRender", "Removes the annoying stuff from your screen.", Category.RENDER);
      Settings.initialize(this);
      this.setDrawn(false);
      this.entities.method_31("EntitiesPage");
      this.selection.method_31("ParticleSelection");
      this.selection2.method_31("EntitySelection");
      this.opacity.method_31("HandsOpacity");
   }

   @Subscribe(
      method_800 = -100
   )
   public void method_2(Event_14 var1) {
      if (this.noCluster.getValue()) {
         double var2 = mc.player.position().distanceTo(var1.method_11().position());
         float var4 = var1.method_11().getBbWidth();
         if (var2 <= (double)var4 && var1.method_11() != mc.player) {
            float var5 = (float)(var2 / (double)var4);
            float var6 = (float)this.clusterAlpha.getValue().intValue() / 255F;
            var1.method_425(var6 + var5 * (1F - var6));
            this.field_765 = var1.method_937();
         } else {
            this.field_765 = 1F;
         }
      } else {
         this.field_765 = 1F;
      }
   }

   @Subscribe
   public void method_2(Event_13 var1) {
      if (this.method_230(var1.method_11()) && var1.method_11() != mc.player && this.removal.getValue() == RemovalMode.FULL) {
         var1.method_463();
      }
   }

   @Subscribe
   public void method_9(EntityRenderEvent var1) {
      if (this.nameTags.getValue()) {
         var1.method_463();
      }
   }

   @Subscribe
   public void method_9(ParticleEvent var1) {
      Particle var2 = var1.method_853();
      if ((var2 instanceof HugeExplosionParticle || var2 instanceof ExplodeParticle) && this.explosions.getValue()) {
         var1.method_463();
      }

      if (this.potions.getValue() && var2 instanceof SpellParticle) {
         var1.method_463();
      }
   }

   @Subscribe
   public void method_2(StatusEffectEvent var1) {
      if (this.blindness.getValue() && var1.method_1026() == MobEffects.BLINDNESS) {
         var1.method_463();
      }

      if (this.darkness.getValue() && var1.method_1026() == MobEffects.DARKNESS) {
         var1.method_463();
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (this.fire.getValue() && BlockUtil.method_1071() == Dimension.THE_END) {
         if (var1.method_127() instanceof ClientboundBlockUpdatePacket var2 && this.field_355 != null) {
            if (!var2.getPos().equals(this.field_355)) {
               return;
            }

            if (!var2.getBlockState().is(Blocks.FIRE)) {
               return;
            }

            this.field_355 = null;
            var1.method_463();
         }

         if (var1.method_127() instanceof ClientboundAddEntityPacket var4 && var4.getType() == EntityType.END_CRYSTAL) {
            this.field_355 = BlockPos.containing(var4.getX(), var4.getY(), var4.getZ());
         }
      }
   }

   public boolean method_279() {
      return this.isToggled() && this.hands.getValue() && !RenderManager.field_2003 && !RenderManager.field_4422 && this.opacity.getValue() == 0.0F;
   }

   public boolean method_280() {
      return this.isToggled() && this.tabIcons.getValue();
   }

   public boolean method_2(ParticleOptions var1) {
      return this.isToggled() && this.particles.getValue() ? !this.selection.getValue().method_2(var1.getType(), this.particleTypes.getValue()) : false;
   }

   public boolean method_230(Entity var1) {
      return this.method_2(var1.getType());
   }

   public boolean method_2(EntityType<?> var1) {
      return this.isToggled() && this.entities2.getValue() ? !this.selection2.getValue().method_2(var1, this.entityTypes.getValue()) : false;
   }

   public boolean method_281() {
      return this.isToggled() && this.noBlockBreaking.getValue();
   }

   public float method_282() {
      if (this.isToggled() && this.hands.getValue() && !RenderManager.field_4422 && !RenderManager.field_2003) {
         float var1 = this.opacity.getValue();
         return var1 == 0.0F ? 0.0F : Math.max(var1, 0.11F);
      } else {
         return 1F;
      }
   }

   public float method_283() {
      if (this.isToggled() && this.armor.getValue()) {
         float var1 = this.opacity2.getValue();
         return var1 == 0.0F ? 0.0F : Math.max(var1, 0.11F);
      } else {
         return 1F;
      }
   }

   public float method_284() {
      return !this.isToggled() ? 1F : this.field_765;
   }

   public boolean method_285() {
      return this.isToggled() && this.tint.getValue();
   }
}
