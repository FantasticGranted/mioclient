package me.mioclient.record;

import me.mioclient.enum_.BindType;
import me.mioclient.internal.KeybindUtil;

/** An immutable key binding: key code, {@link me.mioclient.enum_.BindType}, and a mouse-button flag (UNBOUND = no key). */
public final class Keybind {
   public final int key;
   public final BindType type;
   public final boolean mouse;
   public static final Keybind UNBOUND = new Keybind(-1, BindType.TOGGLE, false);

   public Keybind(int var1, BindType var2, boolean var3) {
      super();
      this.key = var1;
      this.type = var2;
      this.mouse = var3;
   }

   public Keybind method_9(int var1) {
      return new Keybind(var1, this.type, this.mouse);
   }

   public Keybind method_2(BindType var1) {
      return new Keybind(this.key, var1, this.mouse);
   }

   public Keybind method_9(boolean var1) {
      return new Keybind(this.key, this.type, var1);
   }

   public boolean method_29() {
      return this.key < 0;
   }

   public String method_4() {
      return KeybindUtil.method_2(this);
   }

   public int method_38() {
      return this.key;
   }

   public BindType method_78() {
      return this.type;
   }

   public boolean method_39() {
      return this.mouse;
   }
}
