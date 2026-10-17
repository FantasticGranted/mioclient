package me.mioclient.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.ExplosionEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.module.movement.VelocityModule;
import me.mioclient.module.player.SpeedMineModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.PosRot;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.world.phys.AABB;

public class Class_0552 extends VelocityBehavior {
   public static final SpeedMineModule field_1756 = Hub.field_2595.method_78(SpeedMineModule.class);
   public static final AbstractModule_28 field_1757 = Hub.field_2595.method_78(AbstractModule_28.class);
   public final Timer field_1758 = new Timer();
   public final List<Long> field_1759 = Collections.synchronizedList(new ArrayList<>());
   public boolean field_54;
   public float field_1760;
   public float field_1761;

   public Class_0552(VelocityModule var1) {
      super(var1);
   }

   @Override
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundSetEntityMotionPacket var2 && var2.id() == mc.player.getId() && !this.method_581()) {
         var1.method_463();
      }

      if (var1.method_127() instanceof ClientboundPlayerPositionPacket) {
         this.field_1759.add(System.currentTimeMillis());
      }
   }

   @Override
   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundMovePlayerPacket var2 && var2.hasRotation()) {
         this.field_1760 = var2.getYRot(0.0F);
         this.field_1761 = var2.getXRot(0.0F);
      }
   }

   @Override
   public void method_2(ExplosionEvent var1) {
      boolean var2 = var1.method_398() != 0.0F || var1.method_399() != 0.0F || var1.method_400() != 0.0F;
      if (var2 && this.field_88.explosions.getValue() && !this.method_581()) {
         var1.method_52(0.0F);
         var1.method_185(0.0F);
         var1.method_92(0.0F);
      }
   }

   @Override
   public void method_40() {
      this.field_54 = BlockPos.betweenClosedStream(mc.player.getBoundingBox()).anyMatch(var0 -> !mc.level.isEmptyBlock(var0));
      if (mc.player.tickCount > 20 && !this.method_581()) {
         this.field_1759.removeIf(var0 -> System.currentTimeMillis() > var0 + 1000L);
         if (this.field_1759.size() > 2) {
            this.field_1758.reset();
            this.field_1759.clear();
         } else {
            this.method_578();
         }
      }
   }

   public void method_578() {
      BlockPos var1 = this.method_579();
      if (var1 != null) {
         PacketSender.method_2(
            new PosRot(
               mc.player.getX(), mc.player.getY(), mc.player.getZ(), this.field_1760, this.field_1761, mc.player.onGround()
            , false)
         );
         PacketSender.method_2(new ServerboundPlayerActionPacket(Action.STOP_DESTROY_BLOCK, var1, Direction.DOWN));
      }
   }

   public BlockPos method_579() {
      return BlockPos.betweenClosedStream(mc.player.getBoundingBox())
         .<BlockPos>map(BlockPos::immutable)
         .filter(BlockUtil::method_14)
         .max(Comparator.comparing(this::method_580))
         .orElse(null);
   }

   public float method_580(BlockPos var1) {
      AABB var2 = new AABB(var1);
      return (float)mc.player.getBoundingBox().intersect(var2).getSize();
   }

   public boolean method_581() {
      boolean var1 = this.method_579() == null && !this.field_54;
      return mc.player.isFallFlying() || PlayerUtil.method_5(mc.player) || var1 || !this.field_1758.method_9(200L);
   }
}
