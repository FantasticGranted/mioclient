package me.mioclient.internal;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.awt.Color;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import me.mioclient.api.RegistryCompleter;
import me.mioclient.deobf.Named;
import me.mioclient.enum_.Priority;
import me.mioclient.module.Module;
import me.mioclient.setting.Class0211Setting;
import me.mioclient.setting.ColorSetting;
import me.mioclient.setting.CustomSetting;
import me.mioclient.setting.CustomSetting2;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Exposes a Module as a chat command: toggle it, and get/set/reset/clear each of its
 * settings, plus a formatted listing of all settings with clickable suggestions.
 */
public final class ModuleCommand extends ChatCommand {
   public final Module module;

   public ModuleCommand(Module var1) {
      super(var1.getName());
      this.module = var1;
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      ((LiteralArgumentBuilder<SharedSuggestionProvider>)((LiteralArgumentBuilder<SharedSuggestionProvider>)((LiteralArgumentBuilder<SharedSuggestionProvider>)var1.then(
                  RequiredArgumentBuilder.<SharedSuggestionProvider, Boolean>argument("state", BoolArgumentType.bool()).suggests((var0, var1x) -> var1x.buildFuture()).executes(var1x -> {
                     this.module.method_38((Boolean)var1x.getArgument("state", Boolean.class));
                     return 1;
                  })
               ))
               .then(
                  ((RequiredArgumentBuilder<SharedSuggestionProvider, ?>)((RequiredArgumentBuilder<SharedSuggestionProvider, ?>)((RequiredArgumentBuilder<SharedSuggestionProvider, ?>)RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument(
                                 "setting", new SettingNameArgument(this.module)
                              )
                              .then(
                                 ChatCommand.method_2("clear")
                                    .suggests(this::method_29)
                                    .executes(
                                       var1x -> {
                                          Setting var2 = SettingNameArgument.getOption(var1x, this.module, "setting");
                                          if (var2 instanceof CustomSetting2 var3) {
                                             ((Set)var3.getValue()).clear();
                                             ChatUtil.method_2(
                                                Component.literal(this.module.getName())
                                                   .append(Component.literal(" ").append(var2.getName()).withStyle(var0 -> var0.applyFormat(ChatFormatting.GRAY)))
                                                   .append(": Cleared"),
                                                ChatUtil.method_2(this.module),
                                                Priority.LOW
                                             );
                                          }

                                          return 1;
                                       }
                                    )
                              ))
                           .then(
                              RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("value", new SettingValueArgument(this.module, "setting"))
                                 .executes(
                                    var1x -> {
                                       Setting var2 = SettingNameArgument.getOption(var1x, this.module, "setting");
                                       String var3 = (String)var1x.getArgument("value", String.class);

                                       try {
                                          ChatFormatting var4 = null;
                                          if (var2 instanceof RegistrySetting var5) {
                                             RegistryCompleter var6 = RegistryCompleter.method_2(var5.method_231());
                                             Collection<String> var7 = var6.method_114(var3);
                                             if (!var7.isEmpty()) {
                                                if (var7.stream().anyMatch(var5::method_16)) {
                                                   var7.forEach(var5::getId);
                                                   var4 = ChatFormatting.RED;
                                                } else {
                                                   var7.forEach(var5::method_39);
                                                   var4 = ChatFormatting.GREEN;
                                                }
                                             }
                                          }

                                          if (var4 == null) {
                                             var2.method_78(var3);
                                          }

                                          Object var9 = this.method_9(var2);
                                          if (var2 instanceof CustomSetting2 var10) {
                                             if (var4 == null) {
                                                var4 = var10.method_16(var3) ? ChatFormatting.GREEN : ChatFormatting.RED;
                                             }

                                             ChatFormatting var11 = var4;
                                             var9 = Component.literal(var3).withStyle(var1xx -> var1xx.applyFormat(var11));
                                          }

                                          ChatUtil.method_2(
                                             Component.literal(this.module.getName())
                                                .append(Component.literal(" ").append(var2.getName()).withStyle(var0 -> var0.applyFormat(ChatFormatting.GRAY)))
                                                .append(": ")
                                                .append((Component)var9),
                                             ChatUtil.method_2(this.module),
                                             Priority.LOW
                                          );
                                       } catch (Throwable var8) {
                                          ChatUtil.method_2(Component.literal("Invalid value: ").append(var3), ChatUtil.method_2(this.module), Priority.HIGH);
                                       }

                                       return 1;
                                    }
                                 )
                           ))
                        .then(LiteralArgumentBuilder.<SharedSuggestionProvider>literal("reset").executes(var1x -> {
                           Setting var2 = SettingNameArgument.getOption(var1x, this.module, "setting");
                           var2.reset();
                           return 1;
                        })))
                     .executes(
                        var1x -> {
                           Setting var2 = SettingNameArgument.getOption(var1x, this.module, "setting");
                           ChatUtil.method_2(
                              Component.literal(this.module.getName())
                                 .append(Component.literal(" ").append(var2.getName()).withStyle(var0 -> var0.applyFormat(ChatFormatting.GRAY)))
                                 .append(": ")
                                 .append(this.method_9(var2)),
                              ChatUtil.method_2(this.module)
                           );
                           return 1;
                        }
                     )
               ))
            .then(
               ((LiteralArgumentBuilder<SharedSuggestionProvider>)LiteralArgumentBuilder.<SharedSuggestionProvider>literal("Enabled")
                     .then(RequiredArgumentBuilder.<SharedSuggestionProvider, Boolean>argument("state", BoolArgumentType.bool()).executes(var1x -> {
                        this.module.method_38((Boolean)var1x.getArgument("state", Boolean.class));
                        return 1;
                     })))
                  .executes(
                     var1x -> {
                        ChatUtil.method_2(
                           Component.literal("Module ")
                              .append(Component.literal(this.module.getName()).withStyle(var0 -> var0.applyFormat(ChatFormatting.GRAY)))
                              .append(" is ")
                              .append(
                                 Component.literal(this.module.isToggled() ? "enabled" : "disabled")
                                    .withStyle(
                                       var1xx -> this.module.isToggled() ? var1xx.applyFormat(ChatFormatting.GREEN) : var1xx.applyFormat(ChatFormatting.RED)
                                    )
                              ),
                           ChatUtil.method_2(this.module)
                        );
                        return 1;
                     }
                  )
            ))
         .executes(
            var1x -> {
               MutableComponent var2 = Component.empty();
               MutableComponent var3 = this.method_28();
               var2.append(var3).append(" [%s - %s]".formatted(this.module.getKeybind().method_4(), this.module.getKeybind().method_78().getName()));

               for (Object var5 : this.module.getRegistry()) {
                  MutableComponent var6 = Component.literal(new PlaceholderText().method_2(((Named)var5).getName()).method_9("\n\u0001: "));
                  Objects.requireNonNull(var5);
                  switch (var5) {
                     case CustomSetting var9:
                        var6.append(Component.literal(FontRenderer.method_3(((Enum)var9.getValue()).name())).withStyle(ChatFormatting.GRAY));
                        break;
                     case CustomSetting2 var10:
                        var6.append(Component.literal(String.join(", ", var10.method_251())).withStyle(ChatFormatting.GRAY));
                        break;
                     case ColorSetting var11:
                        String var12 = this.method_2(var11);
                        var6.append(Component.literal(var12).withStyle(var1xx -> var1xx.withColor(var11.method_465().hashCode())));
                        break;
                     default:
                        var6.append(Component.literal(((Setting)var5).getValue().toString()).withStyle(ChatFormatting.GRAY));
                  }

                  String var7 = new PlaceholderText()
                     .method_2(((Named)var5).getName())
                     .method_2(this.module.getName().toLowerCase())
                     .method_2(CommandManager.method_927())
                     .method_9("\u0001\u0001 \u0001 ");
                  var6.withStyle(var1xx -> var1xx.withClickEvent(new ClickEvent.SuggestCommand(var7)));
                  var2.append(var6);
               }

               ChatUtil.method_2(var2, ChatUtil.method_38(-1));
               return 1;
            }
         );
      var1.then(LiteralArgumentBuilder.<SharedSuggestionProvider>literal("reset").executes(var1x -> {
         for (Setting var3 : this.module.getRegistry()) {
            var3.reset();
         }

         return 1;
      }));
   }

   @NotNull
   public MutableComponent method_28() {
      MutableComponent var1 = Component.literal(this.module.getName());
      var1.withStyle(var1x -> ChatUtil.method_2(var1x, () -> this.module.isToggled() ? ChatFormatting.GREEN.getColor() : ChatFormatting.RED.getColor()));
      var1.withStyle(
         var1x -> var1x.withHoverEvent(
               new HoverEvent.ShowText( Component.literal(this.module.getDescription().split("\n")[0]))
            )
      );
      var1.withStyle(
         var1x -> var1x.withClickEvent(
               new ClickEvent.RunCommand( new PlaceholderText().method_2(this.module.getName()).method_2(CommandManager.method_927()).method_9("\u0001toggle \u0001")
               )
            )
      );
      return var1;
   }

   public String method_2(ColorSetting var1) {
      Color var2 = var1.method_465();
      return var1.field_3119
         ? "rgb(%d, %d, %d)".formatted(var2.getRed(), var2.getGreen(), var2.getBlue())
         : "rgba(%d, %d, %d, %d)".formatted(var2.getRed(), var2.getGreen(), var2.getBlue(), var2.getAlpha());
   }

   public CompletableFuture<Suggestions> method_29(CommandContext<?> var1, SuggestionsBuilder var2) {
      try {
         Setting var3 = SettingNameArgument.getOption(var1, this.module, "setting");
         if (var3 instanceof CustomSetting2) {
            return SharedSuggestionProvider.suggest(new String[]{"clear"}, var2);
         }
      } catch (CommandSyntaxException var4) {
      }

      return Suggestions.empty();
   }

   public Component method_9(Setting<?> var1) {
      MutableComponent var2 = Component.literal(var1.getValue().toString());
      if (var1 instanceof ColorSetting var3) {
         var2 = Component.literal(Colors.method_2(var3.method_465(), true)).withStyle(var1x -> var1x.withColor(var1.getValue().hashCode()));
      } else if (var1 instanceof CustomSetting2 var4) {
         var2 = ComponentUtils.formatList(var4.method_251(), Component.literal(", ").withStyle(var0 -> var0.applyFormat(ChatFormatting.GRAY)), Component::literal);
      } else if (var1 instanceof Class0211Setting var5) {
         var2 = Component.literal(((SoundId)var5.getValue()).getName());
      }

      return var2;
   }
}
