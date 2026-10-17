package me.mioclient.mixin.ducks;

import net.minecraft.client.User;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({User.class})
public interface DuckSession {
   @Mutable
   @Accessor("name")
   void setUsername(String var1);
}
