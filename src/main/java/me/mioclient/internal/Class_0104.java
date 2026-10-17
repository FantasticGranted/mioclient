package me.mioclient.internal;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Color;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.UUID;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.EspMode;
import me.mioclient.event.Event_3;
import me.mioclient.module.render.ESPModule;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.material.MapColor.Brightness;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Class_0104 implements MinecraftAccess {
   public final ESPModule field_328;
   public final HashMap<UUID, String> field_329 = new HashMap<>();

   public Class_0104(ESPModule var1) {
      super();
      this.field_328 = var1;
   }

   public boolean method_98(Entity var1) {
      if (var1 == null || var1.getBoundingBox() == null) {
         return false;
      } else if (!RotationManager.method_4(var1.getBoundingBox())) {
         return false;
      } else {
         Vec3 var2 = mc.gameRenderer.getMainCamera().position();
         return var1 instanceof ItemEntity && var1.position().distanceTo(var2) > (double)this.field_328.range.getValue().intValue()
            ? false
            : var1 instanceof Player && this.field_328.players.getValue() && var1 != mc.player
               || var1 instanceof ItemEntity && this.field_328.items.getValue() && this.field_328.mode.getValue() != EspMode.TEXT
               || var1 instanceof ThrownExperienceBottle && this.field_328.exp.getValue()
               || (var1 instanceof AgeableMob || var1 instanceof AbstractFish || var1 instanceof Squid) && this.field_328.animals.getValue()
               || var1 instanceof Enemy && this.field_328.hostiles.getValue()
               || var1 instanceof ThrownEnderpearl && this.field_328.pearls.getValue();
      }
   }

   public boolean method_9(BlockEntity var1) {
      if (var1 == null) {
         return false;
      } else {
         return !RotationManager.method_4(new AABB(var1.getBlockPos()))
            ? false
            : (var1 instanceof ChestBlockEntity || var1 instanceof BarrelBlockEntity) && this.field_328.chests.getValue()
               || var1 instanceof BedBlockEntity && this.field_328.beds.getValue()
               || var1 instanceof EnderChestBlockEntity && this.field_328.eChests.getValue()
               || var1 instanceof ShulkerBoxBlockEntity && this.field_328.field_3841.getValue()
               || var1 instanceof AbstractFurnaceBlockEntity && this.field_328.furnaces.getValue()
               || var1 instanceof DispenserBlockEntity && this.field_328.dispensers.getValue()
               || var1 instanceof HopperBlockEntity && this.field_328.hoppers.getValue()
               || var1 instanceof SignBlockEntity && this.field_328.signs.getValue()
               || var1 instanceof DecoratedPotBlockEntity && this.field_328.pots.getValue();
      }
   }

   public boolean method_126() {
      return this.field_328.chests.getValue()
         || this.field_328.eChests.getValue()
         || this.field_328.field_3841.getValue()
         || this.field_328.beds.getValue()
         || this.field_328.signs.getValue()
         || this.field_328.dispensers.getValue()
         || this.field_328.hoppers.getValue()
         || this.field_328.furnaces.getValue();
   }

   public void method_2(Event_3 var1, String var2, Vec3 var3, float var4, Color var5, Color var6) {
      double var7 = MathUtil.method_2(mc.gameRenderer.getMainCamera().position(), var3, (double)var4);
      if (var6 != null) {
         RenderUtil.field_2672
            .method_2(var1.method_10(), var3, 0.0F, 0.0F, FontRenderer.field_3143.method_221(var2), (float)FontRenderer.field_3143.method_66(), var7, var6);
      }

      RenderUtil.field_2672.method_2(var1.method_10(), var2, var3, 0.0F, 0.0F, var7, var5, true);
   }

   public void method_2(UUID var1) {
      try (HttpClient var2 = HttpClient.newHttpClient()) {
         HttpRequest var3 = HttpRequest.newBuilder()
            .uri(
               new URI(
                  new PlaceholderText().method_2(var1.toString().replace("-", "")).method_9("https://sessionserver.mojang.com/session/minecraft/profile/\u0001")
               )
            )
            .timeout(Duration.of(3L, ChronoUnit.SECONDS))
            .GET()
            .build();
         HttpResponse var4 = var2.send(var3, BodyHandlers.ofString());
         JsonObject var5 = JsonParser.parseString((String)var4.body()).getAsJsonObject();
         if (var5.has("name")) {
            String var6 = var5.get("name").getAsString();
            this.field_329.put(var1, var6);
         }
      } catch (Exception var9) {
      }
   }

   public String method_84(Entity var1) {
      UUID var2 = null;
      if (!this.field_328.mobOwner.getValue()) {
         return null;
      } else {
         if (var1 instanceof ThrownEnderpearl var3 && var3.getOwner() != null) {
            return var3.getOwner().getName().getString();
         }

         if (var1 instanceof TamableAnimal var4) {
            var2 = var4.getOwnerReference() != null ? var4.getOwnerReference().getUUID() : null;
         }

         if (var2 != null) {
            Player var5 = mc.level.getPlayerByUUID(var2);
            if (var5 != null) {
               String var7 = var5.getGameProfile().name();
               this.field_329.putIfAbsent(var2, var7);
               return var7;
            }

            if (this.field_329.containsKey(var2)) {
               return this.field_329.get(var2);
            }

            this.field_329.put(var2, null);
            UUID var6 = var2;
            THREAD_POOL.submit(() -> this.method_2(var6));
         }

         return null;
      }
   }

   public Color method_9(Entity var1, boolean var2) {
      Color var3 = Color.white;
      if (var1 instanceof Player) {
         var3 = var2 ? this.field_328.fill2.getValue() : this.field_328.outline2.getValue();
      } else if (var1 instanceof ItemEntity) {
         var3 = var2 ? this.field_328.fill5.getValue() : this.field_328.outline5.getValue();
      } else if (var1 instanceof ThrownExperienceBottle) {
         var3 = var2 ? this.field_328.fill6.getValue() : this.field_328.outline6.getValue();
      } else if (!(var1 instanceof AgeableMob) && !(var1 instanceof AbstractFish) && !(var1 instanceof Squid)) {
         if (var1 instanceof Enemy) {
            var3 = var2 ? this.field_328.fill.getValue() : this.field_328.outline.getValue();
         } else if (var1 instanceof ThrownEnderpearl) {
            var3 = var2 ? this.field_328.fill4.getValue() : this.field_328.outline4.getValue();
         }
      } else {
         var3 = var2 ? this.field_328.fill3.getValue() : this.field_328.outline3.getValue();
      }

      int var4 = (int)Mth.clamp(
         MathUtil.method_5((float)(var1.tickCount - 1), (float)var1.tickCount) * 2e+01F, 0.0F, (float)var3.getAlpha()
      );
      if (var1 instanceof ThrownEnderpearl || var1 instanceof ThrownExperienceBottle) {
         var4 = var3.getAlpha();
      }

      return Colors.method_9(var3, var4);
   }

   public Color method_5(BlockEntity var1) {
      int var2 = mc.level.getBlockState(var1.getBlockPos()).getMapColor(mc.level, var1.getBlockPos()).calculateARGBColor(Brightness.HIGH);
      if (var1 instanceof ShulkerBoxBlockEntity) {
         return new Color(255, 0, 175);
      } else if (var1 instanceof EnderChestBlockEntity) {
         return new Color(125, 40, 180);
      } else {
         if (var1 instanceof BedBlockEntity var3) {
            var2 = var3.getColor().getMapColor().calculateARGBColor(Brightness.HIGH);
         }

         int var6 = var2 >> 16 & 0xFF;
         int var4 = var2 >> 8 & 0xFF;
         int var5 = var2 & 0xFF;
         return new Color(var5, var4, var6).brighter();
      }
   }
}
