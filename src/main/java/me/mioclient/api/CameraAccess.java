package me.mioclient.api;

import net.minecraft.world.phys.Vec3;

/** Mixin accessor for camera yaw/pitch and previous velocity. */
public interface CameraAccess {
   float getCameraYaw();

   float getCameraPitch();

   void setCameraYaw(float var1);

   void setCameraPitch(float var1);

   Vec3 mio$getPrevVelocity();
}
