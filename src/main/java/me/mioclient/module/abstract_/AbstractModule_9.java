package me.mioclient.module.abstract_;

import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import me.mioclient.nick.Settings;

public class AbstractModule_9 extends Module {
   public final Timer field_923 = new Timer();
   public final Timer field_924 = new Timer();
   public Setting<Boolean> always;
   public Setting<Boolean> onlyLoaded;
   public Setting<Float> boost;
   public Setting<Integer> delay;

   public AbstractModule_9() {
      super(
         "Trident",
         new PlaceholderText().method_2(String.valueOf(ChatFormatting.RED)).method_9("Improves your experience with tridents. \n\u0001Riptide enchantment required."),
         Category.EXPLOIT
      );
      Settings.initialize(this);
   }

   @Subscribe
   public void method_2(PostTickEvent var1) {
      int var2 = this.method_318();
      if (var2 != -1 && !mc.player.isUsingItem()) {
         mc.gameMode
            .handleContainerInput(0, InventoryUtil.getId(var2), mc.player.getInventory().selected, ContainerInput.SWAP, mc.player);
         mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
         mc.gameMode.releaseUsingItem(mc.player);
         mc.gameMode
            .handleContainerInput(0, InventoryUtil.getId(var2), mc.player.getInventory().selected, ContainerInput.SWAP, mc.player);
         this.field_923.reset();
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (this.method_319()) {
         this.field_924.reset();
      }

      mc.player.avatarState().walkDist = 0.0F;
   }

   public int method_318() {
      return this.field_923.method_9((long)(this.delay.getValue() * 50)) && !this.field_924.method_9(700L) ? InventoryUtil.method_9(Items.TRIDENT) : -1;
   }

   public boolean method_319() {
      ClientChunkCache var1 = mc.level.getChunkSource();
      return ChunkPos.rangeClosed(mc.player.chunkPosition(), 2).allMatch(var1x -> var1.hasChunk(var1x.x(), var1x.z())) || !this.onlyLoaded.getValue();
   }

   public float method_320() {
      return !this.isToggled() ? 1F : this.boost.getValue();
   }

   public boolean method_321() {
      return this.isToggled() && this.always.getValue();
   }
}
