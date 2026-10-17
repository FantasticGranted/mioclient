package me.mioclient.module.abstract_;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.mioclient.enum_.SelfFillMode;
import me.mioclient.enum_.Class_0801;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.ResizableHudElement;
import me.mioclient.internal.RegistrySetting;
import me.mioclient.internal.FontRenderer;
import me.mioclient.record.NameCount;
import me.mioclient.setting.BooleanSetting;
import me.mioclient.setting.CustomSetting;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class AbstractModule_20 extends AbstractModule_26 {
   public final Setting<Set<EntityType<?>>> field_2134 = this.add(new RegistrySetting<>("WhiteList", BuiltInRegistries.ENTITY_TYPE, EntityType.PLAYER));
   public final Setting<Set<Item>> field_2135 = this.add(new RegistrySetting<>("Items", BuiltInRegistries.ITEM, Items.OBSIDIAN));
   public final Setting<SelfFillMode> selection = this.add(new CustomSetting<>("Selection", SelfFillMode.BLACKLIST));
   public final Setting<SelfFillMode> itemSelection = this.add(new CustomSetting<>("ItemSelection", SelfFillMode.ANY));
   public final Setting<Class_0801> sorting = this.add(new CustomSetting<>("Sorting", Class_0801.ALPHABET));
   public final Setting<Boolean> customNames = this.add(new BooleanSetting("CustomNames", true));
   public final Setting<Boolean> coloredCount = this.add(new BooleanSetting("ColoredCount", true));
   public final Map<String, Integer> field_2141 = new HashMap<>();
   public final List<NameCount> field_2142 = new ArrayList<>();

   public AbstractModule_20() {
      super("EntityList");
      this.itemSelection.method_5(var1 -> this.selection.getValue().method_2(EntityType.ITEM, this.field_2134.getValue()));
      this.method_2(new ResizableHudElement(this));
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      synchronized (this.field_2142) {
         this.field_2141.clear();

         for (Entity var4 : mc.level.entitiesForRendering()) {
            if (this.selection.getValue().method_2(var4.getType(), this.field_2134.getValue())) {
               if (var4 instanceof ItemEntity) {
                  ItemEntity var5 = (ItemEntity)var4;
                  if (var5.getItem().isEmpty()) {
                     continue;
                  }

                  Item var6 = var5.getItem().getItem();
                  if (!this.itemSelection.getValue().method_2(var6, this.field_2135)) {
                     continue;
                  }
               }

               String var10 = this.customNames.getValue() ? var4.getName().getString() : var4.getType().getDescription().getString();
               if (var4 instanceof ItemEntity var11) {
                  var10 = var11.getItem().getHoverName().getString();
               }

               int var12 = var4 instanceof ItemEntity var7 ? var7.getItem().getCount() : 1;
               this.field_2141.compute(var10, (var1x, var2) -> var2 == null ? var12 : var2 + var12);
            }
         }

         this.field_2142.clear();
         this.field_2142.addAll(this.field_2141.entrySet().stream().map(NameCount::of).toList());
         switch ((Class_0801)this.sorting.getValue()) {
            case ALPHABET:
               this.field_2142.sort(Comparator.comparing(var0 -> var0.name));
               break;
            case COUNT:
               this.field_2142.sort(Comparator.comparing(var0 -> -var0.count));
               break;
            case LENGTH:
               this.field_2142.sort(Comparator.comparing(var0 -> FontRenderer.field_3143.method_221(var0.method_2(ChatFormatting.WHITE))));
         }
      }
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1) {
      float var2 = this.field_1844.method_194((float)FontRenderer.field_3143.method_66()) - this.field_1844.method_60();
      synchronized (this.field_2142) {
         for (NameCount var5 : this.field_2142) {
            Color var6 = this.method_9(var2);
            ChatFormatting var7 = this.coloredCount.getValue() ? ChatFormatting.WHITE : ChatFormatting.RESET;
            String var8 = var5.method_2(var7);
            float var9 = FontRenderer.field_3143.method_221(var8);
            FontRenderer.field_3143.method_9(var1, var8, this.field_1844.method_176(var9) - this.field_1844.method_59(), var2, var6);
            var2 += (float)((FontRenderer.field_3143.method_66() + 1) * this.field_1844.method_196());
         }
      }
   }

   @Override
   public float[] method_31() {
      float var1 = 0.0F;
      float var2 = 0.0F;
      synchronized (this.field_2142) {
         for (NameCount var5 : this.field_2142) {
            float var6 = FontRenderer.field_3143.method_221(var5.method_2(ChatFormatting.WHITE));
            var1 += (float)(FontRenderer.field_3143.method_66() + 1);
            if (var6 > var2) {
               var2 = var6;
            }
         }
      }

      return new float[]{var2, var1};
   }
}
