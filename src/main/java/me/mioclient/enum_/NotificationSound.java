package me.mioclient.enum_;

import me.mioclient.internal.SoundId;

/** Selectable notification sound theme. */
public enum NotificationSound {
   KABAN("kaban"),
   NEVERLOSE("neverlose"),
   CSS("css"),
   COD("cod"),
   QUAKE("quake"),
   TOOLBOX("toolbox"),
   WARNING("warning"),
   STEAM("steam"),
   WHATSAPP("whatsapp"),
   VK("vk"),
   ICQ("icq"),
   STALKER("stalker"),
   HOVER("hover"),
   CLICK("click"),
   RCLICK("rclick"),
   BODYSPLAT("bodysplat");

   public final SoundId displayName;

    NotificationSound(String var3) {
      this.displayName = new SoundId(var3);
   }

   public SoundId method_258() {
      return this.displayName;
   }
}
