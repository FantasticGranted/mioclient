package me.mioclient.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.ChunkLoadEvent;
import me.mioclient.event.Subscribe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

public class Class_0032 implements MinecraftAccess {
   public final int field_60;
   public final BiConsumer<ChunkPos, LevelChunk> field_61;
   public final BiConsumer<ChunkPos, LevelChunk> field_62;
   public final BiConsumer<BlockPos, BlockState> field_63;
   public ExecutorService field_64;

   public Class_0032(int var1, BiConsumer<ChunkPos, LevelChunk> var2, BiConsumer<ChunkPos, LevelChunk> var3, BiConsumer<BlockPos, BlockState> var4) {
      super();
      this.field_60 = var1;
      this.field_61 = var2;
      this.field_62 = var3;
      this.field_63 = var4;
   }

   public void method_47() {
      this.field_64 = Executors.newFixedThreadPool(Math.max(this.field_60, 1));
      lookups.method_14(this);
   }

   public void method_48() {
      lookups.method_17(this);
      this.field_64.shutdownNow();
      this.field_64 = null;
   }

   public void method_49() {
      this.field_64.shutdownNow();
      this.field_64 = Executors.newFixedThreadPool(Math.max(this.field_60, 1));
   }

   public void method_50() {
      if (this.field_61 != null) {
         for (LevelChunk var2 : BlockUtil.method_1069()) {
            this.getId(() -> this.field_61.accept(var2.getPos(), var2));
         }
      }
   }

   @Subscribe
   public void method_9(PacketReceiveEvent var1) {
      if (mc.level != null) {
         if (this.field_63 != null && var1.method_127() instanceof ClientboundBlockUpdatePacket var2) {
            this.getId(() -> this.field_63.accept(var2.getPos(), var2.getBlockState()));
          } else if (this.field_63 != null && var1.method_127() instanceof ClientboundExplodePacket var3) {
             // TODO26.1: ClientboundExplodePacket no longer carries the exploded block list (only blockCount).
          } else if (this.field_63 != null && var1.method_127() instanceof ClientboundSectionBlocksUpdatePacket var4) {
            var4.runUpdates((var1x, var2x) -> {
               BlockPos var3x = var1x.immutable();
               this.getId(() -> this.field_63.accept(var3x, var2x));
            });
         } else if (this.field_62 != null && var1.method_127() instanceof ClientboundForgetLevelChunkPacket var5) {
            ChunkPos var11 = new ChunkPos(var5.pos().x(), var5.pos().z());
            LevelChunk var7 = mc.level.getChunk(var11.x(), var11.z());
            this.getId(() -> this.field_62.accept(var11, var7));
         }
      }
   }

   @Subscribe
   public void method_2(ChunkLoadEvent var1) {
      if (this.field_61 != null) {
         this.getId(() -> this.field_61.accept(var1.method_1021().getPos(), var1.method_1021()));
      }
   }

   public ExecutorService method_51() {
      return this.field_64;
   }

   public void getId(Runnable var1) {
      if (this.field_60 == 0) {
         mc.executeIfPossible(var1);
      } else {
         this.field_64.execute(var1);
      }
   }
}
