package me.mioclient.module.render;

import net.minecraft.client.renderer.rendertype.RenderTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import me.mioclient.Hub;
import me.mioclient.enum_.EspMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Event_12;
import me.mioclient.event.Event_3;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0104;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.internal.BoxUtil;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.MathUtil;
import me.mioclient.internal.HudRenderEvent;
import me.mioclient.internal.FramebufferUtil;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.Colors;
import me.mioclient.internal.EntityCullingBridge;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.Class_1288;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.internal.RenderManager;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_21;
import me.mioclient.setting.Setting;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LayerLightEventListener;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import org.joml.Matrix4f;

public class ESPModule extends Module {
   public static AbstractModule_21 field_521 = Hub.field_2595.method_78(AbstractModule_21.class);
   public Setting<Boolean> targets;
   public Setting<Boolean> mobOwner;
   public Setting<Boolean> light;
   public Setting<Boolean> hostiles;
   public Setting<Color> fill;
   public Setting<Color> outline;
   public Setting<Boolean> players;
   public Setting<Color> fill2;
   public Setting<Color> outline2;
   public Setting<Boolean> animals;
   public Setting<Color> fill3;
   public Setting<Color> outline3;
   public Setting<Boolean> pearls;
   public Setting<Color> fill4;
   public Setting<Color> outline4;
   public Setting<Boolean> items;
   public Setting<EspMode> mode;
   public Setting<Integer> range;
   public Setting<Boolean> group;
   public Setting<Color> itemsText;
   public Setting<Color> background;
   public Setting<Float> scale;
   public Setting<Color> fill5;
   public Setting<Color> outline5;
   public Setting<Boolean> exp;
   public Setting<Color> fill6;
   public Setting<Color> outline6;
   public Setting<Boolean> chorus;
   public Setting<Color> chorusText;
   public Setting<Boolean> blocks;
   public Setting<Boolean> chests;
   public Setting<Boolean> eChests;
   public Setting<Boolean> field_3841;
   public Setting<Boolean> beds;
   public Setting<Boolean> signs;
   public Setting<Boolean> dispensers;
   public Setting<Boolean> hoppers;
   public Setting<Boolean> furnaces;
   public Setting<Boolean> pots;
   public Setting<Float> lineWidth;
   public boolean field_3849;
   public final Class_0104 field_3850;
   public final Map<Vec3, Long> field_3851;

   public ESPModule() {
      super("ESP", "Highlights entities through walls.", Category.RENDER);
      Settings.initialize(this);
      this.field_3850 = new Class_0104(this);
      this.field_3851 = Collections.synchronizedMap(new HashMap<>());
      this.fill3.method_31("AnimalsFill");
      this.outline3.method_31("AnimalsOutline");
      this.fill.method_31("HostilesFill");
      this.outline.method_31("HostilesOutline");
      this.fill2.method_31("PlayersFill");
      this.outline2.method_31("PlayersOutline");
      this.fill5.method_31("ItemsFill");
      this.outline5.method_31("ItemsOutline");
      this.fill6.method_31("ExpFill");
      this.outline6.method_31("ExpOutline");
      this.mode.method_31("ItemsMode");
      this.range.method_31("ItemRange");
      this.setDrawn(false);
   }

   @Subscribe
   public void method_7(Event_12 var1) {
      if (this.light.getValue()) {
         this.method_2(var1);
      }
   }

   @Subscribe(
      method_800 = -99999999
   )
   public void method_29(Event_3 var1) {
      if (!this.method_535() && this.field_3850.method_126()) {
         FramebufferUtil.method_2(RenderManager.field_4420, false);
         var1.method_10().pushPose();
         this.field_3849 = true;
         Vec3 var2 = mc.gameRenderer.getMainCamera().position();
         OutlineBufferSource var3 = RenderManager.field_4420.field_2301;
         EntityCullingBridge.method_1013();

         for (BlockEntity var5 : Hub.field_2622.method_347()) {
            if (this.field_3850.method_9(var5)) {
               double var6 = (double)var5.getBlockPos().getX() - var2.x;
               double var8 = (double)var5.getBlockPos().getY() - var2.y;
               double var10 = (double)var5.getBlockPos().getZ() - var2.z;
               var1.method_10().pushPose();
               var1.method_10().translate(var6, var8, var10);
               Color var12 = BoxRenderer.method_2(this.field_3850.method_5(var5), field_521.scheme.getValue().method_727());
                var3.setColor((255 << 24 | var12.getRed() << 16 | var12.getGreen() << 8 | var12.getBlue()));
                if (mc.getBlockEntityRenderDispatcher().getRenderer(var5) == null) {
                   // TODO26.1: BlockRenderDispatcher/direct block-model draw removed in 26.1; no direct fallback path yet.
                } else {
                   // TODO26.1: BE renderers now extract+submit via SubmitNodeCollector; not yet wired into the outline immediate buffer.
                }

               var1.method_10().popPose();
            }
         }

         EntityCullingBridge.method_1014();
         mc.renderBuffers().bufferSource().endBatch();
         this.field_3849 = false;
         var1.method_10().popPose();
         FramebufferUtil.method_31(false);
      }
   }

   @Subscribe(
      method_800 = -200
   )
   public void method_9(Event_3 var1) {
      if (!this.method_535()) {
         ArrayList<Entity> var2 = new ArrayList<>();

         for (Entity var4 : mc.level.entitiesForRendering()) {
            var2.add(var4);
         }

         var2.sort(Comparator.comparing(var0 -> -var0.tickCount));
         ArrayList var13 = new ArrayList();

         for (Entity var5 : (Iterable<Entity>)(Iterable<?>)(var2)) {
            if (this.field_3850.method_98(var5)) {
               BoxRenderer.method_5(var1.method_10(), BoxUtil.method_2(var5, var1.method_880()), this.field_3850.method_9(var5, true));
               BoxRenderer.method_2(
                  var1.method_10(), BoxUtil.method_2(var5, var1.method_880()), this.field_3850.method_9(var5, false), this.lineWidth.getValue()
               );
            }

            if (var5 instanceof ItemEntity var6
               && this.items.getValue()
               && (this.mode.getValue() == EspMode.BOTH || this.mode.getValue() == EspMode.TEXT)) {
               Vec3 var7 = mc.gameRenderer.getMainCamera().position();
               if (var5 instanceof ItemEntity && var5.position().distanceTo(var7) > (double)this.range.getValue().intValue()) {
                  continue;
               }

               if (!this.group.getValue()) {
                  String var20 = var6.getItem().getHoverName().getString();
                  if (var6.getItem().getCount() > 1) {
                     var20 = new PlaceholderText().method_2(var6.getItem().getCount()).method_2((Object)var20).method_9("\u0001 x\u0001");
                  }

                  this.field_3850
                     .method_2(
                        var1,
                        var20,
                        var6.getPosition(var1.method_880()).add(0.0, 0.6000000238418579, 0.0),
                        this.scale.getValue(),
                        this.itemsText.getValue(),
                        this.background.getValue()
                     );
                  continue;
               }

               boolean var8 = false;

               for (Class_1288 var10 : (Iterable<Class_1288>)(Iterable<?>)(var13)) {
                  if (var10.method_2(var6)) {
                     var8 = true;
                  }
               }

               if (!var8) {
                  var13.add(new Class_1288(var6));
               }
            }

            String var17 = this.field_3850.method_84(var5);
            if (var17 != null) {
               Vec3 var19 = var5.getPosition(var1.method_880()).add(0.0, 0.2, 0.0);
               if (!(var5 instanceof ThrownEnderpearl)) {
                  var19 = var19.add(0.0, 1.3, 0.0);
               }

               this.field_3850.method_2(var1, var17, var19, 1F, Color.white, null);
            }
         }

         if (this.group.getValue()) {
            this.method_2(var1, var13);
         }

         if (this.chorus.getValue()) {
            synchronized (this.field_3851) {
               for (Entry var18 : this.field_3851.entrySet()) {
                  this.field_3850.method_2(var1, "Player teleport", (Vec3)var18.getKey(), 1F, this.chorusText.getValue(), null);
               }
            }
         }
      }
   }

   public void method_2(HudRenderEvent var1, List<Class_1288> var2) {
      for (Class_1288 var4 : var2) {
         if (!this.group.getValue()) {
            break;
         }

         Vec3 var5 = var4.method_1151().getCenter();
         double var6 = MathUtil.method_2(mc.gameRenderer.getMainCamera().position(), var5, (double)this.scale.getValue().floatValue());
         float var8 = (float)FontRenderer.field_3143.method_66();
         float var9 = 0.0F;
         if (var4.method_1152().size() == 1) {
            this.method_2(var1.method_10(), var4, var6, (float)var4.method_1152().size() * var8);
         } else {
            this.method_2(var1.method_10(), var4, var6, var8);
            this.method_2(var1.method_10(), var4, var6, (float)(var4.method_1152().size() - 1) * -var8 - 2F);
         }

         for (Entry var11 : var4.method_1152().entrySet()) {
            String var12 = Class_1288.method_5((String)var11.getKey(), (Integer)var11.getValue());
            float var13 = -FontRenderer.field_3143.method_221(var12) / 2F;
            RenderUtil.field_2672.method_2(var1.method_10(), var12, var5, 0.0F, 0.0F, var13, var9, var6, this.itemsText.getValue(), true);
            var9 -= var8;
         }
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundSoundPacket var2 && ((SoundEvent)var2.getSound().value()).equals(SoundEvents.CHORUS_FRUIT_TELEPORT)) {
         this.field_3851.put(new Vec3(var2.getX(), var2.getY(), var2.getZ()), System.currentTimeMillis());
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      this.field_3851.entrySet().removeIf(var0 -> System.currentTimeMillis() - var0.getValue() > 3500L);
   }

   public void method_2(PoseStack var1, Class_1288 var2, double var3, float var5) {
      RenderUtil.field_2672
         .method_2(
            var1, var2.method_1151().getCenter(), 0.0F, 0.0F, var2.method_1153() + 1F, var5, var3, this.background.getValue()
         );
   }

   public void method_2(HudRenderEvent var1) {
      float var2 = 0.75F;
      Vec3 var3 = mc.getEntityRenderDispatcher().camera.position();
      PoseStack var4 = var1.method_10();
      BufferBuilder var5 = Tesselator.getInstance().begin(Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

      GlStateManager._enableDepthTest();
      GlStateManager._enableBlend();
      LayerLightEventListener var6 = mc.level.getLightEngine().getLayerListener(LightLayer.BLOCK);

      for (BlockPos var8 : BlockUtil.method_2(mc.player.getEyePosition(), 12F, true)) {
         BlockState var9 = mc.level.getBlockState(var8);
         if (BlockUtil.method_14(var8.above()) && var6.getLightValue(var8.above()) <= 7) {
            boolean var10 = mc.level.getBiome(var8).is(Biomes.MUSHROOM_FIELDS);
            if (var9.isValidSpawn(mc.level, var8, EntityType.ZOMBIE) && !var10) {
               double var11 = mc.gameRenderer.getMainCamera().position().distanceTo(var8.getCenter());
               float var13 = 1F
                  - (float)Mth.clamp(
                     (var11 - 8.0) / 2.0,
                     0.0,
                     1.0
                  );
               int var14 = Color.red.hashCode();
               if (var11 >= 8.0) {
                  var14 = Colors.method_9(Color.red, var13);
               }

               var4.pushPose();
               var4.translate(
                  (double)var8.getX() - var3.x, (double)var8.getY() - var3.y + 1.02, (double)var8.getZ() - var3.z
               );
               Matrix4f var15 = var4.last().pose();
               var5.addVertex(var15, 1F - var2, 0.0F, 1F - var2).setColor(var14);
               var5.addVertex(var15, var2, 0.0F, var2).setColor(var14);
               var5.addVertex(var15, var2, 0.0F, 1F - var2).setColor(var14);
               var5.addVertex(var15, 1F - var2, 0.0F, var2).setColor(var14);
               var4.popPose();
            }
         }
      }

      MeshData var16 = var5.build();
      if (var16 != null) {
         net.minecraft.client.renderer.rendertype.RenderTypes.debugQuads().draw(var16);
         GlStateManager._disableDepthTest();
         GlStateManager._disableBlend();
      }
   }

   public boolean method_1065() {
      return this.field_3849;
   }
}
