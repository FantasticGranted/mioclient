package me.mioclient.module.abstract_;

import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Currency;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.HudTextElement;
import me.mioclient.internal.Timer;
import me.mioclient.internal.TextHudElement;
import me.mioclient.setting.CustomSetting;
import me.mioclient.setting.Setting;
import me.mioclient.setting.StringSetting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class AbstractModule_3 extends AbstractModule_26 {
   public static final String field_3432 = "MioClient/2.0";
   public final Setting<String> coin = this.add(new StringSetting("Coin", "BTC"));
   public final Setting<Currency> fiat = this.add(new CustomSetting<>("Fiat", Currency.USD));
   public final HttpClient field_3435 = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).build();
   public String field_3436 = "0.00";
   public ChatFormatting field_3437 = ChatFormatting.WHITE;
   public final Timer field_3438 = new Timer();

   public AbstractModule_3() {
      super("Crypto");
      this.method_2(
         new TextHudElement(
            this,
            new HudTextElement(() -> Component.literal("%s %s%s".formatted(this.coin.getValue().toUpperCase(), this.field_3437, this.field_3436)), () -> true)
         )
      );
      this.coin.method_9(() -> {
         this.field_3437 = ChatFormatting.WHITE;
         this.field_3436 = "0.00";
         if (!this.method_982()) {
            this.coin.method_78("UNKNOWN");
         } else {
            this.field_3438.setTime(5000L);
         }
      });
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (this.field_3438.method_2(5.0, TimeUnit.SECONDS)) {
         THREAD_POOL.submit(this::method_981);
         this.field_3438.reset();
      }
   }

   public void method_981() {
      if (!this.method_982()) {
         this.coin.method_78("UNKNOWN");
         this.field_3436 = "0.00";
         this.field_3437 = ChatFormatting.WHITE;
      } else {
         String var1 = this.coin.getValue().toUpperCase(Locale.ROOT);
         String var2 = this.fiat.getValue().getName().toUpperCase(Locale.ROOT);

         try {
            URI var3 = new URI("https://api.coinconvert.net/convert/%s/%s?amount=1".formatted(var1, var2));
            HttpRequest var4 = HttpRequest.newBuilder().GET().uri(var3).header("Accept", "application/json").header("User-Agent", "MioClient/2.0").build();
            HttpResponse var5 = this.field_3435.send(var4, BodyHandlers.ofString());
            if (var5.statusCode() != 200) {
               this.field_3436 = "UNKNOWN";
               this.field_3437 = ChatFormatting.WHITE;
               return;
            }

            JsonObject var6 = (JsonObject)MinecraftAccess.GSON.fromJson((String)var5.body(), JsonObject.class);
            if (var6.has("status") && var6.has(var2)) {
               float var7 = var6.get(var2).getAsFloat();
               String var8 = "%.2f".formatted(var7);
               float var9 = Float.parseFloat(var8);
               if (!this.field_3436.equalsIgnoreCase("0.00") && !this.field_3436.equalsIgnoreCase("UNKNOWN")) {
                  this.method_99(var9);
               }

               this.field_3436 = var8;
            }
         } catch (Exception var10) {
         }
      }
   }

   public void method_99(float var1) {
      float var2 = Float.parseFloat(this.field_3436);
      if ((double)Math.abs(var1 - var2) < 0.01) {
         this.field_3437 = ChatFormatting.WHITE;
      } else {
         if (var2 > var1) {
            this.field_3437 = ChatFormatting.RED;
         } else if (var2 < var1) {
            this.field_3437 = ChatFormatting.GREEN;
         }
      }
   }

   public boolean method_982() {
      String var1 = this.coin.getValue();
      return !var1.isEmpty() && var1.matches("[a-zA-Z]+");
   }
}
