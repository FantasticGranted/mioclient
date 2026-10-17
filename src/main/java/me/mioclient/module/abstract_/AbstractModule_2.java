package me.mioclient.module.abstract_;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;
import me.mioclient.Hub;
import me.mioclient.enum_.Class_0823;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.MovementInputEvent;
import me.mioclient.event.DisconnectEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.FinishUsingItemEvent;
import me.mioclient.event.CameraEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.FadingBox;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.combat.SelfFillModule;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import net.minecraft.world.phys.Vec2;
import me.mioclient.internal.Inputs;

public class AbstractModule_2 extends Module {
   public Setting<Class_0823> yaw;
   public Setting<Boolean> smart;
   public Setting<Float> range;
   public Setting<Boolean> render;
   public Setting<Color> fill;
   public Setting<Color> outline;
   public Setting<Float> lineWidth;
   public static AbstractModule_17 field_798 = Hub.field_2595.method_78(AbstractModule_17.class);
   public static SelfFillModule field_799 = Hub.field_2595.method_78(SelfFillModule.class);
   public Queue<Packet<?>> field_800;
   public final FadingBox field_801;
   public ClientboundPlayerPositionPacket field_802;
   public boolean field_803;
   public boolean field_804;
   public Vec3 field_805;
   public Vec3 field_806;

   public AbstractModule_2() {
      super(
         "ChorusControl",
         new PlaceholderText().method_2(String.valueOf(ChatFormatting.YELLOW)).method_9("Allows you to pick the position to teleport. \n\u0001Sneak to teleport."),
         Category.EXPLOIT
      );
      Settings.initialize(this);
      this.field_800 = new LinkedList<>();
      this.field_801 = new FadingBox();
   }

   @Override
   public void onToggle() {
      this.field_806 = null;
      this.field_803 = false;
      this.field_804 = false;
      this.field_802 = null;
   }

   @Override
   public void onDisable() {
      if (this.field_803) {
         this.method_293();
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (!this.method_535()) {
         if (this.field_803
            && !PlayerUtil.method_425().equals(this.field_806)
            && !this.field_804
            && mc.player.getEyePosition().distanceTo(new Vec3(this.field_806.x(), this.field_806.y(), this.field_806.z()))
               > 1.0) {
            mc.player.setPos(this.field_806.x(), this.field_806.y(), this.field_806.z());
            this.field_804 = true;
         }

         if (this.field_802 != null && this.field_803) {
            AABB var2 = mc.player.getLocalBoundsForPose(Pose.STANDING);
            AABB var3 = new AABB(0.0, 0.0, 0.0, var2.getXsize(), var2.getYsize(), var2.getZsize())
               .move(this.field_802.change().position().x(), this.field_802.change().position().y(), this.field_802.change().position().z());
            this.field_801.method_29(var3);
            this.field_801.method_1016();
         }

         if (this.field_803) {
            mc.player.setDeltaMovement(0.0, 0.0, 0.0);
            mc.player.input.moveVector = new Vec2(mc.player.input.moveVector.x, (0.0F));
            mc.player.input.moveVector = new Vec2((0.0F), mc.player.input.moveVector.y);
            if (this.field_802 != null) {
               Entity var4 = BlockUtil.method_2(true, false, false, false, false, false, 16F, 16F);
               BlockPos var5 = new BlockPos((int)this.field_802.change().position().x(), (int)this.field_802.change().position().y(), (int)this.field_802.change().position().z());
               if (this.smart.getValue()) {
                  if (var4 == null) {
                     this.method_293();
                     return;
                  }

                  if (var5.getCenter().distanceTo(var4.getBoundingBox().getCenter()) >= (double)this.range.getValue().floatValue()) {
                     this.method_293();
                     return;
                  }
               }

               if (mc.player.isShiftKeyDown() || !mc.player.isAlive()) {
                  this.method_293();
               }
            }
         }
      }
   }

   @Subscribe
   public void method_2(FinishUsingItemEvent var1) {
      if (var1.method_602().getItem() == Items.CHORUS_FRUIT && !mc.player.isShiftKeyDown()) {
         this.field_805 = mc.player.getEyePosition();
         this.field_806 = mc.player.position();
         this.field_804 = false;
         this.field_803 = true;
         Hub.field_2602.method_985().reset();
      }
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if ((var1.method_127() instanceof ServerboundAcceptTeleportationPacket || var1.method_127() instanceof ServerboundMovePlayerPacket)
         && this.field_803
         && !mc.player.isShiftKeyDown()) {
         this.field_800.add(var1.method_127());
         var1.method_463();
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundPlayerPositionPacket var2) {
         this.field_802 = var2;
      }
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      if (this.render.getValue()) {
         this.field_801.method_17(this.lineWidth.getValue());
         this.field_801.method_2(var1.method_10(), this.fill.getValue(), this.outline.getValue(), 315F, true);
      }
   }

   @Subscribe
   public void method_2(CameraEvent var1) {
      if (this.field_803 && this.field_805 != null) {
         var1.method_38(this.field_805);
         var1.method_463();
      }
   }

   @Subscribe
   public void method_2(DisconnectEvent var1) {
      this.field_806 = null;
      this.field_803 = false;
      this.field_804 = false;
      this.field_802 = null;
   }

   @Subscribe
   public void method_2(MovementInputEvent var1) {
      boolean var2 = var1.method_276().keyPresses.shift();
      if (this.field_803) {
         var1.reset();
         Inputs.setShift(var1.method_276(), var2);
      }
   }

   public void method_293() {
      this.field_803 = false;
      if (this.field_802 != null) {
         field_798.field_609 = this.field_802.change().position().y();
         field_799.field_609 = this.field_802.change().position().y();
      }

      this.field_802 = null;

      while (!this.field_800.isEmpty()) {
         mc.player.connection.send(this.field_800.poll());
      }

      Hub.field_2602.method_985().reset();
      Hub.field_2599.method_867().stream().filter(var0 -> var0.chorus.getValue()).forEach(Module::disable);
      mc.player.setYRot(this.yaw.getValue().method_3(this.field_806));
   }
}
