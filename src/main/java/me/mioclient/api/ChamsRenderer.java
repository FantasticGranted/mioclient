package me.mioclient.api;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.internal.EntityRenderHooks;
import me.mioclient.internal.Colors;
import me.mioclient.module.abstract_.AbstractModule_21;
import me.mioclient.module.render.ChamsModule;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

/** Default Chams rendering: applies fill/outline colors, fade, friend/enemy tint, and draws the entity. */
public interface ChamsRenderer extends MinecraftAccess {
   AbstractModule_21 field_2972 = Hub.field_2595.method_78(AbstractModule_21.class);
   Color field_2973 = new Color(0, 0, 0, 0);

   default void method_2(ChamsModule var1, Entity var2, PoseStack var3) {
      Color[] var4 = this.method_2(var1);
      if (var1.fade.getValue()) {
         float var5 = var1.method_99(var2);
         var4 = new Color[]{
            Colors.method_9(var4[0], (int)(var5 * (float)var4[0].getAlpha())), Colors.method_9(var4[1], (int)(var5 * (float)var4[1].getAlpha()))
         };
      }

      EntityRenderHooks.method_2(var4[0], var4[1]);
      if (var1.friends.getValue() && var2 instanceof Player var6) {
         if (Hub.field_2603.getId(var6)) {
            this.method_2(var1, var4, field_2972.friendColor.getValue());
         }

         if (Hub.field_2603.method_16(var6)) {
            this.method_2(var1, var4, field_2972.enemyColor.getValue());
         }
      }

      EntityRenderHooks.method_16(var1.extraLayer.getValue());
      if (var2 instanceof Player var7 && var1.method_103()) {
         var7.walkAnimation.position = 0.0F;
         var7.walkAnimation.speed = 0.0F;
         var7.walkAnimation.speedOld = 0.0F;
      }

      EntityRenderHooks.method_2(var3, var2);
   }

   default void method_2(ChamsModule var1, Color[] var2, Color var3) {
      EntityRenderHooks.method_2(Colors.method_9(var3, var2[0].getAlpha()), Colors.method_9(var3, var2[1].getAlpha()));
   }

   default Color[] method_2(ChamsModule var1) {
      return new Color[]{var1.outline.getValue(), var1.fill.getValue()};
   }

   default boolean method_670() {
      return false;
   }
}
