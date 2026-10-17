package me.mioclient.module.player;
import net.minecraft.client.gui.screens.LevelLoadingScreen;

import me.mioclient.Hub;
import me.mioclient.enum_.PreType;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.MovementInputEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.CameraEvent;
import me.mioclient.event.InteractBlockEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.InteractItemEvent;
import me.mioclient.event.MovementInputPostEvent;
import me.mioclient.event.MouseUpdateEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.Class_0411;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.CameraType;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class FreecamModule extends Module {
   public final Class_0411 field_2822 = new Class_0411();
   public Setting<Float> speed;
   public Setting<Float> verticalSpeed;
   public Setting<Boolean> noHands;
   public Setting<Boolean> rotate;
   public Setting<Boolean> always;
   public Vec3 field_806;
   public Vec3 field_2828;
   public float[] field_2829;
   public CameraType field_1352;
   public float field_1760;
   public float field_1542;
   public float field_1761;
   public float field_1543;

   public FreecamModule() {
      super("Freecam", "Allows your camera to fly through walls.", Category.PLAYER);
      Settings.initialize(this);
      this.field_2829 = null;
      this.speed.method_214();
      this.verticalSpeed.method_214();
   }

   @Override
   public void onEnable() {
      if (this.method_535()) {
         this.disable();
      } else {
         this.field_1352 = mc.options.getCameraType();
         new MovementInputEvent(mc.player.input, 0.0F).reset();
         this.field_2828 = this.field_806 = mc.gameRenderer.getMainCamera().position();
         this.field_2829 = new float[]{mc.player.getYRot(), mc.player.getXRot()};
         this.field_1542 = this.field_1760 = this.field_2829[0];
         this.field_1543 = this.field_1761 = this.field_2829[1];
      }
   }

   @Override
   public void onDisable() {
      if (!this.method_535()) {
         mc.player.setYRot(this.field_2829[0]);
         mc.player.setXRot(this.field_2829[1]);
         mc.options.setCameraType(this.field_1352);
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (mc.screen instanceof LevelLoadingScreen) {
         this.disable();
      }

      mc.options.setCameraType(CameraType.FIRST_PERSON);
      this.field_2828 = this.field_806;
      double[] var2 = MovementUtil.method_2(this.field_1760, this.field_2822, (double)this.speed.getValue().floatValue());
      this.field_806 = this.field_806.add(var2[0], 0.0, var2[1]);
      if (mc.options.keyJump.isDown()) {
         this.field_806 = this.field_806.add(0.0, (double)this.verticalSpeed.getValue().floatValue(), 0.0);
      }

      if (mc.options.keyShift.isDown()) {
         this.field_806 = this.field_806.subtract(0.0, (double)this.verticalSpeed.getValue().floatValue(), 0.0);
      }

      this.field_2822.tick(false, 1F);
   }

   @Subscribe
   public void method_2(MovementInputPostEvent var1) {
      var1.method_463();
   }

   @Subscribe(
      method_800 = 1000
   )
   public void method_9(Event_19 var1) {
      boolean var2 = mc.gameMode.isDestroying();
      if (var1.method_213() != PreType.POST
         && this.rotate.getValue()
         && mc.hitResult != null
         && (!RotationManager.method_513() || var2 || this.always.getValue())) {
         var1.method_5(RotationManager.method_78(mc.hitResult.getLocation()));
      }
   }

   @Subscribe
   public void method_5(InteractBlockEvent var1) {
      boolean var2 = mc.player.getItemInHand(var1.method_12()).getItem() instanceof BucketItem;
      if (var2 || this.rotate.getValue() && RotationManager.method_513()) {
         float[] var3 = RotationManager.method_78(var1.method_382().getLocation());
         Hub.field_2598.method_2(var3, 1000);
      }
   }

   @Subscribe
   public void method_2(InteractItemEvent var1) {
      if (this.rotate.getValue() && mc.hitResult != null && !this.always.getValue()) {
         var1.method_9(RotationManager.method_78(mc.hitResult.getLocation()));
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundDisconnectPacket) {
         this.disable();
      }
   }

   @Subscribe
   public void method_2(MouseUpdateEvent var1) {
      this.method_29(var1.method_712() * 0.15000000596046448, var1.method_713() * 0.15000000596046448);
      var1.method_463();
   }

   @Subscribe
   public void method_2(CameraEvent var1) {
      float var2 = mc.getDeltaTracker().getGameTimeDeltaPartialTick(true);
      var1.method_38(new Vec3(this.method_460(var2), this.method_220(var2), this.method_6(var2)));
      var1.method_463();
   }

   public void method_29(double var1, double var3) {
      this.field_1542 = this.field_1760;
      this.field_1543 = this.field_1761;
      this.field_1760 = (float)((double)this.field_1760 + var1);
      this.field_1761 = (float)((double)this.field_1761 + var3);
      this.field_1761 = Mth.clamp(this.field_1761, (float)(-Class_0245.field_685), (float)Class_0245.field_685);
   }

   public double method_460(float var1) {
      return Mth.lerp((double)var1, this.field_2828.x, this.field_806.x);
   }

   public double method_220(float var1) {
      return Mth.lerp((double)var1, this.field_2828.y, this.field_806.y);
   }

   public double method_6(float var1) {
      return Mth.lerp((double)var1, this.field_2828.z, this.field_806.z);
   }

   public double method_8(float var1) {
      return mc.screen != null ? (double)this.field_1760 : (double)Mth.lerp(var1, this.field_1542, this.field_1760);
   }

   public double method_334(float var1) {
      return mc.screen != null ? (double)this.field_1761 : (double)Mth.lerp(var1, this.field_1543, this.field_1761);
   }

   public boolean method_279() {
      return this.isToggled() && this.noHands.getValue();
   }
}
