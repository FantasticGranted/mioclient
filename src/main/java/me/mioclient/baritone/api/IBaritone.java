package me.mioclient.baritone.api;
import me.mioclient.baritone.api.behavior.IPathingBehavior;
import me.mioclient.baritone.api.event.listener.IEventBus;
import me.mioclient.baritone.api.command.IBaritoneChatControl;
import me.mioclient.baritone.api.process.IPathingControlManager;
import me.mioclient.baritone.api.process.ICustomGoalProcess;
import me.mioclient.baritone.api.process.IElytraProcess;
import me.mioclient.baritone.api.selection.ISelectionManager;
public interface IBaritone {
   IPathingBehavior getPathingBehavior();
   IEventBus getGameEventHandler();
   IBaritoneChatControl getBaritoneChatControl();
   IPathingControlManager getPathingControlManager();
   ICustomGoalProcess getCustomGoalProcess();
   IElytraProcess getElytraProcess();
   ISelectionManager getSelectionManager();
}
