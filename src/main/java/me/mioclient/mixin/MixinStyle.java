package me.mioclient.mixin;

import java.util.function.Supplier;
import me.mioclient.api.SupplierAccessor;
import net.minecraft.network.chat.Style;
import me.mioclient.api.StyleColorAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({Style.class})
public class MixinStyle implements StyleColorAccess {
   public MixinStyle() {
      super();
   }

   @Unique
   @Override
   public Style mio$withColor(Supplier<Integer> var1) {
      Style var2 = (Style)(Object)this;
      if (var2.getColor() != null) {
         ((SupplierAccessor)(Object)var2.getColor()).setSupplier(var1);
      }

      return var2;
   }
}
