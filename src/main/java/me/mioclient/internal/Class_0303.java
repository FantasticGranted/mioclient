package me.mioclient.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.DisconnectEvent;
import me.mioclient.event.SlotUpdateEvent;
import me.mioclient.event.Event_35;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.mixin.ducks.DuckAbstractBlock;
import me.mioclient.record.TimedBlock;
import me.mioclient.record.TimedBlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;

public class Class_0303 implements MinecraftAccess {
   public final List<BlockEntity> field_970 = Collections.synchronizedList(new ArrayList<>());
   public final Map<BlockPos, TimedBlock> field_971 = Collections.synchronizedMap(new HashMap<>());
   public final Set<BlockPos> field_972 = Collections.synchronizedSet(new HashSet<>());
   public final Set<BlockPos> field_973 = Collections.synchronizedSet(new HashSet<>());
   public final Set<TimedBlockState> field_974 = Collections.synchronizedSet(new HashSet<>());
   @Deprecated
   public BlockPos unconfirmedBreak = null;
   public ItemStack field_975;
   public ItemStack field_976;
   public final Timer field_977 = new Timer();
   public int field_978;

   public Class_0303() {
      super();
      lookups.method_14(this);
   }

   @Subscribe
   public void method_2(DisconnectEvent var1) {
      this.field_971.clear();
      this.field_972.clear();
      this.field_970.clear();
      this.field_974.clear();
   }

   @Subscribe(
      method_800 = 200
   )
   public void method_2(PostTickEvent var1) {
      this.method_346();
   }

   @Subscribe(
      method_800 = -200
   )
   public void method_2(PlayerTickEvent var1) {
      this.field_973.clear();
      if (this.field_977.method_9(150L)) {
         this.unconfirmedBreak = null;
      }

      this.field_972.removeIf(var0 -> !var0.closerThan(mc.player.blockPosition(), 128.0));
      synchronized (this.field_971) {
         ArrayList var3 = new ArrayList();

         for (Entry var5 : this.field_971.entrySet()) {
            if (System.currentTimeMillis() > ((TimedBlock)var5.getValue()).method_417() + 250L) {
               var3.add((BlockPos)var5.getKey());
            }
         }

         for (BlockPos var16 : (Iterable<BlockPos>)(Iterable<?>)(var3)) {
            this.field_971.remove(var16);
         }
      }

      synchronized (this.method_347()) {
         this.field_970.clear();
         this.field_970.addAll(BlockUtil.method_1070());
         this.field_970.removeIf(Objects::isNull);
         this.field_970.sort(Comparator.comparing(var0 -> mc.player.position().distanceToSqr(var0.getBlockPos().getCenter())));
      }

      synchronized (this.field_974) {
         for (TimedBlockState var15 : this.field_974) {
            if (var15.method_644()) {
               mc.level.setBlockAndUpdate(var15.getPos(), var15.getState());
            }
         }
      }

      this.field_974.removeIf(TimedBlockState::method_644);
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundUseItemOnPacket var2 && this.method_29(var2.getHand()).getItem() instanceof BlockItem var3) {
         BlockHitResult var7 = var2.getHitResult();
         BlockPos var5 = var7.getBlockPos().relative(var7.getDirection());
         this.field_971.put(var5, TimedBlock.method_31(var3.getBlock()));
         this.field_973.add(var5);
         this.method_346();
      }
   }

   @Subscribe
   public void method_2(SlotUpdateEvent var1) {
      this.method_346();
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundBlockUpdatePacket var2) {
         this.field_974.removeIf(var1x -> var1x.getPos().equals(var2.getPos()));
         if (var2.getBlockState().isAir()) {
            this.field_972.remove(var2.getPos());
            this.field_971.remove(var2.getPos());
         } else if (this.field_971.remove(var2.getPos()) != null) {
            this.field_972.add(var2.getPos());
         }
      }

      if (var1.method_127() instanceof ClientboundSectionBlocksUpdatePacket var4) {
         var4.runUpdates((var1x, var2x) -> this.field_974.removeIf(var1xx -> var1xx.getPos().equals(var1x)));
      }

      if (var1.method_127() instanceof ClientboundLoginPacket var5) {
         this.field_978 = var5.chunkRadius();
      }

      if (var1.method_127() instanceof ClientboundSetChunkCacheRadiusPacket var6) {
         this.field_978 = var6.getRadius();
      }
   }

   @Subscribe
   public void method_2(Event_35 var1) {
      if (var1.method_111() != null && mc.player != null) {
         if (this.field_971.containsKey(var1.method_111())) {
            TimedBlock var2 = this.field_971.get(var1.method_111());
            if (var2 == null || var2.getBlock() == null) {
               return;
            }

            AABB var3 = new AABB(var1.method_111());
            if (mc.player.getBoundingBox().intersects(var3) || !((DuckAbstractBlock)(Object)var2.getBlock()).isCollidable()) {
               return;
            }

            if (!mc.level
               .getEntitiesOfClass(
                  Entity.class,
                  var3,
                  var0 -> !(var0 instanceof ThrownExperienceBottle)
                        && !(var0 instanceof ItemEntity)
                        && !(var0 instanceof ExperienceOrb)
                        && !(var0 instanceof Arrow)
                        && !(var0 instanceof ThrownEnderpearl)
               )
               .isEmpty()) {
               return;
            }

            var1.method_9(var2.getBlock().defaultBlockState().getShape(mc.level, var1.method_111()));
         }

         if (RotationManager.method_513()) {
            if (Hub.field_2602.method_984().method_9(300L)) {
               if (var1.method_111().equals(this.unconfirmedBreak)) {
                  var1.method_9(Shapes.empty());
               }
            }
         }
      }
   }

   public void method_99(BlockPos var1) {
      this.field_974.add(TimedBlockState.of(var1));
      mc.gameMode.destroyBlock(var1);
   }

   public void method_100(BlockPos var1) {
      this.unconfirmedBreak = var1;
      this.field_977.reset();
   }

   public BlockPos method_344() {
      return this.unconfirmedBreak;
   }

   public Map<BlockPos, TimedBlock> method_345() {
      return this.field_971;
   }

   public ItemStack method_29(InteractionHand var1) {
      return var1 == InteractionHand.MAIN_HAND ? this.field_975 : this.field_976;
   }

   public void method_346() {
      this.field_975 = mc.player.getMainHandItem().copy();
      this.field_976 = mc.player.getOffhandItem().copy();
   }

   public List<BlockEntity> method_347() {
      return this.field_970;
   }

   public boolean method_101(BlockPos var1) {
      return this.field_972.contains(var1) || this.field_971.containsKey(var1);
   }

   public boolean method_105(BlockPos var1) {
      return !mc.level.getBlockState(var1).canBeReplaced() || this.field_973.contains(var1);
   }

   public int method_348() {
      return this.field_978;
   }
}
