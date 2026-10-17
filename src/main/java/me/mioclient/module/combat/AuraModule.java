package me.mioclient.module.combat;

import java.awt.Color;
import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.enum_.SwapMode;
import me.mioclient.enum_.CriticalsMode;
import me.mioclient.enum_.WeaponMode;
import me.mioclient.enum_.MaceMode;
import me.mioclient.enum_.TargetSort;
import me.mioclient.enum_.SettingMode;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.TimedAnimation;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.internal.BoxUtil;
import me.mioclient.internal.EnchantmentUtil;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.RaycastUtil;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.Colors;
import me.mioclient.internal.BlockUtil;
import me.mioclient.mixin.ducks.DuckPlayerMoveC2SPacket;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.module.movement.FastSwimModule;
import me.mioclient.module.movement.FlightModule;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.AABB;
import me.mioclient.nick.Settings;

public class   AuraModule extends Module {
   public static final CriticalsModule field_1038 = Hub.field_2595.method_78(CriticalsModule.class);
   public static final FastSwimModule field_1039 = Hub.field_2595.method_78(FastSwimModule.class);
   public static final FlightModule field_1040 = Hub.field_2595.method_78(FlightModule.class);
   public static final AbstractModule_28 field_1041 = Hub.field_2595.method_78(AbstractModule_28.class);
   public Setting<TargetSort> sorting;
   public Setting<Float> fov;
   public Setting<Float> delay;
   public Setting<Float> range;
   public Setting<Float> wallRange;
   public Setting<Boolean> tPSSync;
   public Setting<Boolean> pauseBaritone;
   public Setting<Boolean> rotate;
   public Setting<Float> limit;
   public Setting<Boolean> render;
   public Setting<Color> fill;
   public Setting<Color> outline;
   public Setting<Float> lineWidth;
   public Setting<Boolean> weapons;
   public Setting<WeaponMode> weapon;
   public Setting<SwapMode> swap;
   public Setting<MaceMode> mace;
   public Setting<Boolean> lagBack;
   public Setting<Boolean> pauseEating;
   public Setting<Boolean> targets;
   public Setting<Boolean> players;
   public Setting<Boolean> ignoreNakeds;
   public Setting<Boolean> animals;
   public Setting<Boolean> hostiles;
   public Setting<Boolean> neutrals;
   public Setting<Boolean> crystals;
   public Setting<Boolean> projectiles;
   public Setting<Boolean> ignoreNamed;
   public final TimedAnimation field_1070;
   public int field_1071;
   public boolean field_1072;
   public boolean field_1073;
   public Entity field_582;
   public Entity field_1074;

   public AuraModule() {
      super("Aura", "Attacks entities nearby.", Category.COMBAT, "killaura", "forcefield", "ka");
      Settings.initialize(this);
      this.field_1070 = new TimedAnimation();
      this.field_1071 = -1;
      this.limit.method_2("None", SettingMode.MIN);
      this.pauseBaritone.method_334();
   }

   @Override
   public void onDisable() {
      if (this.method_535()) {
         this.field_1071 = -1;
      } else {
         this.method_372();
      }
   }

   @Override
   public void onToggle() {
      this.field_582 = null;
      this.field_1070.method_36(0.0F);
      Hub.field_2630.method_9(this);
   }

   @Override
   public String getInfo() {
      return this.field_582 != null && this.field_582 instanceof Player ? this.field_582.getName().getString() : null;
   }

   @Subscribe
   public void method_7(Event_7 var1) {
      boolean var2 = this.field_582 != null && !this.field_582.isAlive();
      if ((this.method_3(mc.player.getMainHandItem().getItem()) || this.weapon.getValue() != WeaponMode.REQUIRE) && !var2) {
         if (mc.player.containerMenu == mc.player.inventoryMenu || !field_1041.method_1052()) {
            this.field_582 = BlockUtil.method_2(
               this.players.getValue(),
               this.neutrals.getValue(),
               this.animals.getValue(),
               this.hostiles.getValue(),
               this.crystals.getValue(),
               this.projectiles.getValue(),
               this.wallRange.getValue() + 0.5F,
               this.range.getValue() + 0.5F,
               this.ignoreNamed.getValue(),
               this.ignoreNakeds.getValue(),
               this.sorting.getValue().method_55(),
               this::method_374
            );
            Hub.field_2630.method_9(this);
            if (this.pauseBaritone.getValue() && this.field_582 != null) {
               Hub.field_2630.method_2(this);
            }

            if (this.field_582 == null) {
               this.method_372();
            } else if (!RotationManager.method_513() || this.method_107(this.field_582) || mc.player.isFallFlying()) {
               this.method_300();
               if (this.rotate.getValue()) {
                  if (!RotationManager.method_513()
                     && this.limit.getValue() != 0.0F
                     && this.limit.getValue() * this.delay.getValue() > this.method_371()) {
                     return;
                  }

                  float[] var3 = RotationManager.method_2(RotationManager.method_14(this.field_582), Hub.field_2598.method_509());
                  Hub.field_2598.method_2(var3, 5);
               }
            }
         }
      } else {
         this.field_582 = null;
      }
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundMovePlayerPacket var2
         && mc.player.fallDistance > 3F
         && !mc.level
            .noCollision(
               mc.player.getBoundingBox().expandTowards(0.0, mc.player.getDeltaMovement().y * 1.1, 0.0)
            )
         && this.field_1072) {
         ((DuckPlayerMoveC2SPacket)(Object)var2).setY(var2.getY(0.0) + 2.0);
         mc.player.setPosRaw(mc.player.getX(), mc.player.getY() + 3.0, mc.player.getZ());
         this.field_1072 = false;
      }
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      if (this.render.getValue()) {
         this.field_1070.method_2(this.field_582 == null ? 0.0F : 1F, 250L);
         float var2 = this.field_1070.method_45();
         AABB var3 = this.method_370();
         if (var3 == null || var2 == 0.0F) {
            return;
         }

         Color var4 = Colors.method_9(this.fill.getValue(), (int)((float)this.fill.getValue().getAlpha() * var2));
         Color var5 = Colors.method_9(this.outline.getValue(), (int)((float)this.outline.getValue().getAlpha() * var2));
         BoxRenderer.method_5(var1.method_10(), var3, var4);
         BoxRenderer.method_2(var1.method_10(), var3, var5, this.lineWidth.getValue());
      }
   }

   public AABB method_370() {
      AABB var1 = null;
      Entity var2 = this.field_582;
      if (var2 == null) {
         var2 = this.field_1074;
      }

      if (var2 != null) {
         var1 = BoxUtil.method_2(var2, RenderUtil.method_776());
         this.field_1074 = var2;
      }

      return var1;
   }

   public void method_300() {
      if (this.field_582 != null) {
         if (this.method_107(this.field_582)) {
            float var1 = RaycastUtil.getId(this.field_582) ? this.range.getValue() : this.wallRange.getValue();
            if (!(BlockUtil.method_39(this.field_582) > (double)var1)) {
               boolean var2 = mc.player.fallDistance <= 0.0F && !mc.player.onGround();
               if (mc.player.onGround() && Hub.field_2615.method_1161() <= 1) {
                  var2 = true;
               }

               if (mc.player.isPassenger()) {
                  var2 = false;
               }

               if (field_1040.isToggled()
                  || mc.player.getAbilities().flying
                  || mc.player.isFallFlying()
                  || PlayerUtil.method_29(mc.player)
                  || mc.player.onClimbable()
                  || !(this.field_582 instanceof LivingEntity)
                  || !var2
                  || field_1038.isToggled() && field_1038.mode.getValue() == CriticalsMode.GRIM
                  || mc.player.hasEffect(MobEffects.LEVITATION)) {
                  int var3 = mc.player.getInventory().selected;
                  int var4 = InventoryUtil.method_7((Predicate<ItemStack>)(var0 -> var0.getItem() instanceof AxeItem || InventoryUtil.isSword(var0)));
                  boolean var5 = this.swap.getValue() == SwapMode.SILENT && this.method_375();
                  if (this.method_375()) {
                     if ((!var5 || this.pauseEating.getValue())
                        && var4 != var3
                        && mc.player.isUsingItem()
                        && mc.player.getUsedItemHand() == InteractionHand.MAIN_HAND) {
                        return;
                     }

                     InventoryUtil.method_16(var4);
                     if (var3 != var4) {
                        this.field_1071 = var3;
                     }
                  }

                  boolean var6 = mc.player.isSprinting();
                  if (var6) {
                     mc.player.connection.send(new ServerboundPlayerCommandPacket(mc.player, Action.STOP_SPRINTING));
                  }

                  int var7 = this.method_373();
                  if (this.field_582 instanceof LivingEntity var8 && !PlayerUtil.method_29(var8)) {
                     var7 = -1;
                  }

                  if (var7 != -1) {
                     InventoryUtil.method_16(var7);
                  }

                  InteractionUtil.method_29(this.field_582);
                  mc.player.resetAttackStrengthTicker();
                  if (var6) {
                     mc.player.connection.send(new ServerboundPlayerCommandPacket(mc.player, Action.START_SPRINTING));
                  }

                  field_1039.field_4445 = false;
                  if (var7 != -1) {
                     if (this.lagBack.getValue()) {
                        this.field_1072 = EnchantmentUtil.method_9(Enchantments.DENSITY, mc.player.getInventory().getItem(var7));
                     }

                     InventoryUtil.method_16(var3);
                  }

                  if (var5 && var4 != -1) {
                     InventoryUtil.method_16(var3);
                  }
               }
            }
         }
      }
   }

   public boolean method_107(Entity var1) {
      if (!(this.field_582 instanceof Player)
         && var1 instanceof LivingEntity var2
         && (double)EntityUtil.method_2((Entity)var2) <= 1.0
         && var2.getMaxHealth() <= 4F) {
         return true;
      }

      double var4 = (double)this.delay.getValue().floatValue();
      return (double)this.method_371() >= var4;
   }

   public float method_371() {
      double var1 = Class_0245.field_688;
      if (this.tPSSync.getValue()) {
         var1 -= (double)(2e+01F - Hub.field_2602.method_989());
      }

      return mc.player.getAttackStrengthScale((float)var1);
   }

   public boolean method_3(Item var1) {
      return InventoryUtil.isSwordItem(var1)
         || var1 instanceof TridentItem
         || var1 instanceof AxeItem
         || var1 instanceof MaceItem
         || InventoryUtil.isPickaxe(var1)
            && InventoryUtil.method_7((Predicate<ItemStack>)(var0 -> InventoryUtil.isSwordItem(var0.getItem()) || var0.getItem() instanceof AxeItem)) == -1;
   }

   public void method_372() {
      if (this.method_375() && this.swap.getValue() == SwapMode.SWAPBACK && this.field_1071 != -1) {
         InventoryUtil.method_16(this.field_1071);
         this.field_1071 = -1;
      }
   }

   public int method_373() {
      if (this.swap.getValue() == SwapMode.SILENT && this.method_375()) {
         return -1;
      } else {
         int var1 = InventoryUtil.method_7((Predicate<ItemStack>)(var1x -> var1x.is(Items.MACE) && this.mace.getValue().method_16(var1x)));
         return this.mace.getValue() == MaceMode.SMART && mc.player.fallDistance > 3F && var1 == -1
            ? InventoryUtil.method_7((Predicate<ItemStack>)(var0 -> var0.is(Items.MACE)))
            : var1;
      }
   }

   public boolean method_374(Entity var1) {
      float[] var2 = RotationManager.method_14(var1);
      float var3 = Mth.degreesDifferenceAbs(var2[0], mc.player.getYRot());
      float var4 = Math.abs(mc.player.getXRot() - var2[1]);
      return var3 <= this.fov.getValue() && var4 <= this.fov.getValue();
   }

   public boolean method_375() {
      return this.weapon.getValue() == WeaponMode.SWAP;
   }
}
