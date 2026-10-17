package me.mioclient.module.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import me.mioclient.Hub;
import me.mioclient.api.DurationRatioAccess;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.StopUsingItemEvent;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.PotionUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.alchemy.Potion;
import me.mioclient.nick.Settings;

public class ArrowsModule extends Module {
   public static final AtomicBoolean field_653 = new AtomicBoolean();
   public Setting<Boolean> autoShoot;
   public Setting<Float> fov;
   public Setting<Integer> assumeDuration;
   public final Map<MobEffect, Long> field_657;
   public final Timer field_658;
   public int field_659;
   public Potion field_660;

   public ArrowsModule() {
      super("Arrows", "Will swap between effect arrows in your inventory.", Category.COMBAT, "projectiles");
      Settings.initialize(this);
      this.field_657 = new HashMap<>();
      this.field_658 = new Timer();
   }

   @Override
   public void onEnable() {
      if (field_653.get()) {
         this.disable();
      }
   }

   @Subscribe
   public void method_2(StopUsingItemEvent var1) {
      Player var2 = this.method_268();
      InteractionHand var3 = mc.player.getUsedItemHand();
      if (var3 != null && mc.player.getItemInHand(var3).getItem() instanceof ProjectileWeaponItem) {
         if (var2 == null || this.field_659 != -1 && this.method_2(mc.player.getInventory().getItem(this.field_659), var2) > 0) {
            if (this.field_658.method_9(50L)) {
               for (int var8 = 0; var8 < mc.player.getInventory().getContainerSize(); var8++) {
                  ItemStack var5 = mc.player.getInventory().getItem(var8);
                  if (var5.getItem() instanceof ArrowItem) {
                     if (var8 != this.field_659) {
                        this.field_658.reset();
                        Hub.field_2611.method_154(true);
                        InventoryUtil.method_5(InventoryUtil.getId(this.field_659), InventoryUtil.getId(var8));
                        Hub.field_2611.method_154(false);
                        this.field_659 = -1;
                     }

                     if (this.field_660 != null) {
                        for (MobEffectInstance var7 : this.field_660.getEffects()) {
                           this.field_657.put((MobEffect)var7.getEffect().value(), System.currentTimeMillis());
                        }
                     }
                     break;
                  }
               }
            }
         } else {
            int var4 = mc.player.getInventory().selected;
            InventoryUtil.method_16((var4 + 1) % 9);
            PacketSender.method_2(Action.RELEASE_USE_ITEM, BlockPos.ZERO, Direction.DOWN);
            InventoryUtil.method_16(var4);
            var1.method_463();
         }
      }
   }

   @Subscribe
   public void method_9(PostTickEvent var1) {
      if (mc.player.getMainHandItem().getItem() instanceof ProjectileWeaponItem) {
         Object var2 = this.method_268();
         if (var2 == null) {
            var2 = mc.player;
         }

         this.field_657.entrySet().removeIf(var0 -> var0.getValue() + 750L < System.currentTimeMillis());
         int var3 = -1;
         int var4 = -1;

         for (int var5 = 0; var5 < mc.player.getInventory().getContainerSize(); var5++) {
            ItemStack var6 = mc.player.getInventory().getItem(var5);
            int var7 = this.method_2(var6, var2);
            if (var7 >= 0 && var7 > var3) {
               var4 = var5;
               var3 = var7;
            }
         }

         this.field_659 = var4;
         if (this.field_659 != -1) {
            this.field_660 = PotionUtil.method_78(mc.player.getInventory().getItem(this.field_659));
         }
      }
   }

   @Subscribe
   public void method_5(Event_7 var1) {
      if (this.autoShoot.getValue()) {
         if (this.field_659 == -1 || this.method_2(mc.player.getInventory().getItem(this.field_659), mc.player) <= 0) {
            this.disable();
            return;
         }

         this.method_269();
      }
   }

   public Player method_268() {
      if (this.autoShoot.getValue()) {
         return mc.player;
      } else if (mc.player.getXRot() < -85F) {
         return mc.player;
      } else {
         AbstractClientPlayer var1 = null;
         double var2 = 3.4028234663852886e+38;

         for (AbstractClientPlayer var5 : mc.level.players()) {
            if (mc.player != var5 && !Hub.field_2603.getId(var5)) {
               float[] var6 = RotationManager.method_14(var5);
               float var7 = Mth.degreesDifferenceAbs(mc.player.getYRot(), var6[0])
                  - var5.getDimensions(var5.getPose()).width() * 0.5F;
               float var8 = Mth.degreesDifferenceAbs(mc.player.getXRot(), var6[1])
                  - var5.getDimensions(var5.getPose()).height() * 0.5F;
               if (!(var7 > this.fov.getValue()) && !(var8 > this.fov.getValue())) {
                  double var9 = Math.hypot((double)var7, (double)var8);
                  if (var9 < var2) {
                     var1 = var5;
                     var2 = var9;
                  }
               }
            }
         }

         return var1;
      }
   }

   public int method_2(ItemStack var1, Object var2) {
      if (var1.getItem() instanceof ArrowItem && var2 instanceof Player var3) {
         Potion var4 = PotionUtil.method_78(var1);
         boolean var5 = var3 == mc.player || Hub.field_2603.getId(var3);
         int var6 = 0;
         if (var4.getEffects().isEmpty()) {
            var6 = var5 ? -1 : 1;
         }

         for (MobEffectInstance var8 : var4.getEffects()) {
            Holder var9 = var8.getEffect();
            boolean var10 = var3.hasEffect(var9);
            if (var10) {
               MobEffectInstance var11 = var3.getEffect(var9);
               float var12 = ((DurationRatioAccess)var11).mio$getDurationRation();
               if (var12 * 1e+02F < (float)this.assumeDuration.getValue().intValue()) {
                  var10 = false;
               }
            }

            if (!var10 && !this.field_657.containsKey(var9.value())) {
               MobEffect var13 = (MobEffect)var9.value();
               int var14 = (!var13.isInstantenous() ? var8.getAmplifier() : 0) + 1;
               if (this.method_9(var9) != var5) {
                  var14 *= -1;
               }

               var6 += var14;
            }
         }

         return var6;
      } else {
         return -1;
      }
   }

   public boolean method_9(Holder<MobEffect> var1) {
      return var1 == MobEffects.SLOW_FALLING ? false : ((MobEffect)var1.value()).isBeneficial();
   }

   public void method_269() {
      int var1 = mc.player.getInventory().selected;
      int var2 = InventoryUtil.method_5(Items.BOW);
      Hub.field_2598.method_2(new float[]{mc.player.getYRot(), -9e+01F}, 999);
      if (var2 != -1 && !mc.player.isUsingItem()) {
         InventoryUtil.method_78(var2);
         mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
         if (!field_653.get()) {
            field_653.set(true);
            Hub.field_2619.method_2(() -> {
               mc.gameMode.releaseUsingItem(mc.player);
               InventoryUtil.method_78(var1);
               field_653.set(false);
               this.disable();
            }, 3);
         }
      }
   }
}
