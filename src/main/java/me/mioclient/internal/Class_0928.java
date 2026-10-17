package me.mioclient.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.PreType;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.SlotUpdateEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.PacketSendInternalEvent;
import me.mioclient.event.Event_47;
import me.mioclient.event.ServerSlotUpdateEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.module.exploit.XCarryModule;
import me.mioclient.record.TimedSlotUpdate;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.item.ItemStack;

public final class Class_0928 implements MinecraftAccess {
   public static final XCarryModule field_1340 = Hub.field_2595.method_78(XCarryModule.class);
   public final Map<Integer, TimedSlotUpdate> field_2909 = Collections.synchronizedMap(new HashMap<>());
   public final Timer field_2910 = new Timer();
   public volatile boolean field_111;
   public volatile boolean field_2500;
   public volatile boolean field_478;
   public boolean field_2911;
   public int field_682 = -1;

   public Class_0928() {
      super();
      lookups.method_14(this);
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      synchronized (this.field_2909) {
         ArrayList var3 = new ArrayList();
         this.field_2909.forEach((var1x, var2) -> {
            if (System.currentTimeMillis() >= var2.getTime()) {
               var3.add(var1x);
            }
         });

         for (Integer var5 : (Iterable<Integer>)(Iterable<?>)(var3)) {
            TimedSlotUpdate var6 = this.field_2909.remove(var5);
            mc.player.connection.handleContainerSetSlot(var6.getPacket());
         }
      }

      this.field_2911 = false;
      if (!field_1340.isToggled()
         && !(mc.screen instanceof AbstractContainerScreen)
         && mc.player.containerMenu.getCarried().isEmpty()
         && this.field_2910.method_9(50L)
         && this.field_111
         && System.currentTimeMillis() - Hub.field_2602.method_987() <= 2500L) {
         this.close();
      }
   }

   @Subscribe(
      method_800 = -100
   )
   public void method_2(PacketSendEvent var1) {
      if (!var1.method_464()) {
         if (var1.method_127() instanceof ServerboundContainerClickPacket) {
            this.field_111 = true;
            this.field_2910.reset();
            if (!this.field_2500 || !this.field_478) {
               lookups.method_36(new Event_47(PreType.PRE));
            }
         }

         if (var1.method_127() instanceof ServerboundContainerClosePacket || var1.method_127() instanceof ClientboundDisconnectPacket) {
            this.field_111 = false;
         }

         if (var1.method_127() instanceof ServerboundSetCarriedItemPacket var2) {
            if (var2.getSlot() != this.field_682) {
               lookups.method_36(new SlotUpdateEvent(var2.getSlot()));
               this.field_682 = var2.getSlot();
            } else if (RotationManager.method_513()) {
               var1.method_463();
            }
         }

         if (var1.method_127() instanceof ClientboundDisconnectPacket) {
            this.field_682 = -1;
         }
      }
   }

   @Subscribe(
      method_800 = 100
   )
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundContainerClosePacket var2) {
         this.field_111 = false;
      }

      if (var1.method_127() instanceof ClientboundContainerSetContentPacket) {
         this.field_2909.clear();
      }

      if (var1.method_127() instanceof ClientboundContainerSetSlotPacket var4 && RotationManager.method_513()) {
         if (var4.getSlot() == 36 + mc.player.getInventory().selected) {
            return;
         }

         if (var4.getSlot() < 36 || var4.getSlot() > 44) {
            return;
         }

         if (!this.method_2(var4.getItem(), var4.getSlot())) {
            this.field_2909.remove(var4.getSlot());
            return;
         }

         this.field_2909.put(var4.getSlot(), new TimedSlotUpdate(var4, System.currentTimeMillis() + 500L));
         var1.method_463();
      }

      if (var1.method_127() instanceof ClientboundEntityEventPacket var5 && var5.getEventId() == 35 && var5.getEntity(mc.level) == mc.player) {
         this.field_2911 = true;
      }
   }

   @Subscribe
   public void method_2(PacketSendInternalEvent var1) {
      if (var1.method_127() instanceof ServerboundContainerClickPacket && (!this.field_2500 || !this.field_478)) {
         lookups.method_36(new Event_47(PreType.POST));
         this.field_478 = true;
      }
   }

   @Subscribe
   public void method_2(ServerSlotUpdateEvent var1) {
      if (!mc.isLocalServer()) {
         PacketSender.method_14(mc.player.getInventory().selected);
         var1.method_463();
      }
   }

   public boolean method_2(ItemStack var1, int var2) {
      for (int var3 = 0; var3 < 9; var3++) {
         if (var2 - 36 != var3 && ItemStack.matches(mc.player.getInventory().getItem(var3), var1)) {
            return true;
         }
      }

      return false;
   }

   public void close() {
      mc.player.connection.send(new ServerboundContainerClosePacket(mc.player.containerMenu.containerId));
      this.field_111 = false;
   }

   public void method_154(boolean var1) {
      if (this.field_2500 != var1) {
         this.field_2500 = var1;
         this.field_478 = false;
      }
   }

   public boolean method_842() {
      return this.field_2911;
   }

   public int method_843() {
      return this.field_682;
   }
}
