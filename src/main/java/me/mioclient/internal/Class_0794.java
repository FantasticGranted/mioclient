package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.api.EntityInfoAccess;
import me.mioclient.api.MinecraftAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public class Class_0794 implements MinecraftAccess {
   public boolean field_2498;
   public boolean field_2499 = true;
   public boolean field_2500;
   public boolean field_2501 = true;
   public boolean field_2502;
   public boolean field_2503;
   public boolean field_2504;
   public boolean field_2505;
   public int field_2506;
   public float field_2507 = 6F;

   public Class_0794() {
      super();
   }

   public boolean method_185(BlockPos var1) {
      if (!this.field_2504 && !BlockUtil.method_36(var1)) {
         return false;
      } else {
         BlockPos var2 = var1.offset(0, 1, 0);
         Block var3 = mc.level.getBlockState(var2).getBlock();
         boolean var4 = var3 instanceof AirBlock || var2.equals(Hub.field_2622.method_344());
         if (this.field_2501) {
            if (!var4 && var3 != Blocks.FIRE) {
               return false;
            }
         } else if (!var4) {
            return false;
         }

         if (this.field_2498 && !(mc.level.getBlockState(var1.offset(0, 2, 0)).getBlock() instanceof AirBlock)) {
            return false;
         } else {
            AABB var5 = new AABB(var2).setMaxY((double)(var2.getY() + (this.field_2502 ? 1 : 2)));
            int var6 = 0;

            for (Entity var8 : mc.level.entitiesForRendering()) {
               if (var8 != null && !var8.isRemoved() && var8.isAlive()) {
                  AABB var9 = var8.getBoundingBox();
                  if (var8 instanceof Player && var8 != mc.player) {
                     var9 = BoxUtil.method_4((LivingEntity)var8);
                     if (this.field_2503 && !((EntityInfoAccess)var8).mio$isNextToWall()) {
                        var9 = var9.inflate(0.0001, 0.0, 0.0001);
                     }

                     for (int var10 = 1; var10 <= this.field_2506; var10++) {
                        AABB var11 = Hub.field_2612.method_2((Player)var8, var10).setMaxY(var9.maxY).setMinY(var9.minY);
                        if (var11.intersects(var5)) {
                           var6++;
                        }
                     }
                  } else if (var8 instanceof EndCrystal
                        && !this.field_2500
                        && mc.player.getEyePosition().distanceToSqr(var8.position()) <= (double)this.field_2507
                     || var8 instanceof ItemEntity && this.field_2505) {
                     continue;
                  }

                  if (var9.intersects(var5)) {
                     var6++;
                     break;
                  }
               }
            }

            return !this.field_2499 || var6 == 0;
         }
      }
   }

   public Class_0794 method_17(boolean var1) {
      this.field_2498 = var1;
      return this;
   }

   public Class_0794 method_37(boolean var1) {
      this.field_2499 = var1;
      return this;
   }

   public Class_0794 method_33(boolean var1) {
      this.field_2500 = var1;
      return this;
   }

   public Class_0794 method_34(boolean var1) {
      this.field_2501 = var1;
      return this;
   }

   public Class_0794 method_35(boolean var1) {
      this.field_2502 = var1;
      return this;
   }

   public Class_0794 method_107(boolean var1) {
      this.field_2503 = var1;
      return this;
   }

   public Class_0794 method_374(boolean var1) {
      this.field_2504 = var1;
      return this;
   }

   public Class_0794 method_425(boolean var1) {
      this.field_2505 = var1;
      return this;
   }

   public Class_0794 method_101(int var1) {
      this.field_2506 = var1;
      return this;
   }

   public Class_0794 method_33(float var1) {
      this.field_2507 = var1 * var1;
      return this;
   }
}
