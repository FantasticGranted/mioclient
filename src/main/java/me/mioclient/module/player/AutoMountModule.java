package me.mioclient.module.player;

import java.util.concurrent.TimeUnit;
import me.mioclient.Hub;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideable;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import me.mioclient.nick.Settings;
import net.minecraft.world.entity.EquipmentSlot;

public final class AutoMountModule extends Module {
   public Setting<Double> range;
   public Setting<Float> delay;
   public Setting<Boolean> rotate;
   public final Timer field_1196;

   public AutoMountModule() {
      super("AutoMount", "Mounts rideable entities automatically.", Category.PLAYER);
      Settings.initialize(this);
      this.field_1196 = new Timer();
   }

   @Subscribe
   public void method_5(Event_7 var1) {
      if (!mc.player.isPassenger() && this.field_1196.method_2((double)this.delay.getValue().floatValue(), TimeUnit.SECONDS)) {
         for (Entity var3 : mc.level.entitiesForRendering()) {
            if (!((double)mc.player.distanceTo(var3) > this.range.getValue())) {
               if (var3 instanceof LivingEntity) {
                  LivingEntity var4 = (LivingEntity)var3;
                  if (var4.isBaby()) {
                     continue;
                  }
               }

               if (var3 instanceof LivingEntity var5) {
                  if (var5.getItemBySlot(EquipmentSlot.SADDLE).isEmpty() && !(var3 instanceof AbstractHorse)) {
                     continue;
                  }
               }

               if (var3 instanceof PlayerRideable || var3 instanceof VehicleEntity) {
                  InteractionUtil.method_2(var3, InteractionHand.MAIN_HAND);
                  if (this.rotate.getValue()) {
                     Hub.field_2598.method_2(RotationManager.method_78(var3.getBoundingBox().getCenter()), 5);
                  }

                  this.field_1196.reset();
                  break;
               }
            }
         }
      }
   }
}
