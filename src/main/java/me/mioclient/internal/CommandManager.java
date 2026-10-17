package me.mioclient.internal;

import net.minecraft.server.permissions.PermissionSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.api.JsonConfig;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.RelationType;
import me.mioclient.event.MessageSendEvent;
import me.mioclient.event.KeyPressEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.module.Module;
import me.mioclient.module.client.UIModule;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.commands.SharedSuggestionProvider;

/**
 * Registers all client chat commands (and per-module alias commands) into a brigadier
 * dispatcher, parses/executes chat input with the configurable prefix, persisted to json.
 */
public final class CommandManager extends NamedRegistry<ChatCommand> implements MinecraftAccess, JsonConfig {
   public static UIModule clickgui = Hub.field_2595.method_78(UIModule.class);
   public static final SharedSuggestionProvider field_3190 = new ClientSuggestionProvider(null, mc, net.minecraft.server.permissions.PermissionSet.ALL_PERMISSIONS);
   public static final CommandDispatcher<SharedSuggestionProvider> field_3191 = new CommandDispatcher();
   public static String prefix = ";";

   public CommandManager() {
      super();
      lookups.method_14(this);
      MioCommands.init(this);      // replaces the loader's nick.Commands.initialize for our package layout
      this.method_929();
      this.method_931();
      this.method_930();
   }

   public static String method_927() {
      return prefix;
   }

   public static void method_270(String var0) {
      if (!var0.isBlank()) {
         String var1 = var0.substring(0, 1);
         prefix = var1;
         clickgui.prefix.method_78((String)var1);
      }
   }

   public static char method_928() {
      return prefix.charAt(0);
   }

   public void method_929() {
      if (Hub.field_2630.method_264()) {
         this.method_2((ChatCommand)(new Class_1321()));
      }
   }

   public void method_930() {
      for (Module var2 : (Iterable<Module>)(Iterable<?>)((List<me.mioclient.module.Module>)Hub.field_2599.getRegistry())) {
         if (!this.method_2(var1 -> var1.getName().equalsIgnoreCase(var2.getName())).isPresent()) {
            for (String var6 : var2.getAliases()) {
               ModuleAliasBuilder var7 = new ModuleAliasBuilder(var6.toLowerCase());
               new ModuleCommand(var2).exec(var7);
               field_3191.register(var7);
            }
         }
      }
   }

   public static void method_7(String var0) {
      MessageSendEvent var1 = new MessageSendEvent(var0);
      MinecraftAccess.lookups.method_36(var1);
      if (!var1.method_464()) {
         try {
            field_3191.execute(field_3191.parse(var0, field_3190));
         } catch (CommandSyntaxException var3) {
         }
      }
   }

   public void method_931() {
      this.method_2((ChatCommand)(new Class_0881(RelationType.FRIEND)));
      this.method_2((ChatCommand)(new Class_0881(RelationType.ENEMY)));
   }

   public boolean method_2(ChatCommand var1) {
      LiteralArgumentBuilder var2 = LiteralArgumentBuilder.<SharedSuggestionProvider>literal(var1.getName());
      var1.exec(var2);
      LiteralCommandNode var3 = field_3191.register(var2);

      for (String var7 : var1.getAliases()) {
         HiddenCommandBuilder var8 = new HiddenCommandBuilder(var7);

         for (CommandNode var10 : (Iterable<CommandNode>)(Iterable<?>)(var3.getChildren())) {
            var8.then(var10);
         }

         var8.executes(var1x -> var3.getCommand().run(var1x));
         field_3191.register(var8);
      }

      return this.field_3243.add(var1);
   }

   @Subscribe
   public void method_2(KeyPressEvent var1) {
      if (method_927().length() == 1 && method_927().charAt(0) == var1.method_1167()) {
         mc.setScreen(new ChatScreen("", true)); // 26.1: ChatScreen(String, boolean)
      }
   }

   @Override
   public JsonElement toJson() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("prefix", prefix);
      return var1;
   }

   @Override
   public void fromJson(JsonElement var1) {
      if (var1.getAsJsonObject().has("prefix")) {
         method_270(var1.getAsJsonObject().get("prefix").getAsString());
      }
   }

   @Override
   public String getConfigName() {
      return "commands.json";
   }
}
