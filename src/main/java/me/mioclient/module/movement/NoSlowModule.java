package me.mioclient.module.movement;

import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.enum_.NoSlowMode;
import me.mioclient.enum_.PreType;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.InteractBlockEvent;
import me.mioclient.event.Event_47;
import me.mioclient.event.TickMovementEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.AnimatedValue;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.MiniGamePanel;
import me.mioclient.internal.Timer;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.Class_0406;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.Class_0617;
import me.mioclient.internal.ClickGuiPanel;
import me.mioclient.internal.ChestSearchBar;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.mixin.ducks.DuckPlayerMoveC2SPacket;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.AbstractCommandBlockEditScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.gui.screens.inventory.StructureBlockEditScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import me.mioclient.nick.Settings;
import org.lwjgl.glfw.GLFW;
import net.minecraft.world.phys.Vec2;
import me.mioclient.internal.Inputs;

public class NoSlowModule extends Module {
   public static AbstractModule_28 field_144 = Hub.field_2595.method_78(AbstractModule_28.class);
   public Setting<Boolean> guiMove;
   public Setting<Boolean> portals;
   public Setting<Boolean> items;
   public Setting<Boolean> sneak;
   public Setting<Boolean> crawl;
   public Setting<Boolean> field_1690;
   public Setting<NoSlowMode> mode;
   public Setting<Boolean> strictInventory;
   public Setting<Boolean> multiTask;
   public Setting<Boolean> blocks;
   public Setting<Boolean> field_1695;
   public Setting<Boolean> slime;
   public Setting<Boolean> noSlimeBounce;
   public Setting<Boolean> soulSand;
   public Setting<Boolean> honey;
   public Setting<Boolean> berryBush;
   public boolean field_1701;
   public boolean field_1702;
   public boolean field_1703;
   public final Timer field_1704;
   public final AnimatedValue field_1705;
   public final AnimatedValue field_1706;

   public NoSlowModule() {
      super("NoSlow", "Cancels several things that may slow you down.", Category.MOVEMENT);
      Settings.initialize(this);
      this.field_1704 = new Timer();
      this.field_1705 = new AnimatedValue(2F, true);
      this.field_1706 = new AnimatedValue(2F, true);
      this.setDrawn(false);
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundMovePlayerPacket var2) {
         InteractionHand var6 = mc.player.getUsedItemHand();
         ItemStack var4 = mc.player.getItemInHand(var6);
         if (mc.player.isUsingItem()
            && this.items.getValue()
            && !mc.player.isHandsBusy()
            && !mc.player.isFallFlying()
            && var4.has(DataComponents.FOOD)
            && this.method_571()) {
            mc.player.connection.send(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected));
         }

         DuckPlayerMoveC2SPacket var5 = (DuckPlayerMoveC2SPacket)(Object)var2;
         if (this.guiMove.getValue()
            && this.strictInventory.getValue()
            && !this.method_572()
            && this.field_1701
            && !mc.player.isHandsBusy()
            && mc.player.onGround()
            && this.field_1704.method_9(200L)
            && !mc.player.isFallFlying()) {
            if (this.mode.getValue() == NoSlowMode.GRIMV3) {
               return;
            }

            var5.setOnGround(false);
            this.field_1701 = false;
         }
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      this.field_1702 = false;
      this.field_1703 = false;
      if (!mc.player.onGround()) {
         this.field_1704.reset();
      }
   }

   @Subscribe
   public void method_5(InteractBlockEvent var1) {
      if (this.mode.getValue() == NoSlowMode.CONSTANTIAM && this.items.getValue() && !mc.player.isFallFlying()) {
         if (mc.player.getItemInHand(var1.method_12()).has(DataComponents.FOOD)) {
            BlockPos var2 = var1.method_382().getBlockPos();
            if (!InteractionUtil.method_31(var2)) {
               mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
               var1.method_463();
            }
         }
      }
   }

   @Subscribe
   public void method_2(Event_47 var1) {
      if (var1.method_213() == PreType.PRE && this.strictInventory.getValue() && !this.method_572() && this.guiMove.getValue()) {
         if (!this.multiTask.getValue() && !this.field_1702) {
            mc.gameMode.releaseUsingItem(mc.player);
            this.field_1702 = true;
         }

         if (!MovementUtil.method_363()) {
            return;
         }

         if (this.mode.getValue() == NoSlowMode.GRIMV3) {
            return;
         }

         if (Hub.field_2615.method_1161() > 1 && !this.field_1703) {
            PacketSender.method_2(
               mc.player.getX(), mc.player.getY() + 0.0626, mc.player.getZ(), false
            );
            this.field_1703 = true;
            this.field_1701 = true;
         }

         if (mc.player.isSprinting()) {
            mc.player.connection.send(new ServerboundPlayerCommandPacket(mc.player, Action.STOP_SPRINTING));
         }

         if (mc.player.isShiftKeyDown()) {
            mc.player.connection.send(Inputs.shift(false));
         }
      } else if (var1.method_213() == PreType.POST && MovementUtil.method_363() && this.method_571() && this.guiMove.getValue()) {
         if (mc.player.isSprinting()) {
            mc.player.connection.send(new ServerboundPlayerCommandPacket(mc.player, Action.START_SPRINTING));
         }

         if (mc.player.isShiftKeyDown()) {
            mc.player.connection.send(Inputs.shift(true));
         }
      }
   }

   @Subscribe
   public void method_2(TickMovementEvent var1) {
      if (this.items.getValue() && mc.player.isUsingItem() && !mc.player.isPassenger()) {
         boolean var2 = true;
         if (this.mode.getValue() == NoSlowMode.GRIMV3) {
            var2 = mc.player.tickCount % 3 == 0 || mc.player.tickCount % 4 == 0;
            if (mc.player.tickCount % 12 == 0) {
               var2 = false;
            }

            if (mc.player.isFallFlying()) {
               var2 = false;
            }
         }

         if (var2) {
            mc.player.input.moveVector = new Vec2((mc.player.input.moveVector.x / 0.2F), mc.player.input.moveVector.y);
            mc.player.input.moveVector = new Vec2(mc.player.input.moveVector.x, (mc.player.input.moveVector.y / 0.2F));
         }

         if (this.method_572()) {
            this.method_38(mc.player.getUsedItemHand());
         }
      }
   }

   @Subscribe
   public void method_5(Event_3 var1) {
      if (this.method_569()
         && this.guiMove.getValue()
         && !(mc.screen instanceof Class_0406)
         && !(mc.screen instanceof Class_0617)) {
         if (this.method_138(264)) {
            this.field_1705
               .method_3(
                  Math.min(
                     Mth.lerp(var1.method_880(), mc.player.xRotO, mc.player.getXRot()) + 8F,
                     (float)Class_0245.field_685
                  )
               );
         } else if (this.method_138(265)) {
            this.field_1705
               .method_3(
                  Math.max(
                     Mth.lerp(var1.method_880(), mc.player.xRotO, mc.player.getXRot()) - 8F,
                     (float)(-Class_0245.field_685)
                  )
               );
         }

         if (this.method_138(262)) {
            this.field_1706
               .method_3(Mth.lerp(var1.method_880(), mc.player.yRotO, mc.player.getYRot()) + 8F);
         } else if (this.method_138(263)) {
            this.field_1706
               .method_3(Mth.lerp(var1.method_880(), mc.player.yRotO, mc.player.getYRot()) - 8F);
         }

         if (this.method_570()) {
            mc.player.setXRot(this.field_1705.method_45());
            mc.player.setYRot(this.field_1706.method_45());
         }
      } else {
         this.field_1706.method_36(mc.player.getYRot());
         this.field_1705.method_36(mc.player.getXRot());
      }
   }

   public void method_38(InteractionHand var1) {
      InteractionHand var2 = PlayerUtil.method_5(var1);
      if (this.method_251(mc.player.getItemInHand(var2))) {
         if (var2 == InteractionHand.MAIN_HAND) {
            int var4 = mc.player.getInventory().selected;
            InventoryUtil.method_78((var4 + 1) % 9);
            PacketSender.method_2(var2);
            InventoryUtil.method_78(var4);
            return;
         }

         int var3 = InventoryUtil.getId(InventoryUtil.method_2((Predicate<ItemStack>)(var1x -> !this.method_251(var1x)), true));
         mc.gameMode.handleContainerInput(0, var3, 40, ContainerInput.SWAP, mc.player);
         PacketSender.method_2(var2);
         mc.gameMode.handleContainerInput(0, var3, 40, ContainerInput.SWAP, mc.player);
      } else {
         PacketSender.method_2(var2);
      }
   }

   public boolean method_251(ItemStack var1) {
      return var1.has(DataComponents.FOOD) || var1.is(Items.BOW) || var1.is(Items.SHIELD) || var1.is(Items.ENDER_PEARL);
   }

   public boolean method_569() {
      if (mc.screen instanceof Class_0406) {
         for (ClickGuiPanel var2 : Hub.method_755().method_999()) {
            if (var2 instanceof MiniGamePanel && var2.method_194()) {
               return false;
            }
         }

         if (Hub.method_755().field_1310.isFocused() || !Hub.method_755().field_1310.getValue().isBlank()) {
            return false;
         }
      }

      return mc.screen != null
         && !(mc.screen instanceof ChatScreen)
         && !(mc.screen instanceof SignEditScreen)
         && !(mc.screen instanceof AnvilScreen)
         && !(mc.screen instanceof AbstractCommandBlockEditScreen)
         && !(mc.screen instanceof StructureBlockEditScreen)
         && !(mc.screen instanceof me.mioclient.clickgui.ClickGuiScreen)
         && (ChestSearchBar.field_3026 == null || !ChestSearchBar.field_3026.canConsumeInput());
   }

   public boolean method_570() {
      return this.method_138(264) || this.method_138(265) || this.method_138(262) || this.method_138(263);
   }

   public boolean method_138(int var1) {
      return GLFW.glfwGetKey(mc.getWindow().handle(), var1) == 1;
   }

   public boolean method_571() {
      return this.mode.getValue() == NoSlowMode.NCP;
   }

   public boolean method_572() {
      return this.mode.getValue() == NoSlowMode.GRIM;
   }

   public boolean method_573() {
      return this.isToggled()
         && !this.multiTask.getValue()
         && mc.player.isUsingItem()
         && !mc.player.isPassenger()
         && mc.player.getUsedItemHand() == InteractionHand.MAIN_HAND;
   }
}
