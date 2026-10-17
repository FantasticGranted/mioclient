package me.mioclient.event;

import me.mioclient.enum_.PreType;
import me.mioclient.internal.Event;
import net.minecraft.client.multiplayer.chat.GuiMessage.Line;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;

/** Fired when a chat message is added to the chat hud. */
public class ChatMessageEvent extends Event {
   public final PreType field_3643;
   public final GuiMessageTag field_3644;
   public MessageSignature signature;
   public Component field_3645;
   public Line field_3646;
   public String field_3647;

   public ChatMessageEvent(GuiMessageTag var1, MessageSignature var2, Component var3) {
      super();
      this.field_3644 = var1;
      this.signature = var2;
      this.field_3645 = var3;
      this.field_3643 = PreType.PRE;
      this.field_3647 = var3 == null ? null : var3.getString();
   }

   public ChatMessageEvent(Line var1) {
      super();
      this.field_3646 = var1;
      this.field_3643 = PreType.POST;
      this.field_3644 = null;
   }

   public MessageSignature getSignature() {
      return this.signature;
   }

   public void setSignature(MessageSignature var1) {
      this.signature = var1;
   }

   public Component method_1033() {
      return this.field_3645;
   }

   public void method_9(Component var1) {
      this.field_3645 = var1;
   }

   public Line method_1034() {
      return this.field_3646;
   }

   public void method_9(Line var1) {
      this.field_3646 = var1;
   }

   public PreType method_213() {
      return this.field_3643;
   }

   public String method_1035() {
      return this.field_3647;
   }

   public GuiMessageTag method_1036() {
      return this.field_3644;
   }
}
