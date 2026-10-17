package me.mioclient.internal;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Dimension;
import me.mioclient.enum_.ChunkType;
import me.mioclient.module.exploit.NewChunksModule;
import me.mioclient.record.TrackedChunk;
import net.minecraft.world.level.ChunkPos;

public class Class_0598 implements MinecraftAccess {
   public final Lock field_1866 = new ReentrantLock();
   public final NewChunksModule field_1867;
   public String field_1435;

   public Class_0598(NewChunksModule var1) {
      super();
      this.field_1867 = var1;
   }

   public void method_580(String var1) {
      THREAD_POOL.submit(LockUtil.method_9(this.field_1866, () -> this.method_468(var1)));
   }

   public void method_582(String var1) {
      THREAD_POOL.submit(LockUtil.method_9(this.field_1866, () -> this.method_176(var1)));
   }

   public void method_468(String var1) {
      if (!this.field_1867.method_971().isEmpty() && var1 != null) {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();

         try (DataOutputStream var3 = new DataOutputStream(var2)) {
            var3.writeInt(1);
            synchronized (this.field_1867.method_971()) {
               int var5 = 0;

               for (List var7 : this.field_1867.method_971().values()) {
                  var5 += var7.size();
               }

               var3.writeInt(var5);

               for (List var17 : this.field_1867.method_971().values()) {
                  for (TrackedChunk var9 : (Iterable<TrackedChunk>)(Iterable<?>)(var17)) {
                     ChunkPos var10 = var9.getChunkPos();
                     var3.writeInt(var10.x());
                     var3.writeInt(var10.z());
                     var3.writeByte(var9.method_418().ordinal());
                     var3.writeByte(var9.getType().ordinal());
                  }
               }
            }

            FileUtil.method_2(
               FileManager.field_4287.resolve(new PlaceholderText().method_2(FontRenderer.method_222(var1)).method_9("\u0001.data")), var2.toByteArray()
            );
         } catch (Exception var15) {
            var15.printStackTrace();
         }

         if (!this.field_1867.isToggled()) {
            this.field_1867.method_971().clear();
         }
      }
   }

   public void method_176(String var1) {
      Path var2 = FileManager.field_4287.resolve(new PlaceholderText().method_2(FontRenderer.method_222(var1)).method_9("\u0001.data"));
      if (var2.toFile().exists()) {
         try (DataInputStream var3 = new DataInputStream(new FileInputStream(var2.toFile()))) {
            int var4 = var3.readInt();
            int var5 = var3.readInt();
            ArrayList var6 = new ArrayList();

            for (int var7 = 0; var7 < var5; var7++) {
               int var8 = var3.readInt();
               int var9 = var3.readInt();
               byte var10 = var3.readByte();
               byte var11 = var3.readByte();
               if (var10 < Dimension.values().length && var11 < ChunkType.values().length) {
                  Dimension var12 = Dimension.values()[var10];
                  ChunkType var13 = ChunkType.values()[var11];
                  TrackedChunk var14 = new TrackedChunk(new ChunkPos(var8, var9), var12, var13);
                  var6.add(var14);
               }
            }

            this.field_1867.method_971().clear();

            for (TrackedChunk var19 : (Iterable<TrackedChunk>)(Iterable<?>)(var6)) {
               this.field_1867.method_2(var19);
            }
         } catch (Exception var17) {
         }
      }
   }

   public String method_609() {
      return this.field_1435;
   }

   public void method_27(String var1) {
      this.field_1435 = var1;
   }
}
