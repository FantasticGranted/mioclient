package me.mioclient.mixin.ducks;

import java.util.List;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({Screen.class})
public interface DuckScreen {
   @Accessor
   List<Renderable> getRenderables();

   @Mutable
   @Accessor
   void setRenderables(List<Renderable> var1);

   @Invoker("addRenderableWidget")
   <T extends GuiEventListener & Renderable & NarratableEntry> T addDrawableChildHook(T var1);
}
