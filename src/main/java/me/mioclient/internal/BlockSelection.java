package me.mioclient.internal;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;

/** A start/end BlockPos pair with an active flag; used to track a two-corner block selection. */
public class BlockSelection {
   public BlockPos field_355;
   public BlockPos field_3080;
   public boolean field_3081;
   public int field_3082;

   public BlockSelection() {
      super();
   }

   public void reset() {
      this.field_355 = null;
      this.field_3081 = false;
   }

   public void method_895() {
      this.field_3081 = true;
   }

   public boolean method_896() {
      return this.field_3081;
   }

   public BlockPos method_111() {
      return this.field_355;
   }

   public void method_425(BlockPos var1) {
      this.field_355 = var1;
   }

   public BlockPos method_897() {
      return this.field_3080;
   }

   public void method_465(BlockPos var1) {
      this.field_3080 = var1;
   }

   public int method_52() {
      return this.field_3082;
   }

   public void method_194(int var1) {
      this.field_3082 = var1;
   }

   public void method_2(int var1, Consumer<BlockSelection> var2) {
      if (this.field_3082 <= var1 || this.method_111() == null || this.method_896()) {
         var2.accept(this);
         this.field_3082 = var1;
      }
   }
}
