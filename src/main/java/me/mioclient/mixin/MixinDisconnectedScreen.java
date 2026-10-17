package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.misc.AutoReconnectModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.components.Button.Builder;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({DisconnectedScreen.class})
public class MixinDisconnectedScreen extends Screen {
   private static AutoReconnectModule autoreconnect = Hub.field_2595.method_78(AutoReconnectModule.class);
   @Unique
   private long startTime;
   @Shadow
   @Final
   private DisconnectionDetails details;
   @Shadow
   @Final
   private LinearLayout layout;
   @Unique
   private Button reconnectButton;
   @Unique
   private Button autoReconnectButton;

   protected MixinDisconnectedScreen(Component var1) {
      super(var1);
   }

   @Inject(
      method = {"init"},
      at = {@At(
         value = "INVOKE",
          target = "Lnet/minecraft/client/gui/screens/DisconnectedScreen;repositionElements()V",
         shift = Shift.BEFORE
      )}
   )
   private void initHook(CallbackInfo var1) {
      this.startTime = System.currentTimeMillis();
      Builder var2 = Button.builder(Component.literal("Reconnect"), var1x -> {
         ServerData var2x = Hub.field_2602.method_991();
         if (var2x != null) {
            ConnectScreen.startConnecting(new JoinMultiplayerScreen(new TitleScreen()), Minecraft.getInstance(), ServerAddress.parseString(var2x.ip), var2x, true, null);
         }
      }).width(200);
      Builder var3 = Button.builder(Component.literal("AutoReconnect"), var1x -> {
         if (!autoreconnect.isToggled()) {
            Hub.field_2602.field_3453 = false;
         }

         autoreconnect.method_68();
         this.startTime = System.currentTimeMillis();
         var1x.setFocused(false);
      }).width(200);
      this.reconnectButton = var2.build();
      this.autoReconnectButton = var3.build();
       this.layout.arrangeElements();
      this.addRenderableWidget(this.reconnectButton);
      this.addRenderableWidget(this.autoReconnectButton);
   }

    @Inject(
       method = {"repositionElements"},
       at = {@At("TAIL")}
    )
    private void initTabHook(CallbackInfo var1) {
       if (this.reconnectButton != null) {
          int var2 = this.width / 2 - 100;
          this.reconnectButton.setPosition(var2, Math.min(this.height / 2 + this.layout.getHeight() / 2, this.height - 30) - 1);
          this.autoReconnectButton.setPosition(var2, Math.min(this.height / 2 + this.layout.getHeight() / 2, this.height - 30) + 20);
       }
    }

   public void tick() {
      String var1 = this.details.reason().getString();
      if (var1.contains("Mio") && var1.contains("[AutoLog]")) {
         this.autoReconnectButton.setX(-1000);
         this.autoReconnectButton.setY(-1000);
      } else if (autoreconnect.isToggled()) {
         float var2 = this.getAutoReconnectTime(autoreconnect);
         if (!(var2 > 0.0F)) {
            ServerData var3 = Hub.field_2602.method_991();
            if (var3 != null) {
               ConnectScreen.startConnecting(new JoinMultiplayerScreen(new TitleScreen()), Minecraft.getInstance(), ServerAddress.parseString(var3.ip), var3, true, null);
            }
         }
      }
   }

    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
       String var5 = this.details.reason().getString();
       if (var5.contains("Mio") && var5.contains("[AutoLog]")) {
          this.autoReconnectButton.setX(-1000);
          this.autoReconnectButton.setY(-1000);
       } else {
          this.autoReconnectButton.setMessage(this.getAutoReconnectText());
          this.autoReconnectButton.setFocused(false);
          super.extractRenderState(context, mouseX, mouseY, delta);
       }
    }

   private Component getAutoReconnectText() {
      MutableComponent var1 = Component.literal("AutoReconnect ");
      String var2 = this.details.reason().getString();
      boolean var3 = autoreconnect.isToggled() && (!var2.contains("Mio") || !var2.contains("[AutoLog]"));
      float var4 = this.getAutoReconnectTime(autoreconnect);
      if (Hub.field_2602.field_3453) {
         return var1.append(ChatFormatting.RED + "AutoLogged");
      } else {
         return var3 ? var1.append(ChatFormatting.GREEN + "%.1fs".formatted(var4)) : var1.append(ChatFormatting.RED + "OFF");
      }
   }

   private float getAutoReconnectTime(AutoReconnectModule var1) {
      return var1 == null ? 0.1F : Math.max(var1.delay.getValue() - (float)(System.currentTimeMillis() - this.startTime) / 1000.0F, 0.0F);
   }
}
