package me.mioclient.internal;

import me.mioclient.api.GuiComponent;
import me.mioclient.enum_.MiniGame;

/** ClickGui panel hosting the built-in mini-game (Tetris/etc.). */
public class MiniGamePanel extends ClickGuiPanel {
   public MiniGame field_516 = MiniGame.TETRIS;

   public MiniGamePanel() {
      super("Game");
      this.method_9(new Class_0816(this, 0));
      this.method_9(new LabelComponent(this, () -> this.field_516.getName()) {
         @Override
         public void method_2(double var1, double var3, int var5) {
            super.method_2(var1, var3, var5);
            if (this.method_5(var1, var3) && var5 == 0) {
               MiniGamePanel.this.method_2(MiniGame.values()[(MiniGamePanel.this.field_516.ordinal() + 1) % MiniGame.values().length]);
            }
         }
      });
   }

   public void method_2(MiniGame var1) {
      this.field_516 = var1;

      Object var2 = switch (var1) {
         case TETRIS -> new Class_0816(this, 0);
         case SNAKE -> new Class_0571(this, 0);
      };
      synchronized (this.field_2378) {
         this.field_2378.set(0, (GuiComponent)var2);
      }
   }

   @Override
   public int method_216() {
      return 110;
   }
}
