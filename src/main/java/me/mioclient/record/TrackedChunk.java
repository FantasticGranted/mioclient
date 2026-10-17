package me.mioclient.record;

import java.awt.Color;
import java.util.Objects;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Dimension;
import me.mioclient.enum_.ChunkType;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.internal.HudRenderEvent;
import me.mioclient.internal.BlockUtil;
import me.mioclient.module.exploit.NewChunksModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;

/** A chunk tracked by the New Chunks module: its position, dimension, and {@link me.mioclient.enum_.ChunkType}. */
public final class TrackedChunk implements MinecraftAccess {
   public final ChunkPos chunkPos;
   public final Dimension dimension;
   public final ChunkType type;

   public TrackedChunk(ChunkPos var1, Dimension var2, ChunkType var3) {
      super();
      this.chunkPos = var1;
      this.dimension = var2;
      this.type = var3;
   }

   public static TrackedChunk method_2(ChunkPos var0, ChunkType var1) {
      return new TrackedChunk(var0, BlockUtil.method_1071(), var1);
   }

   public boolean method_2(ChunkType var1) {
      return var1 == this.type;
   }

   public void method_2(HudRenderEvent var1, NewChunksModule var2) {
      Color[] var3 = this.type.method_2(var2);
      if (var3 != null) {
         BlockPos var4 = mc.gameRenderer.getMainCamera().blockPosition();
         int var5 = mc.level.getMinY() + var2.height.getValue();
         if (var4.closerThan(this.chunkPos.getMiddleBlockPosition(var4.getY()), (double)(16 * var2.distance.getValue()))) {
            AABB var6 = new AABB(
               (double)this.chunkPos.getMinBlockX(),
               (double)var5,
               (double)this.chunkPos.getMinBlockZ(),
               (double)(this.chunkPos.getMinBlockX() + 16),
               (double)var5,
               (double)(this.chunkPos.getMinBlockZ() + 16)
            );
            if (!RotationManager.method_4(var6)) {
               return;
            }

            BoxRenderer.method_5(var1.method_10(), var6, var3[0]);
            BoxRenderer.method_2(var1.method_10(), var6, var3[1], var2.lineWidth.getValue());
         }
      }
   }

   @Override
   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         TrackedChunk var2 = (TrackedChunk)var1;
         return Objects.equals(this.chunkPos, var2.chunkPos) && this.type == var2.type && this.dimension == var2.dimension;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.chunkPos, this.dimension, this.type);
   }

   public ChunkPos getChunkPos() {
      return this.chunkPos;
   }

   public Dimension method_418() {
      return this.dimension;
   }

   public ChunkType getType() {
      return this.type;
   }
}
