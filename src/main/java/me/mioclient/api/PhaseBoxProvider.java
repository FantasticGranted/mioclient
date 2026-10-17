package me.mioclient.api;

import me.mioclient.module.combat.AntiPhaseModule;
import me.mioclient.record.BlockFaceBox;
import net.minecraft.world.entity.player.Player;

/** Computes the AntiPhase block-face box for a player. */
public interface PhaseBoxProvider {
   BlockFaceBox method_2(AntiPhaseModule var1, Player var2);
}
