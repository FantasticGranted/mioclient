package me.mioclient.module.combat;
import me.mioclient.internal.InventoryUtil;

import java.util.concurrent.TimeUnit;
import me.mioclient.Hub;
import me.mioclient.enum_.PreType;
import me.mioclient.event.Event_19;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.MathUtil;
import me.mioclient.mixin.ducks.DuckMinecraftClient;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.phys.EntityHitResult;
import me.mioclient.nick.Settings;

public class TriggerModule extends Module {
   public Setting<Float> field_3057;
   public Setting<Boolean> field_3058;
   public Setting<Double> field_3059;
   public Setting<Integer> field_3060;
   public Setting<Boolean> field_3061;
   public Setting<Boolean> field_3062;
   public Setting<Boolean> field_3063;
   public Setting<Boolean> field_3064;
   public Setting<Boolean> field_3065;
   public Setting<Boolean> field_3066;
   public final Timer field_3067;

   public TriggerModule() {
      super("Trigger", "Attacks entities under your crosshair.", Category.COMBAT);
      Settings.initialize(this);
      this.field_3067 = new Timer();
   }

   @Subscribe
   public void method_9(Event_19 var1) {
      if (var1.method_213() != PreType.POST) {
         if (!this.method_535()) {
            if (mc.player.getMainHandItem().getItem() instanceof AxeItem
               || InventoryUtil.isSword(mc.player.getMainHandItem())
               || !this.field_3058.getValue()) {
               if (mc.hitResult instanceof EntityHitResult) {
                  Entity var2 = ((EntityHitResult)mc.hitResult).getEntity();
                  boolean var3 = var2 instanceof Player && !Hub.field_2603.method_1009(var2.getName().getString()) && this.field_3062.getValue();
                  if (var3 && this.field_3063.getValue() && !PlayerUtil.method_29((LivingEntity)((Player)var2))) {
                     var3 = false;
                  }

                  if (var2.isAlive()
                     && (
                        var3
                           || var2 instanceof AgeableMob && this.field_3064.getValue()
                           || var2 instanceof Enemy && this.field_3065.getValue()
                           || var2 instanceof EndCrystal && this.field_3066.getValue()
                     )
                     && mc.player.getAttackStrengthScale(0.5F) >= 1F
                     && this.field_3067.method_2(this.field_3059.getValue() * Math.random(), TimeUnit.SECONDS)
                     && mc.player.distanceTo(var2) <= this.field_3057.getValue()) {
                     this.method_230(MathUtil.method_17(this.field_3060.getValue()));
                     this.field_3067.reset();
                  }
               }
            }
         }
      }
   }

   public void method_230(boolean var1) {
      if (var1) {
         mc.player.swing(InteractionHand.MAIN_HAND);
         mc.player.resetAttackStrengthTicker();
      } else {
         ((DuckMinecraftClient)(Object)mc).attack();
      }
   }
}
