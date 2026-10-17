package me.mioclient.mixin;

import me.mioclient.api.DurationRatioAccess;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({MobEffectInstance.class})
public class MixinStatusEffectInstance implements DurationRatioAccess {
   @Shadow
   private int duration;
   @Unique
   private int mio$initialDuration;

   public MixinStatusEffectInstance() {
      super();
   }

   @Inject(
      method = {"<init>(Lnet/minecraft/core/Holder;IIZZZLnet/minecraft/world/effect/MobEffectInstance;)V"},
      at = {@At("TAIL")}
   )
   private void initHook(
      Holder<MobEffect> var1, int var2, int var3, boolean var4, boolean var5, boolean var6, MobEffectInstance var7, CallbackInfo var8
   ) {
      this.mio$initialDuration = var2;
   }

   @Inject(
      method = {"setDetailsFrom"},
      at = {@At("TAIL")}
   )
   private void copyFrom(MobEffectInstance var1, CallbackInfo var2) {
      this.mio$initialDuration = var1.getDuration();
   }

   @Inject(
      method = {"tickDownDuration"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/world/effect/MobEffectInstance;duration:I",
         opcode = 181,
         shift = Shift.AFTER
      )}
   )
   private void updateDuration(CallbackInfo var1) {
      this.mio$initialDuration = this.duration;
   }

   @Override
   public float mio$getDurationRation() {
      return (float)this.duration / (float)this.mio$initialDuration;
   }
}
