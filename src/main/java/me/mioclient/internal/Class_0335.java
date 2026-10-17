package me.mioclient.internal;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.StatusOnly;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;

public final class Class_0335 extends ChatCommand {
   public Class_0335() {
      super("clip");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      ((LiteralArgumentBuilder<SharedSuggestionProvider>)var1.then(
            LiteralArgumentBuilder.<SharedSuggestionProvider>literal("v")
               .then(
                  RequiredArgumentBuilder.<SharedSuggestionProvider, Float>argument("value", FloatArgumentType.floatArg())
                     .executes(
                        var0 -> {
                           float var1x = Math.clamp(
                              (Float)var0.getArgument("value", Float.class), -3e+07F, 3e+07F
                           );
                           int var2 = (int)Math.ceil((double)Math.abs(var1x / 1e+01F));
                           if (var2 > 20) {
                              var2 = 1;
                           }

                           if (mc.player.isPassenger()) {
                              for (int var4 = 0; var4 < var2 - 1; var4++) {
                                 PacketSender.method_2(new ServerboundMoveVehiclePacket(mc.player.getVehicle().position(), mc.player.getVehicle().getYRot(), mc.player.getVehicle().getXRot(), mc.player.getVehicle().onGround()));
                              }

                              mc.player
                                 .getVehicle()
                                 .setPos(
                                    mc.player.getVehicle().getX(),
                                    mc.player.getVehicle().getY() + (double)var1x,
                                    mc.player.getVehicle().getZ()
                                 );
                              return 1;
                           } else {
                              for (int var3 = 0; var3 < var2 - 1; var3++) {
                                 PacketSender.method_2(new StatusOnly(true, true));
                              }

                              mc.player.setPos(mc.player.getX(), mc.player.getY() + (double)var1x, mc.player.getZ());
                              PacketSender.method_2(mc.player.getX(), mc.player.getY() + (double)var1x, mc.player.getZ(), true);
                              return 1;
                           }
                        }
                     )
               )
         ))
         .then(
            LiteralArgumentBuilder.<SharedSuggestionProvider>literal("h")
               .then(
                  RequiredArgumentBuilder.<SharedSuggestionProvider, Float>argument("value", FloatArgumentType.floatArg())
                     .executes(
                        var0 -> {
                           float var1x = Math.clamp(
                              (Float)var0.getArgument("value", Float.class), -3e+07F, 3e+07F
                           );
                           double var2 = Math.cos(Math.toRadians((double)(mc.player.getYRot() + (float)Class_0245.field_685)));
                           double var4 = Math.sin(Math.toRadians((double)(mc.player.getYRot() + (float)Class_0245.field_685)));
                           if (mc.player.isPassenger()) {
                              mc.player
                                 .getVehicle()
                                 .setPos(
                                    mc.player.getVehicle().getX() + (double)var1x * var2,
                                    mc.player.getVehicle().getY(),
                                    mc.player.getVehicle().getZ() + (double)var1x * var4
                                 );
                              return 1;
                           } else {
                              mc.player
                                 .setPos(
                                    mc.player.getX() + (double)var1x * var2, mc.player.getY(), mc.player.getZ() + (double)var1x * var4
                                 );
                              PacketSender.method_2(
                                 mc.player.getX() + (double)var1x * var2,
                                 mc.player.getY(),
                                 mc.player.getZ() + (double)var1x * var4,
                                 true
                              );
                              return 1;
                           }
                        }
                     )
               )
         );
   }
}
