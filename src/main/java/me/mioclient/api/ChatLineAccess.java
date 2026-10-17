package me.mioclient.api;

import me.mioclient.internal.ToggleAnimation;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.util.FormattedCharSequence;

/** Mixin accessor for a chat line's signature, content, progress, and add time. */
public interface ChatLineAccess {
   MessageSignature getSignature();

   void setSignature(MessageSignature var1);

   FormattedCharSequence getContent();

   void setContent(FormattedCharSequence var1);

   ToggleAnimation getProgress();

   long mio$getAddTime();

   void mio$setAddTime(long var1);
}
