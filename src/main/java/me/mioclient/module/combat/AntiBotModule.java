package me.mioclient.module.combat;

import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.FakePlayerEntity;
import me.mioclient.internal.FontRenderer;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Player;
import me.mioclient.nick.Settings;

public class AntiBotModule extends Module {
   public Setting<Boolean> illegalChars;
   public Setting<Boolean> illegalNames;

   public AntiBotModule() {
      super("AntiBot", "Removes the bots that will be spawned by some anti cheats.", Category.COMBAT);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      Player var2 = null;
      synchronized (mc.level.players()) {
         for (Player var5 : mc.level.players()) {
            if (this.method_3(var5)) {
               var2 = var5;
            }
         }
      }

      if (var2 != null) {
         var2.setRemoved(RemovalReason.KILLED);
      }
   }

   public boolean method_3(Player var1) {
      if (mc.player != var1 && !(var1 instanceof FakePlayerEntity)) {
         String var2 = var1.getName().getString();
         if ((var2.length() < 3 || var2.length() > 16) && this.illegalNames.getValue()) {
            return true;
         } else {
            if (this.illegalChars.getValue()) {
               String var3 = "1234567890_qwertyuiopasdfghjklzxcvbnm";

               for (char var7 : var2.toLowerCase().toCharArray()) {
                  if (!FontRenderer.method_2("1234567890_qwertyuiopasdfghjklzxcvbnm", var7)) {
                     return true;
                  }
               }
            }

            PlayerInfo var8 = mc.player.connection.getPlayerInfo(var1.getGameProfile().id());
            return !mc.player.connection.getOnlinePlayers().contains(var8) || var8 == null || var8.getProfile() == null && var1.hasCustomName();
         }
      } else {
         return false;
      }
   }
}
