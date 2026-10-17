package me.mioclient.module.abstract_;

import me.mioclient.Hub;
import me.mioclient.enum_.HorizontalDirection;
import me.mioclient.enum_.SettingMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.MovementInputEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.Class_0621;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.internal.VectorUtil;
import me.mioclient.mixin.ducks.DuckAbstractBlock;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.combat.AutoArmorModule;
import me.mioclient.module.movement.ElytraFlyModule;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import me.mioclient.internal.Inputs;
import net.minecraft.world.phys.Vec2;

public final class AbstractModule_6 extends Module {
   public static final AutoArmorModule field_3895 = Hub.field_2595.method_78(AutoArmorModule.class);
   public static final ElytraFlyModule field_3896 = Hub.field_2595.method_78(ElytraFlyModule.class);
   public final Class_0621 field_3897 = new Class_0621(3);
   public Setting<Boolean> field_3898;
   public Setting<Boolean> field_3899;
   public Setting<Integer> field_3900;
   public Setting<Integer> field_3901;
   public final Timer field_3902;
   public float field_1760;
   public BlockPos field_3534;
   public boolean field_3903;
   public boolean field_3904;
   public boolean field_3905;
   public int field_3906;
   public HorizontalDirection field_3907;

   public AbstractModule_6() {
      super(
         "ObstaclePasser",
         new PlaceholderText()
            .method_2(String.valueOf(ChatFormatting.YELLOW))
            .method_9("Passes obstacles in your way. \n\u0001Requires either ElytraFly or Speed enabled."),
         Category.MOVEMENT
      );
      Settings.initialize(this);
      this.field_3902 = new Timer();
      this.field_3900.method_2("~", SettingMode.MIN);
      this.field_3898.method_9(() -> this.field_3907 = null);
   }

   @Override
   public void onEnable() {
      if (this.method_535()) {
         this.method_68();
      } else {
         this.field_3903 = false;
         this.field_3907 = null;
         this.field_3906 = 0;
         this.field_1760 = (float)Math.round(mc.player.getYRot());
         this.field_3534 = mc.player.blockPosition();
         this.field_3897.method_507();
      }
   }

   @Override
   public void onDisable() {
      Hub.field_2630.method_262();
      if (this.field_3903 && field_3895.isToggled()) {
         field_3895.elytra.method_78(true);
      }

      this.field_3904 = false;
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (!Hub.field_2630.method_261()) {
         mc.player.setYRot(this.field_1760);
      }
   }

   @Subscribe(
      method_800 = -100
   )
   public void method_9(PostTickEvent var1) {
      if (this.method_319()) {
         this.field_3902.reset();
      }

      this.method_1086();
      if (this.field_3907 == null) {
         this.field_3907 = HorizontalDirection.method_294(this.field_1760);
      }

      if (this.field_3906 > 0) {
         this.field_3906--;
      }

      this.field_3897.method_142();
      this.method_1084();
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundPlayerPositionPacket) {
         this.field_3897.method_640();
      }
   }

   @Subscribe
   public void method_29(MovementInputEvent var1) {
      if (!this.field_3899.getValue() || !this.field_3902.method_9(750L)) {
         if (!this.method_1088()) {
            if (this.method_1087() || !mc.player.isFallFlying() && field_3896.isToggled()) {
               Inputs.setJump(var1.method_276(), true);
            }

            Inputs.setForward(var1.method_276(), true);
            var1.method_276().moveVector = new Vec2(var1.method_276().moveVector.x, var1.method_278() ? var1.method_277() : 1F);
         }
      }
   }

   public void method_1084() {
      if (Hub.field_2630.method_261()) {
         BlockPos var1 = Hub.field_2630.method_265();
         if (var1 != null && VectorUtil.method_5(mc.player.position(), var1.getBottomCenter()) <= 1F) {
            Hub.field_2630.method_262();
            this.field_3906 = 10;
            if (field_3896.isToggled()) {
               this.field_3905 = true;
            }
         }
      }

      if (this.method_1085() && !Hub.field_2630.method_261()) {
         this.field_3897.method_507();
         int var8 = this.field_3900.getValue() == -1 ? this.field_3534.getY() : this.field_3900.getValue();
         if (this.field_3907 != null && this.field_3898.getValue()) {
            BlockPos var9 = null;

            for (int var10 = 5; var10 < this.field_3901.getValue(); var10++) {
               var9 = this.field_3907.field_2740.apply(var10).atY(var8);
               if (((DuckAbstractBlock)(Object)BlockUtil.method_33(var9.below())).isCollidable()) {
                  break;
               }
            }

            Hub.field_2630.method_154(var9);
         } else {
            Vec3 var2 = this.field_3534.getBottomCenter();
            Vec3 var3 = this.method_80(this.field_1760);
            float var4 = VectorUtil.method_5(var2, mc.player.position());
            BlockPos var5 = BlockPos.containing(var2.add(var3.scale((double)var4)));
            BlockPos var6 = null;

            for (int var7 = 5; var7 < this.field_3901.getValue(); var7++) {
               var6 = var5.offset(BlockPos.containing(var3.scale((double)var7)));
               if (((DuckAbstractBlock)(Object)BlockUtil.method_33(var6.below())).isCollidable()) {
                  break;
               }
            }

            Hub.field_2630.method_154(var6.atY(var8));
         }
      }
   }

   public boolean method_1085() {
      return this.field_3906 > 0
         ? false
         : mc.player.horizontalCollision
            || this.field_3897.method_641()
            || !PlayerUtil.method_428() && Hub.field_2615.method_1161() > 3 && mc.player.onGround() && field_3896.isToggled();
   }

   public void method_1086() {
      if (Hub.field_2630.method_261()) {
         if (!this.field_3903) {
            this.field_3903 = true;
            PacketSender.method_2(Inputs.shift(true));
            PacketSender.method_2(Inputs.shift(false));
         }
      } else if (this.field_3903) {
         this.field_3903 = false;
      }
   }

   public boolean method_558() {
      return mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);
   }

   public boolean method_319() {
      ClientChunkCache var1 = mc.level.getChunkSource();
      return ChunkPos.rangeClosed(mc.player.chunkPosition(), 2).allMatch(var1x -> var1.hasChunk(var1x.x(), var1x.z())) || !this.field_3899.getValue();
   }

   public boolean method_1087() {
      return this.field_3905 && field_3896.isToggled();
   }

   public boolean method_1088() {
      return Hub.field_2630.method_261();
   }

   public boolean method_639() {
      return !this.isToggled() ? false : this.method_1088();
   }

   public Vec3 method_80(float var1) {
      return mc.player.calculateViewVector(0.0F, var1);
   }
}
