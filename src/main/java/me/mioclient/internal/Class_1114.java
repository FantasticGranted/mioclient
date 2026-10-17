package me.mioclient.internal;

import java.util.ArrayDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.ServerConnectEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.module.exploit.FastLatencyModule;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;

public final class Class_1114 implements MinecraftAccess {
   public static final FastLatencyModule field_3440 = Hub.field_2595.method_78(FastLatencyModule.class);
   public static final Pattern field_3441 = Pattern.compile("\\d+ ping");
   public final ArrayDeque<Float> field_3442 = new ArrayDeque<>(20);
   public final Timer field_3443 = new Timer();
   public final Timer field_3444 = new Timer();
   public float field_3445;
   public float field_3446;
   public long field_3447;
   public long field_677;
   public int field_3448;
   public boolean field_3449;
   public boolean field_3450;
   public ServerData field_3451;
   public int field_3452;
   public boolean field_3453;

   public Class_1114() {
      super();
      lookups.method_14(this);
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      this.field_3447 = System.currentTimeMillis();
      if (var1.method_127() instanceof ClientboundPlayerPositionPacket var2) {
         this.field_3448 = var2.id();
         if (this.field_3444.method_9(50L)) {
            this.field_3443.reset();
         }

         this.field_3449 = mc.player.isSprinting();
      }

      if (var1.method_127() instanceof ClientboundSetTimePacket) {
         if (this.field_677 != 0L) {
            if (this.field_3442.size() > 20) {
               this.field_3442.poll();
            }

            this.field_3445 = Math.max(
               0.0F,
               Math.min(
                  2e+01F,
                  2e+01F * (1e+03F / (float)(System.currentTimeMillis() - this.field_677))
               )
            );
            this.field_3442.add(this.field_3445);
            float var9 = 0.0F;

            for (Float var4 : this.field_3442) {
               var9 += Math.max(0.0F, Math.min(2e+01F, var4));
            }

            if (this.field_3442.size() > 0) {
               var9 /= (float)this.field_3442.size();
            }

            this.field_3446 = var9;
         }

         this.field_677 = System.currentTimeMillis();
      }

      if (var1.method_127() instanceof ClientboundBundlePacket var10) {
         for (Packet var17 : var10.subPackets()) {
            lookups.method_36(new PacketReceiveEvent(var17));
         }
      }

      if (var1.method_127() instanceof ClientboundTabListPacket var11) {
         ServerData var16 = mc.player.connection.getServerData();
         if (var16 == null) {
            return;
         }

         String var18 = var11.footer().getString();
         Matcher var5 = field_3441.matcher(var18);
         if (var5.find()) {
            String var6 = var5.group();

            try {
               var6 = var6.substring(0, var6.length() - 5);
               this.field_3452 = Integer.parseInt(var6);
            } catch (Throwable var8) {
            }
         }
      }
   }

   @Subscribe(
      method_800 = 200
   )
   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundPlayerCommandPacket var2) {
         if (var2.getAction() == Action.START_SPRINTING) {
            if (this.field_3449) {
               var1.method_463();
            }

            this.field_3449 = true;
         }

         if (var2.getAction() == Action.STOP_SPRINTING) {
            if (!this.field_3449) {
               var1.method_463();
            }

            this.field_3449 = false;
         }

      }

      if (var1.method_127() instanceof ServerboundPlayerInputPacket varInp) {
         this.field_3450 = varInp.input().shift();
      }

      if (var1.method_127() instanceof ServerboundAcceptTeleportationPacket) {
         this.field_3444.reset();
      }
   }

   @Subscribe
   public void method_9(ServerConnectEvent var1) {
      this.field_3453 = false;
      this.field_3452 = 0;
   }

   public int method_983() {
      if (mc.player == null) {
         return 0;
      } else if (field_3440.isToggled()) {
         return field_3440.method_983();
      } else {
         PlayerInfo var1 = mc.player.connection.getPlayerInfo(mc.player.getGameProfile().id());
         int var2 = var1 == null ? 0 : var1.getLatency();
         return var2 == 0 ? this.field_3452 : var2;
      }
   }

   public Timer method_984() {
      return this.field_3443;
   }

   public Timer method_985() {
      return this.field_3444;
   }

   public int method_986() {
      return this.field_3448;
   }

   public long method_987() {
      return this.field_3447;
   }

   public float method_988() {
      return this.field_3445;
   }

   public float method_989() {
      return this.field_3446;
   }

   public float method_990() {
      return this.field_3446 / 2e+01F;
   }

   public ServerData method_991() {
      return this.field_3451;
   }

   public void method_2(ServerData var1) {
      this.field_3451 = var1;
   }

   public boolean method_992() {
      return this.field_3449;
   }

   public boolean method_993() {
      return this.field_3450;
   }
}
