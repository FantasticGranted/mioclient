package me.mioclient.module.combat;

import java.util.Comparator;
import java.util.stream.StreamSupport;
import me.mioclient.Hub;
import me.mioclient.enum_.Priority;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.ChatUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import me.mioclient.nick.Settings;

public class SelfWebModule extends Module {
   public Setting<Boolean> rotate;
   public Setting<Boolean> autoDisable;
   public Setting<Boolean> onlyHole;
   public Setting<Boolean> smart;
   public Setting<Float> enemyRange;
   public Setting<Boolean> aggressive;

   public SelfWebModule() {
      super("SelfWeb", "Places a cobweb at your feet.", Category.COMBAT);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_29(Event_7 var1) {
      Entity var2 = this.method_5(this.enemyRange);
      if (this.smart.getValue()) {
         if (var2 != null) {
            this.method_4(var1);
         }
      } else {
         this.method_4(var1);
      }
   }

   public void method_4(Event_7 var1) {
      if (!mc.player.isPassenger() && (Hub.field_2605.method_224() || !this.onlyHole.getValue())) {
         BlockPos var2 = BlockPos.containing(mc.player.position());
         BlockState var3 = mc.level.getBlockState(var2);
         if (var3.canBeReplaced() && !var3.liquid() && mc.player.onGround()) {
            int var4 = InventoryUtil.method_5(Items.COBWEB);
            int var5 = mc.player.getInventory().selected;
            if (var4 == -1) {
               ChatUtil.method_2(Component.literal(this.getName()).append(" is out of blocks!"), ChatUtil.method_38(-2), Priority.HIGH);
               this.disable();
            } else {
               Direction var6 = InteractionUtil.method_78(var2);
               if (var6 != null) {
                  InventoryUtil.method_16(var4);
                  AABB var7 = mc.player.getBoundingBox();
                  mc.player.setBoundingBox(new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
                  InteractionUtil.method_2(var2, var6, false, InteractionHand.MAIN_HAND);
                  mc.player.setBoundingBox(var7);
                  InventoryUtil.method_16(var5);
                  if (this.rotate.getValue()) {
                     Hub.field_2598.method_2(new float[]{var1.method_500(), (float)Class_0245.field_685}, 5);
                  }

                  if (this.autoDisable.getValue()) {
                     this.disable();
                  }
               }
            }
         }
      }
   }

   public Entity method_5(Setting<Float> var1) {
      return StreamSupport.<Entity>stream(mc.level.entitiesForRendering().spliterator(), false)
         .filter(
            var2 -> var2 != mc.player
                  && mc.player.distanceTo(var2) <= (Float)var1.getValue()
                  && this.method_209(var2)
                  && var2 instanceof Player
                  && !Hub.field_2603.method_1009(var2.getName().getString())
                  && var2.isAlive()
         )
         .min(Comparator.comparing(mc.player::distanceToSqr))
         .orElse(null);
   }

   public boolean method_209(Entity var1) {
      return this.aggressive.getValue() ? var1.getY() >= mc.player.getY() : var1.getY() > mc.player.getY();
   }
}
