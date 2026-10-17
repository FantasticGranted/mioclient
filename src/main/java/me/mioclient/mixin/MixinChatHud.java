package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Consumer;
import me.mioclient.Hub;
import me.mioclient.api.ChatLineAccess;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.api.VisibleLinesAccess;
import me.mioclient.enum_.Orientation;
import me.mioclient.event.ChatMessageEvent;
import me.mioclient.internal.MioGlyphRenderer;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.CommandManager;
import me.mioclient.internal.ChatUtil;
import me.mioclient.module.abstract_.AbstractModule_14;
import me.mioclient.module.client.FontsModule;
import me.mioclient.module.misc.BetterChatModule;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.ChatComponent.ChatGraphicsAccess;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.client.multiplayer.chat.GuiMessage.Line;
import net.minecraft.client.multiplayer.chat.GuiMessageSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ChatComponent.class})
public abstract class MixinChatHud implements MinecraftAccess, VisibleLinesAccess {
   private static final NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   private static final BetterChatModule betterchat = Hub.field_2595.method_78(BetterChatModule.class);
   private static final AbstractModule_14 chathud = Hub.field_2595.method_78(AbstractModule_14.class);
   private static final FontsModule fonts = Hub.field_2595.method_78(FontsModule.class);
   @Shadow
   @Final
   private List<Line> trimmedMessages;
   @Unique
   private MessageSignature last;
   @Unique
   private static boolean skip;

   public MixinChatHud() {
      super();
   }

   @Shadow
   protected abstract void logChatMessage(GuiMessage var1);

   @Shadow
   protected abstract void addMessageToDisplayQueue(GuiMessage var1);

   @Shadow
   protected abstract void addMessageToQueue(GuiMessage var1);

   @Shadow
   protected abstract void addMessage(Component var1, MessageSignature var2, GuiMessageSource var3, GuiMessageTag var4);

   @Inject(
      method = {"logChatMessage"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void logChatMessageHook(GuiMessage var1, CallbackInfo var2) {
      if (var1.tag() == ChatUtil.field_3910) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"addMessageToDisplayQueue"},
      at = {@At("HEAD")}
   )
   private void addMessage(GuiMessage var1, CallbackInfo var2) {
      BetterChatModule.field_3921 = true;
      MessageSignature var3 = var1.signature();
      this.last = var3;
      if (var3 != null && var3.asByteBuffer().getInt() < 0) {
         try {
            this.trimmedMessages.removeIf(var1x -> var1x != null && ((ChatLineAccess)(Object)var1x).getSignature() != null && ((ChatLineAccess)(Object)var1x).getSignature().equals(var3));
         } catch (ConcurrentModificationException var5) {
         }
      }
   }

   @Inject(
      method = {"addMessageToDisplayQueue"},
      at = {@At("RETURN")}
   )
   private void noracismcattyn(GuiMessage var1, CallbackInfo var2) {
      BetterChatModule.field_3921 = false;
   }

   @Redirect(
      method = {"addMessageToDisplayQueue"},
      at = @At(
         value = "INVOKE",
         target = "Ljava/util/List;addFirst(Ljava/lang/Object;)V",
         ordinal = 0
      )
   )
   private <E> void visibleInitHook(List<E> var1, E var3) {
      ((ChatLineAccess)var3).setSignature(this.last);
      Line var4 = (Line)var3;
      ChatMessageEvent var5 = new ChatMessageEvent(var4);
      MinecraftAccess.lookups.method_36(var5);
      var1.addFirst(var3);
   }

   @WrapOperation(
      method = {"extractRenderState(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;updatePose(Ljava/util/function/Consumer;)V"
      )}
   )
   private void renderPreHook(ChatGraphicsAccess var1, Consumer<Matrix3x2f> var2, Operation<Void> var3) {
      float var4 = 0.0F;
      float var5 = 0.0F;
      if (chathud.isToggled()) {
         var4 += chathud.method_648();
         var5 += chathud.method_647();
      }

      if (betterchat.method_1092() && betterchat.type.getValue() == Orientation.BOUNCE) {
         var5 += (betterchat.field_3948.method_605() - 1.0F) * 8.0F;
      }

      if (var4 != 0.0F || var5 != 0.0F) {
         final float var6 = var4;
         final float var7 = var5;
         var3.call(
            var1,
            (Consumer<Matrix3x2f>)(var8 -> {
               var2.accept(var8);
               var8.translate(var6, var7);
            })
         );
      } else {
         var3.call(var1, var2);
      }
   }

   @Inject(
      method = {"extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;IIILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;Z)V"},
      at = {@At("TAIL")}
   )
    private void render(GuiGraphicsExtractor var1, net.minecraft.client.gui.Font var2, int var3, int var4, int var5, ChatComponent.DisplayMode var6, boolean var7, CallbackInfo var8) {
       FontRenderer.field_3143.method_474();
    }

   @WrapOperation(
      method = {"extractRenderState(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;handleMessage(IFLnet/minecraft/util/FormattedCharSequence;)Z"
      )}
   )
   private boolean queueTextHook(ChatGraphicsAccess var1, int var2, float var3, FormattedCharSequence var4, Operation<Boolean> var5) {
      if (!var1.getClass().getSimpleName().contains("Clickable")) {
         MioGlyphRenderer var6 = FontRenderer.field_3143.method_914();
         if (var6 != null && fonts.isToggled() && fonts.chat.getValue()) {
            Matrix3x2fStack[] var7 = new Matrix3x2fStack[1];
            var1.updatePose(var8 -> {
               if (var8 instanceof Matrix3x2fStack var9) {
                  var7[0] = var9;
               }
            });
            if (var7[0] != null) {
               int var8 = (int)(Math.min(var3, 1.0F) * 255.0F) << 24 | 16777215;
               var6.method_2(var7[0], var4, 0.0F, (float)var2, var8, true);
               return false;
            }
         }
      }

      return var5.call(var1, var2, var3, var4);
   }

   @Inject(
      method = {"addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/multiplayer/chat/GuiMessageSource;Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/ChatComponent;logChatMessage(Lnet/minecraft/client/multiplayer/chat/GuiMessage;)V",
         shift = Shift.BEFORE
      )},
      cancellable = true
   )
   private void addMessageHook(Component var1, MessageSignature var2, GuiMessageSource var3, GuiMessageTag var4, CallbackInfo var5, @Local GuiMessage var6) {
      if (!skip) {
         ChatMessageEvent var7 = new ChatMessageEvent(var4, var2, var1);
         MinecraftAccess.lookups.method_36(var7);
         var1 = var7.method_1033();
         var2 = var7.getSignature();
         var5.cancel();
         if (!var7.method_464()) {
            if (!var1.equals(var1)) {
               skip = true;
               this.addMessage(var1, var2, var3, var4);
               skip = false;
               return;
            }

            this.logChatMessage(var6);
            this.addMessageToDisplayQueue(var6);
            this.addMessageToQueue(var6);
         }
      }
   }

   @ModifyExpressionValue(
      method = {"addMessageToDisplayQueue"},
      at = {@At(
         value = "INVOKE",
         target = "Ljava/util/List;size()I",
         ordinal = 2
      )}
   )
   public int chatHistory(int var1) {
      return betterchat.isToggled() && betterchat.longChatHistory.getValue() ? 1 : var1;
   }

   @ModifyExpressionValue(
      method = {"addMessageToQueue"},
      at = {@At(
         value = "INVOKE",
         target = "Ljava/util/List;size()I",
         ordinal = 0
      )}
   )
   public int chatHistory2(int var1) {
      return betterchat.isToggled() && betterchat.longChatHistory.getValue() ? 1 : var1;
   }

   @Inject(
      method = {"clearMessages"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void clearHook(boolean var1, CallbackInfo var2) {
      if (betterchat.isToggled() && betterchat.noReset.getValue() && !BetterChatModule.field_3922) {
         var2.cancel();
      }
   }

   @ModifyExpressionValue(
      method = {"addRecentChat"},
      at = {@At(
         value = "INVOKE",
         target = "Ljava/lang/String;startsWith(Ljava/lang/String;)Z"
      )}
   )
   private boolean addToMessageHistory(boolean var1, @Local(argsOnly = true) String var2) {
      return var1 || var2.startsWith(CommandManager.method_927());
   }

   @Override
   public List<Line> getVisible() {
      return this.trimmedMessages;
   }
}
