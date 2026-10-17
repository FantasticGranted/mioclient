package me.mioclient.module.abstract_;

import me.mioclient.internal.ResizableHudElement;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class AbstractModule_24 extends AbstractModule_26 {
   public static boolean field_1777 = false;

   public AbstractModule_24() {
      super("PlayerModel");
      ResizableHudElement var1 = new ResizableHudElement(this);
      var1.method_2(this);
      this.method_2(var1);
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1) {
      LocalPlayer var2 = mc.player;
      Quaternionf var3 = new Quaternionf().rotateZ(3.1415927F);
      Quaternionf var4 = new Quaternionf().rotateX(0.0F);
      var3.mul(var4);
      Vector3f var5 = new Vector3f(0.0F, var2.getBbHeight() / 2F, 0.0F);
      field_1777 = true;
      InventoryScreen.extractEntityInInventoryFollowsMouse(
         var1, 0, 0, 50, 80, 35, 0.0625F, 0.0F, 0.0F, var2
      );
      field_1777 = false;
   }

   @Override
   public float[] method_31() {
      return new float[]{5e+01F, 8e+01F};
   }
}
