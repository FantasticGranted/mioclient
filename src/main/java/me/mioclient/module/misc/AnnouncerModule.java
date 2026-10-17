package me.mioclient.module.misc;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import me.mioclient.Hub;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.FinishUsingItemEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.BlockBreakEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.MathUtil;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.ChatUtil;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.Entry;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import me.mioclient.nick.Settings;

public class AnnouncerModule extends Module {
   public Setting<Boolean> clientSide;
   public Setting<Boolean> compact;
   public Setting<Boolean> joins;
   public Setting<Boolean> friends;
   public Setting<Boolean> enemies;
   public Setting<Boolean> others;
   public Setting<Boolean> move;
   public Setting<Boolean> break_;
   public Setting<Boolean> eat;
   public Setting<Double> delay;
   public final Timer field_1418;
   public double field_1419;
   public int field_1420;
   public int field_1421;

   public AnnouncerModule() {
      super("Announcer", "Lets the others know what you're doing.", Category.MISC);
      Settings.initialize(this);
      this.field_1418 = new Timer();
      this.setDrawn(false);
   }

   @Override
   public void onEnable() {
      this.field_1420 = 0;
      this.field_1421 = 0;
      this.field_1418.reset();
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundChatPacket) {
         this.field_1418.reset();
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (this.joins.getValue() && this.field_1418.method_2(this.delay.getValue(), TimeUnit.SECONDS)) {
         if (var1.method_127() instanceof ClientboundPlayerInfoUpdatePacket var2) {
            for (Entry var4 : var2.newEntries()) {
               if (var4.listed()) {
                  String var5 = var4.profile().name();
                  if (!var5.equals(mc.player.getName().getString())) {
                     boolean var6 = Hub.field_2603.method_1009(var5);
                     boolean var7 = Hub.field_2603.method_289(var5);
                     if ((this.friends.getValue() || !var6) && (this.enemies.getValue() || !var7) && (this.others.getValue() || var6 || var7)) {
                        if (this.clientSide.getValue() && this.compact.getValue()) {
                           var5 = new PlaceholderText().method_2((Object)var5).method_9("\u0001 joined");
                           if (var6) {
                              var5 = new PlaceholderText().method_2((Object)var5).method_9("\u0001 [friend]");
                           } else if (var7) {
                              var5 = new PlaceholderText().method_2((Object)var5).method_9("\u0001 [enemy]");
                           }

                           final String var5_a = var5;
                           mc.executeIfPossible(() -> this.method_2(var5_a, ChatUtil.method_38(-254721)));
                        } else {
                           if (var6) {
                              var5 = new PlaceholderText().method_2((Object)var5).method_9("My friend \u0001");
                           }

                           final String var5_b = var5;
                           mc.executeIfPossible(
                              () -> this.method_2(
                                    new PlaceholderText().method_2((Object)var5_b).method_9("\u0001 has joined the game!"), ChatUtil.method_38(-254721)
                                 )
                           );
                        }

                        this.field_1418.reset();
                     }
                  }
               }
            }
         }

         if (var1.method_127() instanceof ClientboundPlayerInfoRemovePacket var9) {
            for (UUID var13 : var9.profileIds()) {
               for (PlayerInfo var16 : mc.player.connection.getListedOnlinePlayers()) {
                  if (var16.getProfile().id().equals(var13)) {
                     String var17 = var16.getProfile().name();
                     if (this.clientSide.getValue() && this.compact.getValue()) {
                        var17 = new PlaceholderText().method_2((Object)var17).method_9("\u0001 left");
                        if (Hub.field_2603.method_1009(var17)) {
                           var17 = new PlaceholderText().method_2((Object)var17).method_9("\u0001 [friend]");
                        } else if (Hub.field_2603.method_289(var17)) {
                           var17 = new PlaceholderText().method_2((Object)var17).method_9("\u0001 [enemy]");
                        }

                        String var19 = var17;
                        mc.executeIfPossible(() -> this.method_2(var19, ChatUtil.method_38(-254722)));
                     } else {
                        if (Hub.field_2603.method_1009(var17)) {
                           var17 = new PlaceholderText().method_2((Object)var17).method_9("My friend \u0001");
                        }

                        String var8 = var17;
                        mc.executeIfPossible(
                           () -> this.method_2(new PlaceholderText().method_2((Object)var8).method_9("\u0001 has left the game!"), ChatUtil.method_38(-254722))
                        );
                     }

                     this.field_1418.reset();
                  }
               }
            }
         }
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (!this.method_535()) {
         this.field_1419 = this.field_1419
            + Hub.field_2614.method_876() / 3.6 / 2e+01;
         if (this.field_1419 >= 1e+03) {
            this.field_1419 = 0.0;
         }

         if (this.move.getValue()
            && this.field_1419 >= 1.0
            && this.field_1418.method_9((long)(1e+03 * this.delay.getValue()))) {
            this.method_483(this.method_480().replace("{blocks}", new DecimalFormat("0.00").format(this.field_1419)));
            this.field_1419 = 0.0;
            this.field_1418.reset();
         }
      }
   }

   @Subscribe
   public void method_2(FinishUsingItemEvent var1) {
      if (!this.method_535()) {
         int var2 = MathUtil.method_7(1, 6);
         if (this.eat.getValue() && var1.method_602().has(DataComponents.FOOD)) {
            this.field_1420++;
            if (this.field_1420 >= var2 && this.field_1418.method_9((long)(1e+03 * this.delay.getValue()))) {
               int var10003 = this.field_1420;
               this.method_483(
                  this.method_482()
                     .replace("{amount}", new PlaceholderText().method_2(var10003).method_9("\u0001"))
                     .replace("{name}", new PlaceholderText().method_2(var1.method_602().getHoverName().getString()).method_9("\u0001"))
               );
               this.field_1420 = 0;
               this.field_1418.reset();
            }
         }
      }
   }

   @Subscribe
   public void method_2(BlockBreakEvent var1) {
      if (!this.method_535()) {
         int var2 = MathUtil.method_7(1, 6);
         this.field_1421++;
         if (this.break_.getValue()
            && this.field_1421 >= var2
            && this.field_1418.method_9((long)(1e+03 * this.delay.getValue()))) {
            String[] var3 = new String[]{
               new PlaceholderText().method_2(InteractionUtil.getId(var1.method_111()).getName().getString()).method_9("\u0001"), "Air", "Bedrock", "Barrier"
            };
            int var10003 = this.field_1421;
            this.method_483(
               this.method_481()
                  .replace("{amount}", new PlaceholderText().method_2(var10003).method_9("\u0001"))
                  .replace("{name}", var3[new Random().nextInt(var3.length)])
            );
            this.field_1421 = 0;
            this.field_1418.reset();
         }
      }
   }

   public String method_480() {
      String[] var1 = new String[]{
         "I just flew over {blocks} blocks thanks to mioclient.me!",
         "Я только что пролетел над {blocks} блоками с помощью mioclient.me!",
         "mioclient.me sayesinde {blocks} blok uçtum!",
         "我刚刚用 mioclient.me 走了 {blocks} 米!",
         "Dank mioclient.me bin ich gerade über {blocks} Blöcke geflogen!",
         "Jag hoppade precis över {blocks} blocks tack vare mioclient.me!",
         "Właśnie przeleciałem ponad {blocks} bloki dzięki mioclient.me!",
         "Es tikko nolidoju {blocks} blokus, paldies mioclient.me!",
         "Я щойно пролетів понад {blocks} блоками завдяки mioclient.me!",
         "I just fwew ovew {blocks} bwoccs thanks to miocwient.me! :3",
         "Ho appena camminato per {blocks} blocchi grazie a mioclient.me!",
         "עכשיו עפתי {blocks} הודות ל mioclient.me!",
         "Právě jsem proletěl {blocks} bloků díky mioclient.me!",
         "Lensin juuri yli {blocks} blokkia mioclient.me:n ansiosta!",
         "Ravnokar sem preletel {blocks} blokov v zahvali mioclient.me!",
         "أنا هلق طاير فوق {blocks} بلوكس بفضل mioclient.me!"
      };
      return var1[new Random().nextInt(var1.length)];
   }

   public String method_481() {
      String[] var1 = new String[]{
         "I just destroyed {amount} {name} with the power of mioclient.me!",
         "Я только что разрушил {amount} {name} с помощью mioclient.me!",
         "Az önce {amount} tane {name} kırdım. TeŞekkürler mioclient.me!",
         "我刚刚用 mioclient.me 破坏了 {amount} {name}!",
         "Ich habe gerade {amount} {name} mit der Kraft von mioclient.me zerstört!",
         "Jag förstörde precis {amount} {name} tack vare mioclient.me!",
         "Właśnie zniszczyłem {amount} {name} za pomocą mioclient.me",
         "Es tikko salauzu {amount} {name} ar spēku mioclient.me!",
         "Я щойно знищив {amount} {name} за допомогою mioclient.me!",
         "I just destwoyed {amount} {name} with the powew of miocwient.me! :3",
         "Ho appena distrutto {amount} {name} grazie al potere di mioclient.me!",
         "בדיוק חצבתי {amount} {name} בעזרת הכוח של mioclient.me!",
         "Právě jsem zničil {amount} {name} díky mioclient.me!",
         "Rikoin juuri {amount} {name}ia mioclient.me:n ansiosta!",
         "Ravnokar sem uničil {amount} {name} z močjo mioclient.me",
         "أنا هلق دمّرت {amount} {name} بقوة mioclient.me!"
      };
      return var1[new Random().nextInt(var1.length)];
   }

   public String method_482() {
      String[] var1 = new String[]{
         "I just ate {amount} {name} thanks to mioclient.me!",
         "Я только что съел {amount} {name} с помощью mioclient.me!",
         "Tam olarak {amount} tane {name} yedim. TeŞekkürler mioclient.me",
         "我刚用 mioclient.me 吃了 {amount} 个 {name}!",
         "Ich habe gerade {amount} {name} dank mioclient.me gegessen!",
         "Jag åt precis {amount} {name} tack vare mioclient.me",
         "Właśnie zjadłem {amount} {name} dzięki mioclient.me",
         "Es tikko apēdu {amount} {name} ar mioclient.me spēku!",
         "Я щойно з’їв {amount} {name} завдяки mioclient.me!",
         "I just ate {amount} {name} thanks to miocwient.me! ^-^",
         "Ho appena mangiato {amount} {name} grazie a mioclient.me!",
         "כרגע אכלתי {amount} {name} הודות לmioclient.me!",
         "Právě jsem snědl {amount} {name} díky mioclient.me",
         "Söin juuri {amount} {name}a mioclient.me:n ansiosta!",
         "Ravnokar sem pojedel {amount} {name} v zahvali mioclient.me",
         "أنا هلق أكلت {amount} {name} بفضل miocliet.me!"
      };
      return var1[new Random().nextInt(var1.length)];
   }

   public void method_483(String var1) {
      this.method_2(var1, ChatUtil.method_2(this));
   }

   public void method_2(String var1, MessageSignature var2) {
      if (this.clientSide.getValue()) {
         ChatUtil.method_2(Component.literal(var1).withStyle(var0 -> var0.withColor(new Color(161, 161, 161).hashCode())), var2);
      } else {
         mc.player.connection.sendChat(var1);
      }
   }
}
