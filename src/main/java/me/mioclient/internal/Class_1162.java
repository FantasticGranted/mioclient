package me.mioclient.internal;

import java.util.function.Supplier;
import me.mioclient.api.MinecraftAccess;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

public class Class_1162 extends HudTextElement implements MinecraftAccess {
   public final Holder<MobEffect> field_3598;

   public Class_1162(Holder<MobEffect> var1, Supplier<Boolean> var2) {
      super(
         () -> {
            MobEffectInstance var1x = mc.player.getEffect(var1);
            if (var1x == null) {
               return Component.empty();
            } else {
               String var2x = MobEffectUtil.formatDuration(var1x, 1F, mc.level.tickRateManager().tickrate())
                  .getString();
               if (var2x.startsWith("0")) {
                  var2x = var2x.substring(1);
               }

               String var3 = var1x.getAmplifier() == 0 ? "" : new PlaceholderText().method_2(var1x.getAmplifier() + 1).method_9("\u0001 ");
               return Component.literal("%s %s%s%s".formatted(((MobEffect)var1.value()).getDisplayName().getString(), var3, ChatFormatting.WHITE, var2x));
            }
         },
         var2
      );
      this.field_3598 = var1;
   }

   public Holder<MobEffect> method_1026() {
      return this.field_3598;
   }
}
