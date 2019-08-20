package com.vcvinci.guava;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.sun.istack.internal.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrderingTest {

    public static void main(String[] args) {
        Ordering<Foo> ordering = Ordering.natural().nullsFirst().onResultOf(new Function<Foo, Comparable>() {
            @Override
            public String apply(Foo foo) {
                return foo.sortedBy;
            }
        });
        List<Foo> list = Arrays.asList(new Foo("1"),new Foo("2"),new Foo("g")
                ,new Foo("z"),new Foo("a"),new Foo("d"),new Foo(null, 3));
        Collections.sort(list,ordering);
        list.forEach(foo -> System.out.println(foo.toString()));

    }

    static class Foo {
        @Nullable
        String sortedBy;
        int notSortedBy;

        public Foo(String sortedBy) {
            this.sortedBy = sortedBy;
        }

        public Foo(String sortedBy, int notSortedBy) {
            this.sortedBy = sortedBy;
            this.notSortedBy = notSortedBy;
        }

        @Override
        public String toString() {
            return "Foo{" +
                    "sortedBy='" + sortedBy + '\'' +
                    ", notSortedBy=" + notSortedBy +
                    '}';
        }
    }



}
