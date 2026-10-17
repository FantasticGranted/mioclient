package me.mioclient.module.abstract_;

import me.mioclient.event.InteractBlockEvent;
import me.mioclient.event.BlockBreakEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import me.mioclient.nick.Settings;

public class AbstractModule_10 extends Module {
   public Setting<Boolean> place;
   public Setting<Boolean> break_;

   public AbstractModule_10() {
      super(
         "NoGlitchBlocks",
         new PlaceholderText()
            .method_2(String.valueOf(ChatFormatting.RED))
            .method_9("Makes sure there are no ghost blocks as you interact with them. \n\u0001Not recommended on 2b2t/Grim servers."),
         Category.PLAYER
      );
      Settings.initialize(this);
   }

   @Subscribe
   public void method_9(InteractBlockEvent var1) {
      InteractionHand var2 = var1.method_12();
      if (this.place.getValue() && mc.player.getItemInHand(var2).getItem() instanceof BlockItem var4) {
         BlockPos var5 = var1.method_382().getBlockPos();
         if (InteractionUtil.method_2(var5.relative(var1.method_382().getDirection()), var4.getBlock(), false)) {
            PacketSender.method_2(var1.method_12(), var1.method_382());
            mc.player.swing(var1.method_12());
            var1.method_463();
         }
      }
   }

   @Subscribe
   public void method_9(BlockBreakEvent var1) {
      if (this.break_.getValue()) {
         var1.method_463();
      }
   }
}
