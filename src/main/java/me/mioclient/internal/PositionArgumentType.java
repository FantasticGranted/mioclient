package me.mioclient.internal;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.SharedSuggestionProvider.TextCoordinates;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinate;
import net.minecraft.world.phys.Vec3;

/** Brigadier ArgumentType parsing a (possibly relative) coordinate/position. */
public class PositionArgumentType implements ArgumentType<Vec3> {
   public static final Collection<String> field_29 = Arrays.asList("0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "0.1 -0.5 .9", "~0.5 ~1 ~-5");

   public PositionArgumentType() {
      super();
   }

   public Vec3 parse(StringReader var1) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
      int var2 = var1.getCursor();
      WorldCoordinate var3 = WorldCoordinate.parseInt(var1);
      if (var1.canRead() && var1.peek() == ' ') {
         var1.skip();
         WorldCoordinate var4 = WorldCoordinate.parseDouble(var1, false);
         if (var1.canRead() && var1.peek() == ' ') {
            var1.skip();
            Vec3 var5 = Minecraft.getInstance().player.position();
            WorldCoordinate var6 = WorldCoordinate.parseInt(var1);
            return new Vec3(var3.get(var5.x), var4.get(var5.y), var6.get(var5.z));
         } else {
            var1.setCursor(var2);
            throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(var1);
         }
      } else {
         var1.setCursor(var2);
         throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(var1);
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> var1, SuggestionsBuilder var2) {
      if (!(var1.getSource() instanceof SharedSuggestionProvider)) {
         return Suggestions.empty();
      } else {
         String var3 = var2.getRemaining();
         Object var4;
         if (!var3.isEmpty() && var3.charAt(0) == '^') {
            var4 = Collections.singleton(TextCoordinates.DEFAULT_LOCAL);
         } else {
            var4 = ((SharedSuggestionProvider)var1.getSource()).getAbsoluteCoordinates();
         }

         return SharedSuggestionProvider.suggestCoordinates(var3, (Collection)var4, var2, Commands.createValidator(this::parse));
      }
   }

   public Collection<String> getExamples() {
      return field_29;
   }
}
