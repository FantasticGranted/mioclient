package me.mioclient.module.misc;

import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.DisconnectEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.BlockUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import me.mioclient.nick.Settings;

public class CoordLoggerModule extends Module {
   public Setting<Boolean> log;
   public Setting<Boolean> death;
   public String text;

   public CoordLoggerModule() {
      super("CoordLogger", "Copies your coordinates whenever you log/die.", Category.MISC);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      this.text = "X: %d, Y: %d, Z: %d in The %s"
         .formatted(mc.player.getBlockX(), mc.player.getBlockY(), mc.player.getBlockZ(), BlockUtil.method_1071().getDisplayName());
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundPlayerCombatKillPacket && this.death.getValue()) {
         this.method_300();
      }
   }

   @Subscribe
   public void method_2(DisconnectEvent var1) {
      if (mc.player != null && this.log.getValue()) {
         this.method_300();
      }
   }

   public void method_300() {
      mc.keyboardHandler.setClipboard(this.text);
   }
}
