package me.mioclient.internal;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.mioclient.Hub;
import me.mioclient.enum_.InteractType;
import me.mioclient.enum_.PreType;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.DisconnectEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.module.player.FreecamModule;
import me.mioclient.record.PlayerFrame;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.phys.Vec3;

/**
 * "fakeplayer" command: spawns a dummy player entity, optionally records the local
 * player's movement into frames and replays them on the fake player (a bot/recorder).
 */
public final class FakePlayerCommand extends ChatCommand {
   public static FreecamModule freecam = Hub.field_2595.method_78(FreecamModule.class);
   public final List<PlayerFrame> frames = new ArrayList<>();
   public FakePlayerEntity fakePlayer;
   public boolean recording;
   public boolean playing;
   public String field_327;
   public int current;

   public FakePlayerCommand() {
      super("fakeplayer");
      lookups.method_14(this);
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      ((LiteralArgumentBuilder<SharedSuggestionProvider>)((LiteralArgumentBuilder<SharedSuggestionProvider>)((LiteralArgumentBuilder<SharedSuggestionProvider>)var1.then(
                  RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("name", StringArgumentType.word()).executes(var1x -> {
                     this.method_105((String)var1x.getArgument("name", String.class));
                     return 1;
                  })
               ))
               .then(LiteralArgumentBuilder.<SharedSuggestionProvider>literal("record").executes(var1x -> {
                  this.recording = !this.recording;
                  if (this.recording) {
                     this.frames.clear();
                     ChatUtil.method_2(Component.literal("Started recording."), ChatUtil.method_38(-1));
                  } else {
                     ChatUtil.method_2(Component.literal("Stopped recording."), ChatUtil.method_38(-1));
                  }

                  return 1;
               })))
            .then(LiteralArgumentBuilder.<SharedSuggestionProvider>literal("play").executes(var1x -> {
               this.current = 0;
               this.playing = !this.playing;
               return 1;
            })))
         .executes(var1x -> {
            this.method_105("Herobrine");
            return 1;
         });
   }

   public void method_105(String var1) {
      this.frames.clear();
      this.recording = false;
      this.playing = false;
      if (this.fakePlayer == null) {
         this.fakePlayer = new FakePlayerEntity(mc.player, mc.level, new GameProfile(UUID.randomUUID(), var1));
         if (freecam.isToggled()) {
            this.fakePlayer
               .snapTo(
                  freecam.field_806.x, freecam.field_806.y, freecam.field_806.z, mc.player.getYRot(), mc.player.getXRot()
               );
         }

         this.fakePlayer.setId(-9344);
         mc.level.addEntity(this.fakePlayer);
         this.fakePlayer.setOnGround(mc.player.onGround());
         this.field_327 = var1;
         ChatUtil.method_2(Component.literal(new PlaceholderText().method_2((Object)var1).method_9("\u0001 has been spawned.")), ChatUtil.method_38(-1));
      } else {
         mc.level.removeEntity(-9344, RemovalReason.DISCARDED);
         this.fakePlayer = null;
         String var2 = this.field_327 == null ? var1 : this.field_327;
         ChatUtil.method_2(Component.literal(new PlaceholderText().method_2((Object)var2).method_9("\u0001 has been removed.")), ChatUtil.method_38(-1));
      }
   }

   @Subscribe
   public void method_2(Event_19 var1) {
      if (var1.method_213() == PreType.POST && this.recording) {
         this.frames
            .add(
               new PlayerFrame(
                  mc.player.position(),
                  mc.player.getYRot(),
                  mc.player.getXRot(),
                  mc.player.getYHeadRot(),
                  mc.player.getDeltaMovement(),
                  mc.player.isShiftKeyDown(),
                  mc.player.onGround()
               )
            );
      } else if (var1.method_213() == PreType.PRE && this.fakePlayer != null && this.playing && !this.frames.isEmpty()) {
         this.current = (this.current + 1) % this.frames.size();
         PlayerFrame var2 = this.frames.get(this.current);
         this.fakePlayer.snapTo(var2.field_806.x, var2.field_806.y, var2.field_806.z, var2.field_1760, var2.field_1761);
         this.fakePlayer.setDeltaMovement(var2.field_2837);
         this.fakePlayer.setYRot(var2.field_1760);
         this.fakePlayer.setXRot(var2.field_1761);
         this.fakePlayer.setYHeadRot(var2.field_2836);
         this.fakePlayer.setShiftKeyDown(var2.field_2838);
         this.fakePlayer.setOnGround(var2.field_2839);
         if (this.fakePlayer.isShiftKeyDown()) {
            this.fakePlayer.setPose(Pose.CROUCHING);
         } else {
            this.fakePlayer.setPose(Pose.STANDING);
         }
      }
   }

   @Subscribe
   public void method_2(DisconnectEvent var1) {
      this.fakePlayer = null;
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (this.fakePlayer != null) {
         if (var1.method_127() instanceof ClientboundDisconnectPacket || var1.method_127() instanceof ClientboundPlayerCombatKillPacket) {
            this.fakePlayer = null;
         }

         if (var1.method_127() instanceof net.minecraft.network.protocol.game.ServerboundAttackPacket var2
            && mc.level.getEntity(var2.entityId()) != null
            && mc.level.getEntity(var2.entityId()).getId() == this.fakePlayer.getId()) {
            mc.execute(() -> this.fakePlayer.attack(mc.level.getEntity(var2.entityId())));
         }
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundLoginPacket) {
         this.fakePlayer = null;
      }

      if (this.fakePlayer != null) {
         if (var1.method_127() instanceof ClientboundExplodePacket var2) {
            double var18 = var2.center().x();
            double var5 = var2.center().y();
            double var7 = var2.center().z();
            double var9 = this.fakePlayer.position().distanceTo(new Vec3(var18, var5, var7)) / 12.0;
            if (var9 > 1.0) {
               return;
            }

            float var11 = var2.radius();
            double var12 = (double)// TODO(runtime): Explosion.getSeenPercent removed in 26.1
            1.0;
            double var19;
            double var14 = var19 = (1.0 - var9) * var12;
            float var16 = (float)(
               (var14 * var14 + var19)
                     / 2.0
                     * 7.0
                     * (double)var11
                     * 2.0
                  + 1.0
            );
            DamageSource var17 = mc.level.damageSources().explosion(null, null); // TODO(runtime): Fix explosion creation in 26.1
            this.fakePlayer.hurt(var17, var16);
         }
      }
   }
}
