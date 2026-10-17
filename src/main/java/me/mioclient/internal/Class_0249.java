package me.mioclient.internal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import me.mioclient.Hub;
import me.mioclient.module.player.RotationLockModule;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public class Class_0249 extends ChatCommand {
   public static final RotationLockModule field_711 = Hub.field_2595.method_78(RotationLockModule.class);

   public Class_0249() {
      super("angle");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.then(
         RequiredArgumentBuilder.<SharedSuggestionProvider, Vec3>argument("pos", new PositionArgumentType()).then(LiteralArgumentBuilder.<SharedSuggestionProvider>literal("yawlock").executes(var0 -> {
            float[] var1x = RotationManager.method_78((Vec3)var0.getArgument("pos", Vec3.class));
            field_711.value.method_78(var1x[0]);
            return 1;
         })).executes(var0 -> {
            float[] var1x = RotationManager.method_78((Vec3)var0.getArgument("pos", Vec3.class));
            ChatUtil.method_2(Component.literal("Calculated angle. Yaw: %.1f, Pitch: %.1f.".formatted(var1x[0], var1x[1])), ChatUtil.method_38(-1));
            return 1;
         })
      );
   }
}
