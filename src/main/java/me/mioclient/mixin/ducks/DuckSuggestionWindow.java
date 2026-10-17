package me.mioclient.mixin.ducks;

import net.minecraft.client.gui.components.CommandSuggestions.SuggestionsList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({SuggestionsList.class})
public interface DuckSuggestionWindow {
   @Accessor("tabCycles")
   boolean isCompleted();

   @Accessor("current")
   int getSelection();

   @Accessor("originalContents")
   String getTypedText();
}
