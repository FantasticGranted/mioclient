package me.mioclient.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.event.Event_19;
import me.mioclient.event.ExplosionEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.mixin.ducks.DuckEntityVelocityUpdateS2CPacket;
import me.mioclient.module.movement.FastWebModule;
import me.mioclient.module.movement.VelocityModule;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Class_0030 extends VelocityBehavior {
   public static final FastWebModule field_51 = Hub.field_2595.method_78(FastWebModule.class);
   public final List<Long> field_52 = Collections.synchronizedList(new ArrayList<>());
   public final Timer field_53 = new Timer();
   public boolean field_54;

   public Class_0030(VelocityModule var1) {
      super(var1);
   }

   @Override
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundSetEntityMotionPacket var2 && var2.id() == mc.player.getId() && this.method_41()) {
         boolean var6 = field_51.isToggled() && field_51.method_726() && PlayerUtil.method_5(mc.player);
         DuckEntityVelocityUpdateS2CPacket var4 = (DuckEntityVelocityUpdateS2CPacket)(Object)var2;
         Vec3 var7 = var2.movement();
         double var8 = (this.field_54 || var6) && var7.y > 0.0 ? -var7.y : var7.y;
         var4.setMovement(new Vec3(0.0, var8, 0.0));
      }

      if (var1.method_127() instanceof ClientboundPlayerPositionPacket) {
         this.field_52.add(System.currentTimeMillis());
      }
   }

   @Override
   public void method_9(Event_19 var1) {
   }

   @Override
   public void method_2(ExplosionEvent var1) {
      if (this.method_41()) {
         var1.method_52(0.0F);
         var1.method_185(0.0F);
         var1.method_92(0.0F);
      }
   }

   @Override
   public void method_40() {
      this.field_54 = method_43();
      this.field_52.removeIf(var0 -> System.currentTimeMillis() > var0 + 1000L);
      if (this.field_52.size() > 5) {
         this.field_53.reset();
      }
   }

   public boolean method_41() {
      return field_51.isToggled() && field_51.method_726() && PlayerUtil.method_5(mc.player) ? true : this.field_53.method_9(500L) && this.field_54;
   }

   public static boolean method_42() {
      HashSet<BlockPos> var0 = new HashSet<>();
      var0.add(method_39(1F, 0.0F));
      var0.add(method_39(-1F, 0.0F));
      var0.add(method_39(0.0F, 1F));
      var0.add(method_39(0.0F, -1F));
      return var0.stream().filter(Class_0030::method_44).count() >= 2L;
   }

   public static boolean method_43() {
      AABB var0 = mc.player
         .getBoundingBox()
         .contract(BoxUtil.field_2280, 0.0, BoxUtil.field_2280)
         .setMaxY(mc.player.getY())
         .move(0.0, 0.10000000149011612, 0.0);
      return BlockPos.betweenClosedStream(var0).anyMatch(Class_0030::method_44);
   }

   public static BlockPos method_39(float var0, float var1) {
      LocalPlayer var2 = mc.player;
      float var3 = (float)(var2.getBoundingBox().getXsize() / 2.0);
      var0 *= var3 * 1.000001F;
      var1 *= var3 * 1.000001F;
      return BlockPos.containing(var2.getX() + (double)var0, (double)Math.round(var2.getY()), var2.getZ() + (double)var1);
   }

   public static boolean method_44(BlockPos var0) {
      return !mc.level.getBlockState(var0).getCollisionShape(mc.level, var0).isEmpty();
   }
}
