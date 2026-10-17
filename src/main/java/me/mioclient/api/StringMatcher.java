package me.mioclient.api;

import java.util.function.BiPredicate;

/** A predicate over a pair of strings. */
public interface StringMatcher extends BiPredicate<String, String> {
}
