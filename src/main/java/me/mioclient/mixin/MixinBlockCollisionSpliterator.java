package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.Event_35;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({BlockCollisions.class})
public class MixinBlockCollisionSpliterator {
   @Shadow
   @Final
   private CollisionGetter collisionGetter;
   @Shadow
   @Final
   private MutableBlockPos pos;

   public MixinBlockCollisionSpliterator() {
      super();
   }

   @ModifyExpressionValue(
      method = {"computeNext"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/phys/shapes/CollisionContext;getCollisionShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/CollisionGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;"
      )}
   )
   private VoxelShape computeNextHook(VoxelShape var1, @Local BlockState var2) {
      if (this.collisionGetter != Minecraft.getInstance().level) {
         return var1;
      } else {
         Event_35 var3 = MinecraftAccess.lookups.method_36(Event_35.method_2(var1, this.pos, var2));
         return var3.method_464() ? Shapes.empty() : var3.method_957();
      }
   }
}
