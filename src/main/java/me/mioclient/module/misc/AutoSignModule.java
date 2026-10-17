package me.mioclient.module.misc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import me.mioclient.nick.Settings;

public class AutoSignModule extends Module {
   public static final DateFormat field_4300 = new SimpleDateFormat("dd/MM/yyyy");
   public Setting<String> line1;
   public Setting<String> line2;
   public Setting<String> line3;
   public Setting<String> line4;

   public AutoSignModule() {
      super("AutoSign", "Puts stuff on your signs automatically.", Category.MISC);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_29(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundOpenSignEditorPacket var2) {
         PacketSender.method_2(
            new ServerboundSignUpdatePacket(
               var2.getPos(),
               var2.isFrontText(),
               this.method_4(this.line1),
               this.method_4(this.line2),
               this.method_4(this.line3),
               this.method_4(this.line4)
            )
         );
         var1.method_463();
      }
   }

   public String method_4(Setting<String> var1) {
      String var2 = (String)var1.getValue();
      var2 = var2.replace("<date>", field_4300.format(Calendar.getInstance().getTime()));
      var2 = var2.replace("<name>", mc.player.getName().getString());
      return var2.substring(0, Math.min(16, var2.length()));
   }
}
