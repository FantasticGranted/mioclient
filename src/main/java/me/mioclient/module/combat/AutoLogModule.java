package me.mioclient.module.combat;

import java.text.DecimalFormat;
import me.mioclient.Hub;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.FrameRenderEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.internal.ExplosionDamage;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.exploit.IllegalDisconnectModule;
import me.mioclient.module.misc.AutoReconnectModule;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import me.mioclient.nick.Settings;

public class AutoLogModule extends Module {
   public static final IllegalDisconnectModule illegaldisconnect = Hub.field_2595.method_78(IllegalDisconnectModule.class);
   public static AutoReconnectModule autoreconnect = Hub.field_2595.method_78(AutoReconnectModule.class);
   public Setting<Integer> health;
   public Setting<Float> invincTimeout;
   public Setting<Boolean> autoDisable;
   public Setting<Boolean> gearedOnly;
   public Setting<Boolean> unfocusedOnly;
   public Setting<Boolean> onRender;
   public Setting<Boolean> crystal;
   public Setting<Boolean> tNTMinecart;
   public Setting<Boolean> totems;
   public Setting<Integer> totemCount;
   public Setting<Boolean> elytra;
   public Setting<Integer> durability;
   public Setting<Boolean> assumeInventory;
   public Setting<Boolean> yLevel;
   public Setting<Float> y;
   public Runnable field_205;
   public final Timer field_206;

   public AutoLogModule() {
      super("AutoLog", "Logs you out so you don't have to fight anyone.", Category.COMBAT);
      Settings.initialize(this);
      this.field_206 = new Timer();
   }

   @Override
   public void onEnable() {
      this.field_205 = null;
   }

   @Override
   public String getInfo() {
      return new DecimalFormat("0.0").format(this.health.getValue());
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (!this.method_535()) {
         if (!mc.player.isSpectator() && !mc.player.hasInfiniteMaterials()) {
            if (!this.gearedOnly.getValue() || PlayerUtil.method_29(mc.player)) {
               if (this.elytra.getValue() && this.method_90()) {
                  this.method_91("Your elytra durability is too low!");
               } else if (this.yLevel.getValue() && mc.player.getY() < (double)this.y.getValue().floatValue()) {
                  this.method_91(new PlaceholderText().method_2(String.valueOf(this.y.getValue())).method_9("You are below Y level \u0001"));
               } else if (!((float)mc.player.tickCount / 2e+01F < this.invincTimeout.getValue())) {
                  if (this.onRender.getValue()) {
                     for (Player var3 : mc.level.players()) {
                        if (!Hub.field_2603.method_1009(var3.getName().getString()) && var3 != mc.player) {
                           this.method_91(new PlaceholderText().method_2(var3.getName().getString()).method_9("\u0001 has entered your render distance!"));
                        }
                     }
                  }

                  if (!mc.level
                        .getEntitiesOfClass(
                           MinecartTNT.class,
                           mc.player
                              .getBoundingBox()
                              .inflate(
                                 4.0,
                                 4.0,
                                 4.0
                              ),
                           MinecartTNT::isPrimed
                        )
                        .isEmpty()
                     && this.tNTMinecart.getValue()) {
                     this.method_91("You are at risk of getting blown up by a TNT minecart!");
                  }

                  if (this.crystal.getValue()) {
                     mc.level
                        .entitiesForRendering()
                        .forEach(
                           var1x -> {
                              if (var1x instanceof EndCrystal
                                 && mc.player.distanceToSqr(var1x) <= 128.0) {
                                 double var2 = (double)ExplosionDamage.method_2(
                                    var1x.position(),
                                    mc.player,
                                    mc.player.getBoundingBox(),
                                    6.0,
                                    true,
                                    null,
                                    null
                                 );
                                 if (var2 >= (double)EntityUtil.method_76()) {
                                    this.method_91("You are at risk of getting blown up by an end crystal!");
                                 }
                              }
                           }
                        );
                  }

                  int var5 = (int)EntityUtil.method_76();
                  if (!mc.player.isDeadOrDying() && var5 != 0) {
                     if (!mc.isWindowActive() || !this.unfocusedOnly.getValue()) {
                        int var6 = mc.player
                           .getInventory()
                           .items
                           .stream()
                           .filter(var0 -> var0.getItem() == Items.TOTEM_OF_UNDYING)
                           .mapToInt(ItemStack::getCount)
                           .sum();
                        if (mc.player.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING) {
                           var6 += mc.player.getOffhandItem().getCount();
                        }

                        boolean var4 = var6 <= this.totemCount.getValue();
                        if (!this.totems.getValue()) {
                           var4 = true;
                        }

                        if (var5 <= this.health.getValue() && var4) {
                           this.method_91(new PlaceholderText().method_2(var5).method_9("Your health is too low! (\u0001)."));
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Subscribe
   public void method_9(FrameRenderEvent var1) {
      if (this.method_535()) {
         this.field_205 = null;
      }

      if (this.field_205 != null && this.field_206.method_9(1500L)) {
         this.field_205.run();
         this.field_205 = null;
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundDisconnectPacket) {
         this.field_205 = null;
      }
   }

   public boolean method_90() {
      ItemStack var1 = mc.player.getItemBySlot(EquipmentSlot.CHEST);
      if (var1.is(Items.ELYTRA) && var1.isDamageableItem()) {
         boolean var2 = InventoryUtil.method_29(var1) > (float)this.durability.getValue().intValue();
         if (var2) {
            return false;
         } else {
            if (this.assumeInventory.getValue()) {
               for (ItemStack var4 : mc.player.getInventory().items) {
                  if (var4.is(Items.ELYTRA) && (!var4.isDamageableItem() || InventoryUtil.method_29(var4) > (float)this.durability.getValue().intValue())) {
                     return false;
                  }
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public void method_91(String var1) {
      if (this.autoDisable.getValue()) {
         this.disable();
      }

      Connection var2 = mc.player.connection.getConnection();
      if (var2 != null) {
         Hub.field_2602.field_3453 = true;
         autoreconnect.disable();
         Runnable var3 = () -> var2.disconnect(
               Component.empty()
                  .append(
                     Component.nullToEmpty(
                        new PlaceholderText()
                           .method_2((Object)var1)
                           .method_2(String.valueOf(ChatFormatting.RED))
                           .method_2(String.valueOf(ChatFormatting.GRAY))
                           .method_9("\u0001[AutoLog] \u0001\u0001")
                     )
                  )
            );
         boolean var4 = !illegaldisconnect.ignoreProxy.getValue() || !IllegalDisconnectModule.method_205();
         if (illegaldisconnect.isToggled() && var4) {
            IllegalDisconnectModule.method_203();
            if (this.field_205 == null) {
               this.field_206.reset();
            }

            this.field_205 = var3;
            return;
         }

         var3.run();
      }
   }
}
