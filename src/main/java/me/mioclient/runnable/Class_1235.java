package me.mioclient.runnable;

import java.util.ArrayList;
import me.mioclient.internal.Class_0191;
import me.mioclient.record.SafetyBox;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;

public class Class_1235 implements Runnable {
   public static final MutableBlockPos field_3882 = new MutableBlockPos();
   public final Class_0191 field_3883;

   public Class_1235(Class_0191 var1) {
      super();
      this.field_3883 = var1;
   }

   @Override
   public void run() {
      while (true) {
         try {
            this.method_1080();
            Thread.sleep(50L);
         } catch (Throwable var2) {
         }
      }
   }

   public void method_1080() {
      if (Minecraft.getInstance() != null && Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
         ArrayList<SafetyBox> var1 = new ArrayList<>();
         BlockPos var2 = Minecraft.getInstance().player.blockPosition();

         for (int var3 = -16; var3 < 16; var3++) {
            for (int var4 = -16; var4 < 16; var4++) {
               for (int var5 = -16; var5 < 16; var5++) {
                  field_3882.set(var2.getX() + var3, var2.getY() + var4, var2.getZ() + var5);
                  SafetyBox var6 = Class_0191.method_9(field_3882, null);
                  if (var6 != null && !var1.stream().anyMatch(var1x -> var6.getBox().intersects(var1x.getBox()))) {
                     var1.add(var6);
                  }
               }
            }
         }

         this.field_3883.method_39(var1);
      }
   }
}
