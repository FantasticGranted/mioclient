package me.mioclient.record;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Dimension;
import me.mioclient.internal.EntityUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import me.mioclient.internal.Class_0878;
import me.mioclient.internal.BlockUtil;

/** A captured snapshot of a player: time, bounding box, dimension, health, a per-player count, position, a cloned entity, and name. */
public final class PlayerSnapshot {
   public final long timestamp;
   public final AABB box;
   public final Dimension dimension;
   public final int health;
   public final int count;
   public final Vec3 pos;
   public final Class_0878 entity;
   public final String name;

   public PlayerSnapshot(long var1, AABB var3, Dimension var4, int var5, int var6, Vec3 var7, Class_0878 var8, String var9) {
      super();
      this.timestamp = var1;
      this.box = var3;
      this.dimension = var4;
      this.health = var5;
      this.count = var6;
      this.pos = var7;
      this.entity = var8;
      this.name = var9;
   }

   public static PlayerSnapshot method_2(Player var0, String var1) {
      Class_0878 var2 = new Class_0878(MinecraftAccess.mc.level);
      var2.method_78(var0);
      var2.hurtTime = var2.deathTime = 0;
      long var3 = System.currentTimeMillis();
      AABB var5 = var0.getBoundingBox();
      Dimension var6 = BlockUtil.method_1071();
      int var7 = Math.round(EntityUtil.method_2((net.minecraft.world.entity.Entity)var0));
      int var8 = Hub.field_2613.method_39(var0);
      Vec3 var9 = var0.position();
      return new PlayerSnapshot(var3, var5, var6, var7, var8, var9, var2, var1);
   }

   public long method_417() {
      return this.timestamp;
   }

   public AABB getBox() {
      return this.box;
   }

   public Dimension method_418() {
      return this.dimension;
   }

   public int getHealth() {
      return this.health;
   }

   public int getCount() {
      return this.count;
   }

   public Vec3 getPos() {
      return this.pos;
   }

   public Class_0878 getEntity() {
      return this.entity;
   }

   public String getName() {
      return this.name;
   }
}
