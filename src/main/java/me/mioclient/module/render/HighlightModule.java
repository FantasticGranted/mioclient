package me.mioclient.module.render;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.event.Event_3;
import me.mioclient.event.BlockRenderEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.exploit.ReachModule;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import me.mioclient.nick.Settings;

public class HighlightModule extends Module {
   public static final ReachModule reach = Hub.field_2595.method_78(ReachModule.class);
   public Setting<Boolean> complex;
   public Setting<Boolean> unreachable;
   public Setting<Float> lineWidth;
   public Setting<Color> color;
   public Setting<Color> fillColor;

   public HighlightModule() {
      super("Highlight", "Highlights the block you're looking at.", Category.RENDER, "blockhighlight");
      Settings.initialize(this);
      this.setDrawn(false);
      this.unreachable.method_5(var0 -> reach.isToggled() && (reach.liquidPlace.getValue() || reach.airPlace.getValue()));
   }

   @Subscribe
   public void method_2(BlockRenderEvent var1) {
      if (!this.method_75()) {
         var1.method_463();
      }
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      if (!this.method_535()) {
         BlockPos var2 = this.method_73();
         if (var2 != null) {
            BlockState var3 = mc.level.getBlockState(var2);
            if (this.method_74() || !(var3.getBlock() instanceof AirBlock)) {
               VoxelShape var4 = var3.getShape(mc.level, var2);
               if (var4.isEmpty()) {
                  if (this.method_74()) {
                     this.method_2(var1.method_10(), Shapes.block().bounds(), var2);
                  }
               } else if (!this.method_75()) {
                  if (this.complex.getValue()) {
                     for (AABB var6 : var4.toAabbs()) {
                        this.method_2(var1.method_10(), var6, var2);
                     }
                  } else {
                     this.method_2(var1.method_10(), var4.bounds(), var2);
                  }
               }
            }
         }
      }
   }

   public BlockPos method_73() {
      if (reach.isToggled() && reach.ghostHand.getValue()) {
         BlockHitResult var1 = reach.method_961();
         if (var1 != null && var1.getType() != Type.MISS && var1.getBlockPos() != null) {
            return reach.method_961().getBlockPos();
         }
      }

      if (!this.method_74()) {
         if (mc.hitResult instanceof BlockHitResult var4 && var4.getType() != Type.MISS) {
            return var4.getBlockPos();
         }

         return null;
      } else {
         return ReachModule.method_960() instanceof BlockHitResult var2 ? var2.getBlockPos() : null;
      }
   }

   public void method_2(PoseStack var1, AABB var2, BlockPos var3) {
      BoxRenderer.method_5(var1, var2.move(var3).inflate(0.002), this.fillColor.getValue());
      BoxRenderer.method_2(var1, var2.move(var3).inflate(0.002), this.color.getValue(), this.lineWidth.getValue());
   }

   public boolean method_74() {
      return reach.isToggled()
         && reach.airPlace.getValue()
         && (mc.player.getMainHandItem().getItem() instanceof BlockItem || mc.player.getOffhandItem().getItem() instanceof BlockItem);
   }

   public boolean method_75() {
      return this.unreachable.getValue() && this.unreachable.method_176();
   }
}
