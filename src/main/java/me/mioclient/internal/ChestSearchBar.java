package me.mioclient.internal;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.awt.Color;
import java.util.Locale;
import java.util.regex.Pattern;
import me.mioclient.Hub;
import me.mioclient.api.StringMatcher;
import me.mioclient.mixin.ducks.DuckHandledScreen;
import me.mioclient.module.misc.ChestSearchBarModule;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import com.mojang.blaze3d.opengl.GlStateManager;

/** ChestSearchBar module overlay: a text field over container screens that highlights matching items. */
public class ChestSearchBar {
   public static final ChestSearchBar field_3024 = new ChestSearchBar();
   public static ChestSearchBarModule field_3025 = Hub.field_2595.method_78(ChestSearchBarModule.class);
   public static EditBox field_3026;
   public static String text = "";
   public static int field_3027 = 0;

   public ChestSearchBar() {
      super();
   }

   public void init() {
      field_3026 = null;
      text = "";
      ScreenEvents.AFTER_INIT.register(new Class_0665());
   }

   public void onDrawGui(GuiGraphicsExtractor var1, Screen var2, PoseStack var3, float var4, float var5, float var6) {
      if (field_3025.isToggled() && field_3026 != null) {
         var3.pushPose();
         var3.translate(0.0F, 0.0F, 5e+02F);
         DuckHandledScreen var7 = (DuckHandledScreen)(Object)var2;
         if (!field_3026.getValue().isBlank() && var2 instanceof AbstractContainerScreen var8) {
            AbstractContainerMenu var9 = var8.getMenu();
            int var10 = var7.getX();
            int var11 = var7.getY();
            field_3027 = 0;

            for (Slot var13 : var9.slots) {
               int var14 = var10 + var13.x;
               int var15 = var11 + var13.y;
               ItemStack var16 = var13.getItem();
               if (!namesMatch(var16, field_3026.getValue())) {
                  GlStateManager._disableDepthTest();
                  RenderUtil.field_2672.method_2(var3, (float)var14, (float)var15, (float)(var14 + 16), (float)(var15 + 16), -1442840576);
               } else {
                  field_3027++;
               }

               if (isFull(var16, field_3026.getValue())) {
                  var1.fill(net.minecraft.client.renderer.RenderPipelines.GUI, var14, var15, var14 + 16, var15 + 16, Colors.method_5(Color.green, 100));
               }
            }
         }

         if (field_3027 == 0 && !field_3026.getValue().isEmpty()) {
            field_3026.setTextColor(16733525);
         } else {
            field_3026.setTextColor(16777215);
         }

         // TODO(runtime): Lighting is UBO-based in 26.1
         // Lighting.setupFor3DItems();
         var3.popPose();
      }
   }

   public static boolean namesMatch(ItemStack var0, String var1) {
      var1 = ChatFormatting.stripFormatting(var1.trim().toLowerCase(Locale.ROOT));
      if (var1 == null || var1.isEmpty()) {
         return true;
      } else if (var0.isEmpty()) {
         return false;
      } else {
         Item var2 = var0.getItem();
         if (var2.getDescriptionId().contains("shulker_box")) {
            ItemContainerContents var3 = (ItemContainerContents)var0.get(DataComponents.CONTAINER);
            if (var3 != null) {
               for (net.minecraft.world.item.ItemStackTemplate var6 : var3.nonEmptyItems()) {
                  ItemStack var5 = var6.create();
                  if (namesMatch(var5, var1)) {
                     return true;
                  }
               }
            }
         }

         String var11 = var0.getHoverName().getString();
         var11 = ChatFormatting.stripFormatting(var11.trim().toLowerCase(Locale.ROOT));
         StringMatcher var13 = String::contains;
         if (var1.length() >= 3 && var1.startsWith("\"") && var1.endsWith("\"")) {
            var1 = var1.substring(1, var1.length() - 1);
            var13 = String::equals;
         }

         if (var1.length() >= 3 && var1.startsWith("/") && var1.endsWith("/")) {
            var1 = var1.substring(1, var1.length() - 1);
            var13 = (var0x, var1x) -> Pattern.compile(var1x).matcher(var0x).find();
         }

         ItemEnchantments var14 = (ItemEnchantments)var0.get(DataComponents.STORED_ENCHANTMENTS);
         if (var14 != null) {
            for (Entry var7 : var14.entrySet()) {
               if (testEnchant(var13, var1, var7)) {
                  return true;
               }
            }
         }

         if (var0.isEnchanted()) {
            for (Entry var17 : var0.getEnchantments().entrySet()) {
               if (testEnchant(var13, var1, var17)) {
                  return true;
               }
            }
         }

         if (var13.test(I18n.get(var2.getDescriptionId(), new Object[0]).toLowerCase(Locale.ROOT), var1)) {
            return true;
         } else {
            PotionContents var16 = (PotionContents)var0.get(DataComponents.POTION_CONTENTS);
            if (var16 != null) {
               for (MobEffectInstance var8 : var16.getAllEffects()) {
                  String var9 = I18n.get(var8.getDescriptionId(), new Object[0]);
                  if (var13.test(var9.toLowerCase(Locale.ROOT), var1)) {
                     return true;
                  }
               }
            }

            return var13.test(var11, var1);
         }
      }
   }

   public static boolean testEnchant(StringMatcher var0, String var1, Entry<Holder<Enchantment>> var2) {
      if (var2 == null) {
         return false;
      } else {
         MutableComponent var3 = (MutableComponent)Enchantment.getFullname((Holder)var2.getKey(), var2.getIntValue());
         String var4 = var3.toString().toLowerCase(Locale.ROOT);
         var4 = var4.replaceAll("enchantment", "");
         return var0.test(var4, var1);
      }
   }

   public static boolean isFull(ItemStack var0, String var1) {
      if (!field_3025.highlightFull.getValue()) {
         return false;
      } else {
         var1 = ChatFormatting.stripFormatting(var1.trim().toLowerCase(Locale.ROOT));
         if (var1 != null && !var1.isEmpty()) {
            if (var0.isEmpty()) {
               return false;
            } else {
               Item var2 = var0.getItem();
               if (!var2.getDescriptionId().contains("shulker_box")) {
                  return false;
               } else {
                  ItemContainerContents var3 = (ItemContainerContents)var0.get(DataComponents.CONTAINER);
                  if (var3 == null) {
                     return false;
                  } else if (var3.allItemsCopyStream().toList().size() != 27) {
                     return false;
                  } else {
                     for (ItemStack var5 : var3.allItemsCopyStream().toList()) {
                        if (!namesMatch(var5, var1)) {
                           return false;
                        }
                     }

                     return true;
                  }
               }
            }
         } else {
            return false;
         }
      }
   }
}
