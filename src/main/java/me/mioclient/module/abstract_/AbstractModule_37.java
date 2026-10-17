package me.mioclient.module.abstract_;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.RenderTooltipEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0177;
import me.mioclient.internal.ShulkerPreviewScreen;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.mixin.ducks.DuckHandledScreen;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.record.ContainerTooltip;
import me.mioclient.record.ContainerPreview;
import me.mioclient.record.MapTooltip;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity.Occupant;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import me.mioclient.nick.Settings;
import org.lwjgl.glfw.GLFW;

public class AbstractModule_37 extends Module {
   public Setting<Boolean> colored;
   public Setting<Boolean> majorityItem;
   public Setting<Boolean> keepText;
   public Setting<Boolean> shulkerContent;
   public Setting<Boolean> beeNest;
   public Setting<Boolean> mapOverlay;
   public Setting<Boolean> size;
   public Color field_1191;
   public final List<ContainerPreview> field_1192;

   public AbstractModule_37() {
      super(
         "Tooltips",
         new PlaceholderText()
            .method_2(String.valueOf(ChatFormatting.YELLOW))
            .method_2(String.valueOf(ChatFormatting.YELLOW))
            .method_9(
               "Draws advanced tool tips on items.\n\u0001Middle Click a shulker to open the preview screen.\n\u0001Hold Left Alt when hovering over a shulker to inspect it's items."
            ),
         Category.RENDER
      );
      Settings.initialize(this);
      this.field_1192 = Collections.synchronizedList(new ArrayList<>());
      this.setDrawn(false);
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundContainerSetContentPacket var2 && this.majorityItem.getValue()) {
         mc.executeIfPossible(() -> {
            this.field_1192.clear();

            for (ItemStack var3 : var2.items()) {
               ContainerPreview var4 = ContainerPreview.of(var3);
               if (var4 != null) {
                  this.field_1192.add(var4);
               }
            }
         });
      }

      if (var1.method_127() instanceof ClientboundContainerSetSlotPacket && this.majorityItem.getValue()) {
         Hub.field_2619.method_2(() -> {
            if (mc.player.containerMenu != null) {
               this.field_1192.clear();

               for (Slot var2x : mc.player.containerMenu.slots) {
                  ContainerPreview var3 = ContainerPreview.of(var2x.getItem());
                  if (var3 != null) {
                     this.field_1192.add(var3);
                  }
               }
            }
         }, 0);
      }
   }

   @Subscribe
   public void method_2(RenderTooltipEvent var1) {
      if (var1.method_562() instanceof AbstractContainerScreen) {
         Slot var2 = ((DuckHandledScreen)(Object)var1.method_562()).mio$getFocusedSlot();
         if (var2 != null) {
            ItemStack var3 = var2.getItem();
            NonNullList<ItemStack> var4 = NonNullList.withSize(27, ItemStack.EMPTY);
            if (var3.getItem() instanceof BlockItem var5 && var5.getBlock() instanceof ShulkerBoxBlock var6 && var6.getColor() != null) {
               Color var17 = new Color(var6.getColor().getMapColor().col, false);
               float[] var8 = new float[3];
               var17.getColorComponents(var8);
               this.method_16(new Color(var8[0], var8[1], var8[2], 0.5F));
            }

            ItemContainerContents var12 = (ItemContainerContents)var3.get(DataComponents.CONTAINER);
            if (var12 != null) {
               List var13 = var12.allItemsCopyStream().toList();

               for (int var18 = 0; var18 < var13.size() && var18 < 27; var18++) {
                  ItemStack var22 = (ItemStack)var13.get(var18);
                  if (var22.getCount() == 0) {
                     var22.setCount(69);
                  }

                  var4.set(var18, var22);
               }
            }

            this.method_2(var3, var4);
            if (!var4.stream().allMatch(ItemStack::isEmpty) && this.shulkerContent.getValue()) {
               if (GLFW.glfwGetMouseButton(mc.getWindow().handle(), 2) == 1 && !(var1.method_562() instanceof ShulkerPreviewScreen)) {
                  mc.setScreen(
                     new ShulkerPreviewScreen(
                        new ShulkerBoxMenu(1337, mc.player.getInventory(), new SimpleContainer(var4.toArray(new ItemStack[27]))),
                        mc.player.getInventory(),
                        var3.getHoverName(),
                        var1.method_562()
                     )
                  );
               }

               if (this.keepText.getValue()) {
                  var1.method_563().add(new ContainerTooltip(var4));
               } else {
                  var1.method_563().clear();
                  var1.method_563().addAll(List.of(ClientTooltipComponent.create(var3.getHoverName().getVisualOrderText()), new ContainerTooltip(var4)));
               }
            }

            if (var3.getItem() == Items.FILLED_MAP) {
               MapId var14 = (MapId)var3.get(DataComponents.MAP_ID);
               int var19 = var14 == null ? -1 : var14.id();
               MapItemSavedData var23 = Hub.field_2624.method_9(var3, var19);
               if (var23 != null) {
                  if (!this.keepText.getValue()) {
                     var1.method_563().clear();
                     var1.method_563().addAll(List.of(ClientTooltipComponent.create(var3.getHoverName().getVisualOrderText()), new MapTooltip(var19, var23)));
                  } else {
                     var1.method_563().add(new MapTooltip(var19, var23));
                  }

                  if (this.mapOverlay.getValue()) {
                     MutableComponent var9 = Component.literal("Hold Left-Alt to hide item amount.");
                     var9 = var9.withStyle(var0 -> var0.applyFormat(ChatFormatting.YELLOW));
                     var1.method_563().add(ClientTooltipComponent.create(var9.getVisualOrderText()));
                  }
               }
            }

            List var15 = (List)(Object)var3.get(DataComponents.BEES);
            if (var15 != null && this.beeNest.getValue()) {
               for (Occupant var24 : (Iterable<Occupant>)(Iterable<?>)(var15)) {
                  CompoundTag var27 = var24.entityData().copyTagWithoutId();
                  if (var27 != null && var27.contains("FlowerPos")) {
                     BlockPos var10 = new BlockPos(var27.getCompoundOrEmpty("FlowerPos").getIntOr("X", 0), var27.getCompoundOrEmpty("FlowerPos").getIntOr("Y", 0), var27.getCompoundOrEmpty("FlowerPos").getIntOr("Z", 0));
                     if (var10 != null) {
                        var1.method_563()
                           .add(ClientTooltipComponent.create(Component.literal(new PlaceholderText().method_2(var10.toShortString()).method_9("Position \u0001")).getVisualOrderText()));
                     }
                  }
               }
            }

            if (this.size.getValue()) {
               try {
                  Class_0177 var21 = new Class_0177();
                  net.minecraft.nbt.Tag varSaveNbt = ItemStack.CODEC.encodeStart(net.minecraft.resources.RegistryOps.create(net.minecraft.nbt.NbtOps.INSTANCE, mc.player.registryAccess()), var3).result().orElse(null);
                  if (varSaveNbt instanceof CompoundTag varSaveTag) { net.minecraft.nbt.NbtIo.write(varSaveTag, var21); }
                  int var25 = var21.method_212();
                  FormattedCharSequence var28 = Component.literal(this.method_78((long)var25)).getVisualOrderText();
                  var1.method_563().add(ClientTooltipComponent.create(var28));
               } catch (Throwable var11) {
               }
            }
         }
      }
   }

   public void method_2(ItemStack var1, List<ItemStack> var2) {
      CustomData var3 = (CustomData)var1.get(DataComponents.CUSTOM_DATA);
      if (var3 != null && var3.copyTag().contains("BlockEntityTag")) {
         CompoundTag var4 = var3.copyTag().getCompoundOrEmpty("BlockEntityTag");
         if (var4.contains("Items")) {
            ListTag var5 = var4.getListOrEmpty("Items");

            for (int var6 = 0; var6 < var5.size(); var6++) {
               CompoundTag var7 = var5.getCompound(var6).orElse(new CompoundTag());
               ItemStack var8 = ItemStack.CODEC.parse(net.minecraft.resources.RegistryOps.create(net.minecraft.nbt.NbtOps.INSTANCE, mc.player.registryAccess()), var7).result().orElse(null);
               byte var9 = -1;
               if (var7.contains("Count")) {
                  var9 = var7.getByteOr("Count", (byte)0);
               }

               if (var8 != null && var9 == 0) {
                  var8.setCount(69);
                  var2.set(var6, var8);
               }
            }
         }
      }
   }

   public String method_78(long var1) {
      long var3 = var1 / 1024L;
      long var5 = var3 / 1024L;
      if (var5 > 0L) {
         return String.format("%,d MB", var5);
      } else {
         return var3 > 0L ? String.format("%,d KB", var3) : String.format("%,d B", var1);
      }
   }

   public Color method_408() {
      return !this.colored.getValue() ? null : this.field_1191;
   }

   public void method_16(Color var1) {
      this.field_1191 = var1;
   }
}
