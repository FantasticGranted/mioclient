package me.mioclient.module.combat;

import java.util.function.Predicate;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import me.mioclient.nick.Settings;

public class AutoBowReleaseModule extends Module {
   public Setting<Integer> delay;

   public AutoBowReleaseModule() {
      super("AutoBowRelease", "Shoots your bow automatically.", Category.COMBAT);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_9(PostTickEvent var1) {
      if (!this.method_535() && InventoryUtil.method_29((Predicate<ItemStack>)(var0 -> var0.getItem() instanceof ArrowItem)) > 0) {
         int var2 = this.delay.getValue();
         if (mc.player.getTicksUsingItem() >= var2
            && mc.player.getUseItem().getItem() instanceof ProjectileWeaponItem var3
            && mc.player.isUsingItem()) {
            if (var3 instanceof CrossbowItem var5 && !CrossbowItem.isCharged(mc.player.getUseItem())) {
               return;
            }

            mc.gameMode.releaseUsingItem(mc.player);
            PacketSender.method_2(
               new ServerboundUseItemPacket(
                  mc.player.getUsedItemHand(), PacketSender.method_1101(), mc.player.getYRot(), mc.player.getXRot()
               )
            );
         }
      }
   }
}
