package me.mioclient.module.abstract_;

import java.util.ArrayList;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.enum_.Class_0388;
import me.mioclient.module.Category;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import me.mioclient.nick.Settings;

public class AbstractModule_16 extends AbstractModule_32 {
   public Setting<Class_0388> offsets;
   public Setting<Boolean> corners;
   public Setting<Float> blockDamage;

   public AbstractModule_16() {
      super("Blocker", "Blocks certain offsets to prevent crystal damage.", Category.COMBAT);
      Settings.initialize(this);
      this.jump.method_39(false);
   }

   @Override
   public String getInfo() {
      return this.offsets.getValue().getName();
   }

   @Override
   public List<BlockPos> method_17() {
      ArrayList var1 = new ArrayList();

      for (Vec3i var5 : this.offsets.getValue().method_440()) {
         BlockPos var6 = BlockPos.containing(mc.player.position()).offset(var5);
         if (Hub.field_2629.method_251(var6) >= this.blockDamage.getValue()) {
            var1.add(var6);
         }
      }

      ArrayList var10 = new ArrayList();

      for (BlockPos var12 : (Iterable<BlockPos>)(Iterable<?>)(var1)) {
         for (Vec3i var8 : Class_0388.field_1258.method_440()) {
            if (var12.getY() != (int)mc.player.position().y()) {
               break;
            }

            BlockPos var9 = BlockPos.containing(mc.player.position());
            if (this.corners.getValue()) {
               if (!var12.offset(var8).equals(var9)) {
                  var10.add(var12.offset(var8));
               }
            } else if (var12.subtract(var9).equals(var8)) {
               var10.add(var12.offset(var8));
               break;
            }
         }

         if ((double)var12.getY() == mc.player.getY() + 3.0) {
            var10.add(var12.below());
         } else {
            var10.add(var12.above());
         }
      }

      return var10;
   }
}
