package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.enum_.PreType;
import me.mioclient.event.Event_19;
import me.mioclient.event.MoveEvent;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.module.movement.SpeedModule;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity.AnimationStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Class_1197 extends SpeedMode {
   public static AbstractModule_28 field_144 = Hub.field_2595.method_78(AbstractModule_28.class);
   public AABB field_1887;

   public Class_1197(SpeedModule var1) {
      super(var1);
   }

   @Override
   public void method_9(MoveEvent var1) {
      if (this.field_1887 != null) {
         if (MovementUtil.method_363()) {
            int var2 = 0;

            for (BlockEntity var4 : BlockUtil.method_1070()) {
               if (var4 instanceof ShulkerBoxBlockEntity) {
                  ShulkerBoxBlockEntity var5 = (ShulkerBoxBlockEntity)var4;
                  if (this.field_1887
                        .inflate(0.5)
                        .intersects(var5.getBoundingBox(Blocks.SHULKER_BOX.defaultBlockState()).move(var4.getBlockPos()))
                     && var5.getAnimationStatus() != AnimationStatus.CLOSED) {
                     var2 += field_144.method_1052() ? 0 : 4;
                  }
               }
            }

            float var6 = field_144.method_1052() ? 0.0F : 1F;

            for (Entity var10 : mc.level.entitiesForRendering()) {
               if (!(var10 instanceof FakePlayerEntity)
                  && var10 != mc.player
                  && var10.isPushable()
                  && var10.getBoundingBox().intersects(this.field_1887.inflate((double)var6))) {
                  if (var10 instanceof Boat) {
                     var2 += 4;
                  }

                  var2++;
               }
            }

            if (var2 != 0) {
               Vec3 var8 = var1.method_1066().normalize();
               var8 = var8.with(Axis.Y, 0.0);
               var1.method_4(var1.method_1066().add(var8.scale(0.08 * (double)var2)));
            }
         }
      }
   }

   @Override
   public void method_2(Event_19 var1) {
      if (var1.method_213() == PreType.POST) {
         this.field_1887 = mc.player.getBoundingBox();
      }
   }
}
