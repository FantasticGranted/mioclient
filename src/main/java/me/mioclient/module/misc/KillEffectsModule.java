/*
 * Decompiled with CFR 0.152.
 */
package me.mioclient.module.misc;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.LambdaMetafactory;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import me.mioclient.Hub;
import me.mioclient.enum_.PreType;
import me.mioclient.enum_.Priority;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.DisconnectEvent;
import me.mioclient.event.AttackEntityEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.SoundId;
import me.mioclient.internal.SoundSample;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.FileUtil;
import me.mioclient.internal.ChatUtil;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.internal.FileManager;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.client.IRCModule;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.ThreadSafeLegacyRandomSource;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import org.apache.commons.io.FileUtils;

public class KillEffectsModule
extends Module {
    public Setting<Boolean> killSound;
    public Setting<Boolean> players;
    public Setting<SoundId> sound;
    public Setting<Float> volume;
    public Setting<Boolean> self;
    public Setting<SoundId> sound2;
    public Setting<Float> volume2;
    public Setting<Boolean> killStreak;
    public Setting<Float> volume3;
    public Setting<Boolean> thunder;
    public Setting<Integer> lightnings;
    public Setting<Boolean> self2;
    public Setting<Boolean> ignoreNakeds;
    public Setting<Boolean> ashes;
    public Setting<Boolean> self3;
    public Setting<Boolean> autoEZ;
    public Setting<Float> delay;
    public Setting<String> path;
    public Setting<Boolean> random;
    public Setting<Boolean> refresh;
    public static IRCModule field_906 = Hub.field_2595.method_78(IRCModule.class);
    public static final RandomSource field_907 = new ThreadSafeLegacyRandomSource(System.currentTimeMillis());
    public final ConcurrentHashMap<UUID, Integer> field_908;
    public final ObjectList<UUID> field_909;
    public int field_910;
    public int field_911;
    public List<String> field_912;

    public KillEffectsModule() {
        super("KillEffects", "Does various things when someone dies.", Category.MISC, new String[0]);
        Settings.initialize((Object)this);
        this.field_908 = new ConcurrentHashMap();
        this.field_909 = ObjectLists.synchronize((ObjectList)new ObjectArrayList());
        this.field_910 = 0;
        this.field_911 = 0;
        this.field_912 = Collections.synchronizedList(new ArrayList());
        this.players.method_31("KillSoundPlayers");
        this.sound.method_31("KillSoundPlayersSound");
        this.volume.method_31("KillSoundPlayersVolume");
        this.self.method_31("KillSoundSelf");
        this.sound2.method_31("KillSoundSelfSound");
        this.volume2.method_31("KillSoundSelfVolume");
        this.self2.method_31("ThunderSelf");
        this.volume3.method_31("KillStreakSoundVolume");
        this.self3.method_31("AshesSelf");
        this.autoEZ.method_9(() -> {
            if (this.autoEZ.getValue().booleanValue()) {
                this.method_99(false);
            }
        });
        this.refresh.method_9(() -> {
            if (this.refresh.getValue().booleanValue()) {
                this.method_99(true);
                this.refresh.method_78(false);
            }
        });
        this.random.method_9(() -> {
            this.field_911 = 0;
        });
        this.path.method_9(() -> this.method_99(true));
        this.path.method_31("AutoEZPath");
        this.random.method_31("AutoEZRandom");
        this.refresh.method_31("AutoEZRefresh");
        this.setDrawn(false);
    }

    @Override
    public void onEnable() {
        this.field_910 = 0;
        this.method_99(true);
    }

    @Subscribe
    public void method_2(DisconnectEvent event_20) {
        this.field_910 = 0;
    }

    @Subscribe
    public void method_2(AttackEntityEvent event_33) {
        Player playerEntity;
        if (event_33.method_213() == PreType.POST) {
            return;
        }
        if (this.method_535()) {
            return;
        }
        Entity entity = event_33.method_11();
        if (entity instanceof Player && (playerEntity = (Player)entity) != KillEffectsModule.mc.player && !Hub.field_2603.method_1009(playerEntity.getName().getString()) && EntityUtil.method_2((Entity)playerEntity) > 0.0f) {
            try {
                this.field_908.put(playerEntity.getUUID(), KillEffectsModule.mc.player.tickCount);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    @Subscribe
    public void method_5(PacketReceiveEvent event_4) {
        if (event_4.method_127() instanceof ClientboundPlayerCombatKillPacket) {
            this.field_910 = -1;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Subscribe
    public void method_2(PlayerTickEvent event_1) {
        Object object;
        if (this.method_535()) {
            return;
        }
        if (this.field_910 == -1) {
            this.field_910 = 0;
            if (this.killSound.getValue().booleanValue() && this.self.getValue().booleanValue() && this.volume2.getValue().floatValue() > 0.0f && (object = Hub.field_2606.method_2(this.sound2.getValue())) != null) {
                ((SoundSample)object).method_230(this.volume2.getValue().floatValue());
            }
            if (this.thunder.getValue().booleanValue() && this.self2.getValue().booleanValue()) {
                this.method_36(KillEffectsModule.mc.player.position());
            }
            if (this.ashes.getValue().booleanValue() && this.self3.getValue().booleanValue()) {
                this.method_14(KillEffectsModule.mc.player.position());
            }
        }
        this.field_909.clear();
        object = this.field_908;
        synchronized (object) {
            for (UUID uUID : this.field_908.keySet()) {
                try {
                    Object object2;
                    Player playerEntity = KillEffectsModule.mc.level.getPlayerByUUID(uUID);
                    if (playerEntity == null) {
                        this.field_909.add(uUID);
                        continue;
                    }
                    int n = KillEffectsModule.mc.player.tickCount - this.field_908.get(uUID);
                    if (n > 20) {
                        this.field_909.add(uUID);
                        continue;
                    }
                    Player playerEntity2 = playerEntity;
                    if (!(playerEntity2.getHealth() <= 0.0f)) continue;
                    this.field_909.add(uUID);
                    ++this.field_910;
                    if (this.autoEZ.getValue().booleanValue() && (object2 = this.method_311()) != null) {
                        object2 = ((String)object2).replace("{name}", playerEntity2.getName().getString());
                        if (!field_906.isToggled() || !((String)object2).startsWith(KillEffectsModule.field_906.prefix.getValue())) {
                            String string = (String) (object2);
                            // Original indy site was inlined to a Runnable lambda that opened a
                            // chat message; ForwarderInliner removed the lambda target so we
                            // approximate with a no-op Runnable.
                            Runnable runnable = () -> {};
                            if (this.delay.method_105()) {
                                runnable.run();
                            } else {
                                Hub.field_2619.method_2(runnable, (int)(this.delay.getValue().floatValue() / 0.05F));
                            }
                        }
                    }
                    if (this.thunder.getValue().booleanValue() && (PlayerUtil.method_29((LivingEntity)playerEntity) || !this.ignoreNakeds.getValue().booleanValue())) {
                        this.method_36(playerEntity.position());
                    }
                    if (this.ashes.getValue().booleanValue()) {
                        this.method_14(playerEntity.position());
                    }
                    if (this.killSound.getValue().booleanValue() && this.players.getValue().booleanValue() && this.volume.getValue().floatValue() > 0.0f && (object2 = Hub.field_2606.method_2(this.sound.getValue())) != null) {
                        ((SoundSample)object2).method_230(this.volume.getValue().floatValue());
                    }
                    if (!this.killStreak.getValue().booleanValue() || this.field_910 < 2 || !(this.volume3.getValue().floatValue() > 0.0f) || (object2 = Hub.field_2606.method_6(this.field_910)) == null) continue;
                    ((SoundSample)object2).method_230(this.volume3.getValue().floatValue());
                }
                catch (Exception exception) {}
            }
        }
        object = this.field_909;
        synchronized (object) {
            try {
                for (UUID uUID : this.field_909) {
                    this.field_908.remove(uUID);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public void method_185(Player playerEntity) {
        if (!this.method_535() && playerEntity != KillEffectsModule.mc.player && !Hub.field_2603.method_1009(playerEntity.getName().getString())) {
            try {
                this.field_908.put(playerEntity.getUUID(), KillEffectsModule.mc.player.tickCount);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public void method_99(boolean bl) {
        block6: {
            if (!this.autoEZ.getValue().booleanValue() && !bl) {
                return;
            }
            Path path = FileUtil.method_2(FileManager.field_4284.resolve(this.path.getValue()), new String[]{".txt"});
            try {
                if (!path.toFile().exists()) {
                    throw new IOException("AutoEZ file %s not found".formatted(this.path.getValue()));
                }
                this.field_912.clear();
                this.field_912.addAll(FileUtils.readLines((File)path.toFile(), (Charset)StandardCharsets.UTF_8));
                this.field_911 = 0;
            }
            catch (Exception exception) {
                if (!this.autoEZ.getValue().booleanValue()) break block6;
                ChatUtil.method_2((Component)Component.literal((String)new PlaceholderText().method_2((Object)String.valueOf(exception)).method_9("Failed to update AutoEZ lines: \u0001")).withStyle(style -> style.withColor(ChatFormatting.RED)), ChatUtil.method_38(-9634482), Priority.LOW);
                try {
                    Files.createFile(path, new FileAttribute[0]);
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        }
    }

    public String method_311() {
        try {
            if (this.field_912.isEmpty()) {
                return null;
            }
            if (this.random.getValue().booleanValue()) {
                return this.field_912.get(ThreadLocalRandom.current().nextInt(this.field_912.size()));
            }
            return this.field_912.get(this.field_911++ % this.field_912.size());
        }
        catch (Exception exception) {
            return null;
        }
    }

    public void method_36(Vec3 vec3d) {
        for (int i = 0; i < this.lightnings.getValue(); ++i) {
            LightningBolt lightningEntity = new LightningBolt(EntityType.LIGHTNING_BOLT, (Level)KillEffectsModule.mc.level);
            lightningEntity.setPos(vec3d);
            lightningEntity.teleportTo(vec3d.x, vec3d.y, vec3d.z);
            KillEffectsModule.mc.level.addEntity((Entity)lightningEntity);
        }
    }

    public void method_14(Vec3 vec3d) {
        for (int i = 0; i < field_907.nextInt(35) + 25; ++i) {
            Particle particle = KillEffectsModule.mc.particleEngine.createParticle((ParticleOptions)(field_907.nextBoolean() ? ParticleTypes.ASH : ParticleTypes.WHITE_ASH), vec3d.x() + field_907.nextGaussian() * 0.22999999523162842, vec3d.y() + Double.longBitsToDouble(0x4000000000000000L) - field_907.nextGaussian() * 0.5299999952316284, vec3d.z() + field_907.nextGaussian() * 0.22999999523162842, 0.0, -0.1 - field_907.nextGaussian() * 0.5, 0.0);
            if (particle == null) continue;
            particle.lifetime += 15;
        }
    }
}

