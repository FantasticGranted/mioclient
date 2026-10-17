package me.mioclient.record;

/** Outcome of a rotation solver: whether a rotation was found and the resulting {@link Rotation} (NONE = empty). */
public final class RotationResult {
   public final boolean success;
   public final Rotation rotation;
   public static final RotationResult NONE = new RotationResult(false, null);

   public RotationResult(boolean var1, Rotation var2) {
      super();
      this.success = var1;
      this.rotation = var2;
   }

   public boolean method_114() {
      return this.success;
   }

   public Rotation method_209() {
      return this.rotation;
   }
}
