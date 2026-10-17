package me.mioclient.module.abstract_;

import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import net.minecraft.ChatFormatting;

public class AbstractModule_38 extends Module {
   public AbstractModule_38() {
      super(
         "NoHitDelay",
         new PlaceholderText().method_2(String.valueOf(ChatFormatting.RED)).method_9("Removes the weapon attack delay. \n\u0001Only works on pre 1.9 servers."),
         Category.COMBAT
      );
   }
}
