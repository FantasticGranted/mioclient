package me.mioclient.module.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.TrackedAnimal;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.equine.Donkey;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class AutoBreedModule extends Module {
   public Setting<Float> range;
   public Setting<Integer> frequency;
   public Setting<Float> delay;
   public Setting<Boolean> rotate;
   public Setting<Boolean> targets;
   public Setting<Boolean> dogs;
   public Setting<Boolean> cats;
   public Setting<Boolean> parrots;
   public Setting<Boolean> donkeys;
   public Setting<Boolean> horses;
   public Setting<Boolean> others;
   public final Timer field_818;
   public final List<TrackedAnimal> field_819;

   public AutoBreedModule() {
      super("AutoBreed", "Breeds animals in between each other automatically.", Category.PLAYER);
      Settings.initialize(this);
      this.field_818 = new Timer();
      this.field_819 = Collections.synchronizedList(new ArrayList<>());
   }

   @Override
   public void onEnable() {
      this.field_819.clear();
   }

   @Subscribe
   public void method_5(Event_7 var1) {
      this.field_819.forEach(TrackedAnimal::method_273);
      this.field_819.removeIf(var0 -> var0.method_275() >= 6000);
      if (this.field_818.method_2((double)this.delay.getValue().floatValue(), TimeUnit.SECONDS)) {
         int var2 = 0;

         label49:
         for (Entity var4 : mc.level.entitiesForRendering()) {
            if (var4 instanceof Animal) {
               Animal var5 = (Animal)var4;

               for (TrackedAnimal var7 : this.field_819) {
                  if (var5 == var7.method_274()) {
                     continue label49;
                  }
               }

               if (this.method_9(var5) && var5.distanceTo(mc.player) <= this.range.getValue()) {
                  int var8 = InventoryUtil.method_7(method_2(var5));
                  if (var8 == -1) {
                     return;
                  }

                  Vec3 var9 = var5.getBoundingBox().getCenter();
                  if (var9 != null) {
                     InventoryUtil.method_16(var8);
                     InteractionUtil.method_2(var5, InteractionHand.MAIN_HAND);
                     this.field_819.add(new TrackedAnimal(var5));
                     var2++;
                     if (this.rotate.getValue()) {
                        Hub.field_2598.method_2(RotationManager.method_78(var9), 5);
                     }

                     if (var2 >= this.frequency.getValue()) {
                        break;
                     }
                  }
               }
            }
         }

         this.field_818.reset();
      }
   }

   public static Predicate<ItemStack> method_2(Animal var0) {
      return var0 instanceof Parrot
         ? var0x -> List.of(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS).contains(var0x.getItem())
         : var0::isFood;
   }

   public boolean method_9(Animal var1) {
      if (var1.isBaby()) {
         return false;
      } else {
         Objects.requireNonNull(var1);

         return switch (var1) {
            case Wolf var4 -> this.dogs.getValue();
            case Cat var5 -> this.cats.getValue();
            case Parrot var6 -> this.parrots.getValue();
            case Donkey var7 -> this.donkeys.getValue();
            case Horse var8 -> this.horses.getValue();
            default -> var1.getAge() != 0 && this.others.getValue();
         };
      }
   }
}
