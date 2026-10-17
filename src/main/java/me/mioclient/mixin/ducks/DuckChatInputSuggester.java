package me.mioclient.mixin.ducks;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.CommandSuggestions.SuggestionsList;
import net.minecraft.commands.SharedSuggestionProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({CommandSuggestions.class})
public interface DuckChatInputSuggester {
   @Accessor("pendingSuggestions")
   CompletableFuture<Suggestions> getSuggestion();

   @Accessor("suggestions")
   SuggestionsList getWindow();

   @Accessor("currentParse")
   ParseResults<SharedSuggestionProvider> getParse();
}
