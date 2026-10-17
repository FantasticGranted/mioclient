package me.mioclient.module.render;

import java.awt.Color;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import me.mioclient.Hub;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.DisconnectEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0032;
import me.mioclient.internal.SoundId;
import me.mioclient.internal.Timer;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.internal.BoxUtil;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.Colors;
import me.mioclient.internal.BlockUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_21;
import me.mioclient.setting.Setting;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor.Brightness;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import me.mioclient.nick.Settings;

public class SearchModule extends Module {
   public static AbstractModule_21 field_521 = Hub.field_2595.method_78(AbstractModule_21.class);
   public static final MutableBlockPos field_2076 = new MutableBlockPos();
   public Setting<Set<Block>> whitelist;
   public Setting<Set<EntityType<?>>> entityTypes;
   public Setting<Boolean> entities;
   public Setting<Boolean> ignoreNeg;
   public Setting<Boolean> fill;
   public Setting<Float> fillOpacity;
   public Setting<Boolean> outline;
   public Setting<Boolean> tracers;
   public Setting<Float> tracerOpacity;
   public Setting<Boolean> sound;
   public Setting<SoundId> type;
   public Setting<Float> volume;
   public Setting<Boolean> ignoreNatural;
   public Setting<Boolean> dungeon;
   public Setting<Boolean> fortress;
   public Setting<Boolean> bastion;
   public Setting<Boolean> ancientCity;
   public Setting<Boolean> trialChambers;
   public final Timer field_2095;
   public final Set<BlockPos> field_2096;
   public final Class_0032 field_2097;
   public boolean field_2098;

   public SearchModule() {
      super("Search", "Helps finding whitelisted stuff.", Category.RENDER);
      Settings.initialize(this);
      this.field_2095 = new Timer();
      this.field_2096 = Collections.synchronizedSet(new HashSet<>());
      this.field_2097 = new Class_0032(
         1,
         (var1, var2) -> {
            if (this.field_2096.size() <= 100000) {
               Set var3 = this.whitelist.getValue();

               for (int var4 = 0; var4 < 16; var4++) {
                  for (int var5 = this.ignoreNeg.getValue() ? 0 : mc.level.getMinY(); var5 < mc.level.getMaxY(); var5++) {
                     for (int var6 = 0; var6 < 16; var6++) {
                        field_2076.set(var1.getMinBlockX() + var4, var5, var1.getMinBlockZ() + var6);
                        BlockState var7 = var2.getBlockState(field_2076);
                        if (!var7.isAir()
                           && (
                              !this.ignoreNatural.getValue()
                                 || !BlockUtil.method_2(
                                    var2,
                                    field_2076,
                                    this.fortress.getValue(),
                                    this.bastion.getValue(),
                                    this.ancientCity.getValue(),
                                    this.dungeon.getValue(),
                                    this.trialChambers.getValue()
                                 )
                           )
                           && var3.contains(var7.getBlock())) {
                           this.field_2098 = true;
                           this.field_2096.add(field_2076.immutable());
                        }
                     }
                  }
               }
            }
         },
         (var1, var2) -> this.field_2096
               .removeIf(
                  var1x -> var1x.getX() >= var1.getMinBlockX()
                        && var1x.getX() <= var1.getMaxBlockX()
                        && var1x.getZ() >= var1.getMinBlockZ()
                        && var1x.getZ() <= var1.getMaxBlockZ()
               ),
         (var1, var2) -> {
            if (this.whitelist.getValue().contains(var2.getBlock())) {
               if (var2.isAir()) {
                  return;
               }

               if (this.field_2096.add(var1)) {
                  this.field_2098 = true;
               }
            } else {
               this.field_2096.remove(var1);
            }
         }
      );
      this.whitelist.method_9(() -> {
         if (mc.level != null && this.field_2097.method_51() != null) {
            this.field_2096.clear();
            this.field_2097.method_50();
         }
      });
   }

   @Override
   public void onEnable() {
      this.field_2098 = false;
      this.field_2097.method_47();
      if (!this.method_535()) {
         this.field_2097.method_50();
      }
   }

   @Override
   public void onDisable() {
      this.field_2096.clear();
      this.field_2097.method_48();
   }

   @Subscribe
   public void method_2(DisconnectEvent var1) {
      this.method_660();
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundRespawnPacket) {
         this.method_660();
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (this.sound.getValue() && this.field_2098) {
         this.field_2098 = false;
         if (this.field_2095.method_9(100L)) {
            Hub.field_2606.method_2(this.type.getValue()).method_230(this.volume.getValue());
            this.field_2095.reset();
         }
      }
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      if (this.entities.getValue()) {
         for (Entity var3 : mc.level.entitiesForRendering()) {
            if (this.entityTypes.getValue().contains(var3.getType())) {
               AABB var4 = BoxUtil.method_2(var3, var1.method_880());
               Color var5 = this.method_251(var3);
               field_521.scheme
                  .getValue()
                  .method_5(
                     () -> {
                        if (RotationManager.method_4(var4)) {
                           if (this.fill.getValue()) {
                              BoxRenderer.method_5(
                                 var1.method_10(), var4, Colors.method_9(var5, (int)(255F * this.fillOpacity.getValue()))
                              );
                           }

                           if (this.outline.getValue()) {
                              BoxRenderer.method_2(var1.method_10(), var4, var5, 1F);
                           }
                        }

                        if (this.tracers.getValue()) {
                           Camera var4x = mc.gameRenderer.getMainCamera();
                           Vec3 var5x = new Vec3(0.0, 0.0, 1.0)
                              .xRot(-((float)Math.toRadians((double)var4x.xRot())))
                              .yRot(-((float)Math.toRadians((double)var4x.yRot())))
                              .add(mc.getEntityRenderDispatcher().camera.position());
                           RenderUtil.field_2672
                              .method_2(
                                 var1.method_10(),
                                 var5x,
                                 var4.getCenter(),
                                 Colors.method_9(var5, (int)(255F * this.tracerOpacity.getValue()))
                              );
                        }
                     }
                  );
            }
         }
      }

      synchronized (this.field_2096) {
         for (BlockPos var13 : this.field_2096) {
            BlockState var14 = mc.level.getBlockState(var13);
            if (!var14.isAir() && !var14.is(Blocks.AIR)) {
               VoxelShape var6 = var14.getShape(mc.level, var13);
               if (var6.isEmpty()) {
                  var6 = Shapes.box(
                     0.0,
                     0.0,
                     0.0,
                     1.0,
                     1.0,
                     1.0
                  );
               }

               AABB var7 = var6.bounds().move(var13);
               Color var8 = this.method_9(var14, var13);
               field_521.scheme
                  .getValue()
                  .method_5(
                     () -> {
                        if (RotationManager.method_4(var7)) {
                           if (this.fill.getValue()) {
                              BoxRenderer.method_5(
                                 var1.method_10(), var7, Colors.method_9(var8, (int)(255F * this.fillOpacity.getValue()))
                              );
                           }

                           if (this.outline.getValue()) {
                              BoxRenderer.method_2(var1.method_10(), var7, var8, 1F);
                           }
                        }

                        if (this.tracers.getValue()) {
                           Camera var5x = mc.gameRenderer.getMainCamera();
                           Vec3 var6x = new Vec3(0.0, 0.0, 1.0)
                              .xRot(-((float)Math.toRadians((double)var5x.xRot())))
                              .yRot(-((float)Math.toRadians((double)var5x.yRot())))
                              .add(mc.getEntityRenderDispatcher().camera.position());
                           RenderUtil.field_2672
                              .method_2(
                                 var1.method_10(),
                                 var6x,
                                 Vec3.atCenterOf(var13),
                                 Colors.method_9(var8, (int)(255F * this.tracerOpacity.getValue()))
                              );
                        }
                     }
                  );
            }
         }
      }
   }

   public void method_660() {
      this.field_2097.method_49();
      this.field_2096.clear();
   }

   public Color method_9(BlockState var1, BlockPos var2) {
      if (var1.getBlock() == Blocks.NETHER_PORTAL) {
         return new Color(144, 28, 255);
      } else if (var1.getBlock() == Blocks.ENDER_CHEST) {
         return new Color(125, 40, 180);
      } else {
         int var3 = var1.getMapColor(mc.level, var2).calculateARGBColor(Brightness.HIGH);
         if (var1.getBlock() instanceof BedBlock var4) {
            var3 = var4.getColor().getMapColor().calculateARGBColor(Brightness.HIGH);
         }

         int var7 = var3 >> 16 & 0xFF;
         int var8 = var3 >> 8 & 0xFF;
         int var6 = var3 & 0xFF;
         return new Color(var6, var8, var7);
      }
   }

   public Color method_251(Entity var1) {
      Color var2 = Color.GRAY;
      if (var1 instanceof AbstractMinecart) {
         var2 = Color.blue;
      }

      if (EntityUtil.method_9(var1)) {
         var2 = Color.RED;
      }

      if (var1 instanceof Player var3) {
         var2 = Hub.field_2603.method_9(var3.getName().getString(), var2);
      }

      if (var1 instanceof Animal || var1 instanceof AbstractFish) {
         var2 = Color.GREEN;
      }

      return var2;
   }
}
