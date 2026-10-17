package me.mioclient.internal;

import java.awt.Color;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.player.SpeedMineModule;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class Class_0277 implements MinecraftAccess {
   public final BlockPos field_875;
   public float field_876;
   public float field_877;

   public Class_0277(BlockPos var1) {
      super();
      this.field_875 = var1;
   }

   public void method_2(SpeedMineModule var1, HudRenderEvent var2) {
      VoxelShape var3 = mc.level.getBlockState(this.field_875).getShape(mc.level, this.field_875);
      if (!var3.isEmpty() && BlockUtil.method_3(this.field_875)) {
         float var4 = this.method_200();
         Color[] var5 = var1.field_4012.getValue().method_2(var1, var4);
         AABB var6 = var1.renderMode.getValue().method_2(var1, var3.bounds(), var4).move(this.field_875);
         BoxRenderer.method_5(var2.method_10(), var6, var5[0]);
         BoxRenderer.method_2(var2.method_10(), var6, var5[1], var1.lineWidth.getValue());
      }
   }

   public BlockPos method_111() {
      return this.field_875;
   }

   public float method_200() {
      return Mth.clamp(Mth.lerp(RenderUtil.method_776(), this.field_876, this.field_877), 0.0F, 1F);
   }

   public void method_310(float var1) {
      this.method_263(this.field_877 + var1);
   }

   public void method_263(float var1) {
      this.field_876 = this.field_877;
      this.field_877 = var1;
   }
}
