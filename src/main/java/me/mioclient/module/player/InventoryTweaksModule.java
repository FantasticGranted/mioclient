package me.mioclient.module.player;

import me.mioclient.event.Event_26;
import me.mioclient.event.Subscribe;
import me.mioclient.mixin.ducks.DuckHandledScreen;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import me.mioclient.nick.Settings;
import org.lwjgl.glfw.GLFW;

public class InventoryTweaksModule extends Module {
   public Setting<Boolean> fastMove;
   public Setting<Boolean> onlyContainer;
   public Setting<Boolean> fastArmor;
   public Setting<Boolean> xCarryTweaks;
   public int field_682;

   public InventoryTweaksModule() {
      super("InventoryTweaks", "Modifies your inventory actions.", Category.PLAYER);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_2(Event_26 var1) {
      if (this.fastMove.getValue()) {
         if (mc.screen instanceof AbstractContainerScreen) {
            if (!this.onlyContainer.getValue()
               || mc.screen instanceof ContainerScreen
               || mc.screen instanceof ShulkerBoxScreen) {
               if (mc.player.containerMenu.getCarried().isEmpty()) {
                  Slot var2 = ((DuckHandledScreen)(Object)mc.screen).mio$getFocusedSlot();
                  if (var2 != null && var2.index != this.field_682 && !var2.getItem().isEmpty()) {
                     byte var3 = -1;
                     if (GLFW.glfwGetMouseButton(mc.getWindow().handle(), 0) == 1) {
                        var3 = 0;
                     }

                     if (GLFW.glfwGetKey(mc.getWindow().handle(), 340) == 1 && var3 != -1) {
                        mc.gameMode
                           .handleContainerInput(mc.player.containerMenu.containerId, var2.index, var3, ContainerInput.QUICK_MOVE, mc.player);
                        this.field_682 = var2.index;
                     }
                  }
               }
            }
         }
      }
   }
}
