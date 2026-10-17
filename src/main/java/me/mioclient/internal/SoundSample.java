package me.mioclient.internal;

import java.io.ByteArrayInputStream;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.mixin.ducks.DuckAbstractSoundInstance;
import net.minecraft.world.phys.Vec3;

/** In-memory sound clip (raw ogg bytes) that can be played at a position or relative. */
public class SoundSample implements MinecraftAccess {
   public static final SoundSample field_1127 = new SoundSample(null) {
      @Override
      public void method_230(float var1) {
      }

      @Override
      public void method_2(Vec3 var1, float var2) {
      }
   };
   public final byte[] field_1128;

   public SoundSample(byte[] var1) {
      super();
      this.field_1128 = var1;
   }

   public void method_387() {
      this.method_230(1F);
   }

   public void getId(Vec3 var1) {
      this.method_2(var1, 1F);
   }

   public void method_230(float var1) {
      if (mc.getSoundManager() != null) {
         try {
            mc.execute(() -> {
               Class_0423 var2 = new Class_0423(new ByteArrayInputStream(this.method_388()));
               ((DuckAbstractSoundInstance)(Object)var2).setVolume(var1);
               mc.getSoundManager().play(var2);
            });
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }
   }

   public void method_2(Vec3 var1, float var2) {
      if (mc.level != null) {
         try {
            mc.execute(() -> {
               Class_0423 var3 = new Class_0423(var1, new ByteArrayInputStream(this.method_388()));
               ((DuckAbstractSoundInstance)(Object)var3).setVolume(var2);
               mc.getSoundManager().play(var3);
            });
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }
   }

   public byte[] method_388() {
      return this.field_1128;
   }
}
