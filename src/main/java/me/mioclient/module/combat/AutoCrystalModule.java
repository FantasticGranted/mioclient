package me.mioclient.module.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import me.mioclient.Hub;
import me.mioclient.api.AttackTrackingAccess;
import me.mioclient.api.ServerSideDeadAccess;
import me.mioclient.api.JoinTimeAccess;
import me.mioclient.enum_.DamageResult;
import me.mioclient.enum_.AutoSwapMode;
import me.mioclient.enum_.InstantMode;
import me.mioclient.enum_.WeaknessMode;
import me.mioclient.enum_.RebreakMode;
import me.mioclient.enum_.CrystalSwingMode;
import me.mioclient.enum_.Class_1054;
import me.mioclient.enum_.SilentMode;
import me.mioclient.enum_.RaytraceMode;
import me.mioclient.enum_.Class_1172;
import me.mioclient.enum_.SequentialMode;
import me.mioclient.enum_.PreType;
import me.mioclient.event.FrameRenderEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.DisconnectEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.SlotUpdateEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.LockUtil;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.ItemUtil;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BoxUtil;
import me.mioclient.internal.Class_0794;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.internal.ExplosionDamage;
import me.mioclient.mixin.ducks.DuckEntityStatusS2CPacket;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.module.abstract_.AbstractModule_39;
import me.mioclient.module.misc.KillEffectsModule;
import me.mioclient.module.player.SpeedMineModule;
import net.minecraft.world.inventory.ContainerInput;
import me.mioclient.record.CrystalTargetInfo;
import me.mioclient.record.SpawnedEntity;
import me.mioclient.record.RenderStyle;
import me.mioclient.record.RaytraceTarget;
import me.mioclient.setting.Setting;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class AutoCrystalModule extends Module {
   public static final ExecutorService field_4048 = Executors.newVirtualThreadPerTaskExecutor();
   public static SpeedMineModule speedmine = Hub.field_2595.method_78(SpeedMineModule.class);
   public static AbstractModule_28 field_144 = Hub.field_2595.method_78(AbstractModule_28.class);
   public static AbstractModule_39 field_4049 = Hub.field_2595.method_78(AbstractModule_39.class);
   public Setting<Boolean> field_4050;
   public Setting<Integer> delay;
   public Setting<Float> range;
   public Setting<Float> wallRange;
   public Setting<Float> minDamage;
   public Setting<Float> maxSelfDamage;
   public Setting<Float> damageRatio;
   public Setting<SequentialMode> sequential;
   public Setting<Boolean> field_4058;
   public Setting<Boolean> strictDirection;
   public Setting<Boolean> mineIgnore;
   public Setting<Boolean> limit;
   public Setting<Boolean> lowerHitBox;
   public Setting<Boolean> smartTrace;
   public Setting<Boolean> basePlace;
   public Setting<Boolean> air;
   public Setting<Boolean> break_;
   public Setting<Integer> delay2;
   public Setting<Integer> factor;
   public Setting<Float> range2;
   public Setting<Float> wallRange2;
   public Setting<Float> minDamage2;
   public Setting<Float> maxSelfDamage2;
   public Setting<Boolean> antiSuicide;
   public Setting<Boolean> balance;
   public Setting<Boolean> resync;
   public Setting<Boolean> inhibit;
   public Setting<InstantMode> instant;
   public Setting<Boolean> await;
   public Setting<Integer> ticksExisted;
   public Setting<WeaknessMode> weakness;
   public Setting<Boolean> rotate;
   public Setting<Boolean> place;
   public Setting<Boolean> break_2;
   public Setting<Boolean> facePlace;
   public Setting<Float> field_4085;
   public Setting<Integer> armorThreshold;
   public Setting<Boolean> damageSync;
   public Setting<Boolean> extrapolation;
   public Setting<Integer> ticks;
   public Setting<Boolean> draw;
   public Setting<Boolean> render;
   public Setting<Color> fill;
   public Setting<Color> outline;
   public Setting<Float> lineWidth;
   public Setting<Boolean> fade;
   public Setting<Boolean> multiFade;
   public Setting<Float> fadeTime;
   public Setting<Boolean> pause;
   public Setting<Boolean> mining;
   public Setting<Boolean> eating;
   public Setting<Float> field_4101;
   public Setting<Boolean> targeting;
   public Setting<Float> targetRange;
   public Setting<Float> crystalRange;
   public Setting<Boolean> antiFriendKill;
   public Setting<Boolean> forceSuicide;
   public Setting<Boolean> ignoreNakeds;
   public Setting<Boolean> assumeBestArmor;
   public Setting<Boolean> assumeInvincibility;
   public Setting<Boolean> self;
   public Setting<Float> timeout;
   public Setting<Boolean> misc;
   public Setting<AutoSwapMode> autoSwap;
   public Setting<SilentMode> silent;
   public Setting<Boolean> swapBack;
   public Setting<Boolean> strictTiming;
   public Setting<Float> swapDelay;
   public Setting<Float> swapPenalty;
   public Setting<Class_1172> multipoint;
   public Setting<Boolean> hBFix;
   public final Timer field_4121;
   public final AtomicReference<RaytraceTarget> field_4122;
   public final AtomicReference<Vec3> field_4123;
   public final AtomicBoolean field_4124;
   public final Timer field_4125;
   public final Timer field_4126;
   public final Timer field_4127;
   public final Timer field_4128;
   public final Timer field_4129;
   public final Timer field_4130;
   public final Timer field_4131;
   public final Timer field_4132;
   public final LinkedList<Long> field_4133;
   public final List<Integer> field_4134;
   public final MutableBlockPos field_4135;
   public CompletableFuture<RaytraceTarget> field_3789;
   public volatile BlockPos field_3020;
   public volatile BlockPos field_4136;
   public volatile BlockPos field_4137;
   public volatile Direction field_4138;
   public volatile boolean field_4139;
   public volatile boolean field_4140;
   public volatile boolean field_4141;
   public volatile float field_4142;
   public int field_4143;
   public int field_4144;
   public int field_4145;
   public int field_1071;
   public BlockPos field_4146;
   public boolean skip;
   public float[] field_4147;
   public SpawnedEntity field_4148;
   public Player field_2290;
   public Player field_4149;

   public AutoCrystalModule() {
      super("AutoCrystal", "Kills people with end crystals (if you're good).", Category.COMBAT, "crystalaura", "ca", "ac");
      Settings.initialize(this);
      this.field_4121 = new Timer();
      this.field_4122 = new AtomicReference<>();
      this.field_4123 = new AtomicReference<>();
      this.field_4124 = new AtomicBoolean();
      this.field_4125 = new Timer();
      this.field_4126 = new Timer();
      this.field_4127 = new Timer();
      this.field_4128 = new Timer();
      this.field_4129 = new Timer();
      this.field_4130 = new Timer();
      this.field_4131 = new Timer();
      this.field_4132 = new Timer();
      this.field_4133 = new LinkedList<>();
      this.field_4134 = Collections.synchronizedList(new ArrayList<>());
      this.field_4135 = new MutableBlockPos();
      this.field_4143 = -1;
      Hub.field_2616.method_2(new RenderStyle(this, this.fill, this.outline, this.lineWidth, this.fadeTime, this.multiFade, this.fade, 1000));
      this.delay.method_31("PlaceDelay");
      this.range.method_31("PlaceRange");
      this.wallRange.method_31("PlaceWallRange");
      this.minDamage.method_31("MinPlaceDamage");
      this.maxSelfDamage.method_31("MaxSelfPlace");
      this.delay2.method_31("HitDelay");
      this.factor.method_31("HitFactor");
      this.range2.method_31("BreakRange");
      this.wallRange2.method_31("BreakWallRange");
      this.minDamage2.method_31("MinBreakDamage");
      this.maxSelfDamage2.method_31("MaxSelfHit");
      this.place.method_31("PlaceRotate");
      this.break_2.method_31("BreakRotate");
      this.field_4085.method_31("FacePlaceHealth");
      this.armorThreshold.method_31("FacePlaceArmor");
      this.extrapolation.method_31("ExtraTicks");
      this.timeout.method_31("InvincTimeout");
   }

   @Override
   public void onEnable() {
      this.field_4143 = -1;
      this.field_4149 = this.field_2290;
      this.field_2290 = null;
      this.field_4138 = Direction.DOWN;
      this.field_3020 = null;
      this.field_4122.set(null);
      if (this.field_3789 != null) {
         try {
            this.field_3789.complete(null);
         } catch (CancellationException var2) {
         }
      }

      this.field_4128.reset();
      this.field_4132.setTime(-1L);
      this.field_1071 = -1;
   }

   @Override
   public void onDisable() {
      this.method_1137();
   }

   @Subscribe
   public void method_9(Event_19 var1) {
      if (var1.method_213() == PreType.PRE) {
         this.field_4139 = false;
         if (this.field_4141) {
            this.field_4141 = false;
            return;
         }

         this.field_4141 = false;
         Player var2 = this.field_2290 == null && !this.method_78(this.field_4149) ? this.field_4149 : this.field_2290;
         if (var2 == null) {
            double var3 = 1.7976931348623157e+308;

            for (Player var6 : mc.level.players()) {
               double var7 = (double)mc.player.distanceTo(var6);
               if (!this.method_78(var6) && !(var7 > var3)) {
                  var3 = var7;
                  var2 = var6;
               }
            }

            if (var2 == null) {
               return;
            }

            this.field_2290 = var2;
         }

         for (Entity var4 : mc.level.entitiesForRendering()) {
            if (var4 instanceof ItemEntity) {
               ItemEntity var10 = (ItemEntity)var4;
               if (var10.getItem().getItem() == Items.OBSIDIAN && var10.getItem().getCount() >= 8 && var4.isAlive()) {
                  AABB var11 = var2.getBoundingBox();
                  if (var4.getBoundingBox().intersects(var11.setMinY(var11.getCenter().y()).inflate(Class_0245.field_688, 0.0, Class_0245.field_688))) {
                     this.field_4141 = true;
                     break;
                  }
               }
            }
         }
      }
   }

   @Subscribe
   public void method_9(FrameRenderEvent var1) {
      if (!this.method_535()) {
         if (!this.method_1144()) {
            if (this.field_3789 != null && this.field_3789.isDone()) {
               try {
                  RaytraceTarget var2 = this.field_3789.get();
                  if (var2 == null) {
                     this.field_4122.set(null);
                     this.field_4142 = 0.0F;
                  } else {
                     this.field_4122.set(var2);
                     this.field_4142 = (float)var2.method_445();
                     this.field_2290 = var2.method_444();
                  }
               } catch (ExecutionException | InterruptedException var9) {
                  var9.printStackTrace();
               }
            }

            if (this.field_3789 == null || this.field_3789.isDone() || this.field_3789.isCancelled()) {
               this.method_1134();
               this.field_3789 = CompletableFuture.supplyAsync(this::method_1128, field_4048).exceptionally(var1x -> this.method_2(var1x, "PlaceThread"));
            }

            synchronized (this.field_4134) {
               for (int var4 : this.field_4134) {
                  if (mc.level.getEntity(var4) instanceof EndCrystal var6) {
                     ((AttackTrackingAccess)var6).setMioAttacked(true);
                  }
               }

               this.field_4134.removeIf(var0 -> mc.level.getEntity(var0) != null);
            }
         }
      }
   }

   @Subscribe
   public void method_5(Event_7 var1) {
      this.field_4140 = false;
      this.field_4136 = null;
      Entity var2 = mc.level.getEntity(this.field_4143);
      if (var2 == null || !var2.isAlive()) {
         this.field_4144 = 0;
      }

      if (mc.player.tickCount % 4 == 0) {
         long var3 = System.currentTimeMillis();

         while (!this.field_4133.isEmpty() && this.field_4133.get(0) != null && var3 - this.field_4133.get(0) > 1000L) {
            try {
               this.field_4133.remove();
            } catch (NullPointerException var6) {
            }
         }
      }

      KillEffectsModule var7 = Hub.field_2599.method_78(KillEffectsModule.class);
      if (var7 != null && var7.isToggled() && !this.method_78(this.field_2290)) {
         var7.method_185(this.field_2290);
      }

      if (!this.method_639()) {
         this.method_1130();
         if (this.field_4126.method_9((long)this.delay.getValue().intValue())) {
            LockUtil.method_7(this::method_1129);
         }

         Vec3 var4 = this.field_4123.get();
         if (this.rotate.getValue()) {
            if (var4 != null && (!RotationManager.method_514() || this.method_1131())) {
               Hub.field_2598.method_2(RotationManager.method_2(RotationManager.method_78(var4), Hub.field_2598.method_509()), 100, true);
               if (this.field_4125.method_9(500L)) {
                  this.field_4122.lazySet(null);
                  this.field_4123.lazySet(null);
               }
            } else if (RotationManager.method_514() && !this.method_1131() && this.field_4147 != null) {
               PacketSender.method_2(
                  mc.player.getX(),
                  mc.player.getY(),
                  mc.player.getZ(),
                  this.field_4147[0],
                  this.field_4147[1],
                  mc.player.onGround()
               );
               this.field_4147 = null;
            }
         }
      }
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      if (this.draw.getValue() && this.extrapolation.getValue() && this.field_2290 != null) {
         for (int var2 = 1; var2 < BoxUtil.method_2(this.field_2290, this.ticks); var2++) {
            AABB var3 = Hub.field_2612.method_2(this.field_2290, var2 - 1);
            AABB var4 = Hub.field_2612.method_2(this.field_2290, var2);
            RenderUtil.field_2672.method_2(var1.method_10(), BoxUtil.method_2(var3), BoxUtil.method_2(var4), this.outline.getValue());
         }
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundAddEntityPacket var2) {
         this.field_4148 = SpawnedEntity.method_9(var2);
         if (!this.method_1132()) {
            try {
               if (this.method_639()) {
                  return;
               }

               this.method_2(var2);
            } catch (Throwable var4) {
            }
         }
      }

      if (var1.method_127() instanceof ClientboundEntityEventPacket var5
         && var5.getEventId() == 3
         && this.field_2290 != null
         && ((DuckEntityStatusS2CPacket)(Object)var5).getId() == this.field_2290.getId()) {
         ((ServerSideDeadAccess)this.field_2290).setServerSideDead(true);
      }

      if (var1.method_127() instanceof ClientboundPlayerCombatKillPacket) {
         this.forceSuicide.method_78(false);
      }
   }

   @Subscribe
   public void method_2(SlotUpdateEvent var1) {
      if (this.field_4145 > 0) {
         this.field_4145--;
      } else {
         this.field_4129.reset();
         this.field_4128.reset();
      }
   }

   @Subscribe
   public void method_2(DisconnectEvent var1) {
      this.forceSuicide.method_78(false);
   }

   @Override
   public String getInfo() {
      return this.field_2290 != null && !this.method_78(this.field_2290)
         ? "%s, %.1f, %d".formatted(this.field_2290.getGameProfile().name(), this.field_4142, this.field_4133.size())
         : super.getInfo();
   }

   public void method_2(ClientboundAddEntityPacket var1) {
      if (var1.getType() == EntityType.END_CRYSTAL && this.field_2290 != null) {
         Vec3 var2 = new Vec3(var1.getX(), var1.getY(), var1.getZ());
         if (this.field_3020 != null && var2.distanceToSqr(this.field_3020.getCenter()) <= 9.0) {
            this.field_4133.add(System.currentTimeMillis());
         }

         if (this.instant.getValue() != InstantMode.NONE && this.break_.getValue() && !this.method_107(this.field_2290)) {
            if (this.method_1141() && this.method_1142()) {
               return;
            }

            boolean var3 = BlockUtil.method_9(var2);
            if (mc.player.getEyePosition().distanceTo(var2) > (double)(var3 ? this.range2.getValue() : this.wallRange2.getValue()).floatValue()) {
               return;
            }

            double var4 = (double)ExplosionDamage.method_2(var2, this.field_2290);
            double var6 = (double)ExplosionDamage.method_2(var2, mc.player);
            DamageResult var8 = DamageResult.of(this.field_2290, var4);
            if (this.balance.getValue() && var4 <= var6 && !this.forceSuicide.getValue() && !var8.damaging) {
               return;
            }

            if (var4 < CrystalSwingMode.HIT.method_34(this.field_2290) || CrystalSwingMode.HIT.method_2(this.field_2290, var6, var4, var8)) {
               return;
            }

            BlockPos var9 = BlockPos.containing(var2);
            if (this.instant.getValue() == InstantMode.STRICT && this.field_4146.equals(var9)) {
               return;
            }

            if (this.rotate.getValue() && this.break_2.getValue()) {
               this.method_22(var2);
            }

            if (Hub.field_2623.method_5(var1.getId(), true)) {
               this.field_4136 = var9;
               this.field_4146 = var9;
               this.field_4143 = var1.getId();
               this.field_4144 = 0;
               this.field_4132.reset();
            }

            boolean var10 = mc.player.isHolding(Items.END_CRYSTAL) || EntityUtil.method_76() >= 8F;
            if (!field_144.method_1052()) {
               var10 = true;
            }

            if (this.sequential.getValue().method_1214() && !this.field_4139 && var10) {
               this.field_4130.reset();
               LockUtil.method_7(this::method_1129);
               if (this.sequential.getValue() == SequentialMode.STRICT) {
                  this.field_4139 = true;
               }
            }

            this.method_176(var1.getId());
         }
      }
   }

   public RaytraceTarget method_1128() {
      RaytraceTarget var1 = this.field_4122.get();
      CrystalTargetInfo var2 = null;
      RaytraceTarget var3 = null;
      Class_0794 var4 = new Class_0794()
         .method_17(this.field_4058.getValue())
         .method_35(this.lowerHitBox.getValue())
         .method_107(this.hBFix.getValue() && !this.method_1141())
         .method_374(this.basePlace.getValue() && this.method_1136())
         .method_425(this.method_1141() && speedmine.rebreak2.getValue() != RebreakMode.INSTANT)
         .method_101(this.extrapolation.getValue() ? this.ticks.getValue() : 0)
         .method_33(this.range2.getValue());
      if (var1 != null && var4.method_185(var1.getPos())) {
         var2 = this.method_4(var1.getPos(), BlockUtil.method_36(var1.getPos()));
      }

      for (double var5 = -Math.floor((double)this.range.getValue().floatValue());
         var5 < Math.ceil((double)this.range.getValue().floatValue());
         var5 += 1.0
      ) {
         for (double var7 = -Math.floor((double)this.range.getValue().floatValue());
            var7 < Math.ceil((double)this.range.getValue().floatValue());
            var7 += 1.0
         ) {
            for (double var9 = -Math.floor((double)this.range.getValue().floatValue());
               var9 < Math.ceil((double)this.range.getValue().floatValue());
               var9 += 1.0
            ) {
               this.field_4135.set(mc.player.getX() + var5, mc.player.getEyeY() + var7, mc.player.getZ() + var9);
               boolean var11 = BlockUtil.method_36(this.field_4135);
               if (var3 == null || var3.method_446() == RaytraceMode.OBSIDIAN || var11) {
                  double var12 = mc.player.getEyePosition().distanceTo(this.field_4135.getCenter());
                  if (!(var12 > (double)this.range.getValue().floatValue()) && var4.method_185(this.field_4135)) {
                     List var14 = this.multipoint.getValue().method_38(this.field_4135);
                     boolean var15 = BlockUtil.method_2(var14);
                     if ((var15 || !(var12 > (double)this.wallRange.getValue().floatValue()) || this.smartTrace.getValue())
                        && (!this.strictDirection.getValue() || !InteractionUtil.method_39(this.field_4135).isEmpty())) {
                        double var16 = (double)ExplosionDamage.method_2(
                           Vec3.upFromBottomCenterOf(this.field_4135, 1.0),
                           mc.player,
                           this.method_35(mc.player),
                           6.0,
                           true,
                           null,
                           this.field_4135
                        );
                        CrystalTargetInfo var18 = this.method_4(this.field_4135, var11);
                        if (var18 != null
                           && !CrystalSwingMode.PLACE
                              .method_2(var18.method_444(), var16, var18.method_445(), DamageResult.of(var18.method_444(), var18.method_445()))) {
                           RaytraceMode var19 = RaytraceMode.NONE;
                           if (var2 == null || var1.getPos().equals(this.field_4135) || !((double)Math.round(var2.method_445()) >= var18.method_445())) {
                              if (!var15 && var12 > (double)this.wallRange.getValue().floatValue()) {
                                 var19 = RaytraceMode.RAYTRACE;
                              }

                              if (this.basePlace.getValue() && !var11) {
                                 if (this.method_35(var18.method_444()).minmax(var18.method_444().getBoundingBox()).intersects(new AABB(this.field_4135))
                                    || !InteractionUtil.method_7(this.field_4135, false)
                                    || !this.air.getValue() && InteractionUtil.method_9(this.field_4135, this.strictDirection.getValue()) == null) {
                                    continue;
                                 }

                                 var19 = RaytraceMode.OBSIDIAN;
                              }

                              var3 = new RaytraceTarget(this.field_4135.immutable(), var18.method_444(), var18.method_445(), var19).method_2(var3);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var3;
   }

   public void method_1129() {
      if (!this.method_1141()) {
         this.field_4124.set(false);
      }

      if (this.field_4050.getValue() && !this.method_1132() && !this.field_4124.get()) {
         boolean var1 = mc.player.getOffhandItem().getItem() == Items.END_CRYSTAL;
         boolean var2 = mc.player.isHolding(Items.END_CRYSTAL);
         if (!this.damageSync.getValue() && !this.field_4141
            || !(this.field_4142 < this.minDamage.getValue())
            || !(this.field_4142 < 5F)
            || this.field_4126.method_9(this.method_1138())) {
            RaytraceTarget var3 = this.field_4122.get();
            if (var3 == null) {
               this.method_1137();
            } else {
               BlockPos var4 = var3.getPos();
               if (var4 != null && this.field_2290 != null) {
                  double var5 = CrystalSwingMode.PLACE.method_34(this.field_2290);
                  double var7 = (double)ExplosionDamage.method_2(
                     Vec3.upFromBottomCenterOf(var4, 1.0),
                     this.field_2290,
                     this.method_35(this.field_2290),
                     6.0,
                     true,
                     this.method_1141() ? speedmine.method_1118() : null,
                     var4
                  );
                  if (this.extrapolation.getValue() && this.ticks.getValue() > 0 && var7 > 2.0 && var7 < var5) {
                     AABB var9 = BoxUtil.method_4(this.field_2290);
                     var7 = Math.max(var7, this.method_2(var4, this.field_2290, var9, true));
                  }

                  if (var7 < var5) {
                     this.method_1137();
                  } else if (!BlockUtil.method_36(var4)) {
                     if (var3.method_446() == RaytraceMode.OBSIDIAN && this.basePlace.getValue()) {
                        this.method_214(var4);
                     }
                  } else {
                     int var14 = -1;
                     int var10 = InventoryUtil.method_5(Items.END_CRYSTAL);
                     if (!this.limit.getValue()
                        || InteractionUtil.method_2(var4, this.field_4058.getValue(), true, true, true, this.lowerHitBox.getValue(), this.hBFix.getValue())) {
                        if (mc.level.getBlockState(var4.above()).getBlock() == Blocks.FIRE) {
                           mc.player.connection.send(new ServerboundPlayerActionPacket(Action.START_DESTROY_BLOCK, var4.above(), Direction.DOWN));
                           mc.player.connection.send(new ServerboundPlayerActionPacket(Action.STOP_DESTROY_BLOCK, var4.above(), Direction.DOWN));
                        } else {
                           boolean var11 = false;
                           boolean var12 = this.method_514() && this.silent.getValue() == SilentMode.ALTERNATIVE;
                           boolean var13 = false;
                           if (!var2 && this.method_1141() && speedmine.swapReset.getValue() && speedmine.alternative.getValue()) {
                              var12 = true;
                              var13 = true;
                           }

                           if (!var12 || this.skip && mc.player.containerMenu.getCarried().isEmpty()) {
                              if (var12 && var10 == -1) {
                                 var10 = InventoryUtil.method_9(Items.END_CRYSTAL);
                              }

                              if (this.autoSwap.getValue() != AutoSwapMode.NONE && !var2 && !this.method_1133() && var10 != -1) {
                                 var14 = mc.player.getInventory().selected;
                                 if (var12) {
                                    InventoryUtil.method_39(var10);
                                    var11 = true;
                                    this.field_4124.set(var13);
                                 } else if (this.method_514() && this.silent.getValue() == SilentMode.PICK) {
                                    mc.gameMode.handleContainerInput(var10, mc.player.getInventory().getSelectedSlot(), 0, ContainerInput.SWAP, mc.player);
                                 } else {
                                    if (this.autoSwap.getValue() == AutoSwapMode.SILENT) {
                                       speedmine.field_4030 = true;
                                       this.method_1143();
                                    }

                                    if (this.method_1135()) {
                                       this.field_1071 = var14;
                                    }

                                    InventoryUtil.method_16(var10);
                                 }
                              }

                              if (var10 != -1 && this.autoSwap.getValue() != AutoSwapMode.NONE || var2) {
                                 this.method_2(var4, var1, this.field_4138);
                                 if (this.method_514() || var11) {
                                    if (var14 != -1) {
                                       if (var11) {
                                          InventoryUtil.method_39(var10);
                                       } else if (this.autoSwap.getValue() == AutoSwapMode.SILENT) {
                                          if (this.method_514() && this.silent.getValue() == SilentMode.PICK) {
                                             mc.gameMode.handleContainerInput(var10, mc.player.getInventory().getSelectedSlot(), 0, ContainerInput.SWAP, mc.player);
                                          } else {
                                             this.method_1143();
                                             InventoryUtil.method_16(var14);
                                          }
                                       }
                                    }

                                    if (this.strictTiming.getValue() || var13 || this.skip) {
                                       this.skip = !this.skip;
                                    }
                                 }
                              }
                           } else {
                              this.skip = true;
                           }
                        }
                     }
                  }
               } else {
                  this.method_1137();
               }
            }
         }
      }
   }

   public void method_2(BlockPos var1, boolean var2, Direction var3) {
      if (var1 != null && var3 != null) {
         if (var1.equals(this.field_4136) || this.field_4136 == null || this.sequential.getValue().method_1214()) {
            List var4 = InteractionUtil.method_39(var1);
            Direction var5 = var3;
            if (!var4.isEmpty()) {
               var5 = (Direction)var4.get(0);
            }

            Vec3 var6 = var1.getCenter().relative(var5, Class_0245.field_688);
            double var7 = 1.7976931348623157e+308;
            double[] var9 = new double[]{0.05, 0.95};

            for (double var13 : var9) {
               for (double var18 : new double[]{
                  0.05, 0.95, 0.99
               }) {
                  for (double var23 : var9) {
                     Vec3 var25 = Vec3.atLowerCornerOf(var1).add(var13, var18, var23);
                     BlockHitResult var26 = mc.level
                        .clip(new ClipContext(mc.player.getEyePosition(), var25, Block.COLLIDER, Fluid.NONE, mc.player));
                     double var27 = var26.getLocation().distanceTo(mc.player.getEyePosition());
                     if ((var26.getType() == Type.MISS || var26.getBlockPos().equals(var1))
                        && var25.distanceTo(mc.player.getEyePosition()) <= (double)this.range.getValue().floatValue()
                        && var27 < var7
                        && (var4.contains(var26.getDirection()) || !this.strictDirection.getValue())) {
                        var6 = var26.getLocation();
                        var5 = var26.getDirection();
                        var7 = var27;
                     }
                  }
               }
            }

            if (this.place.getValue() && this.rotate.getValue() && !this.field_4140) {
               this.method_22(var6);
            }

            InteractionHand var29 = var2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            PacketSender.method_2(var29, new BlockHitResult(var1.getCenter().relative(var5, Class_0245.field_688), var5, var1, false));
            mc.player.swing(var29);
            this.field_3020 = var1;
            if (this.render.getValue()) {
               Hub.field_2616.method_2(this, var1);
            }

            this.field_4126.reset();
         }
      }
   }

   public void method_214(BlockPos var1) {
      if (this.method_1136()) {
         int var2 = InventoryUtil.method_5(Items.END_CRYSTAL);
         if (this.autoSwap.getValue() == AutoSwapMode.SILENT && this.silent.getValue() == SilentMode.ALTERNATIVE) {
            var2 = InventoryUtil.method_9(Items.END_CRYSTAL);
         }

         if (mc.player.getOffhandItem().is(Items.END_CRYSTAL) || var2 != -1) {
            int var3 = mc.player.getInventory().selected;
            int var4 = InventoryUtil.method_5(Items.OBSIDIAN);
            Direction var5 = InteractionUtil.method_9(var1, this.strictDirection.getValue());
            boolean var6 = this.method_514() && this.silent.getValue() == SilentMode.ALTERNATIVE
               || this.silent.getValue() == SilentMode.PICK && this.method_1141() && speedmine.method_1118() != null;
            boolean var7 = this.air.getValue() && var5 == null && field_144.method_1052();
            InteractionHand var8 = InteractionHand.MAIN_HAND;
            if (var6 && var4 == -1) {
               var4 = InventoryUtil.method_9(Items.OBSIDIAN);
            }

            if (var4 != -1 && (var5 != null || this.air.getValue()) && InteractionUtil.method_7(var1, false)) {
               if (var4 != var3) {
                  if (var6) {
                     InventoryUtil.method_39(var4);
                  } else if (this.method_514() && this.silent.getValue() == SilentMode.PICK) {
                     mc.gameMode.handleContainerInput(var4, mc.player.getInventory().getSelectedSlot(), 0, ContainerInput.SWAP, mc.player);
                  } else {
                     speedmine.field_4030 = true;
                     InventoryUtil.method_16(var4);
                  }
               }

               if (var7) {
                  PacketSender.method_1100();
                  var8 = InteractionHand.OFF_HAND;
               }

               InteractionUtil.method_2(var1, var5, this.air.getValue(), var8);
               this.field_4132.reset();
               if (var7) {
                  PacketSender.method_1100();
               }

               if (this.place.getValue() && this.rotate.getValue()) {
                  Vec3 var9 = var1.getCenter();
                  if (var5 != null) {
                     var9 = var9.relative(var5, 0.5);
                  }

                  this.method_22(var9);
               }

               this.field_4137 = var1;
               if (var3 != -1 && var4 != var3) {
                  if (var6) {
                     InventoryUtil.method_39(var4);
                  } else if (this.method_514() && this.silent.getValue() == SilentMode.PICK) {
                     mc.gameMode.handleContainerInput(var4, mc.player.getInventory().getSelectedSlot(), 0, ContainerInput.SWAP, mc.player);
                  } else {
                     InventoryUtil.method_16(var3);
                  }
               }
            }
         }
      }
   }

   public void method_1130() {
      if (this.break_.getValue()
         && this.field_4127.method_9((long)this.delay2.getValue().intValue())
         && !this.method_1132()
         && !this.method_107(this.field_2290)) {
         if (this.field_4130.method_9((long)Hub.field_2602.method_983())
            || this.instant.getValue() == InstantMode.NONE
            || !this.method_1142()
            || this.method_1141()) {
            SpeedMineModule var1 = Hub.field_2599.method_78(SpeedMineModule.class);
            Entity var2 = null;
            double var3 = -1337.0;
            boolean var5 = this.method_1141();
            if (!BlockUtil.method_3(speedmine.method_1118())) {
               var5 = false;
            }

            BlockPos var6 = var1.method_1118();
            if (!BlockUtil.method_3(var6)) {
               var6 = null;
            }

            for (Entity var8 : mc.level.entitiesForRendering()) {
               if (var8 instanceof EndCrystal && this.method_2((EndCrystal)var8)) {
                  double var9;
                  if (this.skip) {
                     var9 = (double)ExplosionDamage.method_2(var8.position(), this.field_2290, this.method_35(this.field_2290));
                  } else {
                     var9 = this.method_2(BlockPos.containing(var8.position()).below(), this.field_2290, this.method_35(this.field_2290), false);
                  }

                  double var11 = (double)ExplosionDamage.method_2(var8.position(), mc.player, mc.player.getBoundingBox());
                  DamageResult var13 = DamageResult.of(this.field_2290, var9);
                  if ((!this.balance.getValue() || !(var9 <= var11) || this.forceSuicide.getValue() || var13.damaging)
                     && !CrystalSwingMode.HIT.method_2(this.field_2290, var11, var9, var13)) {
                     if (this.field_3020 != null
                        && var8.getBoundingBox().intersects(new AABB(this.field_3020.above()).expandTowards(0.0, 1.0, 0.0))
                        && var2 == null
                        && (!var5 || var6 == null)) {
                        var2 = var8;
                     }

                     if (!(var9 < CrystalSwingMode.HIT.method_34(this.field_2290)) && var9 > var3) {
                        var3 = var9;
                        var2 = var8;
                     }
                  }
               }
            }

            if (var2 != null) {
               this.method_2((EndCrystal)var2, mc.player.getOffhandItem().getItem() == Items.END_CRYSTAL);
            }
         }
      }
   }

   public void method_2(EndCrystal var1, boolean var2) {
      if (var1.getId() == this.field_4143 && this.field_4131.method_9(500L) && var1.isAlive() && !var1.isRemoved()) {
         this.field_4144++;
      } else {
         this.field_4144 = 0;
      }

      this.field_4143 = var1.getId();
      if (this.field_4144 >= 5) {
         this.field_4131.reset();
      }

      if (!this.method_1131() || !this.resync.getValue()) {
         if (Hub.field_2623.method_80(var1.getId())) {
            ((AttackTrackingAccess)var1).setMioAttacked(true);
            this.method_176(var1.getId());
            this.field_4136 = var1.blockPosition().below();
            if (this.break_2.getValue() && this.rotate.getValue()) {
               this.method_22(BoxUtil.method_9(var1.getBoundingBox()));
               this.field_4140 = true;
            }

            this.field_4127.reset();
            this.field_4132.reset();
         }
      }
   }

   public boolean method_2(EndCrystal var1) {
      if (var1.tickCount < this.ticksExisted.getValue() && !this.method_1141()) {
         return false;
      } else if (!var1.isRemoved() && this.field_2290 != null) {
         boolean var2 = mc.player.hasLineOfSight(var1);
         return mc.player.getEyePosition().distanceTo(var1.position())
            <= (double)(var2 ? this.range2.getValue() : this.wallRange2.getValue()).floatValue();
      } else {
         return false;
      }
   }

   public void method_22(Vec3 var1) {
      if (var1 != null) {
         if (RotationManager.method_513() && field_144.rotations.getValue() == Class_1054.SILENT && !this.method_7(100L)) {
            float[] var2 = RotationManager.method_78(var1);
            Hub.field_2598.method_29(var2);
            this.field_4147 = var2;
         }

         this.field_4125.reset();
         this.field_4123.set(var1);
      }
   }

   public void method_176(int var1) {
      if (!this.factor.method_105()) {
         for (int var2 = 1; var2 < this.factor.getValue(); var2++) {
            int var3 = var1 + var2;
            if (field_144.movementSync.getValue()) {
               var3--;
            }

            for (Entity var5 : mc.level.entitiesForRendering()) {
               if (var5 instanceof LivingEntity && var5.isRemoved() || !var5.isAlive()) {
                  return;
               }
            }

            if (this.field_4148 != null && this.field_4148.getEntityType() != EntityType.END_CRYSTAL && this.field_4148.method_684() == var3) {
               break;
            }

            PacketSender.method_36(var3);
            PacketSender.method_9(InteractionHand.MAIN_HAND);
            this.field_4134.add(var3);
         }
      }
   }

   public boolean method_1131() {
      return this.method_7(500L);
   }

   public boolean method_7(long var1) {
      return !Hub.field_2602.method_984().method_9(200L) ? true : !this.field_4131.method_9((long)((float)var1 / Hub.field_2602.method_990()));
   }

   public boolean method_639() {
      if (this.method_535()) {
         return true;
      } else if (InteractionUtil.method_932()) {
         return true;
      } else if (OffhandModule.method_639() || SpeedMineModule.method_1124()) {
         return true;
      } else if (this.method_78(this.field_2290)) {
         return true;
      } else if (mc.player.isSleeping()) {
         return true;
      } else if (mc.player.isUsingItem() && this.eating.getValue()) {
         return true;
      } else {
         boolean var1 = mc.gameMode.isDestroying() || BlockUtil.method_3(speedmine.method_1120()) && speedmine.isToggled();
         if (var1 && this.mining.getValue()) {
            return true;
         } else if (this.forceSuicide.getValue()) {
            return false;
         } else {
            return field_4049.isToggled()
                  && field_4049.field_3792.get() != null
                  && field_4049.field_3793 != null
                  && this.field_4122.get() != null
                  && field_4049.field_3793.method_445() > this.field_4122.get().method_445()
               ? true
               : EntityUtil.method_76() < this.field_4101.getValue() && !this.forceSuicide.getValue();
         }
      }
   }

   public boolean method_1132() {
      return !this.field_4129.method_9((long)(5e+01F * this.swapPenalty.getValue()));
   }

   public boolean method_1133() {
      return this.autoSwap.getValue() == AutoSwapMode.NORMAL
         && !this.field_4128.method_9((long)(5e+01F * this.swapDelay.getValue()));
   }

   public boolean method_514() {
      return this.autoSwap.getValue() == AutoSwapMode.SILENT;
   }

   public AABB method_35(Player var1) {
      return this.extrapolation.getValue() ? Hub.field_2612.method_2(var1, BoxUtil.method_2(var1, this.ticks)) : BoxUtil.method_4(var1);
   }

   public double method_2(BlockPos var1, LivingEntity var2, AABB var3, boolean var4) {
      return (double)ExplosionDamage.method_2(
         Vec3.upFromBottomCenterOf(var1, 1.0),
         var2,
         var3,
         6.0,
         true,
         this.method_1141() ? speedmine.method_1118() : null,
         var4 ? var1.immutable() : null
      );
   }

   public CrystalTargetInfo method_4(BlockPos var1, boolean var2) {
      CrystalTargetInfo var3 = null;

      for (AbstractClientPlayer var5 : (Iterable<AbstractClientPlayer>)(Iterable<?>)(new ArrayList(mc.level.players()))) {
         if (!this.method_78(var5) && (!((double)var1.getY() > var5.getY() + 1.5) || var2)) {
            AABB var6 = this.method_35(var5);
            double var7 = CrystalSwingMode.PLACE.method_34(var5);
            double var9 = this.method_2(var1, var5, var6, true);
            DamageResult var11 = DamageResult.of(var5, var9);
            if ((!this.ignoreNakeds.getValue() || PlayerUtil.method_29(var5))
               && (
                  !(BoxUtil.method_2(var6).distanceTo(Vec3.atBottomCenterOf(var1)) > (double)this.crystalRange.getValue().floatValue())
                     || var11.damaging
                     || this.field_4141
               )) {
               if (Hub.field_2603.getId(var5) && var5 != mc.player) {
                  if (this.antiFriendKill.getValue() && var9 > (double)EntityUtil.method_2((net.minecraft.world.entity.Entity)var5)) {
                     return null;
                  }
               } else if ((!(var7 > var9) && !(var9 < Class_0245.field_688) || var11.damaging) && (var3 == null || var9 > var3.method_445())) {
                  var3 = new CrystalTargetInfo(var5, var9, RaytraceMode.NONE, var11);
               }
            }
         }
      }

      return var3;
   }

   public void method_1134() {
      if (this.method_78(this.field_2290)) {
         this.field_2290 = null;
      }
   }

   public boolean method_78(LivingEntity var1) {
      if (var1 == null) {
         return true;
      } else if (this.forceSuicide.getValue()) {
         return var1 != mc.player;
      } else {
         return Hub.field_2603.getId((Player)var1)
            ? true
            : !var1.isAlive()
               || var1.isDeadOrDying()
               || !(var1.distanceTo(mc.player) <= this.targetRange.getValue())
               || ((ServerSideDeadAccess)var1).isServerSideDead()
               || mc.player == var1
               || var1.isSpectator()
               || ((Player)var1).getAbilities().instabuild;
      }
   }

   public boolean method_1135() {
      return this.autoSwap.getValue() == AutoSwapMode.NORMAL && this.swapBack.getValue();
   }

   public boolean method_1136() {
      if (this.field_4137 != null && this.field_2290 != null) {
         if (!InteractionUtil.method_2(this.field_4137, this.field_4058.getValue(), true, false, true, this.lowerHitBox.getValue(), this.hBFix.getValue())) {
            return true;
         }

         List var1 = this.multipoint.getValue().method_38(this.field_4137);
         boolean var2 = BlockUtil.method_2(var1);
         double var3 = mc.player.getEyePosition().distanceTo(this.field_4137.getCenter());
         if (var3 > (double)this.range.getValue().floatValue()) {
            return true;
         }

         if (!var2 && var3 > (double)this.wallRange.getValue().floatValue() && !this.smartTrace.getValue()) {
            return true;
         }

         double var5 = (double)ExplosionDamage.method_2(
            Vec3.upFromBottomCenterOf(this.field_4137, 1.0),
            this.field_2290,
            this.method_35(this.field_2290),
            6.0,
            true,
            null,
            null
         );
         if (var5 >= CrystalSwingMode.PLACE.method_34(this.field_2290)) {
            return false;
         }
      }

      return this.field_4132.method_9(this.method_1138());
   }

   public void method_1137() {
      if (!this.method_535() && this.method_1135() && this.field_1071 != -1) {
         InventoryUtil.method_16(this.field_1071);
         this.field_1071 = -1;
      }
   }

   public long method_1138() {
      return (long)(5e+02F / Hub.field_2602.method_990());
   }

   public float method_1139() {
      return !this.assumeInvincibility.getValue() ? -1F : this.timeout.getValue();
   }

   public boolean method_107(Player var1) {
      if (this.method_1140()) {
         return false;
      } else {
         PlayerInfo var2 = mc.player.connection.getPlayerInfo(var1.getGameProfile().id());
         return var2 == null
            ? false
            : (float)(System.currentTimeMillis() - ((JoinTimeAccess)var2).mio$getJoinTime()) <= this.method_1139() * 1e+03F;
      }
   }

   public boolean method_1140() {
      return !this.self.getValue() ? false : (float)mc.player.tickCount / 2e+01F < this.method_1139();
   }

   public boolean method_1141() {
      float var1 = 2.5F;
      boolean var2 = (double)speedmine.method_1119() + speedmine.method_1114() * (double)var1 >= (double)speedmine.damage.getValue().floatValue()
         && speedmine.method_1118() != null
         && BlockUtil.method_3(speedmine.method_1118());
      return this.mineIgnore.getValue() && (var2 || !speedmine.method_4(100L));
   }

   public boolean method_1142() {
      return this.await.getValue();
   }

   public void method_1143() {
      this.field_4145++;
   }

   public RaytraceTarget method_2(Throwable var1, String var2) {
      return null;
   }

   public boolean method_1144() {
      boolean var1 = mc.player.isHolding(var0 -> ItemUtil.method_29(var0.getItem()));
      if (!var1 && this.field_4121.method_9(50L)) {
         return false;
      } else {
         if (var1) {
            this.field_4121.reset();
         }

         if (this.field_3789 != null && !this.field_3789.isDone()) {
            this.method_463();
         }

         return true;
      }
   }

   public void method_463() {
      CompletableFuture var1 = this.field_3789;
      if (var1 != null && !var1.isDone() && !var1.isCancelled()) {
         var1.complete(null);
      }
   }
}
