package me.mioclient.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.FrameRenderEvent;
import me.mioclient.event.DisconnectEvent;
import me.mioclient.event.EntityRemoveEvent;
import me.mioclient.event.Subscribe;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public final class Class_0628 implements MinecraftAccess {
   public final Map<Integer, Class_0308> field_2002 = Collections.synchronizedMap(new HashMap<>());

   public Class_0628() {
      super();
      lookups.method_14(this);
   }

   @Subscribe
   public void method_2(FrameRenderEvent var1) {
      if (!this.method_535()) {
         for (AbstractClientPlayer var3 : mc.level.players()) {
            this.field_2002.putIfAbsent(var3.getId(), new Class_0308(var3));
            this.field_2002.get(var3.getId()).method_142();
         }
      }
   }

   @Subscribe
   public void method_2(EntityRemoveEvent var1) {
      synchronized (this.field_2002) {
         this.field_2002.remove(var1.getId());
      }
   }

   @Subscribe
   public void method_2(DisconnectEvent var1) {
      this.field_2002.clear();
   }

   public synchronized AABB method_2(Player var1, int var2) {
      if (!this.field_2002.containsKey(var1.getId())) {
         return var1.getBoundingBox();
      } else {
         List var3 = this.field_2002.get(var1.getId()).method_359();
         return var3.isEmpty() ? var1.getBoundingBox() : (AABB)var3.get(Math.min(var2, var3.size() - 1));
      }
   }
}
