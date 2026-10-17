package me.mioclient.baritone.api.selection;
import net.minecraft.core.BlockPos;
public interface ISelection {
   BlockPos min();
   BlockPos max();
}
