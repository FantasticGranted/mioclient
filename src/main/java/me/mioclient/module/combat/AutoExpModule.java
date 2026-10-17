package me.mioclient.module.combat;

import me.mioclient.Hub;
import me.mioclient.enum_.AutoExpMode;
import me.mioclient.enum_.PreType;
import me.mioclient.event.Event_19;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BoxUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.internal.ExplosionDamage;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.misc.AntiAimModule;
import me.mioclient.setting.Setting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import me.mioclient.nick.Settings;

public class AutoExpModule extends Module {
   public Setting<AutoExpMode> mode;
   public Setting<Integer> delay;
   public Setting<Integer> frequency;
   public Setting<Boolean> rotate;
   public Setting<Boolean> air;
   public Setting<Boolean> stop;
   public Setting<Integer> stopAt;
   public Setting<Boolean> autoDisable;
   public Setting<Boolean> tools;
   public static AntiAimModule field_3428 = Hub.field_2595.method_78(AntiAimModule.class);
   public final Timer field_3429;
   public int field_3430;
   public boolean field_1702;

   public AutoExpModule() {
      super("AutoExp", "Mends your armor for you.", Category.COMBAT);
      Settings.initialize(this);
      this.field_3429 = new Timer();
      this.field_1702 = false;
   }

   @Override
   public void onEnable() {
      if (!this.method_535()) {
         if (this.rotate.getValue()) {
            this.field_3429.reset();
         }

         if (field_3428.isToggled() && !this.field_1702) {
            field_3428.method_745();
         }

         this.field_3430 = mc.player.getInventory().selected;
      }
   }

   @Override
   public void onDisable() {
      if (!this.method_535()) {
         if (mc.player.getInventory().selected != this.field_3430 && this.mode.getValue() == AutoExpMode.NORMAL) {
            InventoryUtil.method_16(this.field_3430);
         }
      }
   }

   @Override
   public String getInfo() {
      if (this.field_1702) {
         return "Stopped";
      } else {
         int var1 = mc.player
            .getInventory()
            .items
            .stream()
            .filter(var0 -> var0.getItem() == Items.EXPERIENCE_BOTTLE)
            .mapToInt(ItemStack::getCount)
            .sum();
         return String.valueOf(var1);
      }
   }

   @Subscribe(
      method_800 = 250
   )
   public void method_9(Event_19 var1) {
      if (!this.method_404()) {
         if (var1.method_213() == PreType.PRE) {
            AABB var2 = Hub.field_2612.method_2(mc.player, 1);
            float[] var3 = RotationManager.method_78(BoxUtil.method_2(var2));
            if (!this.field_1702 && this.rotate.getValue()) {
               Hub.field_2598.method_2(var3, 4, true);
            }

            boolean var4 = mc.player.isHolding(Items.EXPERIENCE_BOTTLE);
            int var5 = InventoryUtil.method_5(Items.EXPERIENCE_BOTTLE);
            if (this.stop.getValue()) {
               boolean var6 = true;

               for (ItemStack var8 : java.util.List.of(mc.player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.FEET), mc.player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.LEGS), mc.player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST), mc.player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD))) {
                  if (this.method_374(var8)) {
                     var6 = false;
                     break;
                  }
               }

               if ((this.method_374(mc.player.getMainHandItem()) || this.method_374(mc.player.getOffhandItem()))
                  && this.tools.getValue()) {
                  var6 = false;
               }

               if (var6 || var5 == -1) {
                  this.field_1702 = true;
                  if (this.autoDisable.getValue()) {
                     this.disable();
                  }

                  return;
               }
            }

            if ((var5 != -1 || var4) && this.field_3429.method_9((long)(this.delay.getValue() * 50))) {
               InteractionHand var9 = mc.player.getOffhandItem().is(Items.EXPERIENCE_BOTTLE) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
               this.field_1702 = false;
               int var10 = mc.player.getInventory().selected;
               if (mc.player.getMainHandItem().getItem() != Items.EXPERIENCE_BOTTLE && var9 == InteractionHand.MAIN_HAND) {
                  InventoryUtil.method_16(var5);
               }

               for (int var11 = 0; var11 < this.frequency.getValue(); var11++) {
                  if (this.rotate.getValue()) {
                     PacketSender.method_2(var9, var3[0], var3[1]);
                  } else {
                     PacketSender.method_2(var9);
                  }
               }

               if (this.mode.getValue() == AutoExpMode.SILENT && mc.player.getInventory().selected != var10) {
                  InventoryUtil.method_16(var10);
               }

               this.field_3429.reset();
            }
         }
      }
   }

   public boolean method_374(ItemStack var1) {
      return var1.isDamageableItem() && !var1.isEmpty() && ExplosionDamage.method_5(var1) < this.stopAt.getValue();
   }

   public boolean method_404() {
      return (
               this.air.getValue()
                  || mc.hitResult.getType() != Type.MISS
                     && (!(mc.hitResult instanceof BlockHitResult var1) || !mc.level.getBlockState(var1.getBlockPos()).canBeReplaced())
            )
            && !OffhandModule.method_639()
         ? mc.player.isFallFlying()
         : true;
   }
}
