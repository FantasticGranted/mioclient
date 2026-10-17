package me.mioclient.module.movement;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import me.mioclient.enum_.FakeLagMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.DisconnectEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.phys.AABB;
import me.mioclient.nick.Settings;

public class FakeLagModule extends Module {
   public final Queue<Packet<?>> field_3037 = new ArrayDeque<>();
   public Setting<FakeLagMode> mode;
   public Setting<Integer> timeout;
   public Setting<Boolean> packets;
   public Setting<Boolean> movement;
   public Setting<Boolean> interact;
   public Setting<Boolean> swing;
   public Setting<Boolean> swap;
   public Setting<Boolean> interactEntity;
   public Setting<Boolean> digging;
   public Setting<Boolean> all;
   public Setting<Boolean> autoDisable;
   public Setting<Float> disableTime;
   public Setting<Boolean> render;
   public Setting<Color> fill;
   public Setting<Color> outline;
   public Setting<Float> lineWidth;
   public final Timer field_3054;
   public final Timer field_3055;
   public AABB field_1887;

   public FakeLagModule() {
      super("FakeLag", "Cancels movement packets until toggled off.", Category.MOVEMENT);
      Settings.initialize(this);
      this.field_3054 = new Timer();
      this.field_3055 = new Timer();
   }

   @Override
   public void onEnable() {
      this.field_3037.clear();
      if (this.method_535()) {
         this.disable();
      } else {
         this.field_1887 = mc.player.getBoundingBox();
         this.field_3054.reset();
         this.field_3055.reset();
      }
   }

   @Override
   public void onDisable() {
      if (!this.method_535()) {
         this.method_159();
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (this.mode.getValue() == FakeLagMode.PULSE && this.field_3055.method_9((long)this.timeout.getValue().intValue())) {
         this.method_159();
         this.field_3055.reset();
      }

      if (this.autoDisable.getValue() && this.field_3054.method_2((double)this.disableTime.getValue().floatValue(), TimeUnit.SECONDS)) {
         this.method_68();
      }
   }

   @Subscribe(
      method_800 = -200
   )
   public void method_2(PacketSendEvent var1) {
      if (this.method_5(var1.method_127()) && !var1.method_464()) {
         this.field_3037.add(var1.method_127());
         var1.method_463();
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundPlayerPositionPacket && this.mode.getValue() == FakeLagMode.PULSE) {
         this.field_3055.setTime(-1L);
      }
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      if (this.render.getValue()) {
         BoxRenderer.method_5(var1.method_10(), this.field_1887, this.fill.getValue());
         BoxRenderer.method_2(var1.method_10(), this.field_1887, this.outline.getValue(), this.lineWidth.getValue());
      }
   }

   @Subscribe
   public void method_2(DisconnectEvent var1) {
      this.disable();
   }

   public boolean method_5(Packet<?> var1) {
      return var1 instanceof ServerboundMovePlayerPacket && this.movement.getValue()
         || var1 instanceof ServerboundUseItemOnPacket && this.interact.getValue()
         || var1 instanceof ServerboundInteractPacket && this.interactEntity.getValue()
           || var1 instanceof net.minecraft.network.protocol.game.ServerboundAttackPacket && this.interactEntity.getValue()
         || var1 instanceof ServerboundPlayerActionPacket && this.digging.getValue()
         || var1 instanceof ServerboundSwingPacket && this.swing.getValue()
         || var1 instanceof ServerboundSetCarriedItemPacket && this.swap.getValue()
         || var1 instanceof ServerboundPongPacket
         || this.all.getValue();
   }

   public void method_159() {
      while (!this.field_3037.isEmpty()) {
         Packet var1 = this.field_3037.poll();
         PacketSender.method_9(var1);
      }

      this.field_1887 = mc.player.getBoundingBox();
   }
}
