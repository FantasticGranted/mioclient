package me.mioclient.module.misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import me.mioclient.Hub;
import me.mioclient.enum_.Priority;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.DisconnectEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.ChunkLoadEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.SoundId;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.ChatUtil;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import me.mioclient.nick.Settings;

public class VisualRangeModule extends Module {
   public static final int field_3603 = -Math.abs("EnderPearl".hashCode());
   public Setting<Boolean> join;
   public Setting<Boolean> leave;
   public Setting<Boolean> targets;
   public Setting<Boolean> players;
   public Setting<Boolean> friends;
   public Setting<Boolean> enemies;
   public Setting<Boolean> nakeds;
   public Setting<Boolean> signs;
   public Setting<Boolean> enderPearls;
   public Setting<Boolean> playSound;
   public Setting<Boolean> onlyEnemies;
   public Setting<SoundId> sound;
   public Setting<Float> volume;
   public final ArrayList<String> field_3617;
   public final ArrayList<String> field_3618;

   public VisualRangeModule() {
      super("VisualRange", "Informs you of players who enter/leave your visual range.", Category.MISC);
      Settings.initialize(this);
      this.field_3617 = new ArrayList<>();
      this.field_3618 = new ArrayList<>();
      this.setDrawn(false);
   }

   @Override
   public void onDisable() {
      this.field_3617.clear();
      this.field_3618.clear();
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (!this.method_535()) {
         this.field_3618.clear();

         try {
            for (AbstractClientPlayer var3 : mc.level.players()) {
               if (!var3.getName().equals(mc.player.getName()) && this.method_92(var3)) {
                  this.field_3618.add(var3.getName().getString());
               }
            }

            if (!this.field_3617.equals(this.field_3618)) {
               for (String var10 : this.field_3618) {
                  if (!this.field_3617.contains(var10) && this.join.getValue()) {
                     Color var4 = Hub.field_2603.method_9(var10, Color.WHITE);
                     String var5 = "%s%s entered visual range.".formatted(var10, ChatFormatting.WHITE);
                     MutableComponent var6 = Component.literal(var5).withStyle(var1x -> var1x.withColor(var4.hashCode()));
                     ChatUtil.method_2(var6, ChatUtil.method_38(Math.abs(var10.hashCode()) * -1 / 2), Priority.MID);
                     if (!Hub.field_2603.method_1009(var10) && Hub.field_2603.method_289(var10) || !this.onlyEnemies.getValue()) {
                        this.method_387();
                     }
                  }
               }

               for (String var11 : this.field_3617) {
                  if (!this.field_3618.contains(var11) && this.leave.getValue()) {
                     Color var12 = Hub.field_2603.method_9(var11, Color.WHITE);
                     String var13 = "%s%s left visual range.".formatted(var11, ChatFormatting.WHITE);
                     MutableComponent var14 = Component.literal(var13).withStyle(var1x -> var1x.withColor(var12.hashCode()));
                     ChatUtil.method_2(var14, ChatUtil.method_38(Math.abs(var11.hashCode()) * -1 / 2), Priority.LOW);
                  }
               }

               this.field_3617.clear();
               this.field_3617.addAll(this.field_3618);
            }
         } catch (Exception var7) {
         }
      }
   }

   @Subscribe
   public void method_5(ChunkLoadEvent var1) {
      if (this.signs.getValue()) {
         AtomicBoolean var2 = new AtomicBoolean(false);
         var1.method_1021()
            .getBlockEntities()
            .forEach(
               (var1x, var2x) -> {
                  if (var2x instanceof SignBlockEntity var3) {
                     int var4 = -Math.abs(var1x.hashCode());
                     MutableComponent var5 = Component.literal("Found a sign: ");
                     boolean var6 = true;

                     for (Component var10 : var3.getFrontText().getMessages(false)) {
                        if (!ChatFormatting.stripFormatting(var10.getString()).isBlank()) {
                           var5.append(var10);
                           var5.append(" ");
                           var6 = false;
                        }
                     }

                     if (var6) {
                        var5.append("<empty>");
                     }

                     var5.withStyle(var0x -> var0x.applyFormat(ChatFormatting.WHITE));
                     String var11 = "%d, %d, %d".formatted(var1x.getX(), var1x.getY(), var1x.getZ());
                     MutableComponent var12 = Component.literal(new PlaceholderText().method_2((Object)var11).method_9("at \u0001"));
                     HoverEvent var13 = new HoverEvent.ShowText(var12);
                     var5.withStyle(var1xx -> var1xx.withHoverEvent(var13));
                     var5.withStyle(
                        var1xx -> var1xx.withClickEvent(ChatUtil.method_52(new PlaceholderText().method_2(var11.replace(",", "")).method_9("highlight \u0001")))
                     );
                     ChatUtil.method_2(var5, ChatUtil.method_38(var4));
                     var2.set(true);
                  }
               }
            );
         if (var2.get()) {
            this.method_387();
         }
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundAddEntityPacket var2 && var2.getType() == EntityType.ENDER_PEARL && this.enderPearls.getValue()) {
         MutableComponent var6 = Component.literal("Ender Pearl entered visual range.");
         String var4 = "%.0f, %.0f, %.0f".formatted(var2.getX(), var2.getY(), var2.getZ());
         MutableComponent var5 = Component.literal(new PlaceholderText().method_2((Object)var4).method_9("at \u0001"));
         var6.withStyle(var1x -> var1x.withHoverEvent(new HoverEvent.ShowText(var5)));
         var6.withStyle(var1x -> var1x.withClickEvent(ChatUtil.method_52(new PlaceholderText().method_2(var4.replace(",", "")).method_9("highlight \u0001"))));
         ChatUtil.method_2(var6, ChatUtil.method_38(field_3603), Priority.MID);
         this.method_387();
      }
   }

   public boolean method_92(Player var1) {
      if (!PlayerUtil.method_29((LivingEntity)var1)) {
         return this.nakeds.getValue();
      } else if (Hub.field_2603.getId(var1)) {
         return this.friends.getValue();
      } else {
         return Hub.field_2603.method_16(var1) ? this.enemies.getValue() : this.players.getValue();
      }
   }

   @Subscribe
   public void method_2(DisconnectEvent var1) {
      this.field_3617.clear();
      this.field_3618.clear();
   }

   public void method_387() {
      if (this.playSound.getValue()) {
         Hub.field_2606.method_2(this.sound.getValue()).method_230(this.volume.getValue());
      }
   }
}
