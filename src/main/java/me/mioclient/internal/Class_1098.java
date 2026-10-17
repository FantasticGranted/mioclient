package me.mioclient.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import me.mioclient.api.RegistryCompleter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;

public class Class_1098 implements RegistryCompleter {
   public static final Map<String, List<String>> field_3410 = new HashMap<>();

   public Class_1098() {
      super();
   }

   @Override
   public Collection<String> method_114(String var1) {
      return field_3410.getOrDefault(var1.toLowerCase(), Collections.emptyList());
   }

   @Override
   public Collection<String> method_115() {
      return field_3410.keySet();
   }

   @Override
   public Identifier method_116() {
      return Registries.BLOCK.identifier();
   }

   public static void method_2(String var0, Stream<Block> var1) {
      field_3410.put(var0, var1.map(Class_1098::method_9).toList());
   }

   public static String method_9(Block var0) {
      return BuiltInRegistries.BLOCK.getKey(var0).toShortLanguageKey();
   }

   static {
      method_2("beds", BuiltInRegistries.BLOCK.stream().filter(var0 -> var0 instanceof BedBlock));
      method_2("banners", BuiltInRegistries.BLOCK.stream().filter(var0 -> var0 instanceof BannerBlock));
      method_2("doors", BuiltInRegistries.BLOCK.stream().filter(var0 -> var0 instanceof DoorBlock));
      method_2("slabs", BuiltInRegistries.BLOCK.stream().filter(var0 -> var0 instanceof SlabBlock));
      method_2("shulkers", BuiltInRegistries.BLOCK.stream().filter(var0 -> var0 instanceof ShulkerBoxBlock));
      method_2("carpets", BuiltInRegistries.BLOCK.stream().filter(var0 -> var0 instanceof CarpetBlock));
      method_2("glasses", BuiltInRegistries.BLOCK.stream().filter(var0 -> var0 instanceof StainedGlassBlock || var0 == Blocks.GLASS));
      method_2("glass_panes", BuiltInRegistries.BLOCK.stream().filter(var0 -> var0 instanceof StainedGlassPaneBlock || var0 == Blocks.GLASS_PANE));
      method_2("signs", BuiltInRegistries.BLOCK.stream().filter(var0 -> var0 instanceof SignBlock));
      method_2(
         "ores",
         BuiltInRegistries.BLOCK.stream().filter(var0 -> BuiltInRegistries.BLOCK.getKey(var0).toLanguageKey().contains("ore"))
      );
   }
}
