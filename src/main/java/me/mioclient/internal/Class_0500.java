package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.enum_.FloatType;
import me.mioclient.enum_.LinesType;

public class Class_0500 {
   public static Class_0385 field_1576;
   public static final PoseStack field_1577 = new PoseStack();

   public Class_0500() {
      super();
   }

   public static void init() {
      field_1576 = new Class_0385(LinesType.TRIANGLES, FloatType.VEC2);
      field_1576.method_430();
      field_1576.method_5(
         field_1576.method_9(-1.0, -1.0).method_431(),
         field_1576.method_9(-1.0, 1.0).method_431(),
         field_1576.method_9(1.0, 1.0).method_431(),
         field_1576.method_9(1.0, -1.0).method_431()
      );
      field_1576.method_433();
   }

   public static void method_528() {
      field_1576.method_5(field_1577);
   }

   public static void method_529() {
      field_1576.method_7(field_1577);
   }

   public static void method_434() {
      field_1576.method_434();
   }
}
