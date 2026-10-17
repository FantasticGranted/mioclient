package me.mioclient.enum_;

import java.util.Comparator;
import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.RotationManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

/** How Aura sorts candidate targets: by distance, crosshair angle, or health. */
public enum TargetSort implements Nameable {
   DISTANCE("Distance", Comparator.comparing(var0 -> var0.distanceTo(MinecraftAccess.mc.player))),
   CROSSHAIR("Crosshair", Comparator.comparing(var0 -> Mth.degreesDifferenceAbs(MinecraftAccess.mc.player.getYRot(), RotationManager.method_14(var0)[0]))),
   HEALTH("Health", Comparator.comparing(EntityUtil::method_2));

   public final String field_3803;
   public final Comparator<Entity> field_3804;

    TargetSort(String var3, Comparator<Entity> var4) {
      this.field_3803 = var3;
      this.field_3804 = var4;
   }

   @Override
   public String getName() {
      return this.field_3803;
   }

   public Comparator<Entity> method_55() {
      return this.field_3804;
   }
}
