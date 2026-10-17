package me.mioclient.internal;

import me.mioclient.baritone.api.BaritoneAPI;
import me.mioclient.baritone.api.IBaritone;
import me.mioclient.baritone.api.pathing.goals.GoalXZ;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.phys.Vec3;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

public class Class_0412 extends ChatCommand {
   public Class_0412() {
      super("walk");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.then(
         RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("name", StringArgumentType.string())
            .suggests((CommandContext<SharedSuggestionProvider> var0, SuggestionsBuilder var1x) -> WaypointCommand.method_2(var0, var1x, WaypointCommand.method_1054()))
            .executes(var0 -> {
               Vec3 var1x = WaypointCommand.method_107(var0);
               if (var1x == null) {
                  return 0;
               } else {
                  IBaritone var2 = BaritoneAPI.getProvider().getPrimaryBaritone();
                  GoalXZ var3 = new GoalXZ((int)var1x.x(), (int)var1x.z());
                  var2.getCustomGoalProcess().setGoalAndPath(var3);
                  return 1;
               }
            })
      );
   }
}
