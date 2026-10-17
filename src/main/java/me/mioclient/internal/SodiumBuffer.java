package me.mioclient.internal;

import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.lwjgl.system.MemoryStack;

/** BufferSource variant that writes vertices through Sodium's VertexBufferWriter when available. */
public class SodiumBuffer extends BufferSource implements VertexBufferWriter {
   public final boolean field_4334 = this.field_2558 instanceof VertexBufferWriter;

   public SodiumBuffer(int var1) {
      super(var1);
   }

   public void push(MemoryStack var1, long var2, int var4, VertexFormat var5) {
      if (this.field_4334) {
         ((VertexBufferWriter)this.field_2558).push(var1, var2, var4, var5);
      }
   }

   public boolean canUseIntrinsics() {
      return this.field_4334;
   }
}
