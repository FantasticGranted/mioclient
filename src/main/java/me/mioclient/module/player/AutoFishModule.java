package me.mioclient.module.player;

import java.util.concurrent.TimeUnit;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class AutoFishModule extends Module {
   public Setting<Float> castDelay;
   public final Timer field_3087;

   public AutoFishModule() {
      super("AutoFish", "Fishes.", Category.PLAYER);
      Settings.initialize(this);
      this.field_3087 = new Timer();
   }

   @Override
   public void onEnable() {
      this.field_3087.reset();
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundSoundPacket var2
         && var2.getSound().is(var0 -> var0.identifier().toString().equalsIgnoreCase("minecraft:entity.fishing_bobber.splash"))
         && mc.player.fishing != null
         && new Vec3(var2.getX(), var2.getY(), var2.getZ()).distanceTo(mc.player.fishing.position()) <= 4.0) {
         mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
         mc.player.swing(InteractionHand.MAIN_HAND);
         this.field_3087.reset();
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (mc.player.getMainHandItem().is(Items.FISHING_ROD)) {
         if (this.field_3087.method_2(1.0, TimeUnit.MINUTES)
            || mc.player.fishing == null && this.field_3087.method_2((double)this.castDelay.getValue().floatValue(), TimeUnit.SECONDS)) {
            mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
            mc.player.swing(InteractionHand.MAIN_HAND);
            this.field_3087.reset();
         }
      }
   }
}
