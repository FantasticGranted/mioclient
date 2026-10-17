package me.mioclient.module.abstract_;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.api.AttackTrackingAccess;
import me.mioclient.event.Event_50;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.FakePlayerEntity;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.module.Category;
import me.mioclient.setting.BooleanSetting;
import me.mioclient.setting.CustomSetting3;
import me.mioclient.setting.Setting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class AbstractModule_8 extends AbstractModule_32 {
   public Setting<Float> range = this.add(
      new CustomSetting3<>("Range", 5F, 1F, 8F).method_22("m")
   );
   public Setting<Boolean> sequential = this.add(new BooleanSetting("Sequential", false));
   public Setting<Boolean> targets = this.add(new BooleanSetting("Targets", true).method_220());
   public Setting<Boolean> head = this.add(new BooleanSetting("Head", true).method_2(this.targets));
   public Setting<Boolean> face = this.add(new BooleanSetting("Face", true).method_2(this.targets));
   public Setting<Boolean> legs = this.add(new BooleanSetting("Legs", false).method_2(this.targets));
   public Setting<Boolean> antiStep = this.add(new BooleanSetting("AntiStep", false));
   public Setting<Boolean> ignoreNakeds = this.add(new BooleanSetting("IgnoreNakeds", false));
   public Setting<Boolean> onlyObby = this.add(new BooleanSetting("OnlyObby", false), this.raytrace);
   public Player field_2290;

   public AbstractModule_8(String var1, String var2, Category var3) {
      super(var1, var2, var3);
   }

   public AbstractModule_8() {
      super("AutoTrap", "Traps your enemies with obby.", Category.COMBAT);
      Settings.initialize(this);
      this.method_5(true);
      this.method_185();
      this.jump.method_39(false);
   }

   @Override
   public String getInfo() {
      return this.field_2290 != null ? this.field_2290.getGameProfile().name() : null;
   }

   @Override
   public List<BlockPos> method_17() {
      this.field_2290 = this.method_691();
      if (this.field_2290 == null
         || Math.hypot(this.field_2290.getX() - this.field_2290.xo, this.field_2290.getZ() - this.field_2290.zo)
               * 2e+01
               * 3.6
            > 2e+01) {
         return Collections.emptyList();
      } else if (Math.abs(this.field_2290.yo - this.field_2290.getY()) > 0.6) {
         return Collections.emptyList();
      } else {
         Vec3 var1 = this.method_14(this.field_2290);
         List<BlockPos> var2 = this.method_2(new ArrayList<>(), BlockPos.containing(var1), 0);
         var2.removeIf(this::method_157);
         var2.sort(Comparator.comparing(var0 -> -var0.getY()));
         if (this.raytrace.getValue()) {
            var2.sort(Comparator.comparing(var0 -> -mc.player.position().distanceTo(var0.getCenter())));
         }

         return var2;
      }
   }

   @Override
   public int method_34() {
      return this.onlyObby.getValue() ? InventoryUtil.method_5(Items.OBSIDIAN) : super.method_34();
   }

   @Subscribe
   public void method_2(Event_50 var1) {
      if (this.sequential.getValue()) {
         this.method_33();
      }
   }

   public List<BlockPos> method_2(List<Integer> var1, BlockPos var2, int var3) {
      ArrayList var4 = new ArrayList();
      var1.add(var2.hashCode());

      for (Vec3 var6 : this.method_2(var2, var3, this.field_2290.getBbHeight())) {
         BlockPos var7 = var2.offset((int)var6.x, (int)var6.y, (int)var6.z);
         if (mc.level.getBlockState(var7).canBeReplaced()) {
            if (!mc.level.getEntitiesOfClass(Player.class, new AABB(var7), var0 -> true).isEmpty()
               && !var1.contains(var7.hashCode())
               && var6.y == (double)var3) {
               var4.addAll(this.method_2(var1, var7, var3));
            } else {
               Direction var8 = this.method_2(var7, this.strictDirection.getValue(), var4);
               if (var8 == null && !this.airPlace.getValue()) {
                  BlockPos var9 = this.method_2(var7, var4);
                  if (var9 != null) {
                     var4.add(var9);
                  }
               }

               var4.add(var7);
            }
         }
      }

      return var4;
   }

   public BlockPos method_2(BlockPos var1, List<BlockPos> var2) {
      return Arrays.stream(Direction.values())
         .<BlockPos>map(var1::relative)
         .sorted(Comparator.comparing(this::method_294))
         .filter(var2x -> this.method_2(var2x, this.strictDirection.getValue(), var2) != null && InteractionUtil.method_9(var2x, true, this.attack.getValue()))
         .findFirst()
         .orElse(null);
   }

   public double method_294(BlockPos var1) {
      return this.raytrace.getValue() ? -mc.player.position().distanceToSqr(var1.getCenter()) : (double)var1.getY();
   }

   public boolean method_157(BlockPos var1) {
      if (this.field_2290 == null) {
         return true;
      } else {
         AABB var2 = new AABB(var1.above());

         for (Entity var4 : mc.level.entitiesForRendering()) {
            if (var4 instanceof AttackTrackingAccess var5
               && !var5.isMioAttacked()
               && var4.getBoundingBox().intersects(var2)
               && BlockPos.containing(var4.position()).equals(var1.above())) {
               return true;
            }
         }

         return mc.player.getEyePosition().distanceTo(var1.getCenter()) > (double)this.range.getValue().floatValue()
            ? true
            : !this.legs.getValue() && var1.getY() == this.field_2290.blockPosition().getY() && !mc.level.getBlockState(var1.above()).canBeReplaced();
      }
   }

   public Direction method_2(BlockPos var1, boolean var2, List<BlockPos> var3) {
      for (Direction var7 : Direction.values()) {
         BlockPos var8 = var1.relative(var7);
         if ((!mc.level.getBlockState(var8).canBeReplaced() || var3.contains(var8))
            && (!var2 || InteractionUtil.method_39(var8).contains(var7.getOpposite()))) {
            return var7;
         }
      }

      return null;
   }

   public List<Vec3> method_2(BlockPos var1, int var2, float var3) {
      ArrayList var4 = new ArrayList();
      if (this.method_692()) {
         var4.add(new Vec3(0.0, (double)((float)var2 + var3 + 1F), 0.0));
         Iterator var5 = this.method_9(var2, var3).iterator();
         if (var5.hasNext()) {
            Vec3 var6 = (Vec3)var5.next();
            BlockPos var7 = BlockPos.containing(var6).offset(var1);
            if (mc.level.getBlockState(var7).canBeReplaced()
               && InteractionUtil.method_2(var7, this.strictDirection.getValue(), this.raytrace.getValue()) == null) {
               if (var6.y() > (double)var2) {
                  var4.add(new Vec3(-1.0, (double)(var2 + 1), 0.0));
               } else {
                  var4.add(new Vec3(-1.0, (double)var2, 0.0));
               }
            }
         }
      } else if (this.head.getValue()) {
         var4.add(new Vec3(0.0, (double)((float)var2 + var3 + 1F), 0.0));
      }

      for (Vec3 var9 : this.method_9(var2, var3)) {
         if (!(var9.y() > (double)var3)
            && (this.face.getValue() || !(var9.y() > (double)var2))
            && (this.legs.getValue() || var9.y() != (double)var2)) {
            var4.add(var9);
         }
      }

      if (this.antiStep.getValue()) {
         var4.add(new Vec3(0.0, (double)((float)var2 + var3 + 2F), 0.0));
      }

      return var4;
   }

   public List<Vec3> method_9(int var1, float var2) {
      return Arrays.asList(
         new Vec3(0.0, (double)((float)var1 + var2 + 1F), 0.0),
         new Vec3(1.0, (double)var1, 0.0),
         new Vec3(0.0, (double)var1, 1.0),
         new Vec3(0.0, (double)var1, -1.0),
         new Vec3(-1.0, (double)var1, 0.0),
         new Vec3(1.0, (double)(var1 + 1), 0.0),
         new Vec3(0.0, (double)(var1 + 1), 1.0),
         new Vec3(0.0, (double)(var1 + 1), -1.0),
         new Vec3(-1.0, (double)(var1 + 1), 0.0)
      );
   }

   public boolean method_36(Player var1) {
      if (var1.isDeadOrDying() || !var1.isAlive() || var1 == mc.player) {
         return false;
      } else if (Hub.field_2603.getId(var1)) {
         return false;
      } else {
         return this.ignoreNakeds.getValue() && !PlayerUtil.method_29((LivingEntity)var1)
            ? false
            : var1.distanceTo(mc.player) <= this.range.getValue() && var1.onGround();
      }
   }

   public Vec3 method_14(Player var1) {
      Vec3 var2 = var1.position();
      if (!(this.field_2290 instanceof FakePlayerEntity) && !(this.field_2290 instanceof LocalPlayer)) {
         var2 = this.field_2290.getInterpolation().position();
      }

      return var2;
   }

   public Player method_691() {
      return mc.level
         .players()
         .stream()
         .filter(this::method_36)
         .min(Comparator.comparing(var0 -> Mth.degreesDifferenceAbs(mc.player.getYRot(), RotationManager.method_14(var0)[0])))
         .orElse(null);
   }

   public boolean method_692() {
      return this.head.getValue() && !this.face.getValue();
   }

   @Override
   public int method_52() {
      return 150;
   }
}
