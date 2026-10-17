package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.api.GuiComponent;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.Category;
import me.mioclient.module.Module;

/** ClickGui panel listing all modules belonging to one Category. */
public class ModuleCategoryPanel extends ClickGuiPanel implements MinecraftAccess {
   public Category field_37;

   public ModuleCategoryPanel(Category var1) {
      super(FontRenderer.method_3(var1.getName()));
      this.field_37 = var1;

      for (Module var3 : Hub.field_2599.method_2(var1)) {
         ModulePanel var4 = new ModulePanel(var3, this, 0);
         this.field_2378.add(var4);
      }

      this.method_142();
   }

   @Override
   public boolean method_2(GuiComponent var1) {
      net.minecraft.client.gui.components.EditBox search =
            Hub.method_755() == null ? null : Hub.method_755().method_452();
      String query = search == null ? "" : search.getValue();
      if (var1 instanceof ModulePanel var2 && !query.isEmpty()) {
         for (String var6 : var2.method_65().getAliases()) {
            if (var6.toLowerCase().contains(query.toLowerCase())) {
               return true;
            }
         }

         return false;
      }

      return true;
   }
}
