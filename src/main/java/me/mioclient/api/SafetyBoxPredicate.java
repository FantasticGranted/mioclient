package me.mioclient.api;

import me.mioclient.record.SafetyBox;

/** A predicate over a SafetyBox, with client access. */
public interface SafetyBoxPredicate extends MinecraftAccess {
   boolean method_2(SafetyBox var1);
}
