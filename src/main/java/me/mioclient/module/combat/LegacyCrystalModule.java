package me.mioclient.module.combat;

import java.util.HashMap;
import java.util.Map;
import me.mioclient.enum_.InteractType;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.InteractBlockEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.InteractPacketReader;
import me.mioclient.internal.Timer;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.mixin.ducks.DuckMinecraftClient;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;
import me.mioclient.nick.Settings;

public class LegacyCrystalModule extends Module {
   public Setting<Integer> field_4339;
   public Setting<Boolean> field_4340;
   public final Map<BlockPos, Long> field_4341;
   public final Timer field_4342;
   public long field_4343;

   public LegacyCrystalModule() {
      super("LegacyCrystal", "Legit auto crystal (crystal aura).", Category.COMBAT);
      Settings.initialize(this);
      this.field_4341 = new HashMap<>();
      this.field_4342 = new Timer();
      this.field_4343 = 150L;
   }

   @Subscribe
   public void method_9(PostTickEvent var1) {
      synchronized (this.field_4341) {
         this.field_4341.entrySet().removeIf(var0 -> var0.getValue() + 15000L < System.currentTimeMillis());
      }

      if (mc.hitResult instanceof EntityHitResult) {
         Entity var5 = ((EntityHitResult)mc.hitResult).getEntity();
         if (this.field_4342.method_9(this.field_4343) && var5 instanceof EndCrystal && this.field_4341.containsKey(var5.blockPosition().below())) {
            ((DuckMinecraftClient)(Object)mc).attack();
            this.field_4343 = (long)(Math.random() * (double)this.field_4339.getValue().intValue());
            this.field_4342.reset();
         }
      }
   }

   @Subscribe
   public void method_2(InteractBlockEvent var1) {
      if (mc.level.getBlockState(var1.method_382().getBlockPos().above(2)).isAir()) {
         boolean var2 = InteractionUtil.method_2(var1.method_382().getBlockPos(), false, false, true, false, false, false);
         if (InventoryUtil.isSword(mc.player.getMainHandItem())) {
            InventoryUtil.method_16(InventoryUtil.method_5(var2 ? Items.END_CRYSTAL : Items.OBSIDIAN));
         } else if (mc.player.getMainHandItem().is(Items.OBSIDIAN) && var2) {
            InventoryUtil.method_78(InventoryUtil.method_5(Items.END_CRYSTAL));
         }
      }
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundUseItemOnPacket var2 && mc.player.getItemInHand(var2.getHand()).is(Items.END_CRYSTAL)) {
         this.field_4341.compute(var2.getHitResult().getBlockPos(), (var0, var1x) -> System.currentTimeMillis());
      }

      if (var1.method_127() instanceof net.minecraft.network.protocol.game.ServerboundAttackPacket var4
         && this.field_4340.getValue()) {
         Entity var5 = mc.level.getEntity(var4.entityId());
         if (var5 instanceof EndCrystal) {
            var5.setRemoved(RemovalReason.KILLED);
         }
      }
   }
}
