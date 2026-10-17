package me.mioclient.module.abstract_;

import java.util.ArrayList;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.enum_.TrapType;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.module.Category;
import me.mioclient.record.TrapPlacement;
import me.mioclient.setting.Setting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Items;
import me.mioclient.nick.Settings;

public class AbstractModule_30 extends AbstractModule_32 {
   public static final MutableBlockPos field_347 = new MutableBlockPos();
   public Setting<Float> targetRange;
   public Setting<Boolean> ignoreNakeds;
   public boolean field_350;

   public AbstractModule_30() {
      super("AnvilAura", "Drops anvils on your enemies.", Category.COMBAT, "autoanvil");
      Settings.initialize(this);
      this.unregister(this.chorus);
   }

   @Override
   public List<BlockPos> method_17() {
      ArrayList var1 = new ArrayList();
      this.field_350 = false;
      TrapPlacement var2 = this.method_137();
      if (var2 == null) {
         return var1;
      } else {
         if (var2.getTrapType() == TrapType.CAGE) {
            for (int var3 = 0; var3 < 3; var3++) {
               var1.add(PlayerUtil.method_9(var2.getPlayer()).relative(var2.getDirection()).above(var3));
            }
         } else {
            var1.add(PlayerUtil.method_9(var2.getPlayer()).above(2));
            this.field_350 = true;
         }

         return var1;
      }
   }

   @Override
   public int method_34() {
      return this.field_350 ? InventoryUtil.method_5(Items.ANVIL) : super.method_34();
   }

   public TrapPlacement method_137() {
      double var1 = 1.7976931348623157e+308;
      TrapPlacement var3 = null;

      for (AbstractClientPlayer var5 : mc.level.players()) {
         if (var5.isAlive() && mc.player != var5 && !Hub.field_2603.getId(var5) && (!this.ignoreNakeds.getValue() || PlayerUtil.method_29(var5))) {
            BlockPos var6 = PlayerUtil.method_9(var5).above(2);
            double var7 = mc.player.getEyePosition().distanceTo(var5.position());
            if (!(var7 > (double)this.targetRange.getValue().floatValue())
               && mc.level.getBlockState(var6.below()).canBeReplaced()
               && InteractionUtil.method_7(var6, false)) {
               Direction var9 = InteractionUtil.method_9(var6, this.strictDirection.getValue());
               TrapPlacement var10;
               if (var9 != null) {
                  var10 = new TrapPlacement(var5, TrapType.ANVIL, null);
               } else {
                  Direction var11 = this.method_138(var6);
                  if (var11 == null) {
                     continue;
                  }

                  var10 = new TrapPlacement(var5, TrapType.CAGE, var11);
               }

               if (var7 < var1) {
                  var3 = var10;
                  var1 = var7;
               }
            }
         }
      }

      return var3;
   }

   public Direction method_138(BlockPos var1) {
      List var2 = InteractionUtil.method_39(var1);
      int var3 = 1337;
      Direction var4 = null;

      for (Direction var8 : Direction.values()) {
         if (var2.contains(var8.getOpposite()) || !this.strictDirection.getValue()) {
            int var9 = 0;

            for (int var10 = 0; var10 < 3; var10++) {
               field_347.set(var1.getX() + var8.getStepX(), var1.getY() - var10, var1.getZ() + var8.getStepZ());
               if (!mc.level.getBlockState(field_347).canBeReplaced()) {
                  var9 = var10;
                  break;
               }

               var9 = -1;
            }

            if (var9 != -1 && var9 < var3) {
               var3 = var9;
               var4 = var8;
            }
         }
      }

      return var4;
   }
}
