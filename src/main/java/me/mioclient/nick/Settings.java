package me.mioclient.nick;

import me.mioclient.internal.MioSettings;

/**
 * In-source stand-in for the {@code me.mioclient.nick.Settings} class that used to live in the
 * loader jar ({@code libs/mio-loader-deobf.jar}). The decompiled module sources call
 * {@code Settings.initialize(this)} from their constructors to have their
 * {@code Setting<?>} fields reflectively created and registered.
 *
 * <p>Providing the class here lets the project build without the loader jar on the
 * classpath: the call sites and their {@code import me.mioclient.nick.Settings;} lines are left
 * exactly as decompiled, and the real work is delegated to
 * {@link me.mioclient.internal.MioSettings}.
 */
public final class Settings {
   private Settings() {
      super();
   }

   public static void initialize(Object module) {
      MioSettings.initialize(module);
   }
}
