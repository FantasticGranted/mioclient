package me.mioclient.mixin;

import me.mioclient.ducks.DuckEndCrystalRenderState;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({EndCrystalRenderState.class})
public class MixinEndCrystalRenderState implements DuckEndCrystalRenderState {
   @Unique
   private EndCrystal mio$crystal;

   @Override
   public EndCrystal mio$getCrystal() {
      return this.mio$crystal;
   }

   @Override
   public void mio$setCrystal(EndCrystal var1) {
      this.mio$crystal = var1;
   }
}
