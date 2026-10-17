package me.mioclient.module.abstract_;

import me.mioclient.Hub;
import me.mioclient.enum_.Dimension;
import me.mioclient.enum_.ScreenCorner;
import me.mioclient.enum_.SettingMode;
import me.mioclient.internal.HudTextElement;
import me.mioclient.internal.TextHudElement;
import me.mioclient.internal.MathUtil;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.player.FreecamModule;
import me.mioclient.record.Vec2i;
import me.mioclient.setting.BooleanSetting;
import me.mioclient.setting.CustomSetting3;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public class AbstractModule_18 extends AbstractModule_26 {
   public static FreecamModule freecam = Hub.field_2595.method_78(FreecamModule.class);
   public final Setting<Float> safeRange = this.add(new CustomSetting3<>("SafeRange", 0.0F, 0.0F, 1e+02F).method_22("k"));
   public final Setting<Boolean> nether = this.add(new BooleanSetting("Nether", true));
   public final Setting<Boolean> fake = this.add(new BooleanSetting("Fake", false));
   public Vec2i field_3279;

   public AbstractModule_18() {
      super("Position", "coordinates");
      this.safeRange.method_2("None", SettingMode.MIN);
      this.fake
         .method_9(
            () -> Hub.field_2619
                  .method_2(
                     () -> {
                        boolean var1x = MathUtil.method_17(50);
                        boolean var2 = MathUtil.method_17(50);
                        int var3 = (int)(
                           -mc.player.getX() * 2.0
                              + Math.random() * mc.player.getX() * 4.0
                        );
                        int var4 = (int)(
                           -mc.player.getZ() * 2.0
                              + Math.random() * mc.player.getZ() * 4.0
                        );
                        var3 += 13;
                        var4 -= 37;
                        if (var1x) {
                           var3 = -var3;
                        }

                        if (var2) {
                           var4 = -var4;
                        }

                        this.field_3279 = new Vec2i(var3, var4);
                     },
                     0
                  )
         );
      HudTextElement var1 = new HudTextElement(
         () -> {
            if (!this.method_535()
               && (
                  !(this.safeRange.getValue() > 0.0F)
                     || mc.player.position().closerThan(Vec3.ZERO, (double)(this.safeRange.getValue() * 1e+03F))
                     || mc.player.tickCount >= 200
               )) {
               boolean var1x = mc.level.dimension().identifier().getPath().contains("nether");
               Vec3 var2 = mc.player.position();
               if (freecam.isToggled()) {
                  var2 = mc.gameRenderer.getMainCamera().position().subtract(0.0, (double)mc.player.getBbHeight(), 0.0);
               }

               if (this.fake.getValue() && this.field_3279 != null) {
                  if (this.field_3279.method_401() == 0 && this.field_3279.getY() == 0) {
                     this.fake.method_6();
                  }

                  var2 = var2.add((double)this.field_3279.method_401(), 0.0, (double)this.field_3279.getY());
               }

               BlockPos var3 = BlockPos.containing(var2);
               BlockPos var4 = var1x
                  ? BlockPos.containing(
                     var2.multiply(
                        8.0,
                        1.0,
                        8.0
                     )
                  )
                  : BlockPos.containing(
                     var2.multiply(
                        0.125,
                        1.0,
                        0.125
                     )
                  );
               String var5 = new PlaceholderText().method_2(String.valueOf(ChatFormatting.WHITE)).method_9("XYZ: \u0001");
               String var6 = new PlaceholderText().method_2(var3.getZ()).method_2(var3.getY()).method_2(var3.getX()).method_9("\u0001, \u0001, \u0001.");
               String var7 = new PlaceholderText().method_2(var4.getZ()).method_2(var4.getY()).method_2(var4.getX()).method_9(" (\u0001, \u0001, \u0001.)");
               if (this.nether.getValue() && BlockUtil.method_1071() != Dimension.THE_END) {
                  var6 = new PlaceholderText().method_2((Object)var7).method_2((Object)var6).method_9("\u0001\u0001");
               }

               return Component.literal(new PlaceholderText().method_2((Object)var6).method_2((Object)var5).method_9("\u0001\u0001"));
            } else {
               return Component.literal("XYZ: %sREDACTED".formatted(ChatFormatting.WHITE));
            }
         },
         () -> true
      );
      this.method_2(new TextHudElement(this, var1));
      this.method_14().method_2(ScreenCorner.BOTTOM_LEFT);
   }
}
