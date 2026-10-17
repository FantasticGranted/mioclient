package me.mioclient.mixin;

import me.mioclient.api.AttackTrackingAccess;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EndCrystal.class})
public class MixinEndCrystalEntity implements AttackTrackingAccess {
   @Unique
   private long spawnTime;
   @Unique
   private boolean mioAttacked;

   public MixinEndCrystalEntity() {
      super();
   }

   @Inject(
      method = {"<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V"},
      at = {@At("RETURN")}
   )
   private void init(EntityType<?> var1, Level var2, CallbackInfo var3) {
      this.spawnTime = System.currentTimeMillis();
      this.mioAttacked = false;
   }

   @Override
   public long getSpawnTime() {
      return this.spawnTime;
   }

   @Override
   public boolean isMioAttacked() {
      return this.mioAttacked;
   }

   @Override
   public void setMioAttacked(boolean var1) {
      this.mioAttacked = var1;
   }
}
