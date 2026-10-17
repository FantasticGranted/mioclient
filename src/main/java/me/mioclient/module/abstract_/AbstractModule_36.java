package me.mioclient.module.abstract_;

import me.mioclient.Hub;
import me.mioclient.enum_.RotationMode;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.BlockUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Rot;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class AbstractModule_36 extends Module {
   public static final Vec2[] field_661 = new Vec2[]{
      new Vec2(-1F, 1F),
      new Vec2(0.0F, 1F),
      new Vec2(1F, 1F),
      new Vec2(-1F, 0.0F),
      new Vec2(1F, 0.0F),
      new Vec2(-1F, 0.0F),
      new Vec2(0.0F, 0.0F),
      new Vec2(1F, 0.0F)
   };
   public Setting<Boolean> always;
   public Setting<RotationMode> rotations;
   public Setting<Integer> keep;
   public Setting<Integer> delay;
   public Setting<Integer> offset;
   public Setting<Boolean> resolver;
   public Setting<Float> bruteOffset;
   public Setting<Integer> iterations;
   public Setting<Float> bruteStep;
   public Setting<Float> basePitch;
   public Setting<Boolean> backup;
   public Setting<Float> backUpPitch;
   public final Timer field_674;
   public int field_675;
   public float[] field_676;

   public AbstractModule_36() {
      super("RaytraceBypass", "Does magic to bypass NCP raytracing. \n%sPatched in newer NCP versions.".formatted(ChatFormatting.YELLOW), Category.EXPLOIT);
      Settings.initialize(this);
      this.field_674 = new Timer();
   }

   @Subscribe(
      method_800 = -1000
   )
   public void method_38(Event_7 var1) {
      if (this.field_676 != null && this.rotations.getValue() == RotationMode.MOTION) {
         Hub.field_2598.method_2(this.field_676, 1000);
         this.field_675++;
         if (this.field_675 > this.keep.getValue()) {
            this.field_676 = null;
         }
      }
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundUseItemOnPacket var2 && this.field_674.method_9((long)this.delay.getValue().intValue())) {
         this.method_9(var2.getHand(), var2.getHitResult());
      }

      if (var1.method_127() instanceof ServerboundPlayerActionPacket var4 && var4.getAction() == Action.START_DESTROY_BLOCK) {
         this.method_9(InteractionHand.MAIN_HAND, new BlockHitResult(var4.getPos().getCenter(), null, var4.getPos(), false));
      }
   }

   public void method_9(InteractionHand var1, BlockHitResult var2) {
      if ((this.always.getValue() || !BlockUtil.method_9(var2.getLocation()))
         && (!mc.player.isUsingItem() || !mc.player.getItemInHand(var1).is(Items.EXPERIENCE_BOTTLE))) {
         Object var3 = null;
         boolean var4 = false;
         if (this.resolver.getValue()) {
            float[] var5 = RotationManager.method_78(var2.getLocation().add(0.0, (double)this.offset.getValue().intValue(), 0.0));
            HitResult var6 = this.method_38(var5[0], var5[1]);
            if (!this.method_9(var6)) {
               var6 = this.method_105(RotationManager.method_78(var2.getLocation())[0] + 1.8e+02F);
            }

            if (!this.method_9(var6)) {
               var4 = true;
            }

            if (var4 && !this.backup.getValue()) {
               return;
            }

            if (var4 && this.backup.getValue()) {
               float var7 = RotationManager.method_78(var2.getLocation())[0];
               this.field_676 = new float[]{var7, this.backUpPitch.getValue()};
               this.method_38(this.field_676);
               return;
            }

            var3 = var6.getLocation();
         } else {
            var3 = var2.getLocation().add(0.0, (double)this.offset.getValue().intValue(), 0.0);
         }

         if (var3 != null) {
            float[] var9 = RotationManager.method_78((Vec3)var3);
            this.field_676 = var9;
            this.method_38(var9);
         }
      }
   }

   public void method_38(float[] var1) {
      if (this.rotations.getValue() == RotationMode.PACKET) {
         mc.player.connection.send(new Rot(var1[0], var1[1], mc.player.onGround(), false));
      } else if (this.rotations.getValue() == RotationMode.CLIENT) {
         mc.player.setYRot(var1[0]);
         mc.player.setXRot(var1[1]);
      }

      this.field_675 = 1;
      this.field_674.reset();
   }

   public HitResult method_38(float var1, float var2) {
      Vec3 var3 = mc.player.getEyePosition(RenderUtil.method_776());
      float var4 = var2 * Class_0245.field_690;
      float var5 = -var1 * Class_0245.field_690;
      float var6 = Mth.cos(var5);
      float var7 = Mth.sin(var5);
      float var8 = Mth.cos(var4);
      float var9 = Mth.sin(var4);
      float var10 = 28F;
      Vec3 var11 = new Vec3((double)(var7 * var8), (double)(-var9), (double)(var6 * var8));
      Vec3 var12 = var3.add(var11.x * (double)var10, var11.y * (double)var10, var11.z * (double)var10);
      return mc.level.clip(new ClipContext(var3, var12, Block.OUTLINE, Fluid.NONE, mc.player));
   }

   public HitResult method_105(float var1) {
      for (float var2 = var1 - this.bruteStep.getValue(); var2 < var1 + this.bruteStep.getValue(); var2 += this.bruteStep.getValue()) {
         for (float var3 = -9e+01F; var3 < -this.basePitch.getValue(); var3 += this.bruteStep.getValue()) {
            HitResult var4 = this.method_78(var2, var3);
            if (this.method_9(var4)) {
               return var4;
            }
         }
      }

      return null;
   }

   public HitResult method_78(float var1, float var2) {
      HitResult var3 = this.method_38(var1, var2);
      if (!this.method_9(var3)) {
         return null;
      } else {
         for (Vec2 var7 : field_661) {
            for (int var8 = 0; var8 < this.iterations.getValue(); var8++) {
               if (!this.method_9(
                  this.method_38(var1 + var7.x * this.bruteOffset.getValue() * (float)var8, var2 + var7.y * this.bruteOffset.getValue() * (float)var8)
               )) {
                  return null;
               }
            }
         }

         return var3;
      }
   }

   public boolean method_9(HitResult var1) {
      return var1 == null ? false : var1.getType() == Type.MISS;
   }
}
