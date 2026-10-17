package me.mioclient.module.misc;

import me.mioclient.Hub;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.exploit.RocketModule;
import me.mioclient.module.movement.ElytraFlyModule;
import me.mioclient.module.movement.FireworksModule;
import me.mioclient.setting.Setting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import me.mioclient.nick.Settings;
import org.lwjgl.glfw.GLFW;

public class MiddleClickModule extends Module {
   public static final FireworksModule field_1129 = Hub.field_2595.method_78(FireworksModule.class);
   public static final RocketModule field_1130 = Hub.field_2595.method_78(RocketModule.class);
   public static ElytraFlyModule elytrafly = Hub.field_2595.method_78(ElytraFlyModule.class);
   public Setting<Boolean> friends;
   public Setting<Boolean> fireworks;
   public Setting<Boolean> alternative;
   public boolean field_1134;

   public MiddleClickModule() {
      super("MiddleClick", "Middle click actions.", Category.MISC);
      Settings.initialize(this);
      this.setDrawn(false);
   }

   @Subscribe
   public void method_2(PostTickEvent var1) {
      if (mc.screen == null) {
         if (GLFW.glfwGetMouseButton(mc.getWindow().handle(), 2) == 1 && !this.field_1134) {
            this.field_1134 = true;
            HitResult var2 = mc.hitResult;
            boolean var3 = mc.player.isFallFlying() || field_1129.method_786() || elytrafly.method_1183();
            if (var3 && this.fireworks.getValue()) {
               this.method_298();
            } else if (var2 instanceof EntityHitResult var4 && var4.getEntity() instanceof Player var5 && this.friends.getValue()) {
               if (Hub.field_2603.method_1009(var5.getGameProfile().name())) {
                  Hub.field_2603.method_869(var5.getGameProfile().name());
               } else {
                  Hub.field_2603.method_632(var5.getGameProfile().name());
               }
            }
         } else if (GLFW.glfwGetMouseButton(mc.getWindow().handle(), 2) == 0) {
            this.field_1134 = false;
         }
      }
   }

   public void method_298() {
      InteractionHand var1 = InventoryUtil.method_7(Items.FIREWORK_ROCKET);
      int var2 = mc.player.getInventory().selected;
      int var3 = InventoryUtil.method_9(Items.FIREWORK_ROCKET);
      int var4 = InventoryUtil.method_5(Items.FIREWORK_ROCKET);
      if (var1 != null) {
         mc.gameMode.useItem(mc.player, var1);
      } else if (var3 != -1) {
         boolean var5 = var4 == -1 || this.alternative.getValue();
         this.method_2(var4, var3, var5);
         mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
         field_1130.field_3535 = mc.player.position();
         this.method_2(var2, var3, var5);
      }
   }

   public void method_2(int var1, int var2, boolean var3) {
      if (var3) {
         InventoryUtil.method_39(var2);
      } else {
         InventoryUtil.method_16(var1);
      }
   }
}
