package me.mioclient.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import java.awt.Color;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0204;
import me.mioclient.internal.BufferSource;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.Class_0969;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import org.lwjgl.opengl.GL32C;

public class TrajectoriesModule extends Module {
   public final Class_0969 field_2360 = new Class_0969();
   public Setting<Boolean> onlyOwn;
   public Setting<Boolean> predict;
   public Setting<Boolean> airborne;
   public Setting<Float> lineWidth;
   public Setting<Color> color;
   public Setting<Boolean> targets;
   public Setting<Boolean> bow;
   public Setting<Boolean> xBow;
   public Setting<Boolean> trident;
   public Setting<Boolean> pearls;
   public Setting<Boolean> exp;
   public Setting<Boolean> others;
   public BufferBuilder field_2373;
   public Vec3 field_2374;
   public float field_876;

   public TrajectoriesModule() {
      super("Trajectories", "Draws trajectories for projectiles and bows.", Category.RENDER);
      Settings.initialize(this);
      this.field_2374 = Vec3.ZERO;
      this.setDrawn(false);
   }

   @Subscribe
   public void method_9(PostTickEvent var1) {
      this.field_2374 = mc.player.getDeltaMovement();
      this.field_876 = BowItem.getPowerForTime(mc.player.getTicksUsingItem());
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      Tesselator var2 = Tesselator.getInstance();
      BufferBuilder var3 = var2.begin(Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
      this.field_2373 = var3;

      for (Entity var5 : mc.level.entitiesForRendering()) {
         if (this.method_221(var5)) {
            Class_0204 var6 = new Class_0204(this);
            if (var5 instanceof Projectile) {
               Projectile var7 = (Projectile)var5;
               if (!this.field_2360.method_2(var7, true, (double)var1.method_880())) {
                  continue;
               }
            } else if (var5 instanceof Player) {
               Player var8 = (Player)var5;
               if (!this.field_2360.method_2(var8, var8.getItemInHand(var8.getUsedItemHand()), 0.0, true, (double)var1.method_880())) {
                  continue;
               }
            }

            var6.method_239();
            var6.method_80(var5);
            var6.method_4(var1);
            var6.method_208();
         }
      }


      RenderUtil.field_2672.method_780();
      GL32C.glLineWidth(this.lineWidth.getValue());
      BufferSource.method_2(var3);
      RenderUtil.field_2672.method_782();
   }

   public boolean method_221(Entity var1) {
      if (var1 instanceof WitherSkull) {
         return false;
      } else {
         return var1 instanceof Player && this.predict.getValue()
            ? var1 == mc.player || !this.onlyOwn.getValue()
            : var1 instanceof Projectile && this.airborne.getValue();
      }
   }

   public Vec3 method_710() {
      return this.field_2374;
   }

   public float method_711() {
      return this.field_876;
   }
}
