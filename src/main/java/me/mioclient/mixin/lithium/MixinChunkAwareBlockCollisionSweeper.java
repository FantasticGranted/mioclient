package me.mioclient.mixin.lithium;

import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.Event_35;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(
   targets = {"me.jellysquid.mods.lithium.common.entity.movement.ChunkAwareBlockCollisionSweeper"},
   remap = false
)
public class MixinChunkAwareBlockCollisionSweeper {
   public MixinChunkAwareBlockCollisionSweeper() {
      super();
   }

   @Redirect(
      method = {"computeNext"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/block/state/BlockState;getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;"
      )
   )
   private VoxelShape onComputeNextCollisionBox(BlockState var1, BlockGetter var2, BlockPos var3, CollisionContext var4) {
      VoxelShape var5 = var1.getCollisionShape(var2, var3, var4);
      if (var2 != Minecraft.getInstance().level) {
         return var5;
      } else {
         Event_35 var6 = MinecraftAccess.lookups.method_36(Event_35.method_2(var5, var3, var1));
         return var6.method_464() ? Shapes.empty() : var6.method_957();
      }
   }
}
