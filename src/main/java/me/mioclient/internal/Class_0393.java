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

public class Class_0393 extends ChatCommand {
   public Class_0393() {
      super("elytra");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.then(
         RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("name", StringArgumentType.string())
            .suggests((CommandContext<SharedSuggestionProvider> var0, SuggestionsBuilder var1x) -> WaypointCommand.method_2(var0, var1x, WaypointCommand.method_1054()))
            .executes(var1x -> {
               Vec3 var2 = WaypointCommand.method_107(var1x);
               if (var2 == null) {
                  return 0;
               } else {
                  IBaritone var3 = BaritoneAPI.getProvider().getPrimaryBaritone();
                  GoalXZ var4 = new GoalXZ((int)var2.x(), (int)var2.z());
                  var3.getCustomGoalProcess().setGoal(var4);
                  var3.getElytraProcess().pathTo(net.minecraft.core.BlockPos.containing(var2));
                  if (mc.player.isFallFlying()) {
                     return 1;
                  } else {
                     if (mc.player.onGround()) {
                        mc.player.push(0.0, 0.42, 0.0);
                     }

                     this.method_2(() -> {
                        PacketSender.method_1099();
                        mc.player.startFallFlying();
                     });
                     return 1;
                  }
               }
            })
      );
   }
}
