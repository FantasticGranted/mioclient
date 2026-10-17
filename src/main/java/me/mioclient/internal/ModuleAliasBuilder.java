package me.mioclient.internal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

/**
 * Literal argument builder that produces a {@link ModuleAliasNode} instead of a plain node.
 */
public class ModuleAliasBuilder<S> extends LiteralArgumentBuilder<S> {
   public ModuleAliasBuilder(String var1) {
      super(var1);
   }

   public LiteralCommandNode<S> build() {
      ModuleAliasNode var1 = new ModuleAliasNode(
         this.getLiteral(), this.getCommand(), this.getRequirement(), this.getRedirect(), this.getRedirectModifier(), this.isFork()
      );

      for (CommandNode var3 : this.getArguments()) {
         var1.addChild(var3);
      }

      return var1;
   }
}
