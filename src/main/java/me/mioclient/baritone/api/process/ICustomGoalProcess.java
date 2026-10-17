package me.mioclient.baritone.api.process;
import me.mioclient.baritone.api.pathing.goals.Goal;
public interface ICustomGoalProcess extends IBaritoneProcess {
   void setGoalAndPath(Goal goal);
   void setGoal(Goal goal);
}
