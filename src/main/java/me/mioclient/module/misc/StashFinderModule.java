package me.mioclient.module.misc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import me.mioclient.Hub;
import me.mioclient.enum_.Priority;
import me.mioclient.event.ChunkLoadEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.SoundId;
import me.mioclient.internal.Timer;
import me.mioclient.internal.Class_0580;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.ChatUtil;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.minecart.MinecartChest;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import me.mioclient.nick.Settings;

public class StashFinderModule extends Module {
   public Setting<Boolean> field_1495;
   public Setting<Integer> chestCount;
   public Setting<Boolean> field_1497;
   public Setting<Boolean> stackedCarts;
   public Setting<Boolean> notify;
   public Setting<Boolean> censorCoords;
   public Setting<Boolean> log;
   public Setting<Boolean> sound;
   public Setting<SoundId> type;
   public Setting<Float> volume;
   public Setting<Boolean> ignoreNatural;
   public Setting<Boolean> dungeon;
   public Setting<Boolean> fortress;
   public Setting<Boolean> bastion;
   public Setting<Boolean> ancientCity;
   public Setting<Boolean> trialChambers;
   public final Timer field_1511;

   public StashFinderModule() {
      super("StashFinder", "Logs and/or notifies you about possible stashes.", Category.MISC);
      Settings.initialize(this);
      this.field_1511 = new Timer();
      this.setDrawn(false);
   }

   @Subscribe
   public void method_2(ChunkLoadEvent var1) {
      String var2 = new SimpleDateFormat("MM/dd HH:mm a").format(new Date());
      LevelChunk var3 = var1.method_1021();
      ChunkPos var4 = var3.getPos();
      int var5 = 0;

      for (BlockEntity var7 : var3.getBlockEntities().values()) {
         if (!this.ignoreNatural.getValue()
            || !BlockUtil.method_2(
               mc.level.getChunk(var7.getBlockPos()),
               var7.getBlockPos(),
               this.fortress.getValue(),
               this.bastion.getValue(),
               this.ancientCity.getValue(),
               this.dungeon.getValue(),
               this.trialChambers.getValue()
            )) {
            if (var7 instanceof ChestBlockEntity || var7 instanceof BarrelBlockEntity && !Hub.field_2622.method_101(var7.getBlockPos())) {
               var5++;
            }

            if (this.field_1495.getValue() && var5 >= this.chestCount.getValue()) {
               String var15 = new PlaceholderText().method_2(var5).method_9("\u0001 chests");
               Class_0580 var18 = this.method_2(
                  new PlaceholderText().method_2((Object)var2).method_2((Object)var15).method_9("\u0001 (\u0001)"), var4.getMiddleBlockX(), var4.getMiddleBlockZ()
               );
               if (this.method_2(var18, var15)) {
                  break;
               }
            } else if (var7 instanceof ShulkerBoxBlockEntity) {
               ShulkerBoxBlockEntity var9 = (ShulkerBoxBlockEntity)var7;
               if (this.field_1497.getValue() && !Hub.field_2622.method_101(var9.getBlockPos())) {
                  String var8 = "a shulkerbox";
                  Class_0580 var10 = this.method_2(
                     new PlaceholderText().method_2((Object)var2).method_2((Object)var8).method_9("\u0001 (\u0001)"), var4.getMiddleBlockX(), var4.getMiddleBlockZ()
                  );
                  if (this.method_2(var10, var8)) {
                     break;
                  }
               }
            }
         }
      }

      if (this.stackedCarts.getValue()) {
         ArrayList var13 = new ArrayList();

         for (Entity var16 : mc.level.entitiesForRendering()) {
            if (var16 instanceof MinecartChest var17 && var17.chunkPosition().equals(var17.chunkPosition())) {
               for (MinecartChest var11 : (Iterable<MinecartChest>)(Iterable<?>)(var13)) {
                  if (var17.getBoundingBox().intersects(var11.getBoundingBox())) {
                     Class_0580 var12 = this.method_2(
                        new PlaceholderText().method_2((Object)var2).method_9("Stacked minecarts (\u0001)"), var4.getMiddleBlockX(), var4.getMiddleBlockZ()
                     );
                     this.method_2(var12, "Stacked minecarts");
                     return;
                  }
               }

               var13.add(var17);
            }
         }
      }
   }

   public Class_0580 method_2(String var1, int var2, int var3) {
      String var4 = BlockUtil.method_1071().name();
      String var5 = mc.player.connection.getServerData() == null ? "singleplayer" : mc.player.connection.getServerData().ip;
      return new Class_0580(var1, var2, var3, var4, var5);
   }

   public boolean method_2(Class_0580 var1, String var2) {
      if (Hub.field_2618.method_2(var1x -> var1x.method_380() == var1.method_380() && var1x.method_396() == var1.method_396()).isEmpty()
         && Hub.field_2618.method_2(var1)) {
         if (this.notify.getValue()) {
            MutableComponent var3 = Component.literal(this.getName()).append(new PlaceholderText().method_2((Object)var2).method_9(" has found \u0001 at: "));
            if (!this.censorCoords.getValue()) {
               var3.append(var1.method_599());
            }

            ChatUtil.method_2(var3, ChatUtil.method_38(-13579), Priority.LOW);
         }

         if (this.sound.getValue() && this.field_1511.method_2(5.0, TimeUnit.SECONDS)) {
            Hub.field_2606.method_2(this.type.getValue(), this.volume.getValue());
            this.field_1511.reset();
         }

         return true;
      } else {
         return false;
      }
   }
}
