package me.mioclient.internal;
import net.minecraft.world.item.ItemStack;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.mioclient.enum_.Class_0060;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.mixin.ducks.DuckMinecraftClient;
import me.mioclient.record.SwapAction;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import me.mioclient.record.IntPair;

public class Class_0985 extends ChatCommand {
   public final Map<Class_0060, List<SwapAction>> field_3031 = new HashMap<>();
   public boolean field_943;

   public Class_0985() {
      super("action");
      lookups.method_14(this);

      for (Class_0060 var4 : Class_0060.values()) {
         this.field_3031.put(var4, new ArrayList<>());
      }
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.then(
         RequiredArgumentBuilder.<SharedSuggestionProvider, Class_0060>argument("swap", new EnumArgumentType<>(Class_0060.class))
               .then(RequiredArgumentBuilder.<SharedSuggestionProvider, Item>argument("item", Class_0624.registry(BuiltInRegistries.ITEM, this::method_2)).executes(var1x -> {
                  Item var2 = (Item)var1x.getArgument("item", Item.class);
                  Class_0060 var3 = (Class_0060)var1x.getArgument("swap", Class_0060.class);
                  IntPair var4 = var3.method_87().method_31(var2);
                  if (var4.method_644()) {
                     this.field_943 = true;
                  }

                  SwapAction var5 = new SwapAction(var3.method_87(), var4);
                  this.field_3031.get(var3).add(var5);
                  if (!var4.method_644()) {
                     var3.method_87().method_2(var4);
                  }

                  return 1;
               })))
            .then(LiteralArgumentBuilder.<SharedSuggestionProvider>literal("back").executes(var1x -> {
               Class_0060 var2 = (Class_0060)var1x.getArgument("swap", Class_0060.class);
               List var3 = this.field_3031.get(var2);
               if (var3.isEmpty()) {
                  return 1;
               } else {
                  SwapAction var4 = (SwapAction)var3.removeLast();
                  if (var4.getSlot().method_644()) {
                     return 1;
                  } else {
                     var4.getHandler().method_9(var4.slot);
                     return 1;
                  }
               }
            })
      );
      var1.then(LiteralArgumentBuilder.<SharedSuggestionProvider>literal("attack").executes(var1x -> {
         if (!this.field_943) {
            ((DuckMinecraftClient)(Object)mc).attack();
         }

         return 1;
      }));
      var1.then(LiteralArgumentBuilder.<SharedSuggestionProvider>literal("interact").executes(var1x -> {
         if (!this.field_943) {
            ((DuckMinecraftClient)(Object)mc).interact();
         }

         return 1;
      }));
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      this.field_943 = false;
   }

   public boolean method_2(CommandContext<?> var1, Item var2) {
      Class_0060 var3 = (Class_0060)var1.getArgument("swap", Class_0060.class);
      return var3.method_78(var2);
   }

   public static boolean method_38(Item var0) {
      net.minecraft.world.item.equipment.Equippable var1 = new ItemStack(var0).get(net.minecraft.core.component.DataComponents.EQUIPPABLE);
      if (var1 == null) {
         return var0 == Items.ELYTRA;
      } else {
         return !var1.allowedEntities().isPresent() && (var1.slot().isArmor() || var0 == Items.ELYTRA);
      }
   }
}
