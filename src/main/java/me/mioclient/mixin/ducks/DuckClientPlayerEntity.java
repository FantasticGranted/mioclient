package me.mioclient.mixin.ducks;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({LocalPlayer.class})
public interface DuckClientPlayerEntity {
   @Accessor("wasSprinting")
   boolean lastSprinting();

   @Accessor("wasSprinting")
   void lastSprinting(boolean var1);

   @Accessor("crouching")
   boolean lastSneaking();

   @Accessor("crouching")
   void lastSneaking(boolean var1);

   @Accessor("lastOnGround")
   boolean lastOnGround();

   @Accessor("lastOnGround")
   void lastOnGround(boolean var1);

   @Accessor("xLast")
   double lastX();

   @Accessor("xLast")
   void lastX(double var1);

   @Accessor("yLast")
   double lastBaseY();

   @Accessor("yLast")
   void lastBaseY(double var1);

   @Accessor("zLast")
   double lastZ();

   @Accessor("zLast")
   void lastZ(double var1);

   @Accessor("yRotLast")
   float lastYaw();

   @Accessor("yRotLast")
   void lastYaw(float var1);

   @Accessor("xRotLast")
   float lastPitch();

   @Accessor("yRotLast")
   void lastPitch(float var1);

   @Accessor("autoJumpEnabled")
   boolean autoJumpEnabled();

   @Accessor("autoJumpEnabled")
   void autoJumpEnabled(boolean var1);

   @Accessor("positionReminder")
   int ticksSinceLastPositionPacketSent();

   @Accessor("positionReminder")
   void ticksSinceLastPositionPacketSent(int var1);
}
