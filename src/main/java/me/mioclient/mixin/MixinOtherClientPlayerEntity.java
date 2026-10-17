package me.mioclient.mixin;

import com.mojang.authlib.GameProfile;
import me.mioclient.api.SuperDamageAccess;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({RemotePlayer.class})
public abstract class MixinOtherClientPlayerEntity extends Player implements SuperDamageAccess {
   public MixinOtherClientPlayerEntity(Level var1, GameProfile var4) {
      super(var1, var4);
   }

   @Override
   public boolean damageSuper(DamageSource var1, float var2) {
      if (var2 <= 0.0F) {
         return false;
      } else {
         float var3 = Math.min(this.getAbsorptionAmount(), var2);
         this.setAbsorptionAmount(this.getAbsorptionAmount() - var3);
         float var4 = var2 - var3;
         this.hurtMarked = true;
         this.hurtDuration = 10;
         this.hurtTime = 10;
         this.playHurtSound(var1);
         if (var4 > 0.0F) {
            this.setHealth(this.getHealth() - var4);
         }

         return true;
      }
   }
}
