package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class Class_0509 implements MinecraftAccess {
   public final Timer field_1616 = new Timer();

   public Class_0509() {
      super();
   }

   public abstract boolean method_206();

   public void method_142() {
      if (this.method_539()) {
         this.field_1616.reset();
      }
   }

   public boolean method_538() {
      return this.method_206() && !this.field_1616.method_9(750L);
   }

   public boolean method_539() {
      synchronized (mc.level.entitiesForRendering()) {
         for (Entity var3 : mc.level.entitiesForRendering()) {
            if (this.method_3(var3) && this.method_29(var3.position())) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean method_29(Vec3 var1) {
      if (var1.distanceToSqr(mc.player.position()) > 144.0) {
         return false;
      } else {
         for (int var2 = 4; var2 >= 0; var2--) {
            AABB var3 = Hub.field_2612.method_2(mc.player, var2);
            double var4 = (double)ExplosionDamage.method_2(var1, mc.player, var3, 6.0, true, null, null);
            if (var4 >= (double)EntityUtil.method_76()) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean method_3(Entity var1) {
      return var1 instanceof EndCrystal && mc.player.distanceToSqr(var1) <= 144.0;
   }
}
