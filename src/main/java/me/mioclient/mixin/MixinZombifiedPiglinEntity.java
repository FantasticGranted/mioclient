package me.mioclient.mixin;

import me.mioclient.api.AttackingAccess;
import me.mioclient.internal.Timer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ZombifiedPiglin.class})
public abstract class MixinZombifiedPiglinEntity extends Mob implements AttackingAccess {
   @Unique
   private Timer stopwatch;

   protected MixinZombifiedPiglinEntity(EntityType<? extends Mob> var1, Level var2) {
      super(var1, var2);
   }

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void initHook(EntityType<?> var1, Level var2, CallbackInfo var3) {
      this.stopwatch = new Timer();
   }

   @Override
   public boolean mio$isAttacking() {
      if (this.isAggressive()) {
         this.stopwatch.reset();
      }

      return !this.stopwatch.method_9(1000L);
   }
}
