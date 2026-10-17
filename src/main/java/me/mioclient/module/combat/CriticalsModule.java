package me.mioclient.module.combat;

import me.mioclient.Hub;
import me.mioclient.enum_.InteractType;
import me.mioclient.enum_.CriticalsMode;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0030;
import me.mioclient.internal.InteractPacketReader;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import me.mioclient.nick.Settings;

public class CriticalsModule extends Module {
   public static final AuraModule field_3405 = Hub.field_2595.method_78(AuraModule.class);
   public Setting<CriticalsMode> mode;
   public Setting<Boolean> pauseOnMove;

   public CriticalsModule() {
      super("Criticals", "Turns your hits into critical ones.", Category.COMBAT);
      Settings.initialize(this);
      this.pauseOnMove.method_5(var1 -> this.mode.getValue() != CriticalsMode.GRIMV3);
   }

   @Override
   public String getInfo() {
      return this.mode.getValue().getName();
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof net.minecraft.network.protocol.game.ServerboundAttackPacket var2 && this.method_7(var2)) {
         Entity var4 = mc.level.getEntity(var2.entityId());
         if (var4 == null) {
            return;
         }

         if (!var1.method_464()) {
            this.method_300();
         }

         mc.execute(() -> mc.player.crit(var4));
      }
   }

   public void method_300() {
      float[] var1 = new float[]{mc.player.getYRot(), mc.player.getXRot()};
      if (field_3405.field_582 != null && field_3405.isToggled()) {
         var1 = RotationManager.method_2(RotationManager.method_14(field_3405.field_582), Hub.field_2598.method_509());
      }

      switch ((CriticalsMode)this.mode.getValue()) {
         case NCP:
            if (PlayerUtil.method_5(mc.player)) {
               this.method_9(0.0625101, 0.0, 0.0125, 0.0);
            } else {
               this.method_9(0.11, 0.1100013579);
            }
            break;
         case GRIM:
            PacketSender.method_2(
               mc.player.getX(),
               mc.player.getY() - 1e-06,
               mc.player.getZ(),
               var1[0],
               var1[1],
               false
            );
            break;
         case GRIMV3:
            float var2 = Math.clamp(
               var1[1], -9e+01F + AbstractModule_28.field_3741, 9e+01F - AbstractModule_28.field_3741
            );
            PacketSender.method_2(
               mc.player.getX(),
               mc.player.getY() + 0.0626,
               mc.player.getZ(),
               var1[0],
               var2 + AbstractModule_28.field_3741,
               false
            );
            PacketSender.method_2(
               mc.player.getX(),
               mc.player.getY() + 0.0455,
               mc.player.getZ(),
               var1[0],
               var2 - AbstractModule_28.field_3741,
               false
            );
            break;
         default:
            this.method_9(0.0625101, 0.0);
      }
   }

   public boolean method_976() {
      if ((this.pauseOnMove.getValue() || this.mode.getValue() == CriticalsMode.GRIMV3) && MovementUtil.method_363()) {
         return false;
      } else if (this.mode.getValue() == CriticalsMode.GRIMV3) {
         return mc.player.onGround() && Class_0030.method_43();
      } else {
         return this.mode.getValue() == CriticalsMode.GRIM && !mc.player.onGround()
            ? true
            : mc.player.onGround() && mc.player.verticalCollision && !mc.player.isInLava() && !mc.player.isInWater();
      }
   }

   public void method_9(double... var1) {
      for (double var5 : var1) {
         PacketSender.method_2(new Pos(mc.player.getX(), mc.player.getY() + var5, mc.player.getZ(), false, false));
      }
   }

   public boolean method_7(net.minecraft.network.protocol.game.ServerboundAttackPacket var1) {
      if (this.method_976()) {
         Entity var2 = mc.level.getEntity(var1.entityId());
         return (!field_3405.isToggled() || !field_3405.field_1073 || this.mode.getValue() == CriticalsMode.GRIM)
            && var2 != null
            && var2.isAlive()
            && !(var2 instanceof EndCrystal);
      } else {
         return false;
      }
   }
}
