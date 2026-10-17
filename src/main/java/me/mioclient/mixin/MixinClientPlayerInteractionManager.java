package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.mioclient.Hub;
import me.mioclient.api.BlockBreakingAccess;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.NoInteractMode;
import me.mioclient.event.StopUsingItemEvent;
import me.mioclient.event.InteractBlockEvent;
import me.mioclient.event.BlockBreakEvent;
import me.mioclient.event.InteractItemEvent;
import me.mioclient.event.UseItemEvent;
import me.mioclient.event.AttackBlockEvent;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.combat.AutoClickerModule;
import me.mioclient.module.player.InventoryTweaksModule;
import me.mioclient.module.player.NoInteractModule;
import me.mioclient.module.player.SpeedMineModule;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import me.mioclient.internal.Inputs;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({MultiPlayerGameMode.class})
public abstract class MixinClientPlayerInteractionManager implements MinecraftAccess, BlockBreakingAccess {
   private static final AutoClickerModule autoclicker = Hub.field_2595.method_78(AutoClickerModule.class);
   private static final NoInteractModule nointeract = Hub.field_2595.method_78(NoInteractModule.class);
   private static final InventoryTweaksModule inventorytweaks = Hub.field_2595.method_78(InventoryTweaksModule.class);
   private static SpeedMineModule speedmine = Hub.field_2595.method_78(SpeedMineModule.class);
   @Shadow
   private boolean isDestroying;
   @Shadow
   private float destroyProgress;
   @Shadow
   private BlockPos destroyBlockPos;
   @Shadow
   private int destroyDelay;

   public MixinClientPlayerInteractionManager() {
      super();
   }

   @Shadow
   public abstract void handleContainerInput(int var1, int var2, int var3, ContainerInput var4, Player var5);

   @Inject(
      method = {"destroyBlock"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void breakBlock(BlockPos var1, CallbackInfoReturnable<Boolean> var2) {
      BlockBreakEvent var3 = new BlockBreakEvent(var1);
      lookups.method_36(var3);
      if (var3.method_464()) {
         var2.setReturnValue(false);
         var2.cancel();
      }
   }

   @Inject(
      method = {"startDestroyBlock"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void attackBlockHook(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      AttackBlockEvent var4 = new AttackBlockEvent(var1, var2);
      lookups.method_36(var4);
      if (var4.method_464()) {
         var3.setReturnValue(true);
         var3.cancel();
      }
   }

   @Inject(
      method = {"handleContainerInput"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onClickArmorSlot(int var1, int var2, int var3, ContainerInput var4, Player var5, CallbackInfo var6) {
      if (inventorytweaks.isToggled()) {
         AbstractContainerMenu var7 = var5.containerMenu;
         if (inventorytweaks.fastArmor.getValue() && var3 == 1 && var4 == ContainerInput.QUICK_MOVE) {
            Slot var8 = var7.getSlot(var2);
            if (var8.getItem().get(DataComponents.EQUIPPABLE) != null || var8.getItem().is(Items.ELYTRA)) {
               InventoryUtil.method_5(var2, switch (mc.player.getEquipmentSlotForItem(var8.getItem())) {
                  case HEAD -> 5;
                  case CHEST -> 6;
                  case LEGS -> 7;
                  case FEET -> 8;
                  default -> -1;
               });
               var6.cancel();
               return;
            }
         }

         if (inventorytweaks.xCarryTweaks.getValue() && var3 == 1 && var4 == ContainerInput.QUICK_MOVE && var2 > 4 && var7 instanceof InventoryMenu) {
            for (int var10 = 0; var10 < 4; var10++) {
               ItemStack var9 = var7.slots.get(var10 + 1).getItem();
               if (var9.isEmpty()) {
                  this.handleContainerInput(var1, var2, 0, ContainerInput.PICKUP, mc.player);
                  this.handleContainerInput(var1, var10 + 1, 0, ContainerInput.PICKUP, mc.player);
                  var6.cancel();
                  return;
               }
            }
         }
      }
   }

   @Inject(
      method = {"useItemOn"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void interactBlockHook(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      InteractBlockEvent var5 = new InteractBlockEvent(var3, var2);
      lookups.method_36(var5);
      if (var5.method_464()) {
         var4.setReturnValue(InteractionResult.FAIL);
         var4.cancel();
      }

      var2 = NoInteractModule.method_490();
      ItemStack var6 = mc.player.getItemInHand(NoInteractModule.method_490());
      if (nointeract.method_2(var6, var3.getBlockPos())) {
         if (nointeract.mode.getValue() == NoInteractMode.SHIFT) {
            nointeract.field_1454 = !Hub.field_2602.method_993();
            PacketSender.method_2(Inputs.shift(true));
         } else {
            var4.setReturnValue(InteractionResult.FAIL);
            var4.cancel();
         }

         mc.gameMode.useItem(mc.player, var2);
      }
   }

   @Inject(
      method = {"useItemOn"},
      at = {@At("RETURN")}
   )
   private void interactBlockHook2(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      if (nointeract.field_1454) {
         PacketSender.method_2(Inputs.shift(false));
         nointeract.field_1454 = false;
      }
   }

   @Inject(
      method = {"releaseUsingItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void stopUsingItemHook(Player var1, CallbackInfo var2) {
      StopUsingItemEvent var3 = new StopUsingItemEvent();
      lookups.method_36(var3);
      if (var3.method_464()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"useItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void interactItemHook(Player var1, InteractionHand var2, CallbackInfoReturnable<InteractionResult> var3) {
      UseItemEvent var4 = new UseItemEvent(var2);
      lookups.method_36(var4);
      if (var4.method_464()) {
         var3.cancel();
         var3.setReturnValue(InteractionResult.FAIL);
      }
   }

   @Inject(
      method = {"attack"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void attackEntityHook(Player var1, Entity var2, CallbackInfo var3) {
      if (autoclicker.method_185(var2) && var1 == mc.player) {
         var3.cancel();
      }
   }

   @WrapWithCondition(
      method = {"stopDestroyBlock"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"
      )}
   )
   private boolean cancelBlockBreaking(ClientPacketListener var1, Packet var2) {
      return !speedmine.isToggled();
   }

   @Inject(
      method = {"continueDestroyBlock"},
      at = {@At("HEAD")}
   )
   private void attackBlock(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      if (speedmine.isToggled()) {
         this.destroyDelay = 0;
      }
   }

   @ModifyExpressionValue(
      method = {"lambda$useItem$0"},
      at = {@At(
         value = "NEW",
         target = "(Lnet/minecraft/world/InteractionHand;IFF)Lnet/minecraft/network/protocol/game/ServerboundUseItemPacket;"
      )}
   )
   private ServerboundUseItemPacket interactItemHook(ServerboundUseItemPacket var1) {
      InteractItemEvent var2 = new InteractItemEvent(var1);
      lookups.method_36(var2);
      return var2.method_464() ? var2.method_1102() : var1;
   }

   @Override
   public boolean isBreakingBlock() {
      return this.isDestroying;
   }

   @Override
   public void setBreakingBlock(boolean var1) {
      this.isDestroying = var1;
   }

   @Override
   public float getBreakingProgress() {
      return this.destroyProgress;
   }

   @Override
   public void setBreakingProgress(float var1) {
      this.destroyProgress = var1;
   }

   @Override
   public BlockPos getCurrentBreakingBlock() {
      return this.destroyBlockPos;
   }

   @Override
   public void setCurrentBreakingBlock(BlockPos var1) {
      this.destroyBlockPos = var1;
   }
}
