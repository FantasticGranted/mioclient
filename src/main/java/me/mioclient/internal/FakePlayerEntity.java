package me.mioclient.internal;

import com.mojang.authlib.GameProfile;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.PacketReceiveEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;

/**
 * The dummy bot spawned by the fakeplayer command: mimics a player, self-heals like a
 * totem when killed, holds a totem, and stays immune to fire so it can be used as a dummy.
 */
public class FakePlayerEntity extends RemotePlayer {
   public FakePlayerEntity(Player var1, ClientLevel var2, GameProfile var3) {
      super(var2, var3);
      this.copyPosition(var1);
      this.yRotO = this.getYRot();
      this.xRotO = this.getXRot();
      this.yHeadRot = var1.yHeadRot;
      this.yHeadRotO = this.yHeadRot;
      this.yBodyRot = var1.yBodyRot;
      this.yBodyRotO = this.yBodyRot;
      this.setPose(var1.getPose());
      // TODO26.1: xCloak/yCloak/zCloak cloak origin fields removed with the cape refactor.
      this.setHealth(2e+01F);
      this.getInventory().replaceWith(var1.getInventory());
   }

   public void setHealth(float health) {
      super.setHealth(health);
      if (this.getHealth() <= 0.0F) {
         super.setHealth(9F);
         this.removeAllEffects();
         this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
         MinecraftAccess.mc.particleEngine.createTrackingEmitter(this, ParticleTypes.TOTEM_OF_UNDYING, 30);
         MinecraftAccess.mc
            .level
            .playSound(
               MinecraftAccess.mc.player,
               this.blockPosition(),
               SoundEvents.TOTEM_USE,
               SoundSource.PLAYERS,
               1F,
               1F
            );
         MinecraftAccess.lookups.method_36(new PacketReceiveEvent(new ClientboundEntityEventPacket(this, (byte)35)));
      }
   }

   public void tick() {
      this.tickCount++;
      if (this.tickCount % 80 == 0) {
         this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 1));
         this.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 6000, 0));
         this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0));
         this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3));
         this.setAbsorptionAmount(16F);
      }

      if (!this.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
         this.getInventory().setItem(40, new ItemStack(Items.TOTEM_OF_UNDYING, 1));
      }

      super.tick();
   }

   public boolean ignoreExplosion(Explosion explosion) {
      return false;
   }

   public boolean isAttackable() {
      return true;
   }

   public boolean fireImmune() {
      return true;
   }

   public boolean isPushable() {
      return false;
   }

   public boolean isAlive() {
      return true;
   }

   public void tickDeath() {
      this.deathTime = 0;
   }

   public void doPush(Entity entity) {
   }
}
