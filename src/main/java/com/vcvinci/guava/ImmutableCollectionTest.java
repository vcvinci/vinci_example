package com.vcvinci.guava;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class ImmutableCollectionTest {

    public static final ImmutableSet<String> COLOR_NAMES = ImmutableSet.of(
            "red",
            "orange",
            "yellow",
            "green",
            "blue",
            "purple");

    static class Foo {
        Set<String> bars;
        Foo(Set<String> bars) {
            this.bars = ImmutableSet.copyOf(bars); // defensive copy!
        }
    }
}
