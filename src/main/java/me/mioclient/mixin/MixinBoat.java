package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.Event_64;
import me.mioclient.internal.VectorUtil;
import me.mioclient.module.movement.EntityControlModule;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AbstractBoat.class})
public abstract class MixinBoat extends Entity implements MinecraftAccess {
   private static final EntityControlModule entitycontrol = Hub.field_2595.method_78(EntityControlModule.class);
   @Unique
   private Event_64 mio$event;

   public MixinBoat(EntityType<?> var1, Level var2) {
      super(var1, var2);
   }

   @Inject(
      method = {"clampRotation"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void clampPassengerYawHook(Entity var1, CallbackInfo var2) {
      if (entitycontrol.method_107(this)) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"controlBoat"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void updatePaddles(CallbackInfo var1) {
      if (entitycontrol.method_107(this)) {
         var1.cancel();
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At(
         value = "INVOKE",
          target = "Lnet/minecraft/world/entity/vehicle/boat/AbstractBoat;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
         shift = Shift.BEFORE
      )}
   )
   private void tickPre(CallbackInfo var1) {
      if (Event_64.method_33(this)) {
         this.mio$event = new Event_64(this.getDeltaMovement());
         lookups.method_36(this.mio$event);
         VectorUtil.method_2(this.getDeltaMovement(), this.mio$event.method_380(), this.mio$event.method_395(), this.mio$event.method_396());
      }
   }
}
