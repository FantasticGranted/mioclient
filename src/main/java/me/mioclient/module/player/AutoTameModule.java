package me.mioclient.module.player;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import me.mioclient.enum_.TameAction;
import me.mioclient.enum_.PreType;
import me.mioclient.event.Event_19;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.RotationManager;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class AutoTameModule extends Module {
   public Setting<TameAction> action;
   public Setting<Float> range;
   public Setting<Integer> frequency;
   public Setting<Float> delay;
   public Setting<Boolean> rotate;
   public Setting<Boolean> targets;
   public Setting<Boolean> dogs;
   public Setting<Boolean> cats;
   public Setting<Boolean> parrots;
   public final Timer field_1366;

   public AutoTameModule() {
      super("AutoTame", "Automatically interacts with tame-able entities.", Category.PLAYER);
      Settings.initialize(this);
      this.field_1366 = new Timer();
   }

   @Subscribe
   public void method_2(Event_19 var1) {
      if (var1.method_213() != PreType.POST) {
         if (this.field_1366.method_2((double)this.delay.getValue().floatValue(), TimeUnit.SECONDS)) {
            int var2 = 0;

            for (Entity var4 : mc.level.entitiesForRendering()) {
               if (var4 instanceof TamableAnimal) {
                  TamableAnimal var5 = (TamableAnimal)var4;
                  if (this.method_2(var5) && var4.distanceTo(mc.player) <= this.range.getValue()) {
                     Vec3 var6 = this.action.getValue().method_5(var5);
                     if (var6 != null) {
                        var2++;
                        if (this.rotate.getValue()) {
                           var1.method_5(RotationManager.method_78(var6));
                        }

                        if (var2 >= this.frequency.getValue()) {
                           break;
                        }
                     }
                  }
               }
            }

            this.field_1366.reset();
         }
      }
   }

   public boolean method_2(TamableAnimal var1) {
      return var1 instanceof Cat && this.cats.getValue()
         || var1 instanceof Wolf && this.dogs.getValue()
         || var1 instanceof Parrot && this.parrots.getValue();
   }

   public static Predicate<ItemStack> method_9(TamableAnimal var0) {
      if (var0 instanceof Wolf) {
         return var0x -> var0x.is(Items.BONE);
      } else {
         return var0 instanceof Parrot
            ? var0x -> List.of(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS).contains(var0x.getItem())
            : var0::isFood;
      }
   }
}
