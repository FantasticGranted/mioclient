package me.mioclient.module.player;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import me.mioclient.enum_.AutoSwapMode;
import me.mioclient.enum_.SelfFillMode;
import me.mioclient.enum_.PreType;
import me.mioclient.event.Event_19;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import me.mioclient.nick.Settings;

public class AutoNameTagModule extends Module {
   public Setting<SelfFillMode> mode;
   public Setting<Set<EntityType<?>>> whitelist;
   public Setting<Float> range;
   public Setting<Integer> frequency;
   public Setting<Float> delay;
   public Setting<Boolean> rotate;
   public Setting<AutoSwapMode> swap;
   public final Timer field_4231;

   public AutoNameTagModule() {
      super("AutoNameTag", "Will use name tags on nearby entities.", Category.PLAYER);
      Settings.initialize(this);
      this.field_4231 = new Timer();
   }

   @Subscribe
   public void method_9(Event_19 var1) {
      if (var1.method_213() != PreType.POST) {
         if (this.field_4231.method_2((double)this.delay.getValue().floatValue(), TimeUnit.SECONDS)) {
            int var2 = InventoryUtil.method_7(this::method_340);
            int var3 = mc.player.getInventory().selected;
            int var4 = 0;
            if (this.swap.getValue() != AutoSwapMode.NONE) {
               InventoryUtil.method_16(var2);
            }

            for (Entity var6 : mc.level.entitiesForRendering()) {
               if (!this.method_340(mc.player.getMainHandItem())) {
                  break;
               }

               if (this.mode.getValue().method_2(var6.getType(), this.whitelist)
                  && !(var6 instanceof Player)
                  && !mc.player.getMainHandItem().getHoverName().getString().equalsIgnoreCase(var6.hasCustomName() ? var6.getCustomName().getString() : "")
                  && !(mc.player.getEyePosition().distanceTo(var6.position()) > (double)this.range.getValue().floatValue())) {
                  InteractionUtil.method_2(var6, InteractionHand.MAIN_HAND);
                  var4++;
                  if (this.rotate.getValue()) {
                     var1.method_5(RotationManager.method_78(var6.position()));
                  }

                  if (var4 >= this.frequency.getValue()) {
                     break;
                  }
               }
            }

            if (this.swap.getValue() == AutoSwapMode.SILENT) {
               InventoryUtil.method_16(var3);
            }

            this.field_4231.reset();
         }
      }
   }

   public boolean method_340(ItemStack var1) {
      return var1.is(Items.NAME_TAG) && var1.has(DataComponents.CUSTOM_NAME);
   }
}
