package me.mioclient.api;

import java.util.List;
import net.minecraft.client.multiplayer.chat.GuiMessage.Line;

/** Mixin accessor exposing the chat hud's visible lines. */
public interface VisibleLinesAccess {
   List<Line> getVisible();
}
