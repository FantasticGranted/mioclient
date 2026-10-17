package me.mioclient.internal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.awt.Desktop;
import java.io.IOException;
import net.minecraft.commands.SharedSuggestionProvider;

public final class Class_1105 extends ChatCommand {
   public Class_1105() {
      super("folder");
      this.method_9("openfolder");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.executes(var0 -> {
         try {
            Desktop.getDesktop().open(FileManager.field_4281.toFile());
         } catch (IOException var2) {
            var2.printStackTrace();
         }

         return 1;
      });
   }
}
