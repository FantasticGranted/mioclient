package me.mioclient.module.combat;
import me.mioclient.internal.InventoryUtil;

import java.util.Comparator;
import me.mioclient.Hub;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.RenderUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class AimAssistModule extends Module {
   public Setting<Float> field_3261;
   public Setting<Float> field_3262;
   public Setting<Float> field_3263;
   public Setting<Boolean> field_3264;
   public Setting<Boolean> field_3265;
   public Setting<Boolean> field_3266;
   public Setting<Boolean> field_3267;
   public Setting<Boolean> field_3268;
   public AbstractClientPlayer field_3269;

   public AimAssistModule() {
      super("AimAssist", "Helps you aiming at players.", Category.COMBAT);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      if (mc.screen == null) {
         if (mc.player.getMainHandItem().getItem() instanceof AxeItem
            || InventoryUtil.isSword(mc.player.getMainHandItem())
            || !this.field_3264.getValue()) {
            if (this.field_3269 == null || !this.field_3266.getValue()) {
               this.field_3269 = mc.level
                  .players()
                  .stream()
                  .sorted(Comparator.comparing(var0 -> mc.player.distanceTo(var0)))
                  .filter(this::method_33)
                  .findFirst()
                  .orElse(null);
            }

            if (!this.method_33(this.field_3269)) {
               this.field_3269 = null;
            } else if (mc.crosshairPickEntity != this.field_3269) {
               if (this.field_3268.getValue()) {
                  float[] var2 = RotationManager.method_78(this.field_3269.getBoundingBox().getCenter());
                  mc.player.setYRot(var2[0]);
                  mc.player.setXRot(var2[1]);
               } else {
                  double var5 = method_34(this.field_3269);
                  if (var5 > 1.0 || var5 < -1.0) {
                     float var4 = (float)(var5 * (double)this.field_3262.getValue().floatValue() * 0.009999999776482582);
                     mc.player.setYRot(mc.player.getViewYRot(RenderUtil.method_776()) - var4);
                  }
               }
            }
         }
      }
   }

   public boolean method_33(Entity var1) {
      return var1 instanceof Player
         && var1 != mc.player
         && var1.isAlive()
         && (!this.field_3265.getValue() || !var1.isInvisible())
         && method_2(var1, (double)this.field_3263.getValue().floatValue())
         && mc.player.distanceTo(var1) <= this.field_3261.getValue()
         && (!this.field_3267.getValue() || !Hub.field_2603.method_1009(((Player)var1).getGameProfile().name()))
         && this.getId(var1);
   }

   public boolean getId(Entity var1) {
      Vec3 var2 = new Vec3(mc.player.getX(), mc.player.getY() + (double)mc.player.getEyeHeight(), mc.player.getZ());
      Vec3 var3 = new Vec3(var1.getX(), var1.getY(), var1.getZ());
      if (mc.level.clip(new ClipContext(var2, var3, Block.COLLIDER, Fluid.NONE, mc.player)).getType() == Type.MISS) {
         return true;
      } else {
         Vec3 var4 = new Vec3(var1.getX(), var1.getY() + (double)var1.getEyeHeight(), var1.getZ());
         return mc.level.clip(new ClipContext(var2, var4, Block.COLLIDER, Fluid.NONE, mc.player)).getType() == Type.MISS;
      }
   }

   public static double method_34(Entity var0) {
      return (
               (double)(mc.player.getViewYRot(RenderUtil.method_776()) - method_35(var0)) % (double)Class_0245.field_686
                  + 5.4e+02
            )
            % (double)Class_0245.field_686
         - 1.8e+02;
   }

   public static boolean method_2(Entity var0, double var1) {
      var1 = (double)((float)(var1 * Class_0245.field_688));
      double var3 = (
               (double)(mc.player.getViewYRot(RenderUtil.method_776()) - method_35(var0)) % (double)Class_0245.field_686
                  + 5.4e+02
            )
            % (double)Class_0245.field_686
         - 1.8e+02;
      return var3 > 0.0 && var3 < var1 || -var1 < var3 && var3 < 0.0;
   }

   public static float method_35(Entity var0) {
      double var1 = var0.getX() - mc.player.getX();
      double var3 = var0.getZ() - mc.player.getZ();
      double var5 = Math.atan2(var1, var3) * 57.2957795;
      return (float)(var5 * -1.0);
   }
}
