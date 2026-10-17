package me.mioclient.module.abstract_;

import me.mioclient.Hub;
import me.mioclient.enum_.Class_1010;
import me.mioclient.enum_.Class_1054;
import me.mioclient.enum_.PreType;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0030;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.Class_0910;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.Category;
import me.mioclient.module.movement.VelocityModule;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.util.Mth;
import me.mioclient.nick.Settings;

public class AbstractModule_28 extends AbstractModule_41 {
   public static final VelocityModule velocity = Hub.field_2595.method_78(VelocityModule.class);
   public static final float field_3741 = (float)(
      Math.pow(0.20000000298023224, 3.0)
            * 8.0
            * 0.15
         - 0.001
   );
   public Setting<Boolean> field_3742;
   public Setting<Float> yawStep;
   public Setting<Boolean> movementSync;
   public Setting<Class_1054> rotations;
   public Setting<Class_1010> strictDirection;
   public final Class_0910 field_3747;
   public boolean field_3748;
   public boolean field_3749;

   public AbstractModule_28() {
      super("AntiCheat", "Manages the client's behavior on different anti-cheats.", Category.CLIENT);
      Settings.initialize(this);
      this.field_3747 = new Class_0910(this);
      lookups.method_14(this);
   }

   @Subscribe
   public void method_2(PostTickEvent var1) {
      this.field_3747.method_221(false);
   }

   @Subscribe
   public void method_9(Event_19 var1) {
      this.field_3749 = var1.method_213() == PreType.PRE;
      if (var1.method_213() == PreType.PRE && this.movementSync.getValue()) {
         if (this.method_1052() && mc.screen instanceof AbstractContainerScreen && !this.field_3747.method_826()) {
            return;
         }

         if (PlayerUtil.method_428()) {
            return;
         }

         if (velocity.isToggled() && velocity.method_735() instanceof Class_0030 var2 && var2.method_41()) {
            return;
         }

         float var4 = field_3741 * 2F * (float)(this.field_3748 ? -1 : 1);
         this.field_3748 = !this.field_3748;
         var1.setPitch(Mth.clamp(var1.method_501() + var4, (float)(-Class_0245.field_685), (float)Class_0245.field_685));
      }
   }

   @Subscribe(
      method_800 = 999
   )
   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundPlayerActionPacket var2 && this.method_1052() && var2.getAction() == Action.START_DESTROY_BLOCK) {
         PacketSender.method_2(Action.STOP_DESTROY_BLOCK, var2.getPos(), var2.getDirection());
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (this.method_1052()) {
         if (var1.method_127() instanceof ClientboundContainerClosePacket var2 && var2.getContainerId() == 0) {
            var1.method_463();
         }
      }
   }

   public float method_1051() {
      return this.yawStep.getValue();
   }

   public boolean method_1052() {
      return this.field_3742.getValue();
   }

   public Class_0910 method_1053() {
      return this.field_3747;
   }
}
