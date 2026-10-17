package me.mioclient.internal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.record.RenderStyle;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Class_0213 extends ChatCommand {
   public static final Color field_596 = new Color(255, 0, 0, 120);
   public static final Color field_597 = new Color(0, 0, 0, 0);

   public Class_0213() {
      super("highlight");
      Hub.field_2616
         .method_2(
            new RenderStyle(
               this,
               () -> field_596,
               () -> field_597,
               () -> 1F,
               () -> 5F,
               () -> true,
               () -> true,
               1000
            )
         );
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.then(
         RequiredArgumentBuilder.<SharedSuggestionProvider, Vec3>argument("pos", new PositionArgumentType())
               .then(
                  LiteralArgumentBuilder.<SharedSuggestionProvider>literal("beam")
                     .executes(
                        var1x -> {
                           Vec3 var2 = (Vec3)var1x.getArgument("pos", Vec3.class);
                           AABB var3 = AABB.ofSize(
                              var2,
                              0.25,
                              0.25,
                              0.25
                           );
                           var3 = var3.setMaxY((double)mc.level.getMaxY()).setMinY((double)mc.level.getMinY());
                           Hub.field_2616.method_2(this, var3);
                           return 1;
                        }
                     )
               )
            .executes(
               var1x -> {
                  Vec3 var2 = (Vec3)var1x.getArgument("pos", Vec3.class);
                  AABB var3 = AABB.ofSize(
                     var2,
                     0.25,
                     0.25,
                     0.25
                  );
                  Hub.field_2616.method_2(this, var3);
                  return 1;
               }
            )
      );
   }
}
