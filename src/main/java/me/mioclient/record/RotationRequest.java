package me.mioclient.record;

/** A requested rotation (angles) with a priority; higher priority wins when requests compete. */
public final class RotationRequest {
   public final float[] rotations;
   public final int priority;

   public RotationRequest(float[] var1, int var2) {
      super();
      this.rotations = var1;
      this.priority = var2;
   }

   public float[] getRotations() {
      return this.rotations;
   }

   public int method_800() {
      return this.priority;
   }
}
