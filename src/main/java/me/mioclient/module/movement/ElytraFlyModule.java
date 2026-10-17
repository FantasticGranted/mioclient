package me.mioclient.module.movement;

import me.mioclient.Hub;
import me.mioclient.enum_.TakeoffMode;
import me.mioclient.enum_.ElytraVerticalMode;
import me.mioclient.enum_.ElytraFlyMode;
import me.mioclient.enum_.SettingMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.MovementInputEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.TravelEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.SoundPlayEvent;
import me.mioclient.event.UseItemEvent;
import me.mioclient.event.Event_7;
import me.mioclient.event.MoveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0094;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.Class_0321;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.Class_0517;
import me.mioclient.internal.Class_0613;
import me.mioclient.internal.ElytraFlyBehavior;
import me.mioclient.internal.Class_0933;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.PacketSender;
import me.mioclient.internal.EnumMapping;
import me.mioclient.mixin.ducks.DuckMinecraftClient;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.module.abstract_.AbstractModule_6;
import me.mioclient.module.misc.MiddleClickModule;
import me.mioclient.record.RotationRequest;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import me.mioclient.internal.Inputs;

public class ElytraFlyModule extends Module {
   public static final MiddleClickModule field_4345 = Hub.field_2595.method_78(MiddleClickModule.class);
   public static final AbstractModule_6 field_4346 = Hub.field_2595.method_78(AbstractModule_6.class);
   public static final NoSlowModule field_4347 = Hub.field_2595.method_78(NoSlowModule.class);
   public static final AbstractModule_28 field_4348 = Hub.field_2595.method_78(AbstractModule_28.class);
   public static boolean field_885 = false;
   public  Setting<ElytraFlyMode> mode;
   public  Setting<TakeoffMode> takeoff;
   public  Setting<Boolean> factorize;
   public  Setting<Boolean> always;
   public  Setting<Boolean> nCPBoost;
   public  Setting<Float> minBoost;
   public  Setting<Integer> verticalBoost;
   public  Setting<Float> limit;
   public  Setting<Boolean> autoJump;
   public  Setting<Boolean> inLava;
   public  Setting<Float> speed;
   public  Setting<Boolean> autoBoost;
   public  Setting<Integer> minY;
   public  Setting<Integer> add;
   public  Setting<Float> boostPitch;
   public  Setting<Float> speed2;
   public  Setting<ElytraVerticalMode> vertical;
   public  Setting<Float> vSpeed;
   public  Setting<Float> vPitch;
   public  Setting<Boolean> antiKick;
   public  Setting<Float> glide;
   public  Setting<Boolean> spoofPitch;
   public  Setting<Float> pitch;
   public  Setting<Boolean> antiAfk;
   public  Setting<Boolean> vertical2;
   public  Setting<Boolean> accelerate;
   public  Setting<Float> verAccelMin;
   public Setting<Float> accelMin;
   public Setting<Float> accelTime;
   public Setting<Float> start;
   public  Setting<Float> speed3;
   public Setting<Integer> deployTime;
   public Setting<Boolean> pitchLock;
   public Setting<Boolean> silent;
   public  Setting<Float> pitch2;
   public Setting<Boolean> compensate;
   public  Setting<Boolean> infDurability;
   public Setting<Boolean> muteElytra;
   public  Setting<Boolean> grimDurability;
   public final EnumMapping<ElytraFlyMode, ElytraFlyBehavior> field_4388;
   public final Timer field_4389;
   public final Timer field_4390;
   public final Timer field_4391;
   public final Timer field_4392;
   public final Timer field_4393;
   public boolean field_4394;
   public boolean field_4395;
   public boolean field_4396;

   public ElytraFlyModule() {
      super("ElytraFly", "Turns you into a block game boeing.", Category.MOVEMENT, "elytraflight");
      Settings.initialize(this);
      this.field_4388 = new EnumMapping<>(this.mode);
      this.field_4389 = new Timer();
      this.field_4390 = new Timer();
      this.field_4391 = new Timer();
      this.field_4392 = new Timer();
      this.field_4393 = new Timer();
      this.speed.method_31("LavaSpeed");
      this.vertical2.method_31("VerticalPacket");
      this.speed3.method_31("StrictSpeed");
      this.pitch2.method_31("BouncePitch");
      this.minBoost.method_2("GrimV3", SettingMode.MAX);
      this.limit.method_2("Unlimited", SettingMode.MAX);
      this.verticalBoost.method_5(var1 -> !this.minBoost.method_105());
      this.infDurability.method_5(var1 -> !this.grimDurability.getValue());
      this.field_4388.method_2(ElytraFlyMode.BOOST, new Class_0613(this));
      this.field_4388.method_2(ElytraFlyMode.CONTROL, new Class_0933(this));
      this.field_4388.method_2(ElytraFlyMode.PACKET, new Class_0321(this));
      this.field_4388.method_2(ElytraFlyMode.STRICT, new Class_0094(this));
      this.field_4388.method_2(ElytraFlyMode.BOUNCE, new Class_0517(this));
   }

   @Override
   public void onEnable() {
      this.field_4392.setTime(-1L);
      this.field_4394 = true;
      if (!this.method_535()) {
         this.field_4388.method_1156().onEnable();
      }
   }

   @Override
   public void onDisable() {
      if (!this.method_535()) {
         this.field_4388.method_1156().onDisable();
      }

      if (!this.method_535() && this.grimDurability.getValue() && this.grimDurability.method_176() && !mc.player.isShiftKeyDown()) {
         PacketSender.method_2(Inputs.shift(true));
         PacketSender.method_2(Inputs.shift(false));
      }

      this.field_4389.reset();
   }

   @Override
   public String getInfo() {
      try {
         return FontRenderer.method_3(this.mode.getValue().getName());
      } catch (Exception var2) {
         return null;
      }
   }

   @Subscribe
   public void method_78(Event_7 var1) {
      if (PlayerUtil.method_428() && this.field_4388.method_1156() instanceof Class_0613 var2 && this.autoBoost.getValue()) {
         RotationRequest var5 = Hub.field_2598.method_510();
         float var4 = var5 != null ? var5.getRotations()[0] : var1.method_500();
         Hub.field_2598.method_2(new float[]{var4, var2.method_501()}, 999, true);
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (!mc.player.isFallFlying()) {
         this.field_4394 = true;
      }

      this.field_4388.method_1156().method_2(var1);
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundPlayerActionPacket var2 && var2.getAction() == net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM) {
         this.field_4391.reset();
      }

      this.field_4388.method_1156().method_2(var1);
   }

   @Subscribe
   public void method_9(UseItemEvent var1) {
      if (!this.field_4396) {
         if (mc.player.getItemInHand(var1.method_12()).is(Items.FIREWORK_ROCKET)) {
            this.field_4395 = true;
            var1.method_463();
         }
      }
   }

   @Subscribe
   public void method_2(PostTickEvent var1) {
      int var2 = InventoryUtil.method_5(Items.ELYTRA);
      boolean var3 = var2 == -1;
      if (var2 == -1) {
         var2 = InventoryUtil.method_9(Items.ELYTRA);
      }

      boolean var4 = this.method_1183();
      if (this.field_4391.method_9(200L)) {
         if (!field_4348.method_1052() || !var3) {
            if (field_4346 == null || !field_4346.method_639()) {
               if (var4) {
                  if (PlayerUtil.method_4(mc.player)) {
                     mc.gameMode.handleContainerInput(0, 6, 0, ContainerInput.PICKUP, mc.player);
                     mc.gameMode.handleContainerInput(0, 6, 0, ContainerInput.PICKUP, mc.player);
                  } else if (var2 != -1) {
                     int var5 = var2;
                     if (var3) {
                        var2 = 0;
                        mc.gameMode.handleContainerInput(0, var5, 0, ContainerInput.SWAP, mc.player);
                     }

                     mc.gameMode.handleContainerInput(0, 6, var2, ContainerInput.SWAP, mc.player);
                     this.method_1177();
                     if (this.field_4395 && this.field_4389.method_5(150L)) {
                        this.field_4396 = true;
                        if (field_4345.isToggled()) {
                           field_4345.method_298();
                        } else {
                           ((DuckMinecraftClient)(Object)mc).interact();
                        }

                        this.field_4396 = false;
                        this.field_4395 = false;
                        this.field_4391.reset();
                     }

                     mc.gameMode.handleContainerInput(0, 6, var2, ContainerInput.SWAP, mc.player);
                     if (var3) {
                        mc.gameMode.handleContainerInput(0, var5, 0, ContainerInput.SWAP, mc.player);
                     }
                  }
               }

               this.field_4388.method_1156().method_2(var1);
            }
         }
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundAddEntityPacket var2 && var2.getType() == EntityType.FIREWORK_ROCKET) {
         Vec3 var4 = new Vec3(var2.getX(), var2.getY(), var2.getZ());
         if (var4.distanceToSqr(mc.player.position()) <= 36.0) {
            this.field_4391.reset();
         }
      }

      this.field_4388.method_1156().method_5(var1);
   }

   @Subscribe
   public void method_9(MoveEvent var1) {
      this.field_4388.method_1156().method_2(var1);
   }

   @Subscribe
   public void method_9(Event_19 var1) {
      this.field_4388.method_1156().method_9(var1);
   }

   @Subscribe
   public void method_9(TravelEvent var1) {
      this.field_4388.method_1156().method_9(var1);
   }

   @Subscribe
   public void method_5(MovementInputEvent var1) {
      this.field_4388.method_1156().method_5(var1);
   }

   @Subscribe
   public void method_2(SoundPlayEvent var1) {
      if (this.method_1183()
         && var1.method_863() != null
         && var1.method_863().getIdentifier() != null
         && var1.method_863().getIdentifier().toString().contains("item.armor.equip")) {
         var1.method_463();
      }
   }

   public void method_1177() {
      if (!this.field_4392.method_9(500L) || this.grimDurability.method_176()) {
         PacketSender.method_1099();
         mc.player.startFallFlying();
      }
   }

   public void method_1099() {
      if (this.method_558() && !mc.player.isFallFlying() && !mc.player.onGround() && this.takeoff.getValue() != TakeoffMode.NONE) {
         if (this.takeoff.getValue() == TakeoffMode.STRICT) {
            Hub.field_2596.method_2(this, 0.1F);
         }

         long var1 = 100L;
         if (this.mode.getValue() == ElytraFlyMode.STRICT) {
            var1 = (long)this.deployTime.getValue().intValue();
         }

         if (this.field_4390.method_9(var1)) {
            mc.player.startFallFlying();
            PacketSender.method_1099();
            this.field_4390.reset();
         }
      } else {
         Hub.field_2596.method_38(this);
      }
   }

   public boolean method_558() {
      return PlayerUtil.method_4(mc.player);
   }

   public boolean method_1178() {
      if (this.method_535() || mc.player.input == null) {
         return false;
      } else {
         return this.vertical2.getValue() && mc.player.input.keyPresses.jump()
            ? false
            : this.isToggled() && !mc.player.onGround() && this.mode.getValue() == ElytraFlyMode.PACKET && this.method_558();
      }
   }

   public boolean method_1179() {
      if (this.grimDurability.getValue()) {
         return false;
      } else if (Hub.field_2630.method_261()) {
         return false;
      } else if (field_4346 != null && field_4346.method_639()) {
         return false;
      } else if (this.method_41()) {
         mc.player.startFallFlying();
         return true;
      } else {
         return false;
      }
   }

   public boolean method_41() {
      if (Hub.field_2630.method_261()) {
         return false;
      } else {
         return field_4346 != null && field_4346.method_639()
            ? false
            : !mc.player.getAbilities().flying
               && !mc.player.isPassenger()
               && !mc.player.onClimbable()
               && this.method_558()
               && !mc.player.isInWater()
               && !mc.player.hasEffect(MobEffects.LEVITATION)
               && mc.player.input.keyPresses.jump();
      }
   }

   public boolean method_1180() {
      return field_4348.method_1052() ? false : mc.player.isUsingItem() && (!field_4347.isToggled() || !field_4347.items.getValue());
   }

   public boolean method_1181() {
      return this.method_1182() && mc.player.getXRot() > 0.0F;
   }

   public boolean method_1182() {
      return this.method_535()
         ? false
         : (mc.player.input.keyPresses.jump() || mc.player.input.keyPresses.shift())
            && this.vertical.getValue() == ElytraVerticalMode.MANUAL
            && mc.player.isFallFlying()
            && this.method_558();
   }

   public boolean method_428() {
      if (field_885) {
         return mc.player.isFallFlying();
      } else {
         field_885 = true;
         boolean var1 = mc.player.isFallFlying();
         field_885 = false;
         return var1;
      }
   }

   public boolean method_1183() {
      if (Hub.field_2630.method_261()) {
         return false;
      } else if (this.method_535()) {
         return false;
      } else if (field_885) {
         return false;
      } else {
         int var1 = InventoryUtil.method_5(Items.ELYTRA);
         if (var1 == -1) {
            var1 = InventoryUtil.method_9(Items.ELYTRA);
         }

         boolean var2 = var1 != -1;
         if (var2) {
            this.field_4393.reset();
         }

         var2 |= !this.field_4393.method_9(150L);
         if (PlayerUtil.method_4(mc.player)) {
            var2 = true;
         }

         if (!var2) {
            return false;
         } else {
            boolean var3 = this.grimDurability.getValue() && this.grimDurability.method_176();
            return this.isToggled() && var3;
         }
      }
   }
}
