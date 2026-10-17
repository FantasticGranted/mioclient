package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.awt.Color;
import java.util.Optional;
import me.mioclient.Hub;
import me.mioclient.internal.MioGlyphRenderer;
import me.mioclient.internal.FontRenderer;
import me.mioclient.module.client.FontsModule;
import me.mioclient.module.misc.BetterChatModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({ComponentRenderUtils.class})
public class MixinChatMessages {
   private static final FontsModule fonts = Hub.field_2595.method_78(FontsModule.class);
   @Unique
   private static final BetterChatModule betterchat = Hub.field_2595.method_78(BetterChatModule.class);

   public MixinChatMessages() {
      super();
   }

   @ModifyExpressionValue(
      method = {"wrapComponents"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/Font;getSplitter()Lnet/minecraft/client/StringSplitter;"
      )}
   )
   private static StringSplitter mio$breakRenderedChatMessageLines(StringSplitter var0) {
      MioGlyphRenderer var1 = FontRenderer.field_3143.method_914();
      return (StringSplitter)(var1 != null && fonts.isToggled() && fonts.chat.getValue() ? var1.method_477() : var0);
   }

   @ModifyVariable(
      method = {"wrapComponents"},
      at = @At("HEAD"),
      ordinal = 0,
      argsOnly = true
   )
   private static FormattedText mio$breakRenderedChatMessageLines(FormattedText var0) {
      if (betterchat.isToggled() && betterchat.highlight.getValue()) {
         String var1 = Minecraft.getInstance().getUser().getName();
         MutableComponent var2 = Component.empty();
         var0.visit(
            (var2x, var3) -> {
               if (!var3.contains(var1)) {
                  var2.append(Component.literal(var3).setStyle(var2x));
               } else {
                  String[] var4 = var3.split("((?=%s)|(?<=%s))".formatted(var1, var1));

                  for (String var8 : var4) {
                     if (!var8.equals(var1)) {
                        var2.append(Component.literal(var8).setStyle(var2x));
                     } else {
                        Color var9 = betterchat.color.getValue();
                        MutableComponent var10 = Component.literal(var8)
                           .withStyle(var1xx -> var1xx.withColor(new Color(var9.getRed(), var9.getGreen(), var9.getBlue(), 255).getRGB()));
                        var10.withStyle(var1xx -> {
                           var1xx.withBold(var2x.isBold());
                           var1xx.withItalic(var2x.isItalic());
                           var1xx.withUnderlined(var2x.isUnderlined());
                           var1xx.withObfuscated(var2x.isObfuscated());
                           var1xx.withStrikethrough(var2x.isStrikethrough());
                           return var1xx;
                        });
                        var2.append(var10);
                     }
                  }
               }

               return Optional.empty();
            },
            Style.EMPTY
         );
         return var2;
      } else {
         return var0;
      }
   }
}
