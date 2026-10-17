package me.mioclient.internal;

import me.mioclient.entityculling.versionless.EntityCullingVersionlessBase;
import me.mioclient.api.Class_0410;

public class Class_1060 implements Class_0410 {
   public Class_1060() {
      super();
   }

   @Override
   public boolean method_458() {
      return EntityCullingVersionlessBase.enabled;
   }

   @Override
   public void method_39(boolean var1) {
      EntityCullingVersionlessBase.enabled = var1;
   }
}
