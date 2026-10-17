package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.ChestStealMode;
import me.mioclient.mixin.ducks.DuckHandledScreen;
import me.mioclient.module.player.ChestStealerModule;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.AfterInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.gui.components.Button.Builder;
import net.minecraft.network.chat.Component;

public class Class_1308 implements MinecraftAccess, AfterInit {
   public final ChestStealerModule field_4217;

   public Class_1308(ChestStealerModule var1) {
      super();
      this.field_4217 = var1;
   }

   public void afterInit(Minecraft var1, Screen var2, int var3, int var4) {
      if ((var2 instanceof ContainerScreen || var2 instanceof ShulkerBoxScreen) && this.field_4217.isToggled() && this.field_4217.onlyButtons.getValue()) {
         this.field_4217.method_2((ChestStealMode)null);
         int var5 = Class_0245.field_684;
         int var6 = (var2.width - ((DuckHandledScreen)var2).getBgWidth()) / 2 - var5 - 2;
         int var7 = (var2.height - ((DuckHandledScreen)var2).getBgHeight()) / 2;

         for (ChestStealMode var11 : ChestStealMode.values()) {
            Builder var12 = new Builder(Component.literal(var11.getName()), var3x -> {
               if (var11 == this.field_4217.method_933()) {
                  Hub.field_2619.method_2(() -> var2.setFocused(null), 0);
                  this.field_4217.method_2((ChestStealMode)null);
               } else {
                  this.field_4217.method_2(var11);
               }
            });
            var12.bounds(var6, var7, var5, 15);
            var7 += 17;
            ((java.util.List)var2.children()).add(var12.build());
         }
      }
   }
}
