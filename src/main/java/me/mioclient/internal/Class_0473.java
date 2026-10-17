package me.mioclient.internal;

import me.mioclient.baritone.api.BaritoneAPI;
import me.mioclient.baritone.api.IBaritone;
import me.mioclient.baritone.api.behavior.IPathingBehavior;
import me.mioclient.baritone.api.pathing.goals.Goal;
import me.mioclient.baritone.api.pathing.goals.GoalBlock;
import me.mioclient.baritone.api.pathing.goals.GoalXZ;
import me.mioclient.baritone.api.selection.ISelection;
import me.mioclient.baritone.api.utils.interfaces.IGoalRenderPos;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.function.Consumer;
import me.mioclient.api.Class_0230;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.deobf.Named;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.AABB;

public final class Class_0473 implements MinecraftAccess, Class_0230 {
   public final ObjectSet<Named> field_1512 = ObjectSets.synchronize(new ObjectOpenHashSet());

   public Class_0473() {
      super();
      lookups.method_14(this);
      BaritoneAPI.getProvider().getPrimaryBaritone().getPathingControlManager().registerProcess(new Class_1064(this));
      BaritoneAPI.getSettings().useMessageTag.value = false;
      BaritoneAPI.getSettings().logger.value = (Consumer<Component>)var0 -> {
         MutableComponent var1 = var0.copy();
         var1.getSiblings().add(0, Component.literal(" "));
         var1.getSiblings().add(0, ChatUtil.method_287());
         mc.gui.getChat().addPlayerMessage(var1, null, ChatUtil.field_3910);
      };
   }

   @Override
   public void method_2(Named var1) {
      if (!this.field_1512.contains(var1)) {
         this.field_1512.add(var1);
      }
   }

   @Override
   public void method_9(Named var1) {
      if (this.field_1512.contains(var1)) {
         this.field_1512.remove(var1);
      }
   }

   @Override
   public boolean method_260() {
      return !this.field_1512.isEmpty();
   }

   @Override
   public boolean method_261() {
      return BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing();
   }

   @Override
   public void method_29(double var1) {
      GoalXZ var3 = GoalXZ.fromDirection(mc.player.position(), mc.player.getYRot(), 5.0);
      BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(var3);
   }

   @Override
   public void method_154(BlockPos var1) {
      GoalBlock var2 = new GoalBlock(var1);
      BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(var2);
   }

   @Override
   public void method_114(BlockPos var1) {
      BaritoneAPI.getProvider().getPrimaryBaritone().getElytraProcess().pathTo(var1);
   }

   @Override
   public void method_209(BlockPos var1) {
      GoalBlock var2 = new GoalBlock(var1);
      BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoal(var2);
   }

   @Override
   public void method_262() {
      IPathingBehavior var1 = BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior();
      var1.cancelEverything();
      var1.forceCancel();
   }

   @Override
   public boolean method_263(BlockPos var1) {
      IBaritone var2 = BaritoneAPI.getProvider().getPrimaryBaritone();
      ISelection var3 = var2.getSelectionManager().getLastSelection();
      return var3 == null ? false : AABB.encapsulatingFullBlocks(var3.min(), var3.max()).contains(var1.getCenter());
   }

   @Override
   public BlockPos method_265() {
      Goal var1 = BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().getGoal();
      if (var1 instanceof IGoalRenderPos var2) {
         return var2.getGoalPos();
      } else {
         return var1 instanceof GoalXZ var3 ? BlockPos.containing((double)var3.getX(), mc.player.getY(), (double)var3.getZ()) : null;
      }
   }

   @Override
   public boolean method_264() {
      return true;
   }
}
