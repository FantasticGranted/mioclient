package me.mioclient.mixin;

import me.mioclient.api.ChatLineAccess;
import me.mioclient.internal.ToggleAnimation;
import me.mioclient.module.misc.BetterChatModule;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.multiplayer.chat.GuiMessage.Line;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Line.class})
public class MixinChatHudLine implements ChatLineAccess {
   @Unique
   private ToggleAnimation progress = null;
   @Unique
   private MessageSignature signature;
   @Mutable
   @Final
   @Shadow
   private FormattedCharSequence content;
   @Unique
   private long mio$addTime;

   public MixinChatHudLine() {
      super();
   }

   @Inject(
      method = {"<init>"},
      at = {@At("RETURN")}
   )
   private void onInit(GuiMessage var1, FormattedCharSequence var2, boolean var3, CallbackInfo var4) {
      this.getProgress().method_36(false);
      this.mio$addTime = System.currentTimeMillis();
   }

   @Override
   public MessageSignature getSignature() {
      return this.signature;
   }

   @Override
   public void setSignature(MessageSignature var1) {
      this.signature = var1;
   }

   @Override
   public FormattedCharSequence getContent() {
      return this.content;
   }

   @Override
   public void setContent(FormattedCharSequence var1) {
      this.content = var1;
   }

   @Override
   public ToggleAnimation getProgress() {
      if (this.progress == null) {
         this.progress = new ToggleAnimation(() -> BetterChatModule.method_1093().speed.getValue() * 2.0F, true);
      }

      return this.progress;
   }

   @Override
   public long mio$getAddTime() {
      return this.mio$addTime;
   }

   @Override
   public void mio$setAddTime(long var1) {
      this.mio$addTime = var1;
   }
}
