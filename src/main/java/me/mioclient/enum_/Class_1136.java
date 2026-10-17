package me.mioclient.enum_;

import com.mojang.brigadier.arguments.ArgumentType;
import me.mioclient.internal.NameArgumentType;
import me.mioclient.internal.ModuleArgumentType;

public enum Class_1136 {
   field_3518(new ModuleArgumentType()),
   field_3519(new NameArgumentType());

   public final ArgumentType<?> field_3520;

    Class_1136(ArgumentType<?> var3) {
      this.field_3520 = var3;
   }
}
