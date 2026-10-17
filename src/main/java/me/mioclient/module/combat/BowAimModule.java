package me.mioclient.module.combat;

import me.mioclient.Hub;
import me.mioclient.enum_.PreType;
import me.mioclient.event.Event_19;
import me.mioclient.event.InteractItemEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.PotionUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BlockUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class BowAimModule extends Module {
   public Setting<Boolean> targets;
   public Setting<Boolean> players;
   public Setting<Boolean> ignoreNakeds;
   public Setting<Boolean> animals;
   public Setting<Boolean> hostiles;
   public Setting<Boolean> neutrals;
   public float[] field_2425;

   public BowAimModule() {
      super("BowAim", "Helps aiming with bows.", Category.COMBAT);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_9(Event_19 var1) {
      Item var2 = mc.player.getMainHandItem().getItem();
      boolean var3 = var2 instanceof ProjectileWeaponItem || var2 instanceof TridentItem;
      boolean var4 = mc.player.isUsingItem();
      if (CrossbowItem.isCharged(mc.player.getMainHandItem())) {
         var4 = true;
      }

      if (var3 && var4) {
         if (var1.method_213() == PreType.PRE) {
            for (MobEffectInstance var6 : this.method_720().getEffects()) {
               Holder var7 = var6.getEffect();
               if (((MobEffect)var7.value()).isBeneficial() && var7 != MobEffects.SLOW_FALLING && var7 != MobEffects.JUMP_BOOST) {
                  return;
               }
            }

            Entity var9 = BlockUtil.method_2(
               this.players.getValue(),
               this.neutrals.getValue(),
               this.animals.getValue(),
               this.hostiles.getValue(),
               false,
               false,
               0.0F,
               128F,
               false,
               this.ignoreNakeds.getValue()
            );
            if (var9 != null && !(var9 instanceof EnderMan)) {
               AABB var10 = var9.getBoundingBox();
               if (var9 instanceof Player var11) {
                  var10 = Hub.field_2612.method_2(var11, 5);
               }

               Vec3 var12 = var10.getCenter();
               if (RotationManager.method_4(var10) && BlockUtil.method_9(var12)) {
                  float[] var8 = method_2(var9, true, 2F);
                  if (Float.isNaN(var8[0]) || Float.isNaN(var8[1])) {
                     return;
                  }

                  this.field_2425 = var8;
                  Hub.field_2598.method_2(var8, 5, true);
               }
            }
         }
      }
   }

   @Subscribe
   public void method_2(InteractItemEvent var1) {
      ServerboundUseItemPacket var2 = var1.method_1102();
      if (mc.player.getItemInHand(var2.getHand()).is(Items.CROSSBOW) && this.field_2425 != null) {
         var1.method_9(this.field_2425);
      }
   }

   public Potion method_720() {
      for (int var1 = 0; var1 < mc.player.getInventory().getContainerSize(); var1++) {
         ItemStack var2 = mc.player.getInventory().getItem(var1);
         if (var2.getItem() instanceof ArrowItem) {
            return PotionUtil.method_78(var2);
         }
      }

      return PotionUtil.field_1158;
   }

   public static float[] method_2(Entity var0, boolean var1, float var2) {
      LocalPlayer var3 = mc.player;
      double var4 = var0.getX() + (var1 ? (var0.getX() - var0.xo) * (double)var2 : 0.0) - (var3.getX() + (var1 ? var3.getX() - var3.xo : 0.0));
      double var10001 = var1 ? (var0.getBoundingBox().minY - var0.yo) * (double)var2 : 0.0;
      double var10000 = var0.getBoundingBox().minY + var10001 + (double)var0.getEyeHeight(var0.getPose()) - 0.15;
      double var10002 = var1 ? var3.getY() - var3.yo : 0.0;
      double var6 = var10000 - (var3.getBoundingBox().minY + var10002) - (double)var3.getEyeHeight(var3.getPose());
      double var8 = var0.getZ() + (var1 ? (var0.getZ() - var0.zo) * (double)var2 : 0.0) - (var3.getZ() + (var1 ? var3.getZ() - var3.zo : 0.0));
      double var10 = Math.hypot(var4, var8);
      float var12 = (float)var3.getTicksUsingItem() / 2e+01F;
      if (var3.isHolding(Items.CROSSBOW)) {
         var12 = 1F;
      }

      var12 = (var12 * var12 + var12 * 2F) / 3F;
      if (var12 > 1F) {
         var12 = 1F;
      }

      return new float[]{
         (float)(Math.atan2(var8, var4) * 1.8e+02 / Class_0245.field_687) - (float)Class_0245.field_685,
         (float)(
            -Math.toDegrees(
               Math.atan(
                  (
                        (double)(var12 * var12)
                           - Math.sqrt(
                              (double)(var12 * var12 * var12 * var12)
                                 - 0.006000000052154064
                                    * (
                                       0.006000000052154064 * var10 * var10
                                          + 2.0 * var6 * (double)(var12 * var12)
                                    )
                           )
                     )
                     / (0.006000000052154064 * var10)
               )
            )
         )
      };
   }
}
