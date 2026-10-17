package me.mioclient.api;

import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

/** Mixin accessor for a nullable Integer supplier. */
public interface SupplierAccessor {
   @Nullable
   Supplier<Integer> getSupplier();

   void setSupplier(@Nullable Supplier<Integer> var1);
}
