package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.mojang.authlib.GameProfile;
import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.enum_.TabLatencyMode;
import me.mioclient.module.misc.ExtraTabModule;
import me.mioclient.module.player.NameProtectModule;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({PlayerTabOverlay.class})
public class MixinPlayerListHud {
   @Shadow
   @Final
   private Minecraft minecraft;
   private static final NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   private static final ExtraTabModule extratab = Hub.field_2595.method_78(ExtraTabModule.class);
   private static final NameProtectModule nameprotect = Hub.field_2595.method_78(NameProtectModule.class);

   public MixinPlayerListHud() {
      super();
   }

   @ModifyExpressionValue(
      method = {"getPlayerInfos"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;PLAYER_COMPARATOR:Ljava/util/Comparator;"
      )}
   )
   private Comparator<PlayerInfo> collectPlayerEntries(Comparator<PlayerInfo> var1) {
      return extratab.isToggled() && extratab.sort.getValue() ? extratab.method_55() : var1;
   }

   @ModifyArg(
      method = {"getPlayerInfos"},
      at = @At(
         value = "INVOKE",
         target = "Ljava/util/stream/Stream;limit(J)Ljava/util/stream/Stream;"
      )
   )
   private long renderHook(long var1) {
      return extratab.isToggled() ? Long.MAX_VALUE : var1;
   }

   @ModifyExpressionValue(
      method = {"extractRenderState"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/PlayerInfo;showHat()Z"
      )}
   )
   private boolean renderHook(boolean var1) {
      return extratab.isToggled() && extratab.betterSkin.getValue() ? true : var1;
   }

   @Inject(
      method = {"getNameForDisplay"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void renderHook2(PlayerInfo var1, CallbackInfoReturnable<Component> var2) {
      if (extratab.isToggled() && extratab.highlight.getValue()) {
         String var3 = ChatFormatting.stripFormatting(((Component)var2.getReturnValue()).getString());
         if (var3 == null) {
            return;
         }

         String var4 = var1.getProfile().name();
         if (extratab.social.getValue()) {
            Color var5 = Hub.field_2603.method_9(var4, null);
            if (var5 != null) {
               var2.setReturnValue(Component.literal(var3).withStyle(var1x -> var1x.withColor(var5.hashCode())));
            }
         }

         if (Minecraft.getInstance().getUser().getName().equals(var4)) {
            var2.setReturnValue(Component.literal(var3).withStyle(var0 -> var0.withColor(extratab.self.getValue().hashCode())));
         }
      }
   }

   @Inject(
      method = {"extractRenderState"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/Minecraft;isLocalServer()Z"
      )}
   )
   private void renderHook(
      GuiGraphicsExtractor var1,
      int var2,
      Scoreboard var3,
      Objective var4,
      CallbackInfo var5,
      @Local(ordinal = 4) LocalIntRef var6,
      @Local(ordinal = 5) LocalIntRef var7,
      @Local(ordinal = 6) LocalIntRef var8,
      @Local(ordinal = 0) List<PlayerInfo> var9
   ) {
      if (extratab.isToggled()) {
         int var10 = var9.size();
         int var11 = var10;

         int var12;
         for (var12 = 1; var11 > 40; var11 = (var10 + var12 - 1) / var12) {
            var12++;
         }

         var8.set(var12);
         var7.set(var11);
         var6.set(var10);
      }
   }

   @Inject(
      method = {"extractPingIcon"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderLatencyIconHook(GuiGraphicsExtractor var1, int var2, int var3, int var4, PlayerInfo var5, CallbackInfo var6) {
      if (extratab.isToggled() && extratab.latency.getValue() != TabLatencyMode.VANILLA) {
         if (extratab.latency.getValue() == TabLatencyMode.NONE) {
            var6.cancel();
         } else {
            String var7 = "" + ChatFormatting.YELLOW + var5.getLatency();
            var1.text(this.minecraft.font, var7, var3 + var2 - this.minecraft.font.width(var7), var4, 16777215);
            var6.cancel();
         }
      }
   }

   @ModifyConstant(
      method = {"extractRenderState"},
      constant = {@Constant(
         intValue = 13
      )}
   )
   private int renderConstant(int var1) {
      return extratab.isToggled() && extratab.latency.getValue() == TabLatencyMode.NONE ? 0 : var1;
   }

   @ModifyArgs(
      method = {"extractRenderState"},
      at = @At(
         value = "INVOKE",
         target = "Ljava/lang/Math;max(II)I",
         ordinal = 0
      )
   )
   private void render(Args var1, @Local PlayerInfo var2) {
      if (extratab.isToggled() && extratab.latency.getValue() == TabLatencyMode.TEXT) {
         var1.set(1, (Integer)var1.get(1) + this.minecraft.font.width(String.valueOf(var2.getLatency())));
      }
   }

   @Redirect(
      method = {"extractRenderState"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/Connection;isEncrypted()Z"
      )
   )
   private boolean africanAmerican(Connection var1) {
      return var1.isEncrypted() && !norender.method_280();
   }

   @Inject(
      method = {"extractRenderState"},
      at = {@At("HEAD")}
   )
   private void renderHead(GuiGraphicsExtractor var1, int var2, Scoreboard var3, Objective var4, CallbackInfo var5) {
      if (extratab.method_54() != 1.0F) {
         var1.pose().pushMatrix();
         var1.pose().scale(extratab.method_54(),  extratab.method_54());
      }
   }

   @Inject(
      method = {"extractRenderState"},
      at = {@At("TAIL")}
   )
   private void renderTail(GuiGraphicsExtractor var1, int var2, Scoreboard var3, Objective var4, CallbackInfo var5) {
      if (extratab.method_54() != 1.0F) {
         var1.pose().popMatrix();
      }
   }

   @ModifyVariable(
      method = {"extractRenderState"},
      at = @At("HEAD"),
      argsOnly = true
   )
   private int renderArgs(int var1) {
      return extratab.method_54() == 1.0F ? var1 : (int)((float)var1 / extratab.method_54());
   }

   @Inject(
      method = {"lambda$static$2"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void methodHook(PlayerInfo var0, CallbackInfoReturnable<String> var1) {
      GameProfile var2 = Minecraft.getInstance().player.getGameProfile();
      if (nameprotect.isToggled() && var0.getProfile().equals(var2)) {
         var1.setReturnValue(nameprotect.name.getValue());
      }
   }
}
