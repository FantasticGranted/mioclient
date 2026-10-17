package me.mioclient.module.movement;

import me.mioclient.Hub;
import me.mioclient.enum_.VelocityMode;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.InteractBlockEvent;
import me.mioclient.event.ExplosionEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.PushOutOfBlocksEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0030;
import me.mioclient.internal.VelocityBehavior;
import me.mioclient.internal.Class_0198;
import me.mioclient.internal.Class_0552;
import me.mioclient.internal.MathUtil;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.EnumMapping;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.setting.Setting;
import me.mioclient.nick.Settings;

public class VelocityModule extends Module {
   public static final SprintModule sprint = Hub.field_2595.method_78(SprintModule.class);
   public static final AbstractModule_28 field_2508 = Hub.field_2595.method_78(AbstractModule_28.class);
   public Setting<VelocityMode> mode;
   public Setting<Integer> horizontal;
   public Setting<Integer> vertical;
   public Setting<Boolean> inverse;
   public Setting<Boolean> explosions;
   public Setting<Boolean> push;
   public Setting<Boolean> liquids;
   public final EnumMapping<VelocityMode, VelocityBehavior> field_2516;

   public VelocityModule() {
      super("Velocity", "Cancels all the pushing you receive.", Category.MOVEMENT);
      Settings.initialize(this);
      this.field_2516 = new EnumMapping<>(this.mode);
      this.field_2516.method_2(VelocityMode.PLAIN, new Class_0198(this));
      this.field_2516.method_2(VelocityMode.GRIM, new Class_0552(this));
      this.field_2516.method_2(VelocityMode.WALLS, new Class_0030(this));
   }

   @Override
   public String getInfo() {
      return this.mode.getValue() != VelocityMode.PLAIN
         ? FontRenderer.method_3(this.mode.getValue().getName())
         : new PlaceholderText()
            .method_2(MathUtil.method_2((float)this.vertical.getValue().intValue(), 1))
            .method_2(MathUtil.method_2((float)this.horizontal.getValue().intValue(), 1))
            .method_9("H\u0001% V\u0001%");
   }

   @Override
   public void onDisable() {
      sprint.field_1033 = false;
      this.field_2516.method_1156().onDisable();
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      this.field_2516.method_1156().method_2(var1);
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      this.field_2516.method_1156().method_5(var1);
   }

   @Subscribe(
      method_800 = -200
   )
   public void method_2(PostTickEvent var1) {
      if (this.mode.getValue() != VelocityMode.WALLS) {
         sprint.field_1033 = false;
      }

      this.field_2516.method_1156().method_40();
   }

   @Subscribe
   public void method_5(InteractBlockEvent var1) {
      this.field_2516.method_1156().method_5(var1);
   }

   @Subscribe
   public void method_9(Event_19 var1) {
      this.field_2516.method_1156().method_9(var1);
   }

   @Subscribe
   public void method_2(ExplosionEvent var1) {
      this.field_2516.method_1156().method_2(var1);
   }

   @Subscribe
   public void method_2(PushOutOfBlocksEvent var1) {
      if (this.push.getValue()) {
         var1.method_463();
      }
   }

   public boolean method_734() {
      if (!this.isToggled()) {
         return false;
      } else if (this.field_2516.method_1156() instanceof Class_0198 && this.horizontal.getValue() == 0 && this.vertical.getValue() == 0) {
         return true;
      } else {
         if (this.field_2516.method_1156() instanceof Class_0552 var1 && !var1.method_581()) {
            return true;
         }

         return false;
      }
   }

   public VelocityBehavior method_735() {
      return this.field_2516.method_1156();
   }
}
