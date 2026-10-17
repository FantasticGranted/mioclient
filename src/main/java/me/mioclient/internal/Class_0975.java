package me.mioclient.internal;

import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.CombatVersion;
import me.mioclient.enum_.InstantMode;
import me.mioclient.enum_.WeaknessMode;
import me.mioclient.enum_.SwingHand;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.module.combat.AutoCrystalModule;
import me.mioclient.module.misc.SwingModule;
import me.mioclient.module.render.HitmarkerModule;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Class_0975 implements MinecraftAccess {
   public static final SwingModule swing = Hub.field_2595.method_78(SwingModule.class);
   public static AutoCrystalModule ac = Hub.field_2595.method_78(AutoCrystalModule.class);
   public static HitmarkerModule field_2993 = Hub.field_2595.method_78(HitmarkerModule.class);
   public final Timer field_2994 = new Timer();
   public long field_2995;
   public int current;

   public Class_0975() {
      super();
      lookups.method_14(this);
   }

   @Subscribe(
      method_800 = -300
   )
   public void method_2(PlayerTickEvent var1) {
      this.current = -1;
   }

   public void method_157(int var1) {
      this.current = var1;
   }

   public boolean method_80(int var1) {
      return this.method_5(var1, false);
   }

   public boolean method_5(int var1, boolean var2) {
      if (!this.method_877() && var2) {
         return false;
      } else if (this.current != -1 && !var2) {
         return false;
      } else {
         int var3 = mc.player.getInventory().selected;
         boolean var4 = this.method_879();
         this.current = var1;
         PacketSender.method_36(this.current);
         Entity var5 = mc.level.getEntity(var1);
         if (var5 != null
            && var5.level() != null
            && var5 != mc.player
            && var5.level().isClientSide()
            && field_2993.method_221(var5)
            && var5 instanceof EndCrystal) {
            field_2993.field_1610 = System.currentTimeMillis();
            field_2993.field_1611 = 255;
            field_2993.field_1612 = true;
         }

         InteractionHand var6 = mc.player.getOffhandItem().getItem() == Items.END_CRYSTAL ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
         if (!(var5 instanceof EndCrystal) && var5 != null) {
            var6 = InteractionHand.MAIN_HAND;
         }

         this.method_7(var6);
         if (var2) {
            PacketSender.method_9(InteractionHand.MAIN_HAND);
         } else {
            mc.player.swing(var6);
         }

         if (var4 && ac.weakness.getValue() == WeaknessMode.SILENT) {
            ac.method_1143();
            InventoryUtil.method_16(var3);
         }

         return true;
      }
   }

   public boolean method_877() {
      return ac.instant.getValue() != InstantMode.NONE;
   }

   public int method_878() {
      return ac.delay2.getValue();
   }

   public int method_523() {
      return ac.ticksExisted.getValue();
   }

   public void method_7(InteractionHand var1) {
      if (this.field_2994.method_5(200L + this.field_2995)
         && var1 == InteractionHand.MAIN_HAND
         && swing.hand.getValue() != SwingHand.OFFHAND
         && swing.type.getValue() != CombatVersion.VANILLA) {
         mc.player.resetAttackStrengthTicker();
         this.field_2995 = (long)(Math.random() * 3e+02);
      }
   }

   public boolean method_879() {
      if (ac.weakness.getValue() != WeaknessMode.NONE
         && mc.player.hasEffect(MobEffects.WEAKNESS)
         && !InventoryUtil.isTiered(mc.player.getMainHandItem())) {
         int var1 = InventoryUtil.method_7((Predicate<ItemStack>)(var0 -> InventoryUtil.isSword(var0)));
         if (var1 != -1) {
            if (ac.weakness.getValue() == WeaknessMode.SILENT) {
               ac.method_1143();
            }

            InventoryUtil.method_16(var1);
            return true;
         }
      }

      return false;
   }
}
