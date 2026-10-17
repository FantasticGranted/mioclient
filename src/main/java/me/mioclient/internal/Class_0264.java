package me.mioclient.internal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.NbtPathArgument.NbtPath;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.commands.data.EntityDataAccessor;

public class Class_0264 extends ChatCommand {
   public Class_0264() {
      super("nbt");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.executes(var0 -> {
         EntityDataAccessor var1x = new EntityDataAccessor(mc.player);
         NbtPath var2 = NbtPath.of("SelectedItem");
         MutableComponent var3 = Component.empty();
         var3 = var3.append("Nbt: ");

         try {
            List var4 = var2.get(var1x.getData());
            if (!var4.isEmpty()) {
               var3.append(" ").append(NbtUtils.toPrettyComponent((Tag)var4.getFirst()));
            }
         } catch (CommandSyntaxException var5) {
            var3.append("{}");
         }

         ChatUtil.method_2(var3, ChatUtil.method_38(-1));
         return 1;
      });
   }
}
