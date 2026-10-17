package me.mioclient.internal;

import java.util.ArrayList;
import java.util.List;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.Event_3;
import me.mioclient.module.render.TrajectoriesModule;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;

public class Class_0204 {
   public final List<Vec3> field_578;
   public boolean field_579;
   public AABB field_580;
   public Entity field_581;
   public Entity field_582;
   public final TrajectoriesModule field_583;

   public Class_0204(TrajectoriesModule var1) {
      super();
      this.field_583 = var1;
      this.field_578 = new ArrayList<>();
   }

   public void method_208() {
      this.field_578.clear();
      this.field_579 = false;
      this.field_581 = null;
   }

   public void method_80(Entity var1) {
      this.field_582 = var1;
   }

   public void method_239() {
      this.method_240();

      for (int var1 = 0; var1 < 2000; var1++) {
         HitResult var2 = this.field_583.field_2360.method_870();
         if (var2 != null) {
            this.method_5(var2);
            break;
         }

         this.method_240();
      }
   }

   public void method_240() {
      this.field_578.add(new Vec3(this.field_583.field_2360.field_2983.x, this.field_583.field_2360.field_2983.y, this.field_583.field_2360.field_2983.z));
   }

   public void method_5(HitResult var1) {
      if (var1.getType() == Type.BLOCK) {
         BlockHitResult var2 = (BlockHitResult)var1;
         this.field_579 = true;
         this.field_580 = new AABB(var2.getLocation(), var2.getLocation());
         if (var2.getDirection() == Direction.UP || var2.getDirection() == Direction.DOWN) {
            this.field_580 = this.field_580.inflate(0.25, 0.0, 0.25);
         } else if (var2.getDirection() != Direction.NORTH && var2.getDirection() != Direction.SOUTH) {
            this.field_580 = this.field_580.inflate(0.0, 0.25, 0.25);
         } else {
            this.field_580 = this.field_580.inflate(0.25, 0.25, 0.0);
         }

         this.field_578.add(var1.getLocation());
      } else if (var1.getType() == Type.ENTITY) {
         this.field_581 = ((EntityHitResult)var1).getEntity();
      }
   }

   public void method_4(Event_3 var1) {
      Vec3 var2 = null;

      for (Vec3 var4 : this.field_578) {
         if (var2 != null) {
            RenderUtil.field_2672
               .method_2(
                  var1.method_10(),
                  this.field_583.field_2373,
                  var2,
                  var4,
                  this.field_583.color.getValue().getRGB(),
                  this.field_583.color.getValue().getRGB()
               );
         }

         var2 = var4;
      }

      if (this.field_579) {
         BoxRenderer.method_5(var1.method_10(), this.field_580, Colors.method_9(this.field_583.color.getValue(), 60));
         BoxRenderer.method_2(var1.method_10(), this.field_580, this.field_583.color.getValue(), this.field_583.lineWidth.getValue());
      }

      if (this.field_581 != null && this.field_582 == MinecraftAccess.mc.player) {
         BoxRenderer.method_5(
            var1.method_10(), BoxUtil.method_2(this.field_581, var1.method_880()), Colors.method_9(this.field_583.color.getValue(), 60)
         );
      }
   }
}
