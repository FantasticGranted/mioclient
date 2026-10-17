package me.mioclient.internal;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import me.mioclient.api.MinecraftAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Class_1288 implements MinecraftAccess {
   public final Map<String, Integer> field_4169 = new TreeMap<>(Comparator.<String, String>comparing(var0 -> (String)var0).reversed());
   public final AABB field_4170;
   public AABB field_4171;
   public float field_4172;

   public Class_1288(ItemEntity var1) {
      super();
      float var2 = mc.getDeltaTracker().getGameTimeDeltaPartialTick(true);
      this.field_4170 = AABB.ofSize(
         var1.getPosition(var2),
         5.0,
         5.0,
         5.0
      );
      this.field_4171 = BoxUtil.method_2(var1, var2);
      this.method_2(var1);
   }

   public AABB method_370() {
      return this.field_4170;
   }

   public AABB method_1151() {
      return this.field_4171;
   }

   public boolean method_2(ItemEntity var1) {
      if (this.field_4170.intersects(var1.getBoundingBox())) {
         float var2 = mc.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         String var3 = var1.getName().getString();
         this.field_4169.compute(var3, (var1x, var2x) -> var1.getItem().getCount() + (var2x == null ? 0 : var2x));
         this.field_4172 = Math.max(FontRenderer.field_3143.method_221(method_5(var3, this.field_4169.get(var3))), this.field_4172);
         Vec3 var4 = new Vec3(var1.getX() - var1.xo, var1.getY() - var1.yo, var1.getZ() - var1.zo);
         if (var4.lengthSqr() < 0.0001) {
            this.field_4171 = this.field_4171.minmax(BoxUtil.method_2(var1, var2));
         }

         return true;
      } else {
         return false;
      }
   }

   public Map<String, Integer> method_1152() {
      return this.field_4169;
   }

   public float method_1153() {
      return this.field_4172;
   }

   public static String method_5(String var0, int var1) {
      return var1 <= 1 ? var0 : new PlaceholderText().method_2(var1).method_2((Object)var0).method_9("\u0001 x\u0001");
   }
}
