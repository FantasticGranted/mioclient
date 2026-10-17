package me.mioclient.baritone.api.process;
import net.minecraft.core.BlockPos;
public interface IElytraProcess extends IBaritoneProcess {
   void pathTo(BlockPos pos);
}
