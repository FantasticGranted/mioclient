package me.mioclient.module.abstract_;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.api.SafetyBoxPredicate;
import me.mioclient.enum_.Class_0214;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Class_0351;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.Class_0700;
import me.mioclient.mixin.ducks.DuckAbstractBlock;
import me.mioclient.module.Category;
import me.mioclient.record.SafetyBox;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import me.mioclient.nick.Settings;

public class AbstractModule_42 extends AbstractModule_32 {
   public Setting<Set<Block>> whitelist;
   public Setting<Boolean> doubles;
   public Setting<Class_0214> mode;
   public Setting<Float> smartRange;
   public Setting<Float> range;
   public Setting<Float> verticalRange;
   public Setting<Boolean> onlySafe;
   public Setting<Boolean> selfSafety;
   public Setting<Boolean> ignoreNakeds;
   public Setting<Boolean> multiTask;
   public Setting<Boolean> self;
   public Setting<Boolean> extrapolation;
   public Setting<Integer> ticks;
   public final SafetyBoxPredicate field_779;
   public final SafetyBoxPredicate field_780;

   public AbstractModule_42() {
      super("HoleFill", "Blocks safe holes nearby with obsidian.", Category.COMBAT);
      Settings.initialize(this);
      this.field_779 = new Class_0351(this);
      this.field_780 = new Class_0700(this);
      this.unregister(this.airPlace);
      this.unregister(this.chorus);
      this.jump.method_39(false);
      this.self.method_5(var1 -> {
         for (Block var3 : this.whitelist.getValue()) {
            if (!((DuckAbstractBlock)(Object)var3).isCollidable()) {
               return true;
            }
         }

         return false;
      });
   }

   @Override
   public String getInfo() {
      return String.valueOf(this.method_17().size());
   }

   @Override
   public void method_33() {
      if ((!mc.player.isUsingItem() || this.multiTask.getValue()) && (Hub.field_2605.method_224() || !this.onlySafe.getValue())) {
         super.method_33();
      }
   }

   @Override
   public List<BlockPos> method_17() {
      ArrayList var1 = new ArrayList();

      for (SafetyBox var3 : Hub.field_2605.method_223()) {
         if (this.mode.getValue().method_2(this).method_2(var3)
            && (this.self.getValue() && this.self.method_176() || !var3.getPos().equals(PlayerUtil.method_425()))) {
            var1.add(var3.getPos());
         }
      }

      return var1;
   }

   @Override
   public int method_34() {
      return this.whitelist.getValue().isEmpty()
         ? super.method_34()
         : InventoryUtil.method_7((Predicate<ItemStack>)(var1 -> this.whitelist.getValue().contains(this.method_263(var1))));
   }

   public Block method_263(ItemStack var1) {
      return var1.getItem() instanceof BlockItem var2 ? var2.getBlock() : Blocks.VOID_AIR;
   }
}
