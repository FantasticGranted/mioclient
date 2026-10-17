package me.mioclient.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Objects;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.record.Vec2i;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

public class Class_0584 implements MinecraftAccess {
   public final ToggleAnimation progress = new ToggleAnimation(2F);
   public final ItemStack field_1846;
   public final Vec2i field_1847;
   public final Vec2i field_1848;

   public Class_0584(ItemStack var1, Vec2i var2, Vec2i var3) {
      super();
      this.field_1846 = var1.copy();
      this.field_1847 = var3;
      this.field_1848 = var2;
      this.progress.method_36(false);
   }

   public void method_5(GuiGraphicsExtractor var1) {
      this.progress.method_3(true);
      float var2 = this.progress.method_45();
      int var3 = (int)((float)this.field_1848.method_401() + (float)(this.field_1847.method_401() - this.field_1848.method_401()) * var2);
      int var4 = (int)((float)this.field_1848.getY() + (float)(this.field_1847.getY() - this.field_1848.getY()) * var2);
      var1.pose().pushMatrix();
      if ((double)var2 > 0.8) {

      }

      var1.item(this.field_1846, var3, var4);
      var1.itemDecorations(mc.font, this.field_1846, var3, var4);
      if ((double)var2 > 0.8) {

      }

      var1.pose().popMatrix();
   }

   public ItemStack method_602() {
      return this.field_1846;
   }

   public Vec2i method_603() {
      return this.field_1847;
   }

   public boolean method_604() {
      return this.progress.method_605() == 2F;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         Class_0584 var2 = (Class_0584)var1;
         return Objects.equals(this.field_1846.getItem(), var2.field_1846.getItem()) && Objects.equals(this.field_1847, var2.field_1847);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.field_1846.getItem(), this.field_1847);
   }
}
