package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.mioclient.Hub;
import me.mioclient.api.ChatLineAccess;
import me.mioclient.enum_.Orientation;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.MioGlyphRenderer;
import me.mioclient.internal.ToggleAnimation;
import me.mioclient.module.client.FontsModule;
import me.mioclient.module.misc.BetterChatModule;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent.ChatGraphicsAccess;
import net.minecraft.client.multiplayer.chat.GuiMessage.Line;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
   targets = {"net.minecraft.client.gui.components.ChatComponent$1"}
)
public class MixinChatHudLines {
   private static final NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   private static final BetterChatModule betterchat = Hub.field_2595.method_78(BetterChatModule.class);
   private static final FontsModule fonts = Hub.field_2595.method_78(FontsModule.class);

   public MixinChatHudLines() {
      super();
   }

   @Redirect(
      method = {"accept(Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;IF)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;tag()Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;"
      )
   )
   private GuiMessageTag mio$hideTag(Line var1) {
      if (norender.isToggled() && norender.messageIndicator.getValue()) {
         return null;
      } else {
         return var1.tag();
      }
   }

   @WrapOperation(
      method = {"accept(Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;IF)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;handleMessage(IFLnet/minecraft/util/FormattedCharSequence;)Z"
      )
   )
   private boolean mio$lineRender(ChatGraphicsAccess var1, int var2, float var3, FormattedCharSequence var4, Operation<Boolean> var5, @Local(argsOnly = true) Line var6) {
      float var7 = 0.0F;
      float var8 = 0.0F;
      float var9 = var3;
      ChatLineAccess var10 = (ChatLineAccess)(Object)var6;
      if (betterchat.method_1092()) {
         ToggleAnimation var11 = var10.getProgress();
         var11.method_3(true);
         float var12 = var11.method_45();
         Orientation var13 = betterchat.type.getValue();
         if (var13 == Orientation.VERTICAL || var13 == Orientation.BOTH) {
            var8 = (1.0F - var12) * 9.0F;
         }

         if (var13 == Orientation.HORIZONTAL || var13 == Orientation.BOTH) {
            var7 = -(1.0F - var12) * (float)Minecraft.getInstance().font.width(var4);
         }
      }

      if (betterchat.isToggled() && betterchat.alpha.getValue() > 0) {
         long var14 = System.currentTimeMillis() - var10.mio$getAddTime();
         float var16 = Math.min((float)var14 / (float)betterchat.alpha.getValue().intValue(), 1.0F);
         var9 = Math.max(var3 * var16, 10.0F / 255.0F);
      }

      if (!var1.getClass().getSimpleName().contains("Clickable")) {
         MioGlyphRenderer var17 = FontRenderer.field_3143.method_914();
         if (var17 != null && fonts.isToggled() && fonts.chat.getValue()) {
            final Matrix3x2fStack[] var18 = new Matrix3x2fStack[1];
            var1.updatePose(var19 -> {
               if (var19 instanceof Matrix3x2fStack var20) {
                  var18[0] = var20;
               }
            });
            if (var18[0] != null) {
               int var19 = (int)(Math.min(Math.max(var9, 0.0F), 1.0F) * 255.0F) << 24 | 16777215;
               var17.method_2(var18[0], var4, var7, (float)var2 + var8, var19, true);
               return false;
            }
         }
      }

      if (var7 != 0.0F || var8 != 0.0F) {
         final float var14 = var7;
         final float var15 = var8;
         var1.updatePose(var16 -> var16.translate(var14, var15));
         boolean var16 = (Boolean)var5.call(var1, var2, var9, var4);
         var1.updatePose(var17 -> var17.translate(-var14, -var15));
         return var16;
      } else {
         return (Boolean)var5.call(var1, var2, var9, var4);
      }
   }
}
