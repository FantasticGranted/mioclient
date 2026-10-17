package me.mioclient.module.movement;

import java.util.HashSet;
import java.util.List;
import me.mioclient.enum_.NoFallMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.FontRenderer;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import me.mioclient.nick.Settings;

public class NoFallModule extends Module {
   public Setting<NoFallMode> mode;
   public Setting<Float> groundDistance;

   public NoFallModule() {
      super("NoFall", "Prevents falling damage.", Category.MOVEMENT);
      Settings.initialize(this);
   }

   @Override
   public String getInfo() {
      return FontRenderer.method_3(this.mode.getValue());
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      this.mode.getValue().method_2(this);
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      this.mode.getValue().method_2(var1);
   }

   public HashSet<BlockPos> method_78(AABB var1) {
      return new HashSet<>(
         List.of(
            BlockPos.containing(var1.maxX, var1.minY, var1.maxZ),
            BlockPos.containing(var1.maxX, var1.minY, var1.minZ),
            BlockPos.containing(var1.minX, var1.minY, var1.maxZ),
            BlockPos.containing(var1.minX, var1.minY, var1.minZ)
         )
      );
   }

   public boolean method_1147() {
      return mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA) && mc.player.isFallFlying();
   }
}
