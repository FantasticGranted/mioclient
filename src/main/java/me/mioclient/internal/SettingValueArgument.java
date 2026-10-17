package me.mioclient.internal;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import me.mioclient.Hub;
import me.mioclient.api.RegistryCompleter;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.Module;
import me.mioclient.setting.BooleanSetting;
import me.mioclient.setting.Class0211Setting;
import me.mioclient.setting.CustomSetting;
import me.mioclient.setting.CustomSetting2;
import me.mioclient.setting.CustomSetting3;
import me.mioclient.setting.Setting;
import net.minecraft.commands.SharedSuggestionProvider;

/**
 * Brigadier argument type for a module setting's value: suggests valid completions based on
 * the resolved setting's type (boolean, enum, numeric range, multi-select list or sound).
 */
public class SettingValueArgument implements ArgumentType<String>, MinecraftAccess {
   public final String argumentName;
   public final Module module;

   public String parse(StringReader var1) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
      String var2 = var1.getRemaining();
      var1.setCursor(var1.getTotalLength());
      return var2;
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> var1, SuggestionsBuilder var2) {
      Setting var3;
      try {
         var3 = SettingNameArgument.getOption(var1, this.module, this.argumentName);
      } catch (Exception var11) {
         return Suggestions.empty();
      }

      if (var3 instanceof BooleanSetting) {
         return Class_1201.bool((BooleanSetting)var3).listSuggestions(var1, var2);
      } else if (var3 instanceof CustomSetting var4) {
         return SharedSuggestionProvider.suggest(
            Arrays.stream((Enum[])((Enum)var4.method_99()).getDeclaringClass().getEnumConstants())
               .filter(var0 -> !AnnotationUtil.method_29((Enum<?>)var0))
               .map(var0 -> var0.name().toLowerCase()),
            var2
         );
      } else if (var3 instanceof CustomSetting3 var5) {
         Number var12 = (Number)var5.getValue();
         if (var2.getRemaining().isBlank()) {
            return var2.suggest(String.valueOf(var5.method_99())).buildFuture();
         } else if (var12 instanceof Float) {
            return FloatArgumentType.floatArg((Float)((Number)var5.method_100()), (Float)((Number)var5.method_101())).listSuggestions(var1, var2);
         } else {
            return var12 instanceof Double
               ? DoubleArgumentType.doubleArg((Double)((Number)var5.method_100()), (Double)((Number)var5.method_101())).listSuggestions(var1, var2)
               : IntegerArgumentType.integer((Integer)((Number)var5.method_100()), (Integer)((Number)var5.method_101())).listSuggestions(var1, var2);
         }
      } else if (var3 instanceof CustomSetting2 var6) {
         Collection var8 = var6.method_186();
         if (var3 instanceof RegistrySetting var9) {
            RegistryCompleter var10 = RegistryCompleter.method_2(var9.method_231());
            if (var10 != RegistryCompleter.field_280) {
               var8.addAll(var10.method_115());
            }
         }

         return SharedSuggestionProvider.suggest(var8, var2);
      } else {
         return var3 instanceof Class0211Setting var7
            ? SharedSuggestionProvider.suggest(
               Hub.field_2606
                  .method_533()
                  .stream()
                  .map(var0 -> new PlaceholderText().method_2(var0.getName()).method_2(var0.method_243()).method_9("\u0001:\u0001")),
               var2
            )
            : ArgumentType.super.listSuggestions(var1, var2);
      }
   }

   public SettingValueArgument(Module var1, String var2) {
      super();
      this.argumentName = var2;
      this.module = var1;
   }

   public static SettingValueArgument value(Module var0, String var1) {
      return new SettingValueArgument(var0, var1);
   }
}
