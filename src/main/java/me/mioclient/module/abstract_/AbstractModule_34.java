package me.mioclient.module.abstract_;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.enum_.Class_0861;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.ResizableHudElement;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.FontRenderer;
import me.mioclient.module.render.NameTagsModule;
import me.mioclient.setting.BooleanSetting;
import me.mioclient.setting.CustomSetting;
import me.mioclient.setting.CustomSetting3;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;

public class AbstractModule_34 extends AbstractModule_26 {
   public final Setting<Class_0861> sort = this.add(new CustomSetting<>("Sort", Class_0861.DISTANCE));
   public final Setting<Boolean> health = this.add(new BooleanSetting("Health", true));
   public final Setting<Boolean> distance = this.add(new BooleanSetting("Distance", false));
   public final Setting<Boolean> totemPops = this.add(new BooleanSetting("TotemPops", false));
   public final Setting<Boolean> friendColor = this.add(new BooleanSetting("FriendColor", true));
   public final Setting<Boolean> enemyColor = this.add(new BooleanSetting("EnemyColor", true));
   public final Setting<Boolean> armor = this.add(new BooleanSetting("Armor", false));
   public final Setting<Boolean> limit = this.add(new BooleanSetting("Limit", false).method_460());
   public final Setting<Integer> max = this.add(new CustomSetting3<>("Max", 8, 1, 32).method_2(this.limit));
   public final Setting<Boolean> ignore = this.add(new BooleanSetting("Ignore", false).method_220());
   public final Setting<Boolean> friends = this.add(new BooleanSetting("Friends", false).method_2(this.ignore));
   public final Setting<Boolean> nakeds = this.add(new BooleanSetting("Nakeds", false).method_2(this.ignore));
   public final List<Player> field_710 = new ArrayList<>();

   public AbstractModule_34() {
      super("TextRadar", "playerlist");
      this.method_2(new ResizableHudElement(this));
   }

   @Override
   public void onEnable() {
      this.field_710.clear();
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      synchronized (this.field_710) {
         this.field_710.clear();
         mc.level
            .players()
            .stream()
            .filter(this::method_22)
            .sorted(Comparator.comparing(this::method_37))
            .limit(this.limit.getValue() ? (long)this.max.getValue().intValue() : Long.MAX_VALUE)
            .forEachOrdered(this.field_710::add);
      }
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1) {
      float var2 = this.field_1844.method_194((float)FontRenderer.field_3143.method_66()) - this.field_1844.method_60();

      for (Player var4 : this.field_710) {
         Color var5 = this.method_9(var2);
         if (Hub.field_2603.getId(var4) && this.friendColor.getValue()) {
            var5 = Hub.field_2603.method_9(var4.getGameProfile().name(), var5);
         } else if (Hub.field_2603.method_16(var4) && this.enemyColor.getValue()) {
            var5 = Hub.field_2603.method_9(var4.getGameProfile().name(), var5);
         }

         String var6 = this.method_31(var4);
         float var7 = FontRenderer.field_3143.method_221(var6);
         FontRenderer.field_3143.method_9(var1, var6, this.field_1844.method_176(var7) - this.field_1844.method_59(), var2, var5);
         var2 += (float)((FontRenderer.field_3143.method_66() + 1) * this.field_1844.method_196());
      }
   }

   @Override
   public float[] method_31() {
      float var1 = 0.0F;
      float var2 = 0.0F;

      for (Player var4 : this.field_710) {
         float var5 = FontRenderer.field_3143.method_221(this.method_31(var4));
         var1 += (float)(FontRenderer.field_3143.method_66() + 1);
         if (var5 > var2) {
            var2 = var5;
         }
      }

      return new float[]{var2, var1};
   }

   public String method_31(Player var1) {
      StringBuilder var2 = new StringBuilder();
      float var3 = EntityUtil.method_2((net.minecraft.world.entity.Entity)var1);
      if (this.health.getValue()) {
         var2.append(this.method_4((double)var3)).append(Math.round(var3)).append(" ");
      }

      var2.append(ChatFormatting.RESET);
      var2.append(Hub.field_2626.method_8(var1.getGameProfile().name()));
      if (this.totemPops.getValue()) {
         int var4 = Hub.field_2613.method_39(var1);
         if (var4 > 0) {
            var2.append(NameTagsModule.method_323(var4)).append(" -").append(var4);
         }
      }

      if (this.distance.getValue()) {
         var2.append(ChatFormatting.WHITE).append(" ").append(Math.round(mc.player.distanceTo(var1))).append("m");
      }

      if (this.armor.getValue()) {
         String var9 = "G";

         for (ItemStack var6 : java.util.List.of(var1.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.FEET), var1.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.LEGS), var1.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST), var1.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD))) {
            Item var8 = var6.getItem();
            net.minecraft.world.item.equipment.Equippable var72 = var6.get(net.minecraft.core.component.DataComponents.EQUIPPABLE);
            if (var72 != null && var72.slot().isArmor() && !var6.is(Items.ELYTRA)) {
               String var10 = var8.getDescriptionId();
               if (!var10.contains("diamond") && !var10.contains("netherite") || !var6.isEnchanted()) {
                  var9 = "";
               }
            } else {
               if (var6.getItem() == Items.ELYTRA) {
                  var9 = "W";
                  break;
               }

               var9 = "";
            }
         }

         if (!var9.isEmpty()) {
            var2.append(ChatFormatting.WHITE);
            var2.append(" [");
            var2.append(var9);
            var2.append("]");
         }
      }

      return var2.toString();
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

   public double method_37(Entity var1) {
      return this.sort.getValue() == Class_0861.DISTANCE
         ? var1.distanceToSqr(mc.player)
         : (double)Mth.degreesDifferenceAbs(mc.gameRenderer.getMainCamera().yRot(), RotationManager.method_14(var1)[0]);
   }

   public boolean method_22(Player var1) {
      if (!this.method_22() && mc.player == var1) {
         return false;
      } else {
         return this.friends.getValue() && Hub.field_2603.getId(var1) ? false : !this.nakeds.getValue() || PlayerUtil.method_29(var1);
      }
   }
}
