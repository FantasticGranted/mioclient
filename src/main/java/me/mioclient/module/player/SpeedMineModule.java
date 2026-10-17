package me.mioclient.module.player;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import me.mioclient.Hub;
import me.mioclient.api.BlockBreakingAccess;
import me.mioclient.enum_.Class_0174;
import me.mioclient.enum_.Class_0400;
import me.mioclient.enum_.SpeedMineSwapMode;
import me.mioclient.enum_.AutoExpMode;
import me.mioclient.enum_.RebreakMode;
import me.mioclient.enum_.SelfFillMode;
import me.mioclient.enum_.Class_1172;
import me.mioclient.enum_.SpeedMineRenderMode;
import me.mioclient.enum_.PreType;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.DisconnectEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.SlotUpdateEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.AttackBlockEvent;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.Class_0277;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.Class_1116;
import me.mioclient.internal.FadingBox;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_10;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.module.combat.AutoCrystalModule;
import me.mioclient.module.combat.AutoExpModule;
import me.mioclient.module.combat.CombatmineModule;
import me.mioclient.module.movement.NoSlowModule;
import me.mioclient.runnable.Class_1282;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import me.mioclient.nick.Settings;
import org.jetbrains.annotations.Nullable;

public class SpeedMineModule extends Module {
   public static final AutoExpModule field_3981 = Hub.field_2595.method_78(AutoExpModule.class);
   public static AutoCrystalModule autoCrystal = Hub.field_2595.method_78(AutoCrystalModule.class);
   public static final CombatmineModule field_3982 = Hub.field_2595.method_78(CombatmineModule.class);
   public static final AbstractModule_10 field_3983 = Hub.field_2595.method_78(AbstractModule_10.class);
   public static final AbstractModule_28 field_3984 = Hub.field_2595.method_78(AbstractModule_28.class);
   public static final NoSlowModule field_3985 = Hub.field_2595.method_78(NoSlowModule.class);
   public static ItemSaverModule field_405 = Hub.field_2595.method_78(ItemSaverModule.class);
   public static boolean field_3986;
   public Setting<SelfFillMode> mode;
   public Setting<Float> damage;
   public Setting<Float> range;
   public Setting<Boolean> extraBreak;
   public Setting<Boolean> tPSSync;
   public Setting<Boolean> field_3992;
   public Setting<Boolean> rebreak;
   public Setting<RebreakMode> rebreak2;
   public Setting<Boolean> assistant;
   public Setting<Float> instantDelay;
   public Setting<Boolean> clickReset;
   public Setting<Boolean> holdingBest;
   public Setting<Boolean> autoSwap;
   public Setting<SpeedMineSwapMode> autoSwap2;
   public Setting<Boolean> swapBack;
   public Setting<Class_0400> field_4002;
   public Setting<Boolean> alternative;
   public Setting<Boolean> swapReset;
   public Setting<Boolean> rotate;
   public Setting<Float> limit;
   public Setting<Boolean> render;
   public Setting<Float> lineWidth;
   public Setting<SpeedMineRenderMode> renderMode;
   public Setting<Boolean> air;
   public Setting<Boolean> fade;
   public Setting<Class_0174> field_4012;
   public Setting<Color> fill;
   public Setting<Color> outline;
   public Setting<Set<Block>> field_4015;
   public final ArrayDeque<Boolean> field_4016;
   public final Timer field_4017;
   public final Timer field_4018;
   public final Timer field_4019;
   public final Timer field_4020;
   public static final AtomicBoolean field_4021 = new AtomicBoolean();
   public final AtomicBoolean field_4022;
   public final Class_1116 field_4023;
   public final FadingBox field_4024;
   @Nullable
   public Class_1282 field_4025;
   public float field_877;
   public float field_4026;
   public int field_4027;
   public int field_1980;
   public Class_0277 field_4028;
   public BlockPos field_3164;
   public BlockPos field_4029;
   public volatile boolean field_4030;

   public SpeedMineModule() {
      super("SpeedMine", "Mines blocks silently.", Category.PLAYER);
      Settings.initialize(this);
      this.field_4016 = new ArrayDeque<>();
      this.field_4017 = new Timer();
      this.field_4018 = new Timer();
      this.field_4019 = new Timer();
      this.field_4020 = new Timer();
      this.field_4022 = new AtomicBoolean();
      this.field_4023 = new Class_1116(this);
      this.field_4024 = new FadingBox();
      this.field_4027 = -1;
      this.field_4028 = null;
      this.field_4030 = false;
      this.rebreak.method_31("RebreakPage");
      this.autoSwap.method_31("SwapPage");
   }

   @Override
   public void onEnable() {
      this.reset();
      this.field_4029 = null;
      field_4021.set(false);
   }

   @Override
   public String getInfo() {
      return !BlockUtil.method_3(this.field_3164)
         ? null
         : "%.2f".formatted(Mth.clamp(this.method_1119() / this.damage.getValue(), 0.0F, 1F));
   }

   @Subscribe
   public void method_2(AttackBlockEvent var1) {
      if (!this.method_686(var1.method_111())) {
         this.field_4029 = null;
         this.reset();
      } else if (!mc.player.isSpectator() && !mc.player.isCreative()) {
         var1.method_463();
         if (!this.field_4017.method_9(100L) && !field_3986) {
            if (var1.method_111().equals(this.field_3164)) {
               this.field_4017.reset();
            }
         } else if (BlockUtil.method_3(var1.method_111()) || field_3986) {
            if (!this.extraBreak.getValue() || this.field_4028 == null || !var1.method_111().equals(this.field_4028.method_111())) {
               double var2 = mc.player.getEyePosition().distanceToSqr(var1.method_111().getCenter());
               if (!(var2 > (double)(this.range.getValue() * this.range.getValue()))) {
                  this.field_4017.reset();
                  boolean var4 = false;
                  if (this.field_3164 != null && !this.field_3164.equals(var1.method_111())) {
                     this.reset();
                     var4 = true;
                  }

                  if (!var4 && this.field_3164 != null && this.field_3164.equals(var1.method_111())) {
                     if (this.clickReset.getValue() && this.field_4019.method_9(100L)) {
                        this.field_4029 = null;
                        this.reset();
                     }
                  } else {
                     if (this.alternative.getValue() && this.autoSwap2.getValue() == SpeedMineSwapMode.SILENT && this.swapReset.getValue()) {
                        if (this.field_4030) {
                           this.field_4030 = false;
                           this.reset();
                           return;
                        }

                        if (field_3981.isToggled() && field_3981.mode.getValue() == AutoExpMode.SILENT && !field_3981.field_1702) {
                           this.reset();
                           return;
                        }
                     }

                     if (!field_3986) {
                        field_3982.method_251(true);
                     }

                     this.field_3164 = this.field_4029 = var1.method_111();
                     Direction var5 = this.method_664(this.field_3164);
                     if (this.field_3992.getValue()) {
                        mc.player.swing(InteractionHand.MAIN_HAND);
                     }

                     int var6 = this.method_456(this.field_3164);
                     int var7 = mc.player.getInventory().selected;
                     boolean var8 = var6 != -1;
                     if (this.autoSwap2.getValue() == SpeedMineSwapMode.NORMAL) {
                        var6 = InventoryUtil.method_29(this.field_3164, true);
                        if (var6 != var7 && this.swapBack.getValue()) {
                           this.field_1980 = var7;
                        }
                     }

                     if (var6 != -1) {
                        if (this.alternative.getValue() && this.autoSwap2.getValue() == SpeedMineSwapMode.SILENT) {
                           InventoryUtil.method_39(var6);
                        } else if (var7 != var6) {
                           InventoryUtil.method_16(var6);
                        }
                     }

                     if (this.extraBreak.getValue()) {
                        PacketSender.method_2(Action.START_DESTROY_BLOCK, this.field_3164, var5);
                        PacketSender.method_2(Action.STOP_DESTROY_BLOCK, this.field_3164, var5);
                        if (this.field_4028 == null) {
                           this.field_4028 = new Class_0277(this.field_3164);
                        }
                     } else {
                        PacketSender.method_2(Action.START_DESTROY_BLOCK, this.field_3164, var5);
                        if (var8) {
                           PacketSender.method_2(Action.STOP_DESTROY_BLOCK, this.field_3164, var5);
                        }
                     }

                     if (var8 && var6 != -1) {
                        if (this.autoSwap2.getValue() == SpeedMineSwapMode.SILENT) {
                           if (this.alternative.getValue()) {
                              InventoryUtil.method_39(var6);
                           } else if (var7 != var6) {
                              InventoryUtil.method_16(var7);
                           }
                        }

                        Hub.field_2622.method_99(this.field_3164);
                        this.field_4017.setTime(-1L);
                     }

                     mc.player.swing(InteractionHand.MAIN_HAND);
                     this.field_4019.reset();
                  }
               }
            }
         }
      }
   }

   @Subscribe
   public void method_2(DisconnectEvent var1) {
      this.reset();
      this.field_4029 = null;
   }

   @Subscribe(
      method_800 = 100
   )
   public void method_9(SlotUpdateEvent var1) {
      if (this.alternative.getValue() && this.autoSwap2.getValue() == SpeedMineSwapMode.SILENT && this.swapReset.getValue()) {
         this.field_4022.set(true);
         this.field_877 = this.field_4026 = 0.0F;
         Hub.field_2619.method_2(() -> {
            this.field_4022.set(false);
            BlockPos var1x = this.field_3164;
            if (var1x != null) {
               if (this.field_3992.getValue()) {
                  PacketSender.method_9(InteractionHand.MAIN_HAND);
               }

               if (BlockUtil.method_3(var1x)) {
                  field_3986 = true;
                  this.reset();
                  this.method_2(new AttackBlockEvent(var1x, this.method_664(var1x)));
                  field_3986 = false;
               } else if (this.rebreak2.getValue() == RebreakMode.FAST) {
                  PacketSender.method_2(Action.START_DESTROY_BLOCK, var1x, this.method_664(var1x));
                  this.field_4016.clear();
               }
            }
         }, 1);
      }
   }

   @Subscribe
   public void method_4(PacketReceiveEvent var1) {
      this.field_4023.method_7(var1);
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (RotationManager.method_513()
         && this.damage.getValue() < 1F
         && !field_3984.method_1052()
         && var1.method_127() instanceof ServerboundPlayerActionPacket var2
         && var2.getAction() == Action.STOP_DESTROY_BLOCK) {
         PacketSender.method_2(Action.ABORT_DESTROY_BLOCK, var2.getPos().offset(0, 500, 0), var2.getDirection());
      }
   }

   @Subscribe(
      method_800 = 100
   )
   public void method_5(Event_7 var1) {
      if (!field_4021.get()) {
         if (this.autoSwap2.getValue() != SpeedMineSwapMode.NONE || ItemSaverModule.method_101(mc.player.getMainHandItem()) || !field_405.isToggled()) {
            boolean var2 = EntityUtil.method_76() > 4F || !mc.player.getMainHandItem().is(Items.TOTEM_OF_UNDYING);
            if (this.field_4028 != null
               && !this.field_4028.method_111().equals(this.field_3164)
               && this.field_4028.method_200() >= 1F
               && !field_3985.method_573()
               && var2) {
               field_4021.set(true);
               int var9 = mc.player.getInventory().selected;
               int var10 = InventoryUtil.method_29(this.field_4028.method_111(), !this.alternative.getValue());
               if (this.alternative.getValue()) {
                  InventoryUtil.method_39(var10);
               } else {
                  PacketSender.method_9(var10, true);
               }

               this.method_574(this.field_4028.method_111());
               this.field_4020.reset();
               field_3982.field_3712.method_113(this.field_4028.method_111());
               this.field_4028 = null;
               Hub.field_2619.method_2(() -> {
                  if (this.alternative.getValue()) {
                     InventoryUtil.method_39(var10);
                  } else {
                     PacketSender.method_9(var9, true);
                     mc.player.getInventory().selected = var9;
                  }

                  field_4021.set(false);
               }, 2);
            } else if (this.field_1980 != -1
               && !BlockUtil.method_3(this.field_3164)
               && this.autoSwap2.getValue() == SpeedMineSwapMode.NORMAL
               && this.swapBack.getValue()) {
               InventoryUtil.method_16(this.field_1980);
               this.field_1980 = -1;
            } else if (!this.method_1113()) {
               if (!mc.player.isSpectator() && !mc.player.isCreative() && BlockUtil.method_3(this.field_3164)) {
                  if (this.limit.getValue() == 1F
                     || this.method_1119() / this.damage.getValue() >= this.limit.getValue()
                        && (
                           this.autoSwap2.getValue() != SpeedMineSwapMode.NONE
                              || !this.holdingBest.getValue()
                              || this.field_4027 == mc.player.getInventory().selected
                        )) {
                     this.method_574(this.field_3164);
                  }

                  if (!(this.method_1119() < this.damage.getValue())) {
                     if ((
                           this.rebreak2.getValue() != RebreakMode.INSTANT
                              || this.field_4018.method_2((double)this.instantDelay.getValue().floatValue(), TimeUnit.SECONDS)
                        )
                        && !field_3985.method_573()) {
                        if (!this.assistant.getValue() || this.method_1117()) {
                           int var3 = mc.player.getInventory().selected;
                           BlockState var4 = mc.level.getBlockState(this.field_3164);
                           this.field_4025 = new Class_1282(
                              this, mc.player.getMainHandItem().getItem(), this.field_4027, mc.player.getInventory().selected
                           );
                           Direction var5 = this.method_664(this.field_3164);
                           if (this.autoSwap2.getValue() != SpeedMineSwapMode.NONE
                              && this.field_4027 != -1
                              && mc.player.getInventory().selected != this.field_4027) {
                              if (this.autoSwap2.getValue() == SpeedMineSwapMode.SILENT && this.alternative.getValue()) {
                                 InventoryUtil.method_39(this.field_4027);
                              } else {
                                 this.field_1980 = mc.player.getInventory().selected;
                                 InventoryUtil.method_16(this.field_4027);
                              }
                           } else if (var3 != this.field_4027 && this.rebreak2.getValue() == RebreakMode.INSTANT && this.holdingBest.getValue()) {
                              return;
                           }

                           this.field_4018.reset();
                           if (BlockUtil.method_3(this.field_3164)) {
                              this.field_4020.reset();
                           }

                           if (this.field_3992.getValue()) {
                              PacketSender.method_9(InteractionHand.MAIN_HAND);
                           }

                           if (!RotationManager.method_513() && this.rebreak2.getValue() != RebreakMode.INSTANT) {
                              PacketSender.method_2(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.onGround());
                           }

                           BlockPos var6 = this.field_3164;
                           this.field_4023.method_994(this.field_3164);
                           PacketSender.method_2(Action.STOP_DESTROY_BLOCK, this.field_3164, var5);
                           if (field_3984.method_1052() && this.rebreak2.getValue() == RebreakMode.NONE) {
                              PacketSender.method_2(Action.ABORT_DESTROY_BLOCK, this.field_3164, var5);
                           }

                           Hub.field_2622.method_100(this.field_3164);
                           if (this.rebreak2.getValue() == RebreakMode.FAST) {
                              PacketSender.method_2(Action.ABORT_DESTROY_BLOCK, this.field_3164, var5);
                              PacketSender.method_2(Action.STOP_DESTROY_BLOCK, this.field_3164, var5);
                              PacketSender.method_2(Action.START_DESTROY_BLOCK, this.field_3164, var5);
                              this.field_3164 = null;
                           }

                           if (this.autoSwap2.getValue() == SpeedMineSwapMode.SILENT && this.field_4027 != -1) {
                              int var7 = this.field_4027;
                              Runnable var8 = () -> {
                                 if (this.alternative.getValue() && this.autoSwap2.getValue() == SpeedMineSwapMode.SILENT) {
                                    InventoryUtil.method_39(var7);
                                 } else if (var3 != this.field_4027 || this.field_4002.getValue() == Class_0400.TICK) {
                                    InventoryUtil.method_16(var3);
                                 }

                                 autoCrystal.field_4124.set(false);
                              };
                              if (this.field_4002.getValue() == Class_0400.TICK) {
                                 field_4021.set(true);
                                 Hub.field_2619.method_2(() -> {
                                    field_4021.set(false);
                                    var8.run();
                                 }, 2);
                              } else {
                                 var8.run();
                              }
                           }

                           if (this.autoSwap2.getValue() == SpeedMineSwapMode.SILENT && this.alternative.getValue()) {
                              Hub.field_2619.method_2(this.field_4025, 2);
                           }

                           if (this.rebreak2.getValue() != RebreakMode.INSTANT
                              || var4 != null && var4.getBlock() instanceof ShulkerBoxBlock && Hub.field_2622.method_101(this.field_3164)) {
                              this.reset();
                           }

                           if (this.rotate.getValue() && RotationManager.method_514()) {
                              Hub.field_2598.method_2(RotationManager.method_2(RotationManager.method_78(var6.getCenter()), Hub.field_2598.method_509()), 150);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      float var2 = this.tPSSync.getValue() ? Hub.field_2602.method_990() : 1F;
      if (this.field_4028 != null) {
         BlockState var3 = mc.level.getBlockState(this.field_4028.method_111());
         int var4 = InventoryUtil.method_29(this.field_4028.method_111(), !this.alternative.getValue());
         double var5 = BlockUtil.method_2(mc.player.getInventory().getItem(var4), var3, mc.player.onGround()) * (double)var2;
         this.field_4028.method_310((float)var5);
         if (mc.level.isEmptyBlock(this.field_4028.method_111())) {
            this.field_4028 = null;
         }
      }

      if (this.field_4029 != null
         && mc.level.getBlockState(this.field_4029).getBlock() instanceof ShulkerBoxBlock
         && Hub.field_2622.method_101(this.field_4029)) {
         this.field_4029 = null;
      }

      if (!this.field_4022.get()) {
         this.field_4026 = this.field_877;
         if (this.field_3164 == null) {
            if (this.field_4029 != null
               && !mc.player.isCreative()
               && BlockUtil.method_3(this.field_4029)
               && this.rebreak2.getValue() == RebreakMode.FAST) {
               this.field_3164 = this.field_4029;
            }
         } else {
            this.field_4027 = InventoryUtil.method_29(this.field_3164, !this.alternative.getValue());
            if (this.rebreak2.getValue() == RebreakMode.INSTANT
               && this.field_3164 != null
               && mc.level.getBlockState(this.field_3164).getBlock() == Blocks.AIR) {
               this.field_4018.reset();
            }

            if (!BlockUtil.method_3(this.field_3164)) {
               if (this.rebreak2.getValue() == RebreakMode.FAST) {
                  this.field_4016.add(mc.player.onGround());
                  if (this.field_4016.size() > 200) {
                     this.field_4016.removeLast();
                  }
               } else if (this.rebreak2.getValue() != RebreakMode.INSTANT) {
                  this.reset();
               }
            } else {
               BlockState var7 = mc.level.getBlockState(this.field_3164);
               ItemStack var8 = mc.player.getInventory().getItem(this.method_1115());
               if (this.method_1115() != mc.player.getInventory().selected && this.field_4002.getValue() == Class_0400.SLOW) {
                  var8 = new ItemStack(var8.getItem());
               }

               boolean var9 = false;
               if (this.rebreak2.getValue() == RebreakMode.FAST) {
                  while (!this.field_4016.isEmpty()) {
                     boolean var6 = this.field_4016.poll();
                     this.field_877 = this.field_877 + (float)(BlockUtil.method_2(var8, var7, var6) * (double)var2);
                     var9 = true;
                  }

                  this.field_4026 = this.field_877;
               }

               if (var9 && var7.is(Blocks.OBSIDIAN)) {
                  this.field_877 = Mth.clamp(this.field_877, 0.0F, 0.99F);
                  this.field_4026 = this.field_877;
               } else {
                  this.field_877 = this.field_877 + (float)(BlockUtil.method_2(var8, var7, mc.player.onGround()) * (double)var2);
               }
            }
         }
      }
   }

   @Subscribe
   public void method_9(Event_19 var1) {
      if (var1.method_213() == PreType.PRE && this.field_3164 != null) {
         if (!(mc.player.getEyePosition().distanceTo(this.field_3164.getCenter()) <= (double)this.range.getValue().floatValue())) {
            this.method_1116();
         }
      }
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      if (this.render.getValue()) {
         if (this.extraBreak.getValue() && this.field_4028 != null && !this.field_4028.method_111().equals(this.field_3164)) {
            this.field_4028.method_2(this, var1);
         }

         if (this.field_3164 != null) {
            VoxelShape var2 = mc.level.getBlockState(this.field_3164).getShape(mc.level, this.field_3164);
            boolean var3 = this.rebreak2.getValue() == RebreakMode.INSTANT;
            if (!var2.isEmpty() && BlockUtil.method_3(this.field_3164)) {
               this.field_4024.method_1016();
            } else {
               if (!var3) {
                  return;
               }

               if (!this.air.getValue() && !this.fade.getValue()) {
                  return;
               }
            }

            float var4 = Mth.clamp(
               Mth.lerp(RenderUtil.method_776(), this.field_4026, this.method_1119()) / this.damage.getValue(),
               0.0F,
               1F
            );
            Color[] var5 = this.field_4012.getValue().method_2(this, var4);
            AABB var6 = this.renderMode
               .getValue()
               .method_2(
                  this,
                  var2.isEmpty()
                     ? new AABB(
                        0.0,
                        0.0,
                        0.0,
                        1.0,
                        1.0,
                        1.0
                     )
                     : var2.bounds(),
                  var4
               )
               .move(this.field_3164);
            if (mc.level.getBlockState(this.field_3164).isAir()) {
               var6 = new AABB(this.field_3164);
            }

            if (this.fade.getValue() && !this.air.getValue()) {
               this.field_4024.method_29(var6);
               this.field_4024.method_17(this.lineWidth.getValue());
               this.field_4024.method_2(var1.method_10(), var5[0], var5[1], var3 ? 6e+02F : 1F, true);
            } else {
               BoxRenderer.method_5(var1.method_10(), var6, var5[0]);
               BoxRenderer.method_2(var1.method_10(), var6, var5[1], this.lineWidth.getValue());
            }
         }
      }
   }

   public boolean method_1113() {
      if (this.rebreak2.getValue() != RebreakMode.INSTANT) {
         return false;
      } else if (this.autoSwap2.getValue() == SpeedMineSwapMode.NONE) {
         return false;
      } else {
         this.field_4023.method_142();
         boolean var1 = this.field_4023.method_995();
         BlockPos var2 = this.field_3164;
         if (var1) {
            this.reset();
            this.field_4022.set(true);
            Hub.field_2619.method_2(() -> {
               this.field_4022.set(false);
               mc.gameMode.startDestroyBlock(var2, Direction.UP);
            }, 1);
         }

         return var1;
      }
   }

   public void method_574(BlockPos var1) {
      if (this.rotate.getValue() && !RotationManager.method_514()) {
         Vec3 var2 = BlockUtil.method_2(var1, Class_1172.field_3634);
         if (var2 == null) {
            var2 = var1.getCenter();
         }

         float[] var3 = RotationManager.method_2(RotationManager.method_78(var2), Hub.field_2598.method_509());
         Hub.field_2598.method_2(var3, 150, true);
      }
   }

   public double method_1114() {
      if (this.field_3164 == null) {
         return 0.0;
      } else {
         BlockState var1 = mc.level.getBlockState(this.field_3164);
         float var2 = this.tPSSync.getValue() ? Hub.field_2602.method_990() : 1F;
         return BlockUtil.method_2(mc.player.getInventory().getItem(this.method_1115()), var1, mc.player.onGround()) * (double)var2;
      }
   }

   public int method_1115() {
      return this.field_4027 < 0 ? mc.player.getInventory().selected : this.field_4027;
   }

   public void method_1116() {
      this.field_4029 = null;
      this.reset();
   }

   public void reset() {
      if (this.field_3164 != null && !this.method_535()) {
         PacketSender.method_2(Action.ABORT_DESTROY_BLOCK, this.field_3164, this.method_664(this.field_3164));
         PacketSender.method_9(InteractionHand.MAIN_HAND);
         ((BlockBreakingAccess)mc.gameMode).setBreakingBlock(false);
         ((BlockBreakingAccess)mc.gameMode).setBreakingProgress(0.0F);
      }

      this.field_3164 = null;
      this.field_877 = 0.0F;
      this.field_4027 = -1;
      this.field_4016.clear();
      this.field_4019.reset();
      this.field_4023.reset();
   }

   public boolean method_1117() {
      if (!mc.level.getBlockState(this.method_1120()).is(Blocks.OBSIDIAN)) {
         return true;
      } else {
         AABB var1 = new AABB(this.method_1120());
         boolean var2 = false;

         for (Entity var4 : mc.level.entitiesForRendering()) {
            if (var4 instanceof Player && var4 != mc.player && !var2) {
               if (var4.getBoundingBox()
                  .expandTowards(0.0, 1.0, 0.0)
                  .inflate(1.0, 0.0, 1.0)
                  .intersects(var1)) {
                  var2 = true;
               }
            } else if (var4 instanceof EndCrystal && var4.getBoundingBox().inflate(0.0, Class_0245.field_689, 0.0).intersects(var1)) {
               return true;
            }
         }

         return !var2 || this.field_4020.method_9((long)(1e+03F / Hub.field_2602.method_990()));
      }
   }

   public int method_456(BlockPos var1) {
      if (field_3983.isToggled() && field_3983.break_.getValue()) {
         return -1;
      } else {
         int var2 = InventoryUtil.method_29(this.field_3164, !this.alternative.getValue());
         int var3 = mc.player.getInventory().selected;
         double var4 = BlockUtil.method_2(
            mc.player.getInventory().getItem(var2), mc.level.getBlockState(var1), mc.player.onGround()
         );
         if (this.autoSwap2.getValue() == SpeedMineSwapMode.NONE && var2 != var3) {
            return -1;
         } else {
            return var4 > (double)this.damage.getValue().floatValue() ? var2 : -1;
         }
      }
   }

   public BlockPos method_1118() {
      if (this.isToggled()) {
         return this.field_3164;
      } else {
         return mc.gameMode.isDestroying()
               && (double)((BlockBreakingAccess)mc.gameMode).getBreakingProgress() >= 0.9
            ? ((BlockBreakingAccess)mc.gameMode).getCurrentBreakingBlock()
            : null;
      }
   }

   public float method_1119() {
      return Mth.clamp(this.field_877, 0.0F, 1F);
   }

   public BlockPos method_1120() {
      return this.field_3164;
   }

   public BlockPos method_1121() {
      return switch ((RebreakMode)this.rebreak2.getValue()) {
         case NONE -> null;
         case FAST -> this.field_4029;
         case INSTANT -> this.field_3164;
      };
   }

   public Direction method_664(BlockPos var1) {
      List var2 = InteractionUtil.method_39(var1);
      return !var2.isEmpty() ? (Direction)var2.get(0) : Direction.UP;
   }

   public boolean method_686(BlockPos var1) {
      return this.mode.getValue().method_2(mc.level.getBlockState(var1).getBlock(), this.field_4015);
   }

   public float method_760() {
      return this.lineWidth.getValue();
   }

   public Class_1282 method_1122() {
      return this.field_4025;
   }

   public boolean method_4(long var1) {
      return this.field_4020.method_9(var1);
   }

   public Class_0277 method_1123() {
      return this.field_4028;
   }

   public static boolean method_1124() {
      return field_4021.get();
   }
}
