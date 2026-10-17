package me.mioclient.module.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.enum_.Priority;
import me.mioclient.enum_.SettingMode;
import me.mioclient.enum_.TotemType;
import me.mioclient.event.FrameRenderEvent;
import me.mioclient.event.InteractBlockEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.KeybindUtil;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.RegistrySetting;
import me.mioclient.internal.Timer;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.Class_0509;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.ChatUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.module.player.NoInteractModule;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class OffhandModule extends Module {
   public static final AutoCrystalModule field_1959 = Hub.field_2595.method_78(AutoCrystalModule.class);
   public static final OffhandModule field_1960 = Hub.field_2595.method_78(OffhandModule.class);
   public static final NoInteractModule field_1961 = Hub.field_2595.method_78(NoInteractModule.class);
   public static AbstractModule_28 field_144 = Hub.field_2595.method_78(AbstractModule_28.class);
   public Setting<TotemType> item;
   public Setting<Integer> delay;
   public Setting<Float> health;
   public Setting<Boolean> lethal;
   public Setting<Boolean> field_1966;
   public Setting<Boolean> containers;
   public Setting<Boolean> gappleBind;
   public Setting<Float> safe;
   public Setting<Boolean> pickaxe;
   public Setting<Boolean> crystals;
   public Setting<Boolean> totems;
   public Setting<Boolean> crapple;
   public Setting<Set<Item>> custom;
   public final Timer field_1975;
   public final Timer field_1976;
   public final Timer field_1977;
   public final Timer field_1978;
   public final List<Long> field_1979;
   public int field_1980;
   public boolean field_1981;
   public boolean field_1982;
   public boolean field_478;
   public final Class_0509 field_1983;
   public static boolean field_1984;

   public OffhandModule() {
      super("Offhand", "Manages your offhand automatically.", Category.COMBAT, "autototem");
      Settings.initialize(this);
      this.field_1975 = new Timer();
      this.field_1976 = new Timer();
      this.field_1977 = new Timer();
      this.field_1978 = new Timer();
      this.field_1979 = new ArrayList<>();
      this.field_1980 = -1;
      this.field_1983 = new Class_0509() {
         @Override
         public boolean method_206() {
            return mc.player.getMainHandItem().is(Items.TOTEM_OF_UNDYING)
               ? false
               : OffhandModule.this.lethal.getValue() || OffhandModule.this.health.method_105();
         }
      };
      this.health.method_2("Adaptive", SettingMode.MIN);
      this.lethal.method_5(var1 -> !this.health.method_105());
      this.gappleBind.method_31("GappleBind");
      this.safe.method_31("SafeHealth");
      this.method_7(this.custom);
   }

   @Override
   public String getInfo() {
      return this.item.getValue().toString();
   }

   @Override
   public void onToggle() {
      this.field_1980 = -1;
   }

   @Subscribe(
      method_800 = 100
   )
   public void method_9(FrameRenderEvent var1) {
      this.field_1982 = var1.method_93();
      this.method_633();
   }

   public void method_633() {
      if (!this.method_535()) {
         if (mc.player.isAlive()) {
            this.field_478 = false;
         }

         field_1984 = false;
         this.field_1983.method_142();
         if (!this.method_638()) {
            if (this.method_569() && this.field_1975.method_9((long)this.delay.getValue().intValue())) {
               this.field_1979.removeIf(var0 -> var0 == null || System.currentTimeMillis() > var0 + 1000L);
               byte var1 = 4;
               if (this.field_1979.size() > var1) {
                  this.field_1976.reset();
               }

               this.method_300();
            }
         }
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundEntityEventPacket var2
         && !this.method_638()
         && this.method_569()
         && var2.getEntity(mc.level) == mc.player
         && var2.getEventId() == 35) {
         if (!mc.player.getMainHandItem().is(Items.TOTEM_OF_UNDYING) && mc.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
            mc.player.getOffhandItem().shrink(1);
         }

         this.field_1978.reset();
         this.field_1979.clear();
         this.method_300();
      }

      if (var1.method_127() instanceof ClientboundAddEntityPacket var4 && this.field_1983.method_206() && var4.getType() == EntityType.END_CRYSTAL) {
         Vec3 var6 = new Vec3(var4.getX(), var4.getY(), var4.getZ());
         if (this.field_1983.method_29(var6)) {
            this.field_1977.reset();
         }
      }

      if (var1.method_127() instanceof ClientboundPlayerCombatKillPacket) {
         if (!mc.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
            return;
         }

         this.method_637();
      }
   }

   @Subscribe
   public void method_2(InteractBlockEvent var1) {
      if (var1.method_12() == InteractionHand.MAIN_HAND && this.method_2((TotemType)null) != null && !field_144.method_1052()) {
         mc.gameMode.useItem(mc.player, InteractionHand.OFF_HAND);
         var1.method_463();
      }
   }

   public TotemType method_634() {
      if (field_1959.isToggled() && field_1959.forceSuicide.getValue() && this.item.getValue() != TotemType.TOTEM) {
         return TotemType.CRYSTAL;
      } else {
         boolean var1 = this.crapple.getValue();
         TotemType var2 = TotemType.TOTEM;
         if (this.field_1983.method_538()) {
            return TotemType.TOTEM;
         } else {
            float var3 = EntityUtil.method_76();
            if (var3 > this.health.getValue()
               && mc.player.fallDistance < 16F
               && InventoryUtil.method_29((Predicate<ItemStack>)(var2x -> var2x.is(this.item.getValue().method_98(var1)))) > 0) {
               var2 = this.item.getValue();
            }

            return this.method_2(var2);
         }
      }
   }

   public boolean method_14(Item var1) {
      if (InventoryUtil.isSwordItem(var1) || var1 instanceof AxeItem) {
         return true;
      } else if (InventoryUtil.isPickaxe(var1) && this.pickaxe.getValue()) {
         return true;
      } else {
         return var1 == Items.TOTEM_OF_UNDYING && this.totems.getValue() ? true : var1 == Items.END_CRYSTAL && this.crystals.getValue();
      }
   }

   public TotemType method_2(TotemType var1) {
      if (var1 != null && var1.method_98(this.crapple.getValue()) instanceof ShieldItem) {
         return var1;
      } else if (BlockUtil.method_2(mc.hitResult) && !mc.player.isShiftKeyDown() && !field_1961.isToggled()) {
         return var1;
      } else {
         float var2 = EntityUtil.method_76();
         Item var3 = mc.player.getMainHandItem().getItem();
         boolean var4 = this.safe.getValue() < var2;
         if (var3 == Items.TOTEM_OF_UNDYING) {
            var4 = true;
         }

         return this.gappleBind.getValue()
               && mc.options.keyUse.isDown()
               && var4
               && this.method_14(var3)
               && mc.screen == null
               && this.item.getValue() != TotemType.GAPPLE
            ? TotemType.GAPPLE
            : var1;
      }
   }

   public void method_635() {
      this.field_1979.add(System.currentTimeMillis());
      Hub.field_2611.method_154(true);
      if (Hub.field_2602.method_992()) {
         PacketSender.method_2(mc.player, Action.STOP_SPRINTING, 0);
         this.field_1981 = true;
      }
   }

   public void method_636() {
      Hub.field_2611.method_154(false);
      if (!(mc.screen instanceof AbstractContainerScreen)) {
         Hub.field_2611.close();
      }

      if (this.field_1981) {
         PacketSender.method_2(mc.player, Action.START_SPRINTING, 0);
         this.field_1981 = false;
      }
   }

   public void method_300() {
      TotemType var1 = this.method_634();
      int var2 = var1.method_186(this.crapple.getValue());
      if (this.field_1980 != -1
         && (!(mc.screen instanceof AbstractContainerScreen) || mc.screen instanceof InventoryScreen)
         && mc.player
            .getInventory()
            .getItem(this.field_1980 >= 36 ? this.field_1980 - 36 : this.field_1980)
            .is(var1.method_98(this.crapple.getValue()))
         && var2 != -1) {
         var2 = this.field_1980;
      }

      if (var2 != -1) {
         if (this.containers.getValue()
            || !(mc.screen instanceof ShulkerBoxScreen) && !(mc.screen instanceof ContainerScreen)) {
            if (var2 < mc.player.containerMenu.slots.size()) {
               ItemStack var3 = mc.player.containerMenu.getCarried();
               ItemStack var4 = mc.player.containerMenu.getSlot(var2).getItem();
               if (!var4.has(DataComponents.FOOD) || this.field_1982) {
                  this.method_635();
                  int var5 = mc.player.containerMenu.containerId;
                  if (!var3.isEmpty() && !InventoryUtil.method_161()) {
                     mc.gameMode.handleContainerInput(var5, InventoryUtil.method_9(Items.AIR), 0, ContainerInput.PICKUP, mc.player);
                  }

                  if (this.field_1966.getValue() && mc.player.containerMenu.getCarried().isEmpty()) {
                     this.field_1980 = var2;
                     mc.gameMode.handleContainerInput(var5, var2, 40, ContainerInput.SWAP, mc.player);
                     this.field_1975.reset();
                  } else {
                     this.method_78(var2);
                     this.field_1980 = var2;
                  }

                  field_1984 = true;
                  this.method_636();
                  if (KeybindUtil.method_2(mc.options.keyUse) && var4.has(DataComponents.FOOD)) {
                     mc.gameMode.useItem(mc.player, InteractionHand.OFF_HAND);
                  }
               }
            }
         }
      }
   }

   public void method_78(int var1) {
      if (this.field_1975.method_9((long)this.delay.getValue().intValue())) {
         if (mc.player.containerMenu.slots.size() > 45) {
            InventoryUtil.method_5(var1, 45);
            this.field_1975.reset();
         }
      }
   }

   public void method_7(Setting<Set<Item>> var1) {
      var1.method_9(() -> {
         RegistrySetting var1x = (RegistrySetting)var1;
         if (var1x.method_98() != null) {
            ((Set)var1.getValue()).clear();
            ((Set)var1.getValue()).add((Item)var1x.method_98());
         }
      });
   }

   public void method_637() {
      long var1 = this.field_1978.method_271();
      int var3 = Hub.field_2602.method_983();
      if (!this.field_478) {
         MutableComponent var4 = Component.literal("Since last pop: %dms. Latency: %dms.".formatted(var1, var3))
            .append(
               Component.literal(" Possible death reasons: Damage override/Lag spike.*")
                  .withStyle(var0 -> var0.withHoverEvent(new HoverEvent.ShowText( Component.literal("More info in the game log."))))
            );
         if ((long)var3 > var1) {
            ChatUtil.method_2(var4, ChatUtil.method_38(Math.abs(this.getName().hashCode()) * -1 - 1), Priority.HIGH);
            System.out
               .println(
                  "*Death was most likely caused by you taking damage before the server receives the update slot packet, making you die holding a totem client-side."
               );
            this.field_478 = true;
         }
      }
   }

   public boolean method_638() {
      return field_1959.isToggled() && field_1959.forceSuicide.getValue() && this.item.getValue() == TotemType.TOTEM;
   }

   public boolean method_569() {
      return this.field_1966.getValue()
         ? true
         : !(mc.screen instanceof CreativeModeInventoryScreen)
            && mc.player.inventoryMenu == mc.player.containerMenu
            && !(mc.screen instanceof ContainerScreen);
   }

   public static boolean method_639() {
      return field_1984 && field_1960.isToggled();
   }
}
