package me.mioclient.internal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.api.ChatLineAccess;
import me.mioclient.api.VisibleLinesAccess;
import me.mioclient.module.Module;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public final class Class_0152 extends ChatCommand {
   public static boolean field_442 = false;
   public static final String field_443 = "ㅤ";

   public Class_0152() {
      super("panic");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.executes(var1x -> {
         this.method_2(() -> mc.setScreen(new ConfirmScreen(var1xx -> {
               if (var1xx) {
                  this.method_183();
               }

               mc.setScreen(null);
            }, Component.literal("Unload the client."), Component.literal("Continue?"))));
         return 1;
      });
   }

   public void method_183() {
      Hub.field_2597.method_357();
      field_442 = true;
      String var1 = CommandManager.method_927();
      CommandManager.method_270("ㅤ");

      for (Module var3 : (List<me.mioclient.module.Module>)Hub.field_2599.getRegistry()) {
         var3.modifyKeybind(var0 -> var0.method_9(-1));
         if (var3.isToggled()) {
            var3.method_68();
         }
      }

      mc.gui.getChat().getRecentChat().removeIf(var1x -> var1x.startsWith(var1));
      ((VisibleLinesAccess)mc.gui.getChat()).getVisible().removeIf(var0 -> {
         ChatLineAccess var1x = (ChatLineAccess)(Object)var0;
         return var1x.getSignature() != null && var1x.getSignature().asByteBuffer().getInt() < 0;
      });
      mc.gui.getChat().getRecentChat().removeIf(var1x -> var1x.startsWith(var1));
   }
}
