package me.mioclient.module.abstract_;

import me.mioclient.enum_.ScreenCorner;
import me.mioclient.internal.HudTextElement;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.TextHudElement;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import me.mioclient.internal.PlaceholderText;

public class AbstractModule_25 extends AbstractModule_26 {
   public AbstractModule_25() {
      super("Direction", "facing");
      this.method_2(new TextHudElement(this, new HudTextElement(() -> Component.literal(this.method_254()), () -> true)));
      this.method_14().method_2(ScreenCorner.BOTTOM_LEFT);
   }

   public String method_254() {
      String var1 = new PlaceholderText()
         .method_2((int)Mth.wrapDegrees(mc.gameRenderer.getMainCamera().yRot()))
         .method_2(String.valueOf(ChatFormatting.WHITE))
         .method_9("\u0001 (\u0001, ");

      return switch (RotationManager.method_221(mc.gameRenderer.getMainCamera().yRot())) {
         case 0 -> new PlaceholderText().method_2((Object)var1).method_9("South\u0001+Z)");
         case 1 -> new PlaceholderText().method_2((Object)var1).method_9("SouthWest\u0001-X +Z)");
         case 2 -> new PlaceholderText().method_2((Object)var1).method_9("West\u0001-X)");
         case 3 -> new PlaceholderText().method_2((Object)var1).method_9("NorthWest\u0001-X -Z)");
         case 4 -> new PlaceholderText().method_2((Object)var1).method_9("North\u0001-Z)");
         case 5 -> new PlaceholderText().method_2((Object)var1).method_9("NorthEast\u0001+X -Z)");
         case 6 -> new PlaceholderText().method_2((Object)var1).method_9("East\u0001+X)");
         case 7 -> new PlaceholderText().method_2((Object)var1).method_9("SouthEast\u0001+X +Z)");
         default -> "Waiting...";
      };
   }
}
