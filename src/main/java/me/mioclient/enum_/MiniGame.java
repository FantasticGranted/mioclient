package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Selectable built-in mini-game. */
public enum MiniGame implements Nameable {
   TETRIS("Tetris"),
   SNAKE("Snake");

   public final String displayName;

    MiniGame(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
