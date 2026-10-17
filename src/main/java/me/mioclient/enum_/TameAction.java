package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.module.player.AutoTameModule;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.phys.Vec3;

/** AutoTame action. */
public enum TameAction implements Nameable {
   TAME("Tame") {
      @Override
      public Vec3 method_5(TamableAnimal var1) {
         if (var1.getOwner() != null) {
            return null;
         } else {
            int var2 = InventoryUtil.method_7(AutoTameModule.method_9(var1));
            if (var2 == -1) {
               return null;
            } else {
               InventoryUtil.method_16(var2);
               InteractionUtil.method_2(var1, InteractionHand.MAIN_HAND);
               return var1.getBoundingBox().getCenter();
            }
         }
      }
   },
   SIT("Sit") {
      @Override
      public Vec3 method_5(TamableAnimal var1) {
         if (!var1.isInSittingPose()
            && !var1.isOrderedToSit()
            && MinecraftAccess.mc.player == var1.getOwner()
            && !var1.isFood(MinecraftAccess.mc.player.getMainHandItem())) {
            InteractionUtil.method_2(var1, InteractionHand.MAIN_HAND);
            return var1.getBoundingBox().getCenter();
         } else {
            return null;
         }
      }
   },
   STAND("Stand") {
      @Override
      public Vec3 method_5(TamableAnimal var1) {
         if ((var1.isInSittingPose() || var1.isOrderedToSit())
            && MinecraftAccess.mc.player == var1.getOwner()
            && !var1.isFood(MinecraftAccess.mc.player.getMainHandItem())) {
            InteractionUtil.method_2(var1, InteractionHand.MAIN_HAND);
            return var1.getBoundingBox().getCenter();
         } else {
            return null;
         }
      }
   };

   public final String displayName;

    TameAction(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public abstract Vec3 method_5(TamableAnimal var1);
}
