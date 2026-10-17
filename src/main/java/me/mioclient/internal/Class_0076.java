package me.mioclient.internal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.module.Module;
import me.mioclient.record.Keybind;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public final class Class_0076 extends ChatCommand {
   public Class_0076() {
      super("bind");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      ((LiteralArgumentBuilder<SharedSuggestionProvider>)var1.then(
            LiteralArgumentBuilder.<SharedSuggestionProvider>literal("set")
               .then(
                  RequiredArgumentBuilder.<SharedSuggestionProvider, Module>argument("module", new ModuleArgumentType())
                     .then(
                        RequiredArgumentBuilder.<SharedSuggestionProvider, Keybind>argument("key", new KeybindArgumentType())
                           .executes(
                              var1x -> {
                                 Module var2 = (Module)var1x.getArgument("module", Module.class);
                                 Keybind var3 = (Keybind)var1x.getArgument("key", Keybind.class);
                                 var2.modifyKeybind(var1xx -> var1xx.method_9(var3.method_38()).method_9(var3.method_39()));
                                 ChatUtil.method_2(
                                    Component.literal("Bind for ")
                                       .append(this.method_5(var2.getName()))
                                       .append(" has been set to ")
                                       .append(this.method_5(KeybindUtil.method_2(var3.method_38(), var3.method_39()))),
                                    ChatUtil.method_38(-1)
                                 );
                                 return 1;
                              }
                           )
                     )
               )
         ))
         .then(
            ((LiteralArgumentBuilder<SharedSuggestionProvider>)LiteralArgumentBuilder.<SharedSuggestionProvider>literal("clear")
                  .then(RequiredArgumentBuilder.<SharedSuggestionProvider, Module>argument("module", new ModuleArgumentType()).executes(var1x -> {
                     Module var2 = (Module)var1x.getArgument("module", Module.class);
                     var2.modifyKeybind(var0 -> var0.method_9(-1));
                     ChatUtil.method_2(Component.literal("Bind for ").append(this.method_5(var2.getName())).append(" has been reset"), ChatUtil.method_38(-1));
                     return 1;
                  })))
               .then(LiteralArgumentBuilder.<SharedSuggestionProvider>literal("all").executes(var0 -> {
                  for (Module var2 : (List<Module>)Hub.field_2599.getRegistry()) {
                     var2.modifyKeybind(var0x -> var0x.method_9(-1));
                  }

                  ChatUtil.method_2(Component.literal("Bind for all modules has been reset"), ChatUtil.method_38(-1));
                  return 1;
               }))
         );
   }
}
