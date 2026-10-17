package me.mioclient.module.render;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import me.mioclient.Hub;
import me.mioclient.api.EntityInfoAccess;
import me.mioclient.enum_.NameTagVisibility;
import me.mioclient.enum_.Side2;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.EntityRenderEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.Class_0714;
import me.mioclient.internal.Render2D;
import me.mioclient.internal.EnchantmentUtil;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.MathUtil;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.Colors;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.internal.ExplosionDamage;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.client.FontsModule;
import me.mioclient.module.player.FreecamModule;
import me.mioclient.setting.Setting;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.PlayerInfo;
import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.Holder;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;

public class NameTagsModule extends Module {
   public static final Identifier field_1708 = Identifier.fromNamespaceAndPath("mio", "textures/mio.png");
   public static AnimationsModule animations = Hub.field_2595.method_78(AnimationsModule.class);
   public static FreecamModule freecam = Hub.field_2595.method_78(FreecamModule.class);
   public static FontsModule field_984 = Hub.field_2595.method_78(FontsModule.class);
   public static boolean field_1709 = false;
   public Setting<Integer> range;
   public Setting<Float> scale;
   public Setting<Boolean> dead;
   public Setting<Boolean> info;
   public Setting<Boolean> name;
   public Setting<Boolean> ping;
   public Setting<Boolean> health;
   public Setting<Boolean> healthColor;
   public Setting<Boolean> gamemode;
   public Setting<Boolean> itemName;
   public Setting<Boolean> armor;
   public Setting<NameTagVisibility> durability;
   public Setting<Boolean> items;
   public Setting<Boolean> showEating;
   public Setting<Boolean> enchants;
   public Setting<Boolean> hideMax;
   public Setting<Boolean> totemPops;
   public Setting<Boolean> mioCheck;
   public Setting<Boolean> colors;
   public Setting<Boolean> friend;
   public Setting<Boolean> enemy;
   public Setting<Color> fill;
   public Setting<Color> outline;
   public Setting<Boolean> smart;
   public Setting<Color> hole;
   public Setting<Color> phase;
   public Setting<Color> text;
   public Setting<Color> invisibles;
   public Setting<Color> sneak;
   public Setting<Color> eating;
   public final Color field_1740;
   public final List<Player> field_1741;
   public boolean field_1742;
   public boolean field_1743;
   public boolean field_1744;
   public Vector3f[] field_1745;

   public NameTagsModule() {
      super("NameTags", "Draws advanced nametags for players.", Category.RENDER);
      Settings.initialize(this);
      this.field_1740 = new Color(153, 69, 58);
      this.field_1741 = new ObjectArrayList();
      this.field_1745 = null;
   }

   @Override
   public void onDisable() {
      field_1709 = false;
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      synchronized (this.field_1741) {
         this.field_1741.clear();
         this.field_1741.addAll(mc.level.players());
         this.field_1741.sort(Comparator.comparing(var0 -> mc.gameRenderer.getMainCamera().position().distanceToSqr(var0.position())));
      }
   }

   @Subscribe(
      method_800 = -100
   )
   public void method_9(Event_3 var1) {
      // TODO26.1 this.field_1745 = Arrays.copyOf(RenderSystem.shaderLightDirections, RenderSystem.shaderLightDirections.length);
      Vec3 var2 = mc.gameRenderer.getMainCamera().position();
      synchronized (this.field_1741) {
         for (Player var5 : this.field_1741) {
            if ((var5 != mc.player || freecam.isToggled()) && (!var5.isDeadOrDying() || this.dead.getValue())) {
               this.field_1742 = Hub.field_2610.method_326() && Hub.field_2610.method_68(var5.getGameProfile().name()) != null && this.mioCheck.getValue();
               boolean var6 = !RotationManager.method_4(var5.getBoundingBox())
                  || mc.gameRenderer.getMainCamera().position().distanceTo(var5.position()) > (double)this.range.getValue().intValue();
               if (!var6) {
                  float var7 = RenderUtil.method_776();
                  double var8 = Mth.lerp((double)var7, var5.xo, var5.getX()) - var2.x;
                  double var10 = Mth.lerp((double)var7, var5.yo, var5.getY()) - var2.y;
                  double var12 = Mth.lerp((double)var7, var5.zo, var5.getZ()) - var2.z;
                  Vec3 var14 = Vec3.ZERO;
                  double var15 = var8 + var14.x;
                  double var17 = var10 + var14.y;
                  double var19 = var12 + var14.z;
                  var1.method_10().pushPose();
                  var1.method_10().translate(var15, var17, var19);
                  this.method_2(var1.method_881(), var1.method_10(), var5);
                  var1.method_10().popPose();
               }
            }
         }
      }

      GlStateManager._enablePolygonOffset();
      GlStateManager._polygonOffset(1F, -1.5e+06F);
      GL20.glDepthRange(0.0, 0.1);
      GlStateManager._enableBlend();
      GlStateManager._blendFuncSeparate(770, 771, 770, 771);
      // TODO26.1 Lighting.disableGuiDepthLighting();
      // TODO26.1 var1.method_881().getVertexConsumers().draw();
      // TODO26.1 RenderSystem.setShaderLights(this.field_1745[0], this.field_1745[1]);
      GlStateManager._disableBlend();
      Render2D.getId(false);
      GlStateManager._polygonOffset(1F, -5e+03F);
      FontRenderer.field_3143.method_474();
      GlStateManager._polygonOffset(1F, 5e+03F);
      GL20.glDepthRange(0.0, 1.0);
      GlStateManager._polygonOffset(1F, 1.5e+06F);
      GlStateManager._disablePolygonOffset();
   }

   @Subscribe
   public void method_2(EntityRenderEvent var1) {
      if (var1.method_11() instanceof Player) {
         var1.method_463();
      }
   }

   public void method_9(PoseStack var1, Vec3 var2) {
      GlStateManager._enablePolygonOffset();
      GlStateManager._polygonOffset(1F, -1.2e+06F);
      GL20.glDepthRange(0.0, 0.1);

      for (Player var4 : this.field_1741) {
         if (mc.player != var4) {
            float var5 = (float)MathUtil.method_2(
               mc.gameRenderer.getMainCamera().position(), var4.position(), (double)this.scale.getValue().floatValue()
            );
            float var6 = RenderUtil.method_776();
            double var7 = Mth.lerp((double)var6, var4.xo, var4.getX()) - var2.x;
            double var9 = Mth.lerp((double)var6, var4.yo, var4.getY()) - var2.y;
            double var11 = Mth.lerp((double)var6, var4.zo, var4.getZ()) - var2.z;
            Vec3 var13 = Vec3.ZERO;
            double var14 = var7 + var13.x;
            double var16 = var9 + var13.y;
            double var18 = var11 + var13.z;
            var1.pushPose();
            var1.translate(var14, var16, var18);
            var1.pushPose();
            boolean var20 = animations.method_128() && var4 != mc.player;
            var18 = (double)(var4.getBbHeight() * (var20 ? animations.playerScale.getValue() : 1F) + 0.5F);
            var1.translate(0.0, var18, 0.0);
            var1.mulPose(mc.getEntityRenderDispatcher().camera.rotation());
            var1.scale(-var5 * 0.025F, -var5 * 0.025F, var5 * 0.025F);
            String var21 = this.method_263(var4);
            float var22 = (float)((double)(-this.method_221(var21)) * 0.5);
            RenderUtil.field_2672
               .method_9(
                  var1,
                  var22 - 1F,
                  -1F,
                  var22 * -1F + 1F,
                  this.method_575() + 1F,
                  Color.white
               );
            var1.popPose();
            var1.popPose();
         }
      }

      GL20.glDepthRange(0.0, 1.0);
      GlStateManager._disablePolygonOffset();
      GlStateManager._polygonOffset(1F, 1.2e+06F);
   }

   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, Player var3) {
      this.field_1744 = this.field_1743 = false;
      boolean var4 = animations.method_128() && var3 != mc.player;
      float var5 = var3.getBbHeight() * (var4 ? animations.playerScale.getValue() : 1F) + 0.5F;
      float var6 = (float)MathUtil.method_2(mc.gameRenderer.getMainCamera().position(), var3.position(), (double)this.scale.getValue().floatValue());
      var2.pushPose();
      var2.translate(0.0F, var5, 0.0F);
      var2.mulPose(mc.getEntityRenderDispatcher().camera.rotation());
      var2.scale(var6 * 0.025F, -var6 * 0.025F, -var6 * 0.025F);
      String var7 = this.method_263(var3);
      float var8 = (float)((double)(-this.method_221(var7)) * 0.5);
      GlStateManager._enablePolygonOffset();
      GlStateManager._polygonOffset(1F, -1.5e+06F);
      GL20.glDepthRange(0.0, 0.1);
      Color var9 = this.outline.getValue();
      if (this.smart.getValue()) {
         if (this.method_222(var3)) {
            var9 = this.phase.getValue();
         } else if (Hub.field_2605.method_221(var3.blockPosition())) {
            var9 = this.hole.getValue();
         }
      }

      float var10 = 0.5F;
      Render2D.method_4(
         var2,
         var8 - 1F,
         -1F,
         var8 * -1F + 1F,
         this.method_575() + 1F,
         this.fill.getValue()
      );
      Render2D.method_2(
         var2,
         var8 - 2F + var10,
         -2F + var10,
         var8 * -1F + 1F,
         this.method_575() + 1F,
         var10,
         var9
      );
      Color var11 = this.text.getValue();
      if (var3.isShiftKeyDown()) {
         var11 = this.sneak.getValue();
      }

      if (var3.isInvisible()) {
         var11 = this.invisibles.getValue();
      }

      if (Hub.field_2603.getId(var3) && this.friend.getValue()) {
         var11 = Hub.field_2603.method_1145();
      }

      if (Hub.field_2603.method_16(var3) && this.enemy.getValue()) {
         var11 = Hub.field_2603.method_1146();
      }

      boolean var12 = field_984.isToggled();
      float var13 = 1F;
      FontRenderer.field_3143.method_9(var1, var7, var8, var13, var11);
      field_1709 = true;
      this.method_9(var1, var2, var3);
      field_1709 = false;
      if (this.field_1742) {
         var2.pushPose();
         float var14 = 0.0703125F;
         var2.translate(var8 - 4F, -4F, 0.0F);
         var2.scale(var14, var14, var14);
         if (var12) {
            var2.translate(
               -1.0,
               1.3 + (double)field_984.shift.getValue().intValue(),
               0.0
            );
         }


         GlStateManager._blendFuncSeparate(770, 771, 770, 771);
         GlStateManager._texParameter(3553, 10240, 9729);
         var1.blit(field_1708, 0, 0, 0, 0, 256.0F, 256.0F, 256.0F, 256.0F);
         GlStateManager._texParameter(3553, 10240, 9728);
         var2.popPose();
      }

      GL20.glDepthRange(0.0, 1.0);
      GlStateManager._disablePolygonOffset();
      GlStateManager._polygonOffset(1F, 1.5e+06F);
      var2.popPose();
   }

   public void method_9(GuiGraphicsExtractor var1, PoseStack var2, Player var3) {
      ArrayList var4 = new ArrayList();
      if (this.items.getValue() && !var3.getOffhandItem().isEmpty()) {
         var4.add(var3.getOffhandItem());
      }

      for (ItemStack var6 : new ItemStack[]{
         var3.getItemBySlot(EquipmentSlot.HEAD),
         var3.getItemBySlot(EquipmentSlot.CHEST),
         var3.getItemBySlot(EquipmentSlot.LEGS),
         var3.getItemBySlot(EquipmentSlot.FEET)
      }) {
         if (!var6.isEmpty() && this.armor.getValue()) {
            var4.add(var6);
         }
      }

      if (this.items.getValue() && !var3.getMainHandItem().isEmpty()) {
         var4.add(var3.getMainHandItem());
      }

      int var10 = -3;

      for (ItemStack var7 : (Iterable<ItemStack>)(Iterable<?>)(var4)) {
         int var8 = EnchantmentUtil.method_7(var7).size();
         if (this.hideMax.getValue() && Class_0714.method_9(var7)) {
            var8 = 0;
         }

         if (var8 > var10) {
            var10 = var8;
         }
      }

      var10 = Math.max(var10, 0);
      if (this.durability.getValue() == NameTagVisibility.ONLY || !this.enchants.getValue()) {
         var10 = 0;
      }

      float var13 = (float)var10 * this.method_575() * 0.5F / 1.2F / 17.25F;
      float var14 = -Math.max(var13, 0.6F);

      for (int var15 = 0; var15 < var4.size(); var15++) {
         this.method_2(
            var1,
            var3,
            var2,
            (ItemStack)var4.get(var4.size() - 1 - var15),
            (float)var15 + 0.5F - (float)var4.size() * 0.5F,
            var14,
            17.25F
         );
      }

      if (!var3.getMainHandItem().isEmpty() && this.itemName.getValue()) {
         float var16 = var14 * 17.25F - 4F - this.method_575();
         if (!this.field_1743) {
            var16 = -this.method_575();
         }

         if (this.field_1744) {
            var16 -= this.method_575() * 0.5F;
            if (!this.field_1743) {
               var16 -= 1F;
            }
         }

         String var9 = ChatFormatting.stripFormatting(var3.getMainHandItem().getHoverName().getString());
         this.method_2(var2, var9, 0.0F, var16, 22.424999177455902, Color.white, false, Side2.CENTER);
      }
   }

   public void method_2(GuiGraphicsExtractor var1, Player var2, PoseStack var3, ItemStack var4, float var5, float var6, float var7) {
      float var8 = 0.6F;
      int var9 = ExplosionDamage.method_5(var4);
      if (this.items.getValue()
         && this.showEating.getValue()
         && var2.isUsingItem()
         && var2.getItemInHand(var2.getUsedItemHand()) == var4
         && var4.has(DataComponents.FOOD)) {
         float var10 = var6 * var7 + 7.55F;
         float var11 = var6 * var7 - 7.55F;
         long var12 = System.currentTimeMillis() - ((EntityInfoAccess)var2).mio$getLastEatingTime();
         float var14 = 0.9F
            * Mth.clamp(
               (float)var12 / ((float)var4.getUseDuration(var2) * 5e+01F + 5e+01F),
               0.0F,
               1F
            );
         Render2D.method_9(
            var3,
            (var5 - 0.45F) * var7,
            var11,
            (var5 - 0.45F + var14) * var7,
            var10,
            Colors.method_9(this.eating.getValue(), 0.2F)
         );
      }

      boolean var19 = false;
      if (this.durability.getValue() != NameTagVisibility.ONLY || var4.get(DataComponents.EQUIPPABLE) == null && !var4.is(Items.ELYTRA)) {
         var19 = true;
         this.field_1743 = true;
         GlStateManager._polygonOffset(1F, 5e+03F);
         var3.pushPose();
         var3.translate(var5 * var7, var6 * var7, 0.0F);
         var3.scale(-var7, -var7, 0.001F);
         // TODO26.1: world-space item model rendering moved to the submit pipeline
         // (ItemStackRenderState + SubmitNodeCollector); no immediate path remains.
         var3.popPose();
         GlStateManager._polygonOffset(1F, -5e+03F);
      }

      if (var4.getCount() > 1) {
         this.method_2(
            var3,
            String.valueOf(var4.getCount()),
            (var5 + 0.5F) * var7,
            var6 * var7 + 1F,
            (double)var7 * 2.25,
            Color.white,
            true,
            Side2.RIGHT
         );
      }

      if (var4.isBarVisible() && var19) {
         float var21 = (float)((int)(var5 + 0.2F)) * var7;
         float var23 = var6 * var7 + 5.5F;
         Render2D.method_4(
            var3,
            (var5 - 0.45F) * var7,
            var23 - 0.05F,
            (var5 + 0.45F) * var7,
            var23 + 2F,
            new Color(-16777216)
         );
         Render2D.method_4(
            var3,
            (var5 - 0.45F) * var7,
            var23 - 0.05F,
            (
                  var5
                     + Mth.lerp(
                        (float)var4.getBarWidth() / 13F, -0.45F, 0.45F
                     )
               )
               * var7,
            var23 + 1.05F,
            new Color(var4.getBarColor(), false)
         );
      }

      Set var22 = var4.getEnchantments().entrySet();
      float var24 = -var8 * 2F;
      if (var4.is(Items.ENCHANTED_GOLDEN_APPLE) && var19) {
         this.method_2(
            var3,
            "God",
            var5 * var7,
            var6 * var7 - 1F + this.method_575() * var8 * var24,
            (double)(var7 * 1.3F),
            this.field_1740,
            false,
            Side2.RIGHT
         );
      } else {
         if (var19 && this.enchants.getValue()) {
            boolean var13 = this.hideMax.getValue() && Class_0714.method_9(var4);
            if (var13) {
               this.method_2(
                  var3,
                  "Max",
                  (var5 - 0.5F) * var7,
                  var6 * var7 - 2F + this.method_575() * var8 / 1.2F * var24,
                  (double)(var7 * 1.3F),
                  Color.RED,
                  false,
                  Side2.LEFT
               );
            }

            for (Entry var15 : (Iterable<Entry>)(Iterable<?>)(var22)) {
               if (var13) {
                  break;
               }

               Enchantment var16 = (Enchantment)((Holder)var15.getKey()).value();
               boolean var17 = var16.getMaxLevel() == 1;
               String var18 = Enchantment.getFullname((Holder)var15.getKey(), var15.getIntValue()).getString();
               var18 = var18.substring(0, Math.min(var17 ? 3 : 2, var18.length()));
               if (!var18.equalsIgnoreCase("Cu")) {
                  if (!var17) {
                     var18 = new PlaceholderText().method_2(var15.getIntValue()).method_2((Object)var18).method_9("\u0001\u0001");
                  }

                  this.method_2(
                     var3,
                     var18,
                     (var5 - 0.5F) * var7,
                     var6 * var7 - 2F + this.method_575() * var8 / 1.2F * var24,
                     (double)(var7 * 1.3F),
                     Color.WHITE,
                     false,
                     Side2.LEFT
                  );
                  var24 += 1F;
               }
            }
         }

         if (var4.isDamageableItem() && this.durability.getValue() != NameTagVisibility.HIDE) {
            float var25 = var6 * var7 - 4F - this.method_575();
            if (!var19) {
               var25 = var6 - this.method_575();
            }

            this.field_1744 = true;
            this.method_2(
               var3,
               String.format("%d%s", var9, "%"),
               (float)(((double)var5 - Class_0245.field_688) * (double)var7) + 1F,
               var25,
               (double)(var7 * 1.5F),
               new Color(var4.getBarColor(), false),
               false,
               Side2.LEFT
            );
         }
      }
   }

   public void method_2(PoseStack var1, String var2, float var3, float var4, double var5, Color var7, boolean var8, Side2 var9) {
      PoseStack var10 = var1;
      var10.pushPose();
      var10.translate(var3, var4, 0.0F);
      var10.scale(0.025F * (float)var5, 0.025F * (float)var5, 1F);
      float var11 = var9.method_9(var2, var8);
      if (var8) {
         Render2D.method_2(var10, var2, (int)var11, 0, var7.hashCode(), true);
      } else {
         FontRenderer.field_3143.method_9(var1, var2, var11, 0.0F, var7);
      }

      var10.popPose();
   }

   public String method_263(Player var1) {
      StringBuilder var2 = new StringBuilder();
      if (this.field_1742) {
         var2.append(ChatFormatting.RESET);
         var2.append("   ");
      }

      if (this.name.getValue()) {
         var2.append(Hub.field_2626.method_8(var1.getGameProfile().name()));
      }

      if (this.gamemode.getValue()) {
         var2.append(" [");

         try {
            PlayerInfo var3 = mc.player.connection.getPlayerInfo(var1.getGameProfile().id());
            var2.append(var3 == null ? "S" : this.method_574(var3.getGameMode().getName()));
         } catch (Throwable var5) {
            var2.append(0);
         }

         var2.append("]");
      }

      if (this.ping.getValue()) {
         var2.append(" ");

         try {
            PlayerInfo var6 = mc.player.connection.getPlayerInfo(var1.getGameProfile().id());
            var2.append(var6 == null ? 0 : var6.getLatency());
         } catch (Throwable var4) {
            var2.append(0);
         }

         var2.append("ms");
      }

      if (this.health.getValue()) {
         float var7 = EntityUtil.method_2((net.minecraft.world.entity.Entity)var1);
         if (this.healthColor.getValue()) {
            var2.append(this.method_4((double)var7));
         }

         var2.append(" ").append(String.format("%.1f", var7)).append(ChatFormatting.RESET);
      }

      if (this.totemPops.getValue()) {
         int var8 = Hub.field_2613.method_39(var1);
         if (var8 > 0) {
            var2.append(method_323(var8)).append(" -").append(var8);
         }
      }

      return var2.toString().trim();
   }

   public ChatFormatting method_4(double var1) {
      if (var1 >= 2e+01) {
         return ChatFormatting.GREEN;
      } else if (var1 >= 16.0) {
         return ChatFormatting.DARK_GREEN;
      } else if (var1 >= 1e+01) {
         return ChatFormatting.GOLD;
      } else {
         return var1 >= 4.0 ? ChatFormatting.RED : ChatFormatting.DARK_RED;
      }
   }

   public static ChatFormatting method_323(int var0) {
      if (var0 > 6) {
         return ChatFormatting.DARK_RED;
      } else {
         return var0 > 3 ? ChatFormatting.GOLD : ChatFormatting.YELLOW;
      }
   }

   public String method_574(String var1) {
      String var2 = var1.toLowerCase(Locale.ROOT);

      return switch (var2) {
         case "survival" -> "S";
         case "creative" -> "C";
         case "adventure" -> "A";
         case "spectator" -> "SP";
         default -> "";
      };
   }

   public float method_575() {
      return (float)FontRenderer.field_3143.method_66();
   }

   public float method_221(String var1) {
      return FontRenderer.field_3143.method_221(var1);
   }

   public boolean method_222(Entity var1) {
      float var2 = var1.getDimensions(var1.getPose()).width() * 0.8F;
      Vec3 var3 = var1.getBoundingBox().getCenter();
      AABB var4 = AABB.ofSize(var3, (double)var2, (double)(var1.getBbHeight() * 0.5F), (double)var2);
      return BlockPos.betweenClosedStream(var4).anyMatch(var0 -> {
         BlockState var1x = mc.level.getBlockState(var0);
         return !var1x.isAir() && var1x.isSolidRender();
      });
   }
}
