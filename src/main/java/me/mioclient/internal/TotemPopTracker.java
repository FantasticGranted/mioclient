package me.mioclient.internal;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Class_1176;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Event_15;
import me.mioclient.event.ServerConnectEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.Entry;
import net.minecraft.world.entity.player.Player;

/**
 * Tracks how many totems each player has popped (and their leave times), firing a
 * totem-pop/death event so other modules can display or react to pop counts.
 */
public final class TotemPopTracker implements MinecraftAccess {
   public final Map<String, Integer> popCounts = new ConcurrentHashMap<>();
   public final Map<String, Long> leaveTimes = new ConcurrentHashMap<>();

   public TotemPopTracker() {
      super();
      lookups.method_14(this);
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (!this.method_535()) {
         if (var1.method_127() instanceof ClientboundEntityEventPacket var2 && var2.getEventId() == 35 && var2.getEntity(mc.level) instanceof Player var8
            )
          {
            String var13 = var8.getGameProfile().name();
            this.popCounts.compute(var13, (var0, var1x) -> var1x == null ? 1 : var1x + 1);
            lookups.method_36(new Event_15(var8, this.popCounts.get(var13), Class_1176.TOTEM_POP));
         }

         if (var1.method_127() instanceof ClientboundPlayerInfoUpdatePacket var6) {
            for (Entry var14 : var6.newEntries()) {
               this.leaveTimes.remove(var14.profile().name());
            }
         }

         if (var1.method_127() instanceof ClientboundPlayerInfoRemovePacket var7) {
            for (UUID var15 : var7.profileIds()) {
               PlayerInfo var5 = mc.player.connection.getPlayerInfo(var15);
               if (var5 != null) {
                  this.leaveTimes.put(var5.getProfile().name(), System.currentTimeMillis());
               }
            }
         }
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      for (Player var3 : mc.level.players()) {
         if (var3 != null && !(EntityUtil.method_2((net.minecraft.world.entity.Entity)var3) > 0.0F)) {
            String var4 = var3.getName().getString();
            if (this.popCounts.containsKey(var4)) {
               int var5 = this.popCounts.get(var4);
               this.popCounts.remove(var4);
               lookups.method_36(new Event_15(var3, var5, Class_1176.DEATH));
            }
         }
      }

      String var6 = null;

      for (java.util.Map.Entry var8 : this.leaveTimes.entrySet()) {
         if ((Long)var8.getValue() + 120000L < System.currentTimeMillis()) {
            var6 = (String)var8.getKey();
            this.popCounts.remove(var8.getKey());
         }
      }

      if (var6 != null) {
         this.leaveTimes.remove(var6);
      }
   }

   @Subscribe
   public void method_9(ServerConnectEvent var1) {
      if (Hub.field_2602.method_991() == null || var1.method_106().equals(Hub.field_2602.method_991().ip)) {
         this.popCounts.clear();
      }
   }

   public int method_39(Player var1) {
      return this.method_194(var1.getGameProfile().name());
   }

   public int method_194(String var1) {
      return this.popCounts.getOrDefault(var1, 0);
   }
}
