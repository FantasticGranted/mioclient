package me.mioclient.internal;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.deobf.Named;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.record.RenderStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public final class Class_0178 extends CollectionRegistry<RenderStyle, List<RenderStyle>> implements MinecraftAccess {
   public final Map<RenderStyle, Set<FadingBox>> field_502 = new HashMap<>();

   public Class_0178() {
      super(new ArrayList<>());
      MinecraftAccess.lookups.method_14(this);
   }

   public void method_2(Named var1, BlockPos var2) {
      if (var2 != null) {
         this.method_2(var1, new AABB(var2));
      }
   }

   public void method_2(Named var1, AABB var2) {
      if (var2 != null) {
         RenderStyle var3 = this.method_9(var1x -> var1x.getOwner().equals(var1));
         if (var3 == null) {
            throw new NoSuchElementException();
         } else {
            synchronized (this.field_502) {
               Set var5 = this.field_502.get(var3);
               if (var3.isConditionMet()) {
                  for (FadingBox var7 : (Iterable<FadingBox>)(Iterable<?>)(var5)) {
                     if (var2.equals(var7.method_1017())) {
                        var7.method_1016();
                        return;
                     }
                  }
               } else {
                  var5.clear();
               }

               FadingBox var10 = new FadingBox();
               var10.method_29(var2);
               var10.method_1016();
               var5.add(var10);
            }
         }
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      synchronized (this.field_502) {
         for (Entry var4 : this.field_502.entrySet()) {
            ((Set<FadingBox>)var4.getValue()).removeIf(var1x -> var1x.method_37(((RenderStyle)var4.getKey()).getFadeDuration()) == 0.0F);
         }
      }
   }

   @Subscribe
   public void method_5(Event_3 var1) {
      synchronized (this.field_502) {
         for (Entry var4 : this.field_502.entrySet()) {
            RenderStyle var5 = (RenderStyle)var4.getKey();

            for (FadingBox var7 : (Iterable<FadingBox>)(Iterable<?>)((Set)var4.getValue())) {
               var7.method_17(var5.method_760());
               var7.method_2(var1.method_10(), var5.getFillColor(), var5.getOutlineColor(), var5.getFadeDuration(), var5.isFading());
            }
         }
      }
   }

   public boolean method_2(RenderStyle var1) {
      this.field_502.put(var1, new ObjectLinkedOpenHashSet());
      return ((List)this.getRegistry()).add(var1);
   }

   public boolean method_9(RenderStyle var1) {
      throw new UnsupportedOperationException();
   }

   @Override public boolean register(RenderStyle var1)   { return method_2(var1); }
   @Override public boolean unregister(RenderStyle var1) {
      this.field_502.remove(var1);
      return ((java.util.List<?>)this.getRegistry()).remove(var1);
   }
}
