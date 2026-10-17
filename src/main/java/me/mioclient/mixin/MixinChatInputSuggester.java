package me.mioclient.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.CommandManager;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.CommandSuggestions.SuggestionsList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.commands.SharedSuggestionProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({CommandSuggestions.class})
public abstract class MixinChatInputSuggester implements MinecraftAccess {
   @Shadow
   @Final
   EditBox input;
   @Shadow
   boolean keepSuggestions;
   @Shadow
   private ParseResults<SharedSuggestionProvider> currentParse;
   @Shadow
   private CompletableFuture<Suggestions> pendingSuggestions;
   @Shadow
   private SuggestionsList suggestions;

   public MixinChatInputSuggester() {
      super();
   }

   @Shadow
   public abstract void showSuggestions(boolean var1);

   @Inject(
      method = {"updateCommandInfo"},
      at = {@At(
         value = "INVOKE",
         target = "Lcom/mojang/brigadier/StringReader;canRead()Z",
         remap = false
      )},
      cancellable = true,
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   public void refresh(CallbackInfo var1, String var2, StringReader var3) {
      String var4 = CommandManager.method_927();
      if (var3.canRead(var4.length()) && var3.getString().startsWith(var4, var3.getCursor())) {
         var3.setCursor(var3.getCursor() + var4.length());
         CommandDispatcher var5 = CommandManager.field_3191;
         if (this.currentParse == null) {
            this.currentParse = var5.parse(var3, CommandManager.field_3190);
         }

         int var6 = this.input.getCursorPosition();
         if (var6 >= 1 && (this.suggestions == null || !this.keepSuggestions)) {
            this.pendingSuggestions = var5.getCompletionSuggestions(this.currentParse, var6);
            this.pendingSuggestions.thenRun(() -> {
               if (this.pendingSuggestions.isDone()) {
                  this.showSuggestions(false);
               }
            });
         }

         var1.cancel();
      }
   }
}
