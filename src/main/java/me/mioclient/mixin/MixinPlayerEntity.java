package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.authlib.GameProfile;
import me.mioclient.Hub;
import me.mioclient.api.EntityInfoAccess;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.RelationType;
import me.mioclient.enum_.PreType;
import me.mioclient.event.AttackEntityEvent;
import me.mioclient.event.SafeWalkEvent;
import me.mioclient.internal.FakePlayerEntity;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.module.exploit.ReachModule;
import me.mioclient.module.movement.SprintModule;
import me.mioclient.module.player.ScaffoldModule;
import me.mioclient.record.SocialEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Player.class})
public abstract class MixinPlayerEntity extends LivingEntity implements EntityInfoAccess {
   private static final ScaffoldModule scaffold = Hub.field_2595.method_78(ScaffoldModule.class);
   private static final SprintModule sprint = Hub.field_2595.method_78(SprintModule.class);
   private static final ReachModule reach = Hub.field_2595.method_78(ReachModule.class);
   @Unique
   private long mio$lastEatingTime;
   @Unique
   private RelationType mio$role;
   @Unique
   private boolean mio$isNextToWall;

   protected MixinPlayerEntity(EntityType<? extends LivingEntity> var1, Level var2) {
      super(var1, var2);
   }

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void initHook(Level var1, GameProfile var2, CallbackInfo var3) {
      for (SocialEntry var7 : Hub.field_2603.getRegistry()) {
         if (var2.name().equalsIgnoreCase(var7.method_228())) {
            this.mio$setRole(var7.getRelation());
            break;
         }
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   private void tick(CallbackInfo var1) {
      this.mio$isNextToWall = !this.level().noCollision(this.getBoundingBox().inflate(1.0E-4, 0.0, 1.0E-4));
      if (!this.isUsingItem() || this.method_2(this.getUseItem(), this) > 1.0F) {
         this.mio$lastEatingTime = System.currentTimeMillis();
      }
   }

   @Inject(
      method = {"isStayingOnGroundSurface"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void clipAtLedgeHook(CallbackInfoReturnable<Boolean> var1) {
      if (MinecraftAccess.mc.player != null) {
         SafeWalkEvent var2 = new SafeWalkEvent();
         MinecraftAccess.lookups.method_36(var2);
         if (var2.method_464()) {
            var1.setReturnValue(true);
         }
      }
   }

   @ModifyExpressionValue(
      method = {"maybeBackOffFromEdge"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/player/Player;maxUpStep()F"
      )}
   )
   private float clipAtLedgeHook2(float var1) {
      if (scaffold.isToggled() && scaffold.safeWalk.getValue()) {
         Item var2 = MinecraftAccess.mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
         if (!(var2 instanceof BlockItem)) {
            return var1;
         } else {
            VoxelShape var3 = ((BlockItem)var2).getBlock().defaultBlockState().getCollisionShape(MinecraftAccess.mc.level, BlockPos.ZERO);
            return var3 != null && !var3.isEmpty() ? (float)var3.bounds().getYsize() : var1;
         }
      } else {
         return var1;
      }
   }

   @Inject(
      method = {"attack"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void attackHookPre(Entity var1, CallbackInfo var2) {
      AttackEntityEvent var3 = new AttackEntityEvent(PreType.PRE, var1);
      MinecraftAccess.lookups.method_36(var3);
      if (var3.method_464()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"attack"},
      at = {@At("TAIL")}
   )
   private void attackHookPost(Entity var1, CallbackInfo var2) {
      AttackEntityEvent var3 = new AttackEntityEvent(PreType.POST, var1);
      MinecraftAccess.lookups.method_36(var3);
   }

   @WrapWithCondition(
      method = {"causeExtraKnockback"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/player/Player;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
      )}
   )
   private boolean attackHook(Player var1, Vec3 var2) {
      return !sprint.isToggled() || !sprint.keep.getValue();
   }

   @WrapWithCondition(
      method = {"causeExtraKnockback"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"
      )}
   )
   private boolean attack(LivingEntity var1, double var2, double var4, double var6) {
      return !((Object)this instanceof FakePlayerEntity) || var1 != MinecraftAccess.mc.player;
   }

   @Inject(
      method = {"entityInteractionRange"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getEntityInteractionRangeHook(CallbackInfoReturnable<Double> var1) {
      if (reach.isToggled()) {
         var1.setReturnValue(reach.modifier.getValue().doubleValue());
      }
   }

   @Inject(
      method = {"blockInteractionRange"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getBlockInteractionRangeHook(CallbackInfoReturnable<Double> var1) {
      if (reach.isToggled()) {
         var1.setReturnValue(reach.modifier.getValue().doubleValue());
      }
   }

   @Inject(
      method = {"isSecondaryUseActive"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void shouldCancelInteractionHook(CallbackInfoReturnable<Boolean> var1) {
      if (InteractionUtil.field_3196) {
         var1.setReturnValue(true);
      }
   }

   @Override
   public long mio$getLastEatingTime() {
      return this.mio$lastEatingTime;
   }

   @Override
   public RelationType mio$getRole() {
      return this.mio$role;
   }

   @Override
   public void mio$setRole(RelationType var1) {
      this.mio$role = var1;
   }

   @Override
   public boolean mio$isNextToWall() {
      return this.mio$isNextToWall;
   }
}
