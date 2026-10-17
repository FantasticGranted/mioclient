package me.mioclient.module.abstract_;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.enum_.ScreenCorner;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.HudTextElement;
import me.mioclient.internal.TextHudElement;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.setting.BooleanSetting;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

public class AbstractModule_11 extends AbstractModule_26 {
   public final Setting<Boolean> speed = this.add(new BooleanSetting("Speed", true).method_460());
   public final Setting<Boolean> bPS = this.add(new BooleanSetting("BPS", false).method_2(this.speed));
   public final Setting<Boolean> tPS = this.add(new BooleanSetting("TPS", true));
   public final Setting<Boolean> ping = this.add(new BooleanSetting("Ping", true));
   public final Setting<Boolean> fPS = this.add(new BooleanSetting("FPS", true));
   public final Setting<Boolean> serverBrand = this.add(new BooleanSetting("ServerBrand", true));
   public final Setting<Boolean> durability = this.add(new BooleanSetting("Durability", false));
   public final Setting<Boolean> chest = this.add(new BooleanSetting("Chest", false).method_460());
   public final Setting<Boolean> double_ = this.add(new BooleanSetting("Double", false).method_2(this.chest));
   public final List<HudTextElement> field_3978 = new ArrayList<>();
   public int field_3979;

   public AbstractModule_11() {
      super("Metrics", "infohud", "info");
      this.method_2(new TextHudElement(this, this.field_3978));
      this.method_14().method_2(ScreenCorner.BOTTOM_RIGHT);
   }

   @Override
   public void onEnable() {
      this.field_3978.clear();
      this.field_3978
         .addAll(
            List.of(
               new HudTextElement(
                  () -> Component.literal(
                        new PlaceholderText()
                           .method_2(this.bPS.getValue() ? "b/s" : "km/h")
                           .method_9("Speed %s%.2f\u0001")
                           .formatted(
                              ChatFormatting.WHITE,
                              Hub.field_2614.method_875()
                                 / (this.bPS.getValue() ? 3.6 : 1.0)
                           )
                           .replace(",", ".")
                     ),
                  this.speed::getValue
               ),
               new HudTextElement(
                  () -> Component.literal("TPS %s%.2f".formatted(ChatFormatting.WHITE, Hub.field_2602.method_989()).replace(",", ".")), this.tPS::getValue
               ),
               new HudTextElement(() -> Component.literal("Ping %s%dms".formatted(ChatFormatting.WHITE, Hub.field_2602.method_983())), this.ping::getValue),
               new HudTextElement(() -> Component.literal("FPS %s%d".formatted(ChatFormatting.WHITE, Hub.field_2601.method_814())), this.fPS::getValue),
               new HudTextElement(
                  () -> Component.literal("ServerBrand %s%s".formatted(ChatFormatting.WHITE, mc.player.connection.serverBrand())), this.serverBrand::getValue
               ),
               new HudTextElement(
                  () -> Component.literal(
                        "Durability %s%s"
                           .formatted(ChatFormatting.WHITE, mc.player.getMainHandItem().getMaxDamage() - mc.player.getMainHandItem().getDamageValue())
                     ),
                  () -> this.durability.getValue() && mc.player.getMainHandItem().isDamageableItem()
               ),
               new HudTextElement(() -> Component.literal("Chests %s%d".formatted(ChatFormatting.WHITE, this.field_3979)), this.chest::getValue)
            )
         );
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      this.field_3978.sort(Comparator.comparing(var0 -> -FontRenderer.field_3143.method_221(var0.method_15().getString())));
      this.field_3979 = 0;

      for (BlockEntity var3 : Hub.field_2622.method_347()) {
         if (var3 instanceof ChestBlockEntity || var3 instanceof BarrelBlockEntity) {
            this.field_3979++;
         }
      }

      if (this.double_.getValue()) {
         this.field_3979 /= 2;
      }
   }
}
