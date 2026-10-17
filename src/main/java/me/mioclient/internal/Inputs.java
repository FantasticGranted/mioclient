package me.mioclient.internal;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.world.entity.player.Input;

public final class Inputs {
   private Inputs() {
   }

   public static ServerboundPlayerInputPacket shift(boolean shift) {
      Input var2 = Minecraft.getInstance().player.input.keyPresses;
      return new ServerboundPlayerInputPacket(
         new Input(var2.forward(), var2.backward(), var2.left(), var2.right(), var2.jump(), shift, var2.sprint())
      );
   }

   public static void setJump(net.minecraft.client.player.ClientInput ci, boolean v) {
      net.minecraft.world.entity.player.Input k = ci.keyPresses;
      ci.keyPresses = new net.minecraft.world.entity.player.Input(k.forward(), k.backward(), k.left(), k.right(), v, k.shift(), k.sprint());
   }

   public static void setForward(net.minecraft.client.player.ClientInput ci, boolean v) {
      net.minecraft.world.entity.player.Input k = ci.keyPresses;
      ci.keyPresses = new net.minecraft.world.entity.player.Input(v, k.backward(), k.left(), k.right(), k.jump(), k.shift(), k.sprint());
   }

   public static void setBackward(net.minecraft.client.player.ClientInput ci, boolean v) {
      net.minecraft.world.entity.player.Input k = ci.keyPresses;
      ci.keyPresses = new net.minecraft.world.entity.player.Input(k.forward(), v, k.left(), k.right(), k.jump(), k.shift(), k.sprint());
   }

   public static void setLeft(net.minecraft.client.player.ClientInput ci, boolean v) {
      net.minecraft.world.entity.player.Input k = ci.keyPresses;
      ci.keyPresses = new net.minecraft.world.entity.player.Input(k.forward(), k.backward(), v, k.right(), k.jump(), k.shift(), k.sprint());
   }

   public static void setRight(net.minecraft.client.player.ClientInput ci, boolean v) {
      net.minecraft.world.entity.player.Input k = ci.keyPresses;
      ci.keyPresses = new net.minecraft.world.entity.player.Input(k.forward(), k.backward(), k.left(), v, k.jump(), k.shift(), k.sprint());
   }

   public static void setShift(net.minecraft.client.player.ClientInput ci, boolean v) {
      net.minecraft.world.entity.player.Input k = ci.keyPresses;
      ci.keyPresses = new net.minecraft.world.entity.player.Input(k.forward(), k.backward(), k.left(), k.right(), k.jump(), v, k.sprint());
   }

   public static void setSprint(net.minecraft.client.player.ClientInput ci, boolean v) {
      net.minecraft.world.entity.player.Input k = ci.keyPresses;
      ci.keyPresses = new net.minecraft.world.entity.player.Input(k.forward(), k.backward(), k.left(), k.right(), k.jump(), k.shift(), v);
   }
}
