package me.mioclient.internal;

import java.io.InputStream;
import java.util.Optional;
import java.util.Set;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackResources.ResourceOutput;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.Nullable;

public class Class_0859 implements PackResources {
   public PackLocationInfo field_2769 = new PackLocationInfo("mio", Component.literal("mio"), PackSource.BUILT_IN, Optional.empty());

   public Class_0859() {
      super();
   }

   @Nullable
   public IoSupplier<InputStream> getRootResource(String... segments) {
      return null;
   }

   @Nullable
   public IoSupplier<InputStream> getResource(PackType type, Identifier id) {
      return null;
   }

   public void listResources(PackType type, String namespace, String prefix, ResourceOutput consumer) {
   }

   public Set<String> getNamespaces(PackType type) {
      return null;
   }

   @Nullable
   public <T> T getMetadataSection(MetadataSectionType<T> metaReader) {
      return null;
   }

   public PackLocationInfo location() {
      return this.field_2769;
   }

   public void close() {
   }
}
