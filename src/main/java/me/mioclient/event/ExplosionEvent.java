package me.mioclient.event;

import me.mioclient.internal.Event;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.world.phys.Vec3;

/** Fired when an explosion packet is received. */
public class ExplosionEvent extends Event {
   public final double field_1159;
   public final double field_1160;
   public final double field_1161;
   public final float field_1162;
   public float field_1163;
   public float field_1164;
   public float field_1165;

   public ExplosionEvent(ClientboundExplodePacket var1) {
      this(var1.center().x(), var1.center().y(), var1.center().z(), var1.radius(), (float)var1.playerKnockback().orElse(Vec3.ZERO).x, (float)var1.playerKnockback().orElse(Vec3.ZERO).y, (float)var1.playerKnockback().orElse(Vec3.ZERO).z);
   }

   public ExplosionEvent(double var1, double var3, double var5, float var7, float var8, float var9, float var10) {
      super();
      this.field_1159 = var1;
      this.field_1160 = var3;
      this.field_1161 = var5;
      this.field_1162 = var7;
      this.field_1163 = var8;
      this.field_1164 = var9;
      this.field_1165 = var10;
   }

   public double method_380() {
      return this.field_1159;
   }

   public double method_395() {
      return this.field_1160;
   }

   public double method_396() {
      return this.field_1161;
   }

   public float method_397() {
      return this.field_1162;
   }

   public float method_398() {
      return this.field_1163;
   }

   public void method_52(float var1) {
      this.field_1163 = var1;
   }

   public float method_399() {
      return this.field_1164;
   }

   public void method_185(float var1) {
      this.field_1164 = var1;
   }

   public float method_400() {
      return this.field_1165;
   }

   public void method_92(float var1) {
      this.field_1165 = var1;
   }
}
