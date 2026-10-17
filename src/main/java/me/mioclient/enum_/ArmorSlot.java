package me.mioclient.enum_;

/** The four armor slots with their container slot id and armor index. */
public enum ArmorSlot {
   HELMET(39, 3),
   CHESTPLATE(38, 2),
   LEGGINGS(37, 1),
   BOOTS(36, 0);

   public final int slotId;
   public final int field_1822;

    ArmorSlot(int var3, int var4) {
      this.slotId = var3;
      this.field_1822 = var4;
   }

   public int method_34() {
      return this.slotId;
   }

   public int getArmorIndex() {
      return this.field_1822;
   }
}
