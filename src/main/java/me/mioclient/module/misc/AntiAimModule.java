package me.mioclient.module.misc;

import me.mioclient.Hub;
import me.mioclient.enum_.AntiAimLook;
import me.mioclient.enum_.AntiAimYaw;
import me.mioclient.enum_.AntiAimPitch;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.RaycastUtil;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class AntiAimModule extends Module {
   public Setting<Boolean> yaw;
   public Setting<AntiAimYaw> yaw2;
   public Setting<Float> speed;
   public Setting<Float> static_;
   public Setting<Float> flip;
   public Setting<Float> jitterRange;
   public Setting<Float> base;
   public Setting<Boolean> pitch;
   public Setting<AntiAimPitch> pitch2;
   public Setting<Float> static_2;
   public Setting<Boolean> enderman;
   public Setting<AntiAimLook> look;
   public boolean field_2315;
   public float field_2582;
   public int field_2583;

   public AntiAimModule() {
      super("AntiAim", "Sets your rotations server-side.", Category.MISC);
      Settings.initialize(this);
      this.yaw2.method_31("YawMode");
      this.pitch2.method_31("PitchMode");
   }

   @Subscribe(
      method_800 = -100
   )
   public void method_29(Event_7 var1) {
      this.field_2315 = false;
      if (Hub.field_2598.method_510() == null) {
         if (this.field_2583 > 0) {
            this.field_2583--;
         } else {
            float var2 = var1.method_500();
            float var3 = var1.method_501();
            if (this.yaw.getValue()) {
               var2 = this.yaw2.getValue().method_9(this, mc.player.getYRot());
               this.method_746();
            }

            if (this.pitch.getValue()) {
               var3 = this.pitch2.getValue().method_2(this, mc.player.getXRot());
               this.method_746();
            }

            if (this.enderman.getValue()) {
               EnderMan var4 = this.method_744();
               if (var4 != null) {
                  if (this.look.getValue() == AntiAimLook.AVOID) {
                     var3 = 9e+01F;
                     this.method_746();
                  }

                  if (this.look.getValue() == AntiAimLook.STARE) {
                     float[] var5 = RotationManager.method_78(var4.getEyePosition());
                     var2 = var5[0];
                     var3 = var5[1];
                     this.method_746();
                  }
               }
            }

            if (this.field_2315) {
               Hub.field_2598.method_2(new float[]{var2, var3}, -999, true);
            }
         }
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      Item var2 = mc.player.getMainHandItem().getItem();
      boolean var3 = var2 instanceof ProjectileWeaponItem || var2 instanceof TridentItem;
      if (var3 && mc.player.isUsingItem()) {
         this.field_2583 = 2;
      }
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundUseItemPacket var2) {
         this.method_4(var2.getHand());
      }

      if (var1.method_127() instanceof ServerboundUseItemOnPacket var4) {
         this.method_4(var4.getHand());
      }
   }

   public EnderMan method_744() {
      for (Entity var2 : mc.level.entitiesForRendering()) {
         if (var2 instanceof EnderMan var3
            && !var3.isCreepy()
            && var3.isAlive()
            && (this.look.getValue() != AntiAimLook.AVOID || this.method_2(var3))
            && RaycastUtil.getId(var2)) {
            return var3;
         }
      }

      return null;
   }

   public boolean method_2(EnderMan var1) {
      Vec3 var2 = mc.player.getViewVector(1F).normalize();
      Vec3 var3 = new Vec3(var1.getX() - mc.player.getX(), var1.getEyeY() - mc.player.getEyeY(), var1.getZ() - mc.player.getZ());
      double var4 = var3.length();
      var3 = var3.normalize();
      double var6 = var2.dot(var3);
      return var6 > 1.0 - 0.025 / var4;
   }

   public void method_4(InteractionHand var1) {
      if (mc.player != null) {
         ItemStack var2 = mc.player.getItemInHand(var1);
         if (!var2.isEmpty()) {
            Item var3 = var2.getItem();
            if (var3 instanceof BlockItem || InteractionUtil.method_2(var3)) {
               this.field_2583 = 2;
            }
         }
      }
   }

   public boolean method_706() {
      return this.field_2315;
   }

   public void method_745() {
      this.field_2583 = 2;
   }

   public void method_746() {
      this.field_2315 = true;
   }
}
