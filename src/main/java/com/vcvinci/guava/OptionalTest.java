package com.vcvinci.guava;

import com.google.common.base.Optional;

public class OptionalTest {

    public static void main(String[] args) {
        Optional<Integer> possible = Optional.of(4);
        System.out.println(possible.isPresent());
        System.out.println(possible.get());

        Integer testNum = null;

    }
}
