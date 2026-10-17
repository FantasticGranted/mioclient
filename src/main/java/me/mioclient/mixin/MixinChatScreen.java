package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import java.awt.Color;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import me.mioclient.Hub;
import me.mioclient.api.ChatLineAccess;
import me.mioclient.internal.Class_0227;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.Class_0855;
import me.mioclient.internal.CommandManager;
import me.mioclient.mixin.ducks.DuckChatInputSuggester;
import me.mioclient.mixin.ducks.DuckSuggestionWindow;
import me.mioclient.module.client.HUDModule;
import me.mioclient.module.client.IRCModule;
import me.mioclient.module.client.UIModule;
import me.mioclient.module.misc.BetterChatModule;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.chat.GuiMessage.Line;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ChatScreen.class})
public class MixinChatScreen extends Screen {
   private static final BetterChatModule betterchat = Hub.field_2595.method_78(BetterChatModule.class);
   private static final IRCModule irc = Hub.field_2595.method_78(IRCModule.class);
   private static HUDModule hud = Hub.field_2595.method_78(HUDModule.class);
   private static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   @Shadow
   protected EditBox input;
   @Shadow
   CommandSuggestions commandSuggestions;
   @Unique
   private Class_0855 scroll;

   protected MixinChatScreen(Component var1) {
      super(var1);
   }

   @Inject(
      method = {"init"},
      at = {@At("HEAD")}
   )
   private void initHook(CallbackInfo var1) {
      this.scroll = new Class_0855();
      if (betterchat.always.getValue() && betterchat.method_1092()) {
         for (Line var3 : Minecraft.getInstance().gui.getChat().trimmedMessages) {
            ((ChatLineAccess)(Object)var3).getProgress().method_36(false);
            ((ChatLineAccess)(Object)var3).mio$setAddTime(System.currentTimeMillis());
         }
      }
   }

   @Inject(
      method = {"extractRenderState"},
      at = {@At("RETURN")}
   )
   private void renderHook1(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      boolean var6 = this.input.getValue().startsWith(CommandManager.method_927());
      if (irc.isToggled() && irc.chat.getValue() && this.input.getValue().startsWith(irc.prefix.getValue())) {
         var6 = true;
      }

      Color var7 = UIModule.field_2843.color.getValue();
      if (var6) {
         RenderUtil.field_2672
            .method_2(RenderUtil.mioPose(var1.pose()), 1.0F, (float)(this.height - 2) - hud.method_338(), (float)(this.width - 2), (float)(this.height - 2), var7);
      }
   }

   @WrapWithCondition(
      method = {"extractRenderState"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V"
      )}
   )
   private boolean renderHook2(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, int var6) {
      float var7 = (float)var3 + (float)(var5 - var3) * (1.0F - hud.method_338() / 13.5F);
      RenderUtil.field_2672.method_9(RenderUtil.mioPose(var1.pose()), (float)var2, var7, (float)var4, (float)var5, new Color(var6, true));
      return false;
   }

    @Inject(
       method = {"extractRenderState"},
       at = {@At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/gui/screens/Screen;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
          shift = Shift.BEFORE
       )}
    )
    private void renderHook4(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (this.input.getValue().startsWith(CommandManager.method_927())) {
         DuckSuggestionWindow var6 = (DuckSuggestionWindow)((DuckChatInputSuggester)this.commandSuggestions).getWindow();
         CompletableFuture var7 = ((DuckChatInputSuggester)this.commandSuggestions).getSuggestion();
         if (var7 != null && var7.isDone() && this.input.getValue().startsWith(CommandManager.method_927())) {
            String var8 = "";
            Suggestions var9 = (Suggestions)var7.join();
            ParseResults var10 = ((DuckChatInputSuggester)this.commandSuggestions).getParse();
            List var11 = Collections.emptyList();
            if (var10 != null && this.input.getCursorPosition() == this.input.getValue().length()) {
               List var12 = var10.getContext().getNodes();
               if (!var10.getContext().getNodes().isEmpty()) {
                  var11 = this.getStrings(((ParsedCommandNode)var12.get(var12.size() - 1)).getNode().getChildren());
               }
            }

            if (var6 != null) {
               String var14 = ((Suggestion)var9.getList().get(var6.getSelection())).apply(var6.getTypedText());
               var8 = var8 + (var14.startsWith(this.input.getValue()) ? var14.substring(this.input.getValue().length()) : "");
            }

            if (this.input.getCursorPosition() > 0 && this.input.getValue().charAt(this.input.getCursorPosition() - 1) != ' ' || !var8.isEmpty()) {
               var8 = var8 + " ";
            }

            if (var10 != null && var10.getReader().canRead() && (var10.getReader().peek() != ' ' || var6 != null) && !var11.isEmpty()) {
               var11.remove(0);
            }

            var8 = var8 + String.join(" ", var11);
            this.input.setSuggestion(var8);
         }
      }
   }

    public void filesDragged(List<Path> paths) {
      Class_0227.method_2(this, paths);
   }

   private List<String> getStrings(Collection<CommandNode<SharedSuggestionProvider>> var1) {
      ArrayList var2 = new ArrayList();

      while (true) {
         CommandNode var3 = null;
         int var4 = 0;
         StringBuilder var5 = new StringBuilder("<");

         for (CommandNode var7 : var1) {
            var4++;
            var5.append(var7.getName());
            if (var4 != var1.size()) {
               var5.append(", ");
            } else {
               var3 = var7;
            }
         }

         var5.append(">");
         if (!var1.isEmpty()) {
            var2.add(var5.toString());
         }

         if (var1.size() != 1 || var3 == null) {
            return var2;
         }

         var1 = var3.getChildren();
      }
   }
}
