package me.mioclient.module.combat;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import me.mioclient.Hub;
import me.mioclient.enum_.ArmorSlot;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.EnchantmentUtil;
import me.mioclient.internal.ClickGui;
import me.mioclient.internal.PacketSender;
import me.mioclient.internal.Class_1264;
import me.mioclient.internal.ExplosionDamage;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import me.mioclient.nick.Settings;

public class AutoArmorModule extends Module {
   public Setting<Integer> delay;
   public Setting<Boolean> preferences;
   public Setting<Boolean> bindingCurse;
   public Setting<Boolean> blast;
   public Setting<Boolean> thorns;
   public Setting<Boolean> elytra;
   public Setting<Boolean> turtleShell;
   public Setting<Boolean> noHelmet;
   public Setting<Boolean> onlySafe;
   public Setting<Boolean> safe;
   public Setting<Integer> safeAmount;
   public Setting<Boolean> inRange;
   public Setting<Float> range;
   public Setting<Boolean> allowFriends;
   public final Timer field_2936;
   public final Timer field_2937;
   public final Timer field_2938;
   public Class_1264 field_2939;
   public boolean field_2940;

   public AutoArmorModule() {
      super("AutoArmor", "Equips the best armor in your inventory for you.", Category.COMBAT);
      Settings.initialize(this);
      this.field_2936 = new Timer();
      this.field_2937 = new Timer();
      this.field_2938 = new Timer();
      this.field_2939 = null;
      Runnable var1 = () -> this.field_2936.setTime(-1L);
      this.blast.method_9(var1);
      this.elytra.method_9(var1);
      this.turtleShell.method_9(var1);
   }

   @Override
   public void onEnable() {
      this.field_2939 = null;
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (this.field_2940 != Hub.field_2605.method_224()) {
         this.field_2938.reset();
      }

      this.field_2940 = Hub.field_2605.method_224();
      if (this.method_2(mc.screen)) {
         if (this.field_2936.method_9((long)this.delay.getValue().intValue())) {
            if (mc.player.isFallFlying()) {
               this.field_2937.reset();
            }

            if (PlayerUtil.method_4(mc.player)
               && !mc.player.isFallFlying()
               && !EntityUtil.method_2(mc.player)
               && !this.field_2936.method_9(300L)
               && !this.field_2937.method_9(500L)) {
               PacketSender.method_1099();
               mc.player.startFallFlying();
            }

            boolean var2 = false;
            if (this.inRange.getValue()) {
               for (AbstractClientPlayer var4 : mc.level.players()) {
                  if (var4 != mc.player
                     && var4.distanceTo(mc.player) <= this.range.getValue()
                     && (!Hub.field_2603.method_1009(var4.getGameProfile().name()) || !this.allowFriends.getValue())) {
                     var2 = true;
                  }
               }
            } else {
               var2 = true;
            }

            if (var2) {
               if (this.field_2939 != null && !this.field_2939.method_497()) {
                  this.field_2939.method_273();
                  this.field_2936.reset();
               } else {
                  for (ArmorSlot var6 : ArmorSlot.values()) {
                     ItemStack var7 = mc.player.getInventory().getItem(var6.method_34());
                     if (!EnchantmentUtil.method_9(Enchantments.BINDING_CURSE, var7)) {
                        double var8 = this.method_2(var7, var6);
                        int var10 = var6.method_34();

                        for (int var11 = 0; var11 < 36; var11++) {
                           ItemStack var12 = mc.player.getInventory().getItem(var11);
                           double var13 = this.method_2(var12, var6);
                           if (var13 > var8) {
                              var8 = var13;
                              var10 = var11;
                           }
                        }

                        if (var10 != var6.method_34() && var8 != -1.0) {
                           this.field_2939 = new Class_1264(this, var6, var10);
                           break;
                        }
                     }
                  }

                  if (this.method_848() && (this.field_2939 == null || this.field_2939.method_497())) {
                     ItemStack var16 = mc.player.getInventory().getItem(ArmorSlot.HELMET.method_34());
                     if (!var16.isEmpty() && !EnchantmentUtil.method_9(Enchantments.BINDING_CURSE, var16)) {
                        this.field_2939 = new Class_1264(this, ArmorSlot.HELMET, -1);
                        this.field_2939.method_222(true);
                     }
                  }
               }
            }
         }
      }
   }

   public double method_2(ItemStack var1, ArmorSlot var2) {
      float var3 = InventoryUtil.method_29(var1);
      if (this.safe.getValue() && var3 < (float)this.safeAmount.getValue().intValue()) {
         return -1.0;
      } else if (!this.bindingCurse.getValue() && EnchantmentUtil.method_9(Enchantments.BINDING_CURSE, var1)) {
         return -1.0;
      } else {
         net.minecraft.world.item.equipment.Equippable var4 = var1.get(net.minecraft.core.component.DataComponents.EQUIPPABLE);
         if (var4 != null && var4.slot().isArmor() && var4.slot().getIndex() == var2.getArmorIndex()) {
            double var12 = 0.0;
            if (var4.allowedEntities().isPresent()) {
               return -1.0;
            }

            if (this.method_848() && var4.slot() == EquipmentSlot.HEAD) {
               return -1.0;
            }

            if (this.turtleShell.getValue() && var1.is(Items.TURTLE_HELMET)) {
               var12 = 9999.0;
            }

            var12 += InventoryUtil.armorDefense(var1, var4.slot()) * 10.0;
            var12 += InventoryUtil.armorToughness(var1, var4.slot()) * 10.0;
            if (var1.isEnchanted()) {
               ResourceKey var7 = var2 != ArmorSlot.LEGGINGS && !this.blast.getValue() ? Enchantments.PROTECTION : Enchantments.BLAST_PROTECTION;

               for (Entry var9 : var1.getEnchantments().entrySet()) {
                  if (!((Holder)var9.getKey()).is(EnchantmentTags.CURSE)) {
                     if (var9.getKey() == var7) {
                        var12 += (double)(var9.getValue() * 3);
                     } else {
                        var12 += (double)var9.getValue().intValue() * 0.1;
                     }

                     if (((Holder)var9.getKey()).is(Enchantments.THORNS) && !this.thorns.getValue()) {
                        var12 -= (double)var9.getValue().intValue();
                     }
                  }
               }
            }

            return var12;
         }

         if (var1.is(Items.ELYTRA) && var2 == ArmorSlot.CHESTPLATE) {
            if (var1.getDamageValue() < var1.getMaxDamage() - 1 && ExplosionDamage.method_5(var1) > 1) {
               double var10 = 1.0;
               if (this.elytra.getValue()) {
                  var10 *= 9999.0;
               }

               if (var1.isEnchanted()) {
                  var10 += (double)EnchantmentUtil.method_2(Enchantments.UNBREAKING, var1);
                  var10 += (double)EnchantmentUtil.method_2(Enchantments.MENDING, var1);
               }

               return var10;
            } else {
               return -1.0;
            }
         } else {
            return -1.0;
         }
      }
   }

   public boolean method_2(Screen var1) {
      if (var1 == null) {
         return true;
      } else if (var1 instanceof ClickGui) {
         return true;
      } else {
         return var1 instanceof AbstractContainerScreen ? var1 instanceof InventoryScreen : true;
      }
   }

   public boolean method_848() {
      return !this.noHelmet.getValue() ? false : !this.onlySafe.getValue() || this.field_2940 && this.field_2938.method_9(500L);
   }
}
