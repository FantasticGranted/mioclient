package me.mioclient.enum_;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.BlockUtil;
import me.mioclient.module.player.NukerModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;

/** Nuker area shape. */
public enum NukerShape implements Nameable {
   SPHERE("Sphere") {
      @Override
      public List<BlockPos> method_2(NukerModule var1) {
         return BlockUtil.method_2(MinecraftAccess.mc.player.position(), var1.range.getValue(), true);
      }
   },
   TUNNEL("Tunnel") {
      @Override
      public List<BlockPos> method_2(NukerModule var1) {
         ArrayList var2 = new ArrayList();

         for (int var3 = 0; (double)var3 <= Math.ceil((double)var1.range.getValue().floatValue()); var3++) {
            for (int var4 = 0; (double)var4 < Math.ceil((double)MinecraftAccess.mc.player.getBbHeight()); var4++) {
               BlockPos var5 = MinecraftAccess.mc.player.blockPosition();
               var5 = var5.above(var4);
               var5 = var5.relative(MinecraftAccess.mc.player.getDirection(), var3);
               var2.add(var5);
            }
         }

         return var2;
      }
   },
   CROSSHAIR("Crosshair") {
      @Override
      public List<BlockPos> method_2(NukerModule var1) {
         return MinecraftAccess.mc.hitResult instanceof BlockHitResult var2 ? Collections.singletonList(var2.getBlockPos()) : Collections.emptyList();
      }
   };

   public final String displayName;

    NukerShape(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public abstract List<BlockPos> method_2(NukerModule var1);
}
