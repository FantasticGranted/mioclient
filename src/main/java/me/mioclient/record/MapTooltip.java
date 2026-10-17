package me.mioclient.record;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.api.MinecraftAccess;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

/** Tooltip component that renders a filled map (by id + state) over the vanilla map background texture. */
public final class MapTooltip implements MinecraftAccess, ClientTooltipComponent {
   public final int mapId;
   public final MapItemSavedData mapState;
   public static final Identifier BACKGROUND = Identifier.parse("textures/map/map_background.png");

   public MapTooltip(int var1, MapItemSavedData var2) {
      super();
      this.mapId = var1;
      this.mapState = var2;
   }

   public int getHeight(Font var1) {
      return 110;
   }

   public int getWidth(Font textRenderer) {
      return 108;
   }

   public void renderImage(Font textRenderer, int x, int y, GuiGraphicsExtractor context) {
      PoseStack var5 = new PoseStack();
      var5.mulPose(new org.joml.Matrix4f().mul(context.pose()));
      var5.pushPose();
      var5.translate((float)x, (float)y, 0.0F);
      var5.scale(1.5F, 1.5F, 0.0F);
      var5.scale(1.125F, 1.125F, 0.0F);

      context.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, BACKGROUND, 0, 0, 0, 0, 0, 64, 64, 64, 64);
      var5.popPose();
      BufferSource var6 = mc.renderBuffers().bufferSource();
      var5.pushPose();
      var5.translate((float)x, (float)y, 0.0F);
      var5.scale(0.75F, 0.75F, 0.0F);
      var5.translate(8F, 8F, 0.0F);
      // mc.getMapRenderer().render(var5, var6, new MapId(this.mapId), this.mapState, false, 15728880); // TODO(runtime): MapRenderer needs MapRenderState + SubmitNodeCollector
      var6.endBatch();
      var5.popPose();
   }

   public int method_684() {
      return this.mapId;
   }

   public MapItemSavedData getMapState() {
      return this.mapState;
   }
}
