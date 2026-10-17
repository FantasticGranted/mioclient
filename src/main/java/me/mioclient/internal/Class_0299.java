package me.mioclient.internal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class Class_0299 extends ChatCommand {
   public Class_0299() {
      super("defaults");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.executes(var1x -> {
         ConfirmScreen var2 = new ConfirmScreen(var0 -> {
            if (var0) {
               for (Module var2x : (List<Module>)Hub.field_2599.getRegistry()) {
                  for (Setting var4 : var2x.getRegistry()) {
                     var4.reset();
                  }

                  var2x.method_38(false);
               }
            }

            mc.setScreen(null);
         }, Component.nullToEmpty("Reset to defaults."), Component.nullToEmpty("Continue?"));
         this.method_2(() -> mc.setScreen(var2));
         return 1;
      });
   }
}
