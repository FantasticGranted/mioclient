package me.mioclient.module.combat;

import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.api.PhaseBoxProvider;
import me.mioclient.enum_.ClimbableType;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0003;
import me.mioclient.internal.Class_0035;
import me.mioclient.internal.Class_0088;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.Class_1161;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.movement.NoSlowModule;
import me.mioclient.record.RenderStyle;
import me.mioclient.record.BlockFaceBox;
import me.mioclient.setting.Setting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import me.mioclient.nick.Settings;

public class AntiPhaseModule extends Module {
   public static final NoSlowModule field_843 = Hub.field_2595.method_78(NoSlowModule.class);
   public Setting<ClimbableType> material;
   public Setting<Float> range;
   public Setting<Integer> delay;
   public Setting<Integer> bPT;
   public Setting<Boolean> strictDirection;
   public Setting<Boolean> rotate;
   public Setting<Boolean> noCrawl;
   public Setting<Boolean> render;
   public Setting<Color> fill;
   public Setting<Color> outline;
   public Setting<Float> lineWidth;
   public Setting<Boolean> fade;
   public Setting<Float> fadeTime;
   public final Timer field_857;

   public AntiPhaseModule() {
      super("AntiPhase", "Prevents your enemies from pearl-phasing into nearest block.", Category.COMBAT);
      Settings.initialize(this);
      this.field_857 = new Timer();
      Hub.field_2616.method_2(new RenderStyle(this, this.fill, this.outline, this.lineWidth, this.fadeTime, () -> true, this.fade, 450));
   }

   @Subscribe
   public void method_9(Event_7 var1) {
      if (this.field_857.method_5((long)this.delay.getValue().intValue()) && !field_843.method_573()) {
         int var2 = 0;

         for (AbstractClientPlayer var4 : mc.level.players()) {
            if (this.method_33(var4)) {
               BlockFaceBox var5 = this.method_301().method_2(this, var4);
               if (var5 != null) {
                  if (this.method_2(var5)) {
                     if (this.rotate.getValue()) {
                        float[] var6 = RotationManager.method_78(var5.getOffsetCenter());
                        Hub.field_2598.method_2(var6, 25);
                     }

                     if (this.render.getValue()) {
                        Hub.field_2616.method_2(this, var5.getBox());
                     }

                     var2++;
                  }

                  if (var2 >= this.bPT.getValue()) {
                     break;
                  }
               }
            }
         }
      }
   }

   public boolean method_2(BlockFaceBox var1) {
      Item var2 = this.material.getValue().method_903();
      int var3 = InventoryUtil.method_5(var2);
      int var4 = mc.player.getInventory().selected;
      if (var3 == -1) {
         return false;
      } else {
         InventoryUtil.method_78(var3);
         BlockHitResult var5 = new BlockHitResult(var1.getOffsetCenter(), var1.getDirection(), var1.getPos(), false);
         Hub.field_2615.method_3(() -> PacketSender.method_2(InteractionHand.MAIN_HAND, var5));
         PacketSender.method_9(InteractionHand.MAIN_HAND);
         InventoryUtil.method_78(var4);
         return true;
      }
   }

   public boolean method_33(Player var1) {
      if (!var1.isAlive() || mc.player == var1 || var1.isSwimming()) {
         return false;
      } else if (Hub.field_2603.getId(var1)) {
         return false;
      } else if (PlayerUtil.method_7((LivingEntity)var1).size() != 1) {
         return false;
      } else if (this.method_194(PlayerUtil.method_9(var1))) {
         return false;
      } else if (!PlayerUtil.method_29((LivingEntity)var1)) {
         return false;
      } else {
         return this.noCrawl.getValue() && var1.getBoundingBox().getYsize() <= 1.0
            ? false
            : var1.distanceTo(mc.player) <= this.range.getValue();
      }
   }

   public boolean method_194(BlockPos var1) {
      AABB var2 = new AABB(var1);

      for (Entity var4 : mc.level.entitiesForRendering()) {
         if (var4 instanceof ItemFrame && var4.getBoundingBox().intersects(var2)) {
            return true;
         }
      }

      return false;
   }

   public PhaseBoxProvider method_301() {
      return (PhaseBoxProvider)(switch ((ClimbableType)this.material.getValue()) {
         case LADDER -> new Class_0088();
         case SCAFFOLDING -> new Class_0035();
         case ITEMFRAME -> new Class_1161();
         case VINE -> new Class_0003();
      });
   }
}
