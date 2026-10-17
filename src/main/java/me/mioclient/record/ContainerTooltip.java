package me.mioclient.record;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.RenderTooltipEvent;
import me.mioclient.internal.KeybindUtil;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.abstract_.AbstractModule_37;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

/** Tooltip component that previews a container's contents as a 9-wide item grid, drawing map previews for filled maps. */
public final class ContainerTooltip implements MinecraftAccess, ClientTooltipComponent {
   public final List<ItemStack> items;
   public static final AbstractModule_37 previewModule = Hub.field_2595.method_78(AbstractModule_37.class);

   public ContainerTooltip(List<ItemStack> var1) {
      super();
      this.items = var1;
   }

   public int getHeight(Font var1) {
      return 53;
   }

   public int getWidth(Font textRenderer) {
      return 155;
   }

   public void renderImage(Font textRenderer, int x, int y, GuiGraphicsExtractor context) {
      int var5 = 0;
      int var6 = 0;
      int var7 = RenderTooltipEvent.field_1663;
      int var8 = RenderTooltipEvent.field_1664;
      ItemStack var9 = null;

      for (ItemStack var11 : this.items) {
         int var12 = x + 17 * var5;
         int var13 = y + 17 * var6;
         boolean var14 = var11.getCount() == 69;
         if (var7 >= var12 && var7 <= var12 + 17 && var8 >= var13 && var8 <= var13 + 17 && var9 == null) {
            var9 = var11;
         }

         int var15 = ((MapId)var11.getOrDefault(DataComponents.MAP_ID, new MapId(-1))).id();
         MapItemSavedData var16 = Hub.field_2624.method_9(var11, var15);
         if (previewModule.mapOverlay.getValue() && var15 != -1 && var16 != null) {
            method_2(mioPose(context), var16, var15, (float)(var12 + 1), (float)(var13 + 1));
            if (!KeybindUtil.method_22(342)) {
               context.itemDecorations(textRenderer, var11, var12 + 1, var13 + 1);
            }
         } else {
            context.item(var11, var12 + 1, var13 + 1);
            context.itemDecorations(
               textRenderer, var11, var12 + 1, var13 + 1, var14 ? new PlaceholderText().method_2(String.valueOf(ChatFormatting.RED)).method_9("\u00010") : null
            );
         }

         if (++var5 >= 9) {
            var5 = 0;
            var6++;
         }
      }

      if (var9 != null && !var9.is(Items.AIR)) {
         context.setTooltipForNextFrame(mc.font, var9, var7, var8);
      }
   }

   public static void method_2(PoseStack var0, MapItemSavedData var1, int var2, float var3, float var4) {
      if (var1 != null) {
         BufferSource var5 = mc.renderBuffers().bufferSource();
         var0.pushPose();
         var0.translate(var3, var4, 0.0F);
         var0.scale(0.125F, 0.125F, 0.0F);
         var0.translate(0.0F, 0.0F, 0.0F);
         // mc.getMapRenderer().render(var0, var5, new MapId(var2), var1, false, 15728880); // TODO(runtime): MapRenderer needs MapRenderState + SubmitNodeCollector
         var5.endBatch();
         var0.popPose();
      }
   }

   public List<ItemStack> getItems() {
      return this.items;
   }

   public static PoseStack mioPose(GuiGraphicsExtractor var0) {
      PoseStack var1 = new PoseStack();
      var1.mulPose(new org.joml.Matrix4f().mul(var0.pose()));
      return var1;
   }

}
