package me.mioclient.internal;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import me.mioclient.api.PaletteAccess;
import me.mioclient.enum_.Class_0404;
import net.minecraft.core.Holder;
import net.minecraft.util.BitStorage;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.HashMapPalette;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.LinearPalette;
import net.minecraft.world.level.chunk.Palette;
import net.minecraft.world.level.chunk.PalettedContainer;

public final class Class_1040 {
   public static final IntSet field_3220 = new IntOpenHashSet();

   public Class_1040() {
      super();
   }

   public boolean method_9(LevelChunk var1) {
      return switch (BlockUtil.method_1071()) {
         case OVERWORLD -> {
            switch (this.method_2(var1, true)) {
               case NO_PLAINS:
                  yield false;
               case PLAINS_IN_PALETTE:
                  yield true;
               case PLAINS_PRESENT:
                  yield this.method_5(var1);
               default:
                  throw new MatchException(null, null);
            }
         }
         case THE_NETHER, THE_END -> this.method_2(var1, false) == Class_0404.PLAINS_IN_PALETTE;
      };
   }

   public boolean method_2(Palette<?> var1) {
      return var1.getSize() <= 0 || !(var1 instanceof LinearPalette) && !(var1 instanceof HashMapPalette);
   }

   public synchronized boolean method_5(PalettedContainer<BlockState> var1) {
      field_3220.clear();
      Palette var2 = PaletteAccess.method_9(var1);
      BitStorage var3 = PaletteAccess.method_2(var1);
      var3.getAll(field_3220::add);
      return var2.getSize() > field_3220.size();
   }

   public boolean method_5(LevelChunk var1) {
      LevelChunkSection[] var2 = var1.getSections();
      if (var2.length == 0) {
         return false;
      } else {
         LevelChunkSection var3 = var2[0];
         Palette var4 = PaletteAccess.method_9(var3.getStates());
         if (this.method_2(var4)) {
            return false;
         } else if (var4 instanceof LinearPalette) {
            return ((BlockState)var4.valueFor(0)).getBlock() == Blocks.AIR;
         } else {
            for (int var5 = 0; var5 < Math.min(var2.length, 3); var5++) {
               LevelChunkSection var6 = var2[var5];
               PalettedContainer var7 = var6.getStates();
               Palette var8 = PaletteAccess.method_9(var7);
               if (!this.method_2(var8) && this.method_5(var7)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public synchronized Class_0404 method_2(LevelChunk var1, boolean var2) {
      LevelChunkSection[] var3 = var1.getSections();
      if (var3.length == 0) {
         return Class_0404.NO_PLAINS;
      } else {
         LevelChunkSection var4 = var3[0];
         if (var4.getBiomes() instanceof PalettedContainer var6) {
            Palette<net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome>> var7 = PaletteAccess.method_9(var6);
            boolean var8 = var7.maybeHas(var0 -> var0.is(Biomes.PLAINS));
            if (var8 && var2) {
               if (var7.getSize() == 1) {
                  return Class_0404.PLAINS_PRESENT;
               }

               BitStorage var9 = PaletteAccess.method_2(var6);
               field_3220.clear();
               var9.getAll(field_3220::add);
               IntIterator var10 = field_3220.iterator();

               while (var10.hasNext()) {
                  int var11 = (Integer)var10.next();
                  if (((Holder)var7.valueFor(var11)).is(Biomes.PLAINS)) {
                     return Class_0404.PLAINS_PRESENT;
                  }
               }
            }

            if (var8) {
               return Class_0404.PLAINS_IN_PALETTE;
            }
         }

         return Class_0404.NO_PLAINS;
      }
   }
}
