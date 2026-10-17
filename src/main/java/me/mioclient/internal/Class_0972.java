package me.mioclient.internal;

import java.util.ArrayDeque;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.DisconnectEvent;
import me.mioclient.event.Subscribe;

public class Class_0972 implements MinecraftAccess {
   public final ArrayDeque<Double> field_2988 = new ArrayDeque<>();
   public double field_1570;
   public double field_2989;
   public Timer field_2990 = new Timer();

   public Class_0972() {
      super();
      lookups.method_14(this);
   }

   @Subscribe
   public void method_2(DisconnectEvent var1) {
      this.field_1570 = 0.0;
      this.field_2989 = 0.0;
      this.field_2988.clear();
      this.field_2990.reset();
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      double var2 = this.method_876();
      if (!(var2 > 0.0) && mc.player.tickCount % 4 != 0) {
         this.field_2988.pollFirst();
      } else {
         this.field_2988.add(var2 * (double)Hub.field_2596.method_537());
      }

      while (!this.field_2988.isEmpty() && this.field_2988.size() > 9) {
         this.field_2988.poll();
      }

      double var4 = 0.0;

      for (double var7 : this.field_2988) {
         var4 += var7;
      }

      this.field_1570 = this.field_2988.size() == 0 ? 0.0 : var4 / (double)this.field_2988.size();
      if (this.field_1570 < 21.0) {
         this.field_2989 = this.field_1570;
      } else if (this.field_2990.method_9(50L)) {
         this.field_2989 = this.field_1570;
         this.field_2990.reset();
      }
   }

   public double method_874() {
      return this.field_1570;
   }

   public double method_875() {
      return this.field_2989;
   }

   public double method_876() {
      double var1 = mc.player.isPassenger()
         ? mc.player.getVehicle().getX() - mc.player.getVehicle().xo
         : mc.player.getX() - mc.player.xo;
      double var3 = mc.player.isPassenger()
         ? mc.player.getVehicle().getZ() - mc.player.getVehicle().zo
         : mc.player.getZ() - mc.player.zo;
      return Math.hypot(var1, var3) * 2e+01 * 3.6;
   }
}
