package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.internal.MathUtil;
import me.mioclient.module.movement.ElytraFlyModule;
import me.mioclient.module.movement.FireworksModule;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({FireworkRocketEntity.class})
public abstract class MixinFireworkRocketEntity extends Entity {
   private static FireworksModule fireworks = Hub.field_2595.method_78(FireworksModule.class);
   @Shadow
   @Nullable
   private LivingEntity attachedToEntity;

   public MixinFireworkRocketEntity(EntityType<?> var1, Level var2) {
      super(var1, var2);
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   private void tickHook(CallbackInfo var1) {
      ElytraFlyModule.field_885 = true;
   }

   @Inject(
      method = {"tick"},
      at = {@At("TAIL")}
   )
   private void tickHook2(CallbackInfo var1) {
      ElytraFlyModule.field_885 = false;
   }

   @Inject(
      method = {"tick"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
         shift = Shift.AFTER
      )}
   )
   private void tick(CallbackInfo var1) {
      if (fireworks.field_2696) {
         this.tickCount = 1;
         fireworks.field_2696 = false;
      }

      float var2 = fireworks.method_100(true);
      if (this.attachedToEntity == Minecraft.getInstance().player) {
         if (var2 != 0.0F) {
            float var3 = MathUtil.method_7(this.attachedToEntity.getYRot());
            if ((double)var2 > 0.5) {
               var2 = Mth.clamp(0.2F + 0.3F * (float)this.tickCount, 0.0F, var2);
            }

            if (fireworks.method_785() > 0) {
               this.attachedToEntity.push(this.attachedToEntity.getViewVector(1.0F).scale((double)var2));
            } else {
               this.attachedToEntity.push((double)(Mth.sin(var3) * -var2), 0.0, (double)(Mth.cos(var3) * var2));
            }
         }
      }
   }
}
