package me.mioclient.module.render;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import me.mioclient.Hub;
import me.mioclient.enum_.LogoutModelMode;
import me.mioclient.enum_.LogoutPositionMode;
import me.mioclient.enum_.SettingMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.ServerConnectEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.EntityRemoveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.SoundId;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.EntityRenderHooks;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.Class_0878;
import me.mioclient.internal.FakePlayerEntity;
import me.mioclient.internal.MathUtil;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.client.FontsModule;
import me.mioclient.record.PlayerSnapshot;
import me.mioclient.record.PendingPlayerTarget;
import me.mioclient.setting.Setting;
import net.minecraft.client.Camera;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class LogoutSpotsModule extends Module {
   public static final FontsModule field_984 = Hub.field_2595.method_78(FontsModule.class);
   public static boolean field_2321 = false;
   public Setting<Float> distance;
   public Setting<LogoutModelMode> model;
   public Setting<Boolean> textured;
   public Setting<Float> alpha;
   public Setting<Float> width;
   public Setting<Boolean> tracer;
   public Setting<Float> width2;
   public Setting<Boolean> nameTag;
   public Setting<Float> textScale;
   public Setting<LogoutPositionMode> position;
   public Setting<Boolean> time;
   public Setting<Boolean> health;
   public Setting<Boolean> totems;
   public Setting<Boolean> sounds;
   public Setting<Boolean> login;
   public Setting<SoundId> sound;
   public Setting<Boolean> logout;
   public Setting<SoundId> sound2;
   public Setting<Boolean> colors;
   public Setting<Color> modelFill;
   public Setting<Color> modelLine;
   public Setting<Color> boxFill;
   public Setting<Color> boxLine;
   public Setting<Color> nameTagColor;
   public Setting<Color> fillColor;
   public Setting<Boolean> filter;
   public Setting<Boolean> ignoreFriends;
   public Setting<Boolean> ignoreNakeds;
   public final Object2ObjectOpenHashMap<String, PlayerSnapshot> field_2350;
   public final List<PendingPlayerTarget> field_2351;

   public LogoutSpotsModule() {
      super("LogoutSpots", "Highlights log out spots.", Category.RENDER);
      Settings.initialize(this);
      this.field_2350 = new Object2ObjectOpenHashMap();
      this.field_2351 = new ArrayList<>();
      this.distance.method_2("Unlimited", SettingMode.MAX);
      this.sound.method_31("LoginSoundPath");
      this.sound2.method_31("LogoutSoundPath");
      this.setDrawn(false);
   }

   @Override
   public void onToggle() {
      this.field_2350.clear();
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      Camera var2 = mc.gameRenderer.getMainCamera();
      Vec3 var3 = new Vec3(0.0, 0.0, 1.0)
         .xRot(-((float)Math.toRadians((double)var2.xRot())))
         .yRot(-((float)Math.toRadians((double)var2.yRot())))
         .add(mc.getEntityRenderDispatcher().camera.position());
      synchronized (this.field_2350) {
         ObjectIterator var5 = this.field_2350.entrySet().iterator();

         while (var5.hasNext()) {
            Entry var6 = (Entry)var5.next();
            if (((PlayerSnapshot)var6.getValue()).getName().equals(this.method_106())
               && ((PlayerSnapshot)var6.getValue()).method_418() == BlockUtil.method_1071()
               && (
                  !(
                        ((PlayerSnapshot)var6.getValue()).box.getCenter().distanceTo(mc.gameRenderer.getMainCamera().position())
                           > (double)this.distance.getValue().floatValue()
                     )
                     || this.distance.getValue() == 301F
               )) {
               if (this.model.getValue() == LogoutModelMode.SIMPLE || this.model.getValue() == LogoutModelMode.BOTH) {
                  BoxRenderer.method_5(var1.method_10(), ((PlayerSnapshot)var6.getValue()).getBox(), this.boxFill.getValue());
                  BoxRenderer.method_2(var1.method_10(), ((PlayerSnapshot)var6.getValue()).getBox(), this.boxLine.getValue(), this.width.getValue());
               }

               if (this.model.getValue() == LogoutModelMode.COMPLEX || this.model.getValue() == LogoutModelMode.BOTH) {
                  Class_0878 var7 = ((PlayerSnapshot)var6.getValue()).getEntity();
                  if (this.textured.getValue()) {
                     field_2321 = true;

                     RenderUtil.field_2672
                        .method_2(var7, 1F, var1.method_10(), mc.renderBuffers().bufferSource());
                     mc.renderBuffers().bufferSource().endBatch();

                     field_2321 = false;
                  }

                  EntityRenderHooks.method_5((double)this.width.getValue().floatValue());
                  EntityRenderHooks.method_2(this.modelLine.getValue(), this.modelFill.getValue());
                  EntityRenderHooks.method_2(var1.method_10(), var7);
               }

               if (this.tracer.getValue()) {
                  RenderUtil.field_2672
                     .method_2(
                        var1.method_10(), var3, ((PlayerSnapshot)var6.getValue()).getBox().getCenter(), this.modelLine.getValue(), this.width2.getValue()
                     );
               }

               if (this.nameTag.getValue()) {
                  Vec3 var15 = ((PlayerSnapshot)var6.getValue())
                     .getBox()
                     .getCenter()
                     .with(Axis.Y, ((PlayerSnapshot)var6.getValue()).getBox().maxY + 0.5);
                  String var8 = new PlaceholderText().method_2((String)var6.getKey()).method_9("\u0001 logout spot");
                  String var9 = this.method_2((PlayerSnapshot)var6.getValue());
                  float var10 = field_984.isToggled() ? 1F : 0.0F;
                  if (!var9.isEmpty()) {
                     var8 = new PlaceholderText().method_2((Object)var9).method_2((Object)var8).method_9("\u0001\u0001");
                  }

                  double var11 = MathUtil.method_2(mc.gameRenderer.getMainCamera().position(), var15, (double)this.textScale.getValue().floatValue());
                  RenderUtil.field_2672
                     .method_2(
                        var1.method_10(),
                        var15,
                        -0.03F,
                        0.0F,
                        -(FontRenderer.field_3143.method_221(var8) / 2F) - 2F,
                        5F + var10,
                        var11 * 2.0,
                        this.fillColor.getValue()
                     );
                  RenderUtil.field_2672
                     .method_2(
                        var1.method_881(),
                        var8,
                        var15,
                        0.0F,
                        0.0F,
                        -(FontRenderer.field_3143.method_221(var8) / 2F),
                        2F,
                        var11,
                        this.nameTagColor.getValue(),
                        true
                     );
               }
            }
         }
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      for (PendingPlayerTarget var3 : this.field_2351) {
         Player var4 = var3.getPlayer();
         if (this.method_154(var4)) {
            var3.cancelled.set(true);
            this.field_2350.put(var4.getGameProfile().name(), PlayerSnapshot.method_2(var4, this.method_106()));
            if (this.sounds.getValue() && this.logout.getValue()) {
               Hub.field_2606.method_2(this.sound2.getValue(), 1F);
            }
         }
      }

      this.field_2351.removeIf(PendingPlayerTarget::isExpired);
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundPlayerInfoUpdatePacket var2) {
         for (net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.Entry var4 : var2.newEntries()) {
            mc.executeIfPossible(() -> {
               GameProfile var2x = var4.profile();
               String var3 = var2x == null ? "" : var2x.name();
               if (this.sounds.getValue() && this.login.getValue() && this.field_2350.containsKey(var3)) {
                  Hub.field_2606.method_2(this.sound.getValue(), 1F);
               }

               this.field_2350.remove(var3);
            });
         }
      }
   }

   @Subscribe
   public void method_5(ServerConnectEvent var1) {
      if (Hub.field_2602.method_991() == null || var1.method_106().equals(Hub.field_2602.method_991().ip)) {
         this.field_2350.clear();
      }
   }

   @Subscribe
   public void method_2(EntityRemoveEvent var1) {
      if (mc.level.getEntity(var1.getId()) instanceof Player var2) {
         if (var2 instanceof FakePlayerEntity) {
            return;
         }

         if (var2.deathTime > 0 || EntityUtil.method_2((net.minecraft.world.entity.Entity)var2) <= 0.0F) {
            return;
         }

         if (this.ignoreFriends.getValue() && Hub.field_2603.getId(var2)) {
            return;
         }

         if (this.ignoreNakeds.getValue() && !PlayerUtil.method_29((LivingEntity)var2)) {
            return;
         }

         this.field_2351.add(new PendingPlayerTarget(var2, System.currentTimeMillis(), new AtomicBoolean()));
      }
   }

   public String method_2(PlayerSnapshot var1) {
      StringBuilder var2 = new StringBuilder();
      if (this.position.getValue() == LogoutPositionMode.COORDINATES) {
         Vec3 var3 = var1.getPos();
         var2.append(" ");
         var2.append(String.format(Locale.US, "X: %.1f, Y: %.1f, Z: %.1f", var3.x(), var3.y(), var3.z()));
      }

      if (this.position.getValue() == LogoutPositionMode.DISTANCE) {
         double var5 = mc.player.position().distanceTo(var1.getPos());
         var2.append(" ");
         var2.append("%.1fm".formatted(var5));
      }

      if (this.time.getValue()) {
         String var6 = " %s".formatted(new SimpleDateFormat("HH:mm").format(new Date(var1.method_417())));
         var2.append(var6);
      }

      if (this.health.getValue()) {
         var2.append(" (");
         var2.append(var1.health);
         var2.append("hp)");
      }

      if (var1.count > 0 && this.totems.getValue()) {
         var2.append(" -");
         var2.append(var1.count);
      }

      return var2.toString();
   }

   public boolean method_154(Player var1) {
      return mc.player.connection.getOnlinePlayers().stream().noneMatch(var1x -> var1x.getProfile().name().equals(var1.getGameProfile().name()));
   }

   public static boolean method_708() {
      return field_2321;
   }

   public String method_106() {
      return mc.level != null && !mc.isLocalServer() && mc.player.connection.getServerData() != null
         ? mc.player.connection.getServerData().ip
         : "singleplayer";
   }
}
