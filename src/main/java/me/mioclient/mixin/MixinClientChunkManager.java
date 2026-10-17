package me.mioclient.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.Map;
import java.util.function.Consumer;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.ChunkLoadEvent;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData.BlockEntityTagOutput;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientChunkCache.class})
public class MixinClientChunkManager {
   public MixinClientChunkManager() {
      super();
   }

   @Inject(
      method = {"replaceWithPacketData"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/ClientLevel;onChunkLoaded(Lnet/minecraft/world/level/ChunkPos;)V",
         shift = Shift.BEFORE
      )}
   )
   private void loadChunkFromPacketHook(
      int var1,
      int var2,
      FriendlyByteBuf var3,
      Map<Heightmap.Types, long[]> var4,
      Consumer<BlockEntityTagOutput> var5,
      CallbackInfoReturnable<LevelChunk> var6,
      @Local LevelChunk var7
   ) {
      MinecraftAccess.lookups.method_36(new ChunkLoadEvent(var7));
   }
}
