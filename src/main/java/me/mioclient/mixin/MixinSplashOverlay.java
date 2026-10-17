package me.mioclient.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Consumer;
import me.mioclient.enum_.KeyboardLayout;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LoadingOverlay.class})
public abstract class MixinSplashOverlay extends Overlay {
   @Shadow
   private long fadeOutStart;
   @Shadow
   @Final
   private Minecraft minecraft;
   @Unique
   private KeyboardLayout flag;

   public MixinSplashOverlay() {
      super();
   }

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void initHook(Minecraft var1, ReloadInstance var2, Consumer<?> var3, boolean var4, CallbackInfo var5) {
      try {
         HttpRequest var6 = HttpRequest.newBuilder().uri(new URI("https://api.country.is/")).timeout(Duration.of(3L, ChronoUnit.SECONDS)).GET().build();
         HttpResponse var7 = HttpClient.newHttpClient().send(var6, BodyHandlers.ofString());
         String var8 = (String)var7.body();
         JsonObject var9 = JsonParser.parseString(var8).getAsJsonObject();
         if (var9.has("country")) {
            this.flag = KeyboardLayout.method_35(var9.get("country").getAsString());
         }
      } catch (Throwable var10) {
         this.flag = KeyboardLayout.DEFAULT;
      }

      Calendar var11 = Calendar.getInstance();
      var11.setTime(new Date());
      int var12 = var11.get(2);
      int var13 = var11.get(5);
      if (var12 == 1 && var13 == 23 || var12 == 4 && var13 == 9) {
         this.flag = KeyboardLayout.field_3728;
      }
   }

   @Shadow
   private static int replaceAlpha(int var0, int var1) {
      return 0;
   }

   @Inject(
      method = {"extractRenderState"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;guiWidth()I",
         ordinal = 1,
         shift = Shift.AFTER
      )}
   )
   private void renderHook(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      int var6 = this.minecraft.getWindow().getGuiScaledWidth();
      int var7 = this.minecraft.getWindow().getGuiScaledHeight();
      int var8 = Mth.ceil(
         (1.0F - Mth.clamp((this.fadeOutStart > -1L ? (float)(Util.getMillis() - this.fadeOutStart) / 1000.0F : -1.0F) - 1.0F, 0.0F, 1.0F))
            * 255.0F
      );

      for (int var9 = 0; var9 < this.flag.method_492().size(); var9++) {
         var1.fill(
            0,
            var9 * (var7 / this.flag.method_492().size()),
            var6,
            (var9 + 1) * (var7 / this.flag.method_492().size()),
            replaceAlpha(this.flag.method_492().get(var9).hashCode(), var8)
         );
      }
   }
}
