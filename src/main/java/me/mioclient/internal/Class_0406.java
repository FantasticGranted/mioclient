package me.mioclient.internal;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import me.mioclient.Hub;
import me.mioclient.api.GuiComponent;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_26;
import me.mioclient.module.client.HUDModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import org.lwjgl.glfw.GLFW;

public class Class_0406 extends ClickGui implements MinecraftAccess {
   public SettingWidget<?> field_1306 = null;
   public static final int[] field_1307 = new int[]{265, 265, 264, 264, 263, 262, 263, 262, 66, 65};
   public int current = 0;
   public boolean field_1308;
   public boolean field_1309;
   public EditBox field_1310;
   public final Timer field_1311 = new Timer();
   public String field_1312;
   public List<ModulePanel> field_1313 = new ArrayList<>();

   public Class_0406() {
      super();
      this.field_1308 = FileManager.field_4289.toFile().exists();
      int var1 = 10;

      for (Category var5 : Category.values()) {
         if (var5 != Category.HUD) {
            ModuleCategoryPanel var6 = new ModuleCategoryPanel(var5);
            var6.setX(var1);
            this.field_3462.add(var6);
            var1 += var6.method_216() + 3;
         }
      }

      if (this.field_1308) {
         MiniGamePanel var7 = new MiniGamePanel();
         var7.setX(var1);
         var7.method_80(false);
         this.field_3462.add(var7);
      }
   }

   @Override
   public void init() {
      super.init();
      this.field_1312 = "";
      this.field_1310 = new EditBox(mc.font, 7, -100, 100, 9, Component.literal("Search"));
      this.field_1310.setVisible(true);
      this.field_1310.setEditable(true);
      this.field_1310.setMaxLength(64);
      this.field_1310
         .setResponder(
            var1 -> {
               if (!var1.isEmpty()) {
                  for (Module var3 : Hub.field_2599.getRegistry()) {
                     if (!(var3 instanceof HUDModule)
                        && !(var3 instanceof AbstractModule_26)
                        && var3.getName().toLowerCase(Locale.ROOT).startsWith(var1.toLowerCase(Locale.ROOT))) {
                        this.field_1312 = var3.getName().substring(this.field_1310.getCursorPosition());
                        return;
                     }
                  }
               }

               this.field_1312 = "";
            }
         );
      this.addRenderableWidget(this.field_1310);
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (this.field_1313.size() >= 3) {
         this.field_1313.getFirst().field_111 = false;
      }

      this.field_1313.removeIf(ModulePanel::isClosed);
      if (this.field_1310.isFocused()) {
         FontRenderer var10000 = FontRenderer.field_3143;
         String var10001 = this.field_1310.getValue();
         String var10002 = this.field_1312;
         float var5 = var10000.method_221(new PlaceholderText().method_2((Object)var10002).method_2((Object)var10001).method_9("\u0001\u0001_"));
         var10000 = FontRenderer.field_3143;
         var10002 = this.field_1310.getValue();
         String var10003 = String.valueOf(ChatFormatting.GRAY);
         String var10004 = this.field_1312;
         var10000.method_9(
            var1,
            new PlaceholderText()
               .method_2(this.method_453())
               .method_2(String.valueOf(ChatFormatting.WHITE))
               .method_2((Object)var10004)
               .method_2((Object)var10003)
               .method_2((Object)var10002)
               .method_9("\u0001\u0001\u0001\u0001\u0001"),
            (float)mc.getWindow().getGuiScaledWidth() / 2F - var5 / 2F,
            (float)mc.getWindow().getGuiScaledHeight() / 2F
               - (float)FontRenderer.field_3143.method_66() / 2F,
            Color.white
         );
      } else if (Hub.field_2609.method_802() != 8) {
         String var6 = "Ctrl + F to activate search.";
         FontRenderer.field_3143
            .method_9(
               var1,
               var6,
               (float)mc.getWindow().getGuiScaledWidth() - FontRenderer.field_3143.method_221(var6),
               (float)(mc.getWindow().getGuiScaledHeight() - FontRenderer.field_3143.method_66()),
               Colors.method_2(Color.GRAY, Color.WHITE, 2e+03, 2.5e+02)
            );
      }
   }

   @Override
   public boolean keyPressed(KeyEvent event) {
      int keyCode = event.key();
      int scanCode = event.scancode();
      int modifiers = event.modifiers();
      super.keyPressed(event);
      if (!this.field_1308 && field_1307[this.current] == keyCode) {
         this.current++;
         if (this.current == field_1307.length) {
            try {
               FileManager.field_4289.toFile().createNewFile();
            } catch (IOException var9) {
            }

            this.field_1308 = true;
            this.current = 0;
            MiniGamePanel var4 = new MiniGamePanel();
            var4.method_80(true);
            var4.setX(mc.getWindow().getGuiScaledWidth() / 2 - var4.method_216() / 2);
            var4.setY(mc.getWindow().getGuiScaledHeight() / 2 - var4.method_66() / 2);
            this.field_3462.add(var4);
         }
      } else {
         this.current = 0;
      }

      switch (keyCode) {
         case 70:
            if (GLFW.glfwGetKey(mc.getWindow().handle(), 341) == 1) {
               if (this.field_1310.isFocused()) {
                  this.reset();
               } else {
                  this.setInitialFocus(this.field_1310);
               }
            }
            break;
         case 257:
            label53:
            for (ClickGuiPanel var5 : this.field_3462) {
               for (GuiComponent var7 : var5.method_714()) {
                  if (var7 instanceof ModulePanel var8) {
                     String var11 = var8.method_65().getName();
                     String var12 = this.field_1310.getValue();
                     String var13 = this.field_1312;
                     if (var11.equalsIgnoreCase(new PlaceholderText().method_2((Object)var13).method_2((Object)var12).method_9("\u0001\u0001"))) {
                        var8.field_111 = true;
                        var8.method_70();
                        break label53;
                     }
                  }
               }
            }

            this.reset();
            break;
         case 258:
            if (this.field_1310.isFocused()) {
               EditBox var10000 = this.field_1310;
               String var10001 = this.field_1310.getValue();
               String var10002 = this.field_1312;
               var10000.setValue(new PlaceholderText().method_2((Object)var10002).method_2((Object)var10001).method_9("\u0001\u0001"));
            }
            break;
         case 259:
         case 261:
            if (this.field_1310.isFocused() && !this.field_1310.getValue().isEmpty()) {
               this.field_1310.setValue(this.field_1310.getValue().substring(0, this.field_1310.getValue().length() - 1));
            }
      }

      return true;
   }

   public void filesDragged(List<Path> paths) {
      Class_0227.method_2(this, paths);
   }

   @Override
   public void reset() {
      super.reset();
      this.setFocused(null);
      this.field_1310.setFocused(false);
      this.field_1310.setValue("");
      this.field_1310.setSuggestion("");
   }

   @Override
   public void method_257() {
      super.method_257();
      THREAD_POOL.submit(() -> Hub.field_2597.method_357());
   }

   public EditBox method_452() {
      return this.field_1310;
   }

   public String method_453() {
      if (this.field_1311.method_9(500L)) {
         this.field_1309 = !this.field_1309;
         this.field_1311.reset();
      }

      return this.field_1309 ? "_" : "";
   }

   public SettingWidget<?> method_454() {
      return this.field_1306;
   }

   public void method_2(SettingWidget<?> var1) {
      this.field_1306 = var1;
   }
}
