package me.mioclient.module.client;

import java.awt.Color;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;
import me.mioclient.Hub;
import me.mioclient.clickgui.ClickGuiScreen;
import me.mioclient.enum_.BindType;
import me.mioclient.enum_.MsaaLevel;
import me.mioclient.enum_.Season;
import me.mioclient.enum_.CursorShape;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Event_21;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.SoundId;
import me.mioclient.internal.Class_0406;
import me.mioclient.internal.CommandManager;
import me.mioclient.internal.ClickGui;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.record.Keybind;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import me.mioclient.nick.Settings;
import org.lwjgl.glfw.GLFW;

public final class UIModule extends Module {
   public static UIModule field_2843;
   public final AtomicBoolean field_2844 = new AtomicBoolean(false);
   public Setting<Boolean> reset;
   public Setting<String> prefix;
   public Setting<MsaaLevel> mSAA;
   public Setting<Boolean> descriptions;
   public Setting<Integer> delay;
   public Setting<Boolean> constantReset;
   public Setting<Integer> buttonHeight;
   public Setting<Integer> frameWidth;
   public Setting<Integer> modulePadding;
   public Setting<Float> animSpeed;
   public Setting<Float> guiScale;
   public Setting<Boolean> snow;
   public Setting<Boolean> elements;
   public Setting<Boolean> line;
   public Setting<Boolean> binds;
   public Setting<Boolean> gear;
   public Setting<Boolean> windowShadow;
   public Setting<Color> shadowColor;
   public Setting<Float> shadowSize;
   public Setting<Boolean> sounds;
   public Setting<Boolean> hover;
   public Setting<SoundId> sound;
   public Setting<Float> volume;
   public Setting<Boolean> leftClick;
   public Setting<SoundId> sound2;
   public Setting<Float> volume2;
   public Setting<Boolean> rightClick;
   public Setting<SoundId> sound3;
   public Setting<Float> volume3;
   public Setting<Boolean> text;
   public Setting<Boolean> bounce;
   public Setting<Color> textColor;
   public Setting<Color> enabledColor;
   public Setting<Boolean> colors;
   public Setting<Color> color;
   public Setting<Color> bgColor;
   public Setting<Color> bgButton;
   public Setting<Color> bgEnabled;
   public Setting<Color> tint;
   public Setting<Color> gradientColor;

   public UIModule() {
      super("UI", "Displays the client's click gui.", Category.CLIENT);
      Settings.initialize(this);
      lookups.method_5(UIModule.class);
      this.setDrawn(false);
      this.reset.method_9(() -> {
         if (this.reset.getValue()) {
            Hub.method_755().method_997();
            this.reset.method_78(false);
         }
      });
      this.frameWidth.method_9(() -> this.field_2844.set(true));
      this.modifyKeybind(var0 -> var0.method_9(344));
      field_2843 = this;
      this.sound.method_31("HoverSound");
      this.volume.method_31("HoverVolume");
      this.sound2.method_31("LeftClickSound");
      this.volume2.method_31("LeftClickVolume");
      this.sound3.method_31("RightClickSound");
      this.volume3.method_31("RightClickVolume");
      if (!Season.WINTER.method_29(LocalDate.now().getMonthValue())) {
         this.snow.method_78(false);
         this.unregister(this.snow);
      }

      this.prefix.method_9(() -> {
         if (!this.prefix.getValue().equals(CommandManager.method_927())) {
            CommandManager.method_270(this.prefix.getValue());
         }
      });
      this.prefix.method_7(true);
   }

   @Override
   public void onEnable() {
      if (this.method_535()) return;
      if (mc.screen instanceof ClickGuiScreen) return;
      Runnable open = () -> mc.setScreen(ClickGuiScreen.instance());
      if (mc.screen instanceof ChatScreen) {
         Hub.field_2619.method_2(open, 0);
      } else {
         open.run();
      }
   }

   @Override
   public void onDisable() {
      if (mc.screen instanceof ClickGuiScreen screen) {
         screen.onClose();
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (!(mc.screen instanceof ClickGui) && !(mc.screen instanceof ClickGuiScreen)) {
         this.method_68();
      }
   }

   @Subscribe
   public void method_2(Event_21 var1) {
      if (GLFW.glfwGetMouseButton(mc.getWindow().handle(), 0) != 1 && this.field_2844.get()) {
         Hub.method_755().method_997();
         this.reset.method_78(false);
         this.field_2844.set(false);
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundDisconnectPacket) {
         this.disable();
      }
   }

   @Subscribe
   public static void method_9(Event_21 var0) {
      if (!(mc.screen instanceof ClickGui) && !(mc.screen instanceof ClickGuiScreen)) {
         CursorShape.STANDARD.method_580();
      }
   }

   @Override
   public Keybind getKeybind() {
      Keybind var1 = super.getKeybind();
      if (var1.method_78() != BindType.TOGGLE) {
         var1 = var1.method_2(BindType.TOGGLE);
         this.setKeybind(var1);
      }

      return var1;
   }
}
