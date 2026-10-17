package me.mioclient.internal;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import me.mioclient.api.MinecraftAccess;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.JOrbisAudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class Class_0423 extends AbstractSoundInstance {
   public InputStream field_1338;

   public Class_0423(Vec3 var1, InputStream var2) {
      super(Identifier.fromNamespaceAndPath(MinecraftAccess.method_244() ? "mioloader" : "mio", "sound"), SoundSource.MASTER, SoundInstance.createUnseededRandom());
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.field_1338 = var2;
   }

   public Class_0423(InputStream var1) {
      super(Identifier.fromNamespaceAndPath(MinecraftAccess.method_244() ? "mioloader" : "mio", "sound"), SoundSource.MASTER, SoundInstance.createUnseededRandom());
      this.field_1338 = var1;
      this.relative = true;
      this.x = 0.0;
      this.y = 0.0;
      this.z = -1.0;
   }

   public CompletableFuture<AudioStream> getAudioStream(SoundBufferLibrary var1, Identifier var2, boolean var3) {
      try {
         return CompletableFuture.completedFuture(new JOrbisAudioStream(this.field_1338));
      } catch (Exception var5) {
         return CompletableFuture.failedFuture(var5);
      }
   }
}
