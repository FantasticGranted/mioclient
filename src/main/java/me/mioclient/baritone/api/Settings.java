package me.mioclient.baritone.api;

import java.awt.Color;
import java.util.function.Consumer;

// Lightweight stub of the Baritone API Settings that mirrors only the fields the
// Mio source references. Vendored under me.mioclient.baritone so the project builds
// without the external Baritone API jar on the classpath.
public class Settings {
    public static class Setting<T> {
        public T value;
    }

    public final Setting<Boolean> chatControl             = new Setting<>();
    public final Setting<Boolean> disconnectOnArrival     = new Setting<>();
    public final Setting<Boolean> freeLook                = new Setting<>();
    public final Setting<Boolean> blockFreeLook           = new Setting<>();
    public final Setting<Boolean> elytraFreeLook          = new Setting<>();
    public final Setting<Boolean> censorCoordinates       = new Setting<>();
    public final Setting<Boolean> censorRanCommands       = new Setting<>();
    public final Setting<Boolean> useMessageTag           = new Setting<>();
    public final Setting<Consumer<net.minecraft.network.chat.Component>> logger = new Setting<>();
    public final Setting<String>  prefix                  = new Setting<>();

    public final Setting<Color> colorBestPathSoFar        = new Setting<>();
    public final Setting<Color> colorGoalBox              = new Setting<>();
    public final Setting<Color> colorInvertedGoalBox      = new Setting<>();
    public final Setting<Color> colorCurrentPath          = new Setting<>();
    public final Setting<Color> colorMostRecentConsidered = new Setting<>();
    public final Setting<Color> colorBlocksToBreak        = new Setting<>();
    public final Setting<Color> colorBlocksToPlace        = new Setting<>();
    public final Setting<Color> colorBlocksToWalkInto     = new Setting<>();
    public final Setting<Color> colorNextPath             = new Setting<>();

    public final Setting<Float>  pathRenderLineWidthPixels = new Setting<>();
    public final Setting<Float>  goalRenderLineWidthPixels = new Setting<>();

    public final Setting<Boolean> assumeStep              = new Setting<>();
    public final Setting<Boolean> assumeExternalAutoTool  = new Setting<>();
    public final Setting<Boolean> assumeSafeWalk          = new Setting<>();
    public final Setting<Boolean> assumeWalkOnWater       = new Setting<>();

    public final Setting<Boolean> antiCheatCompatibility  = new Setting<>();
}
