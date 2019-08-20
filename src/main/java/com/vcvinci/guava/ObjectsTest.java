package com.vcvinci.guava;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class ObjectsTest {

    public static void main(String[] args) {
        Objects.equal(null, "a");



    }

    class Person implements Comparable<Person> {
        private String lastName;
        private String firstName;
        private int zipCode;

        /*@Override
        public int compareTo(Person other) {
            int cmp = lastName.compareTo(other.lastName);
            if (cmp != 0) {
                return cmp;
            }
            cmp = firstName.compareTo(other.firstName);
            if (cmp != 0) {
                return cmp;
            }
            return Integer.compare(zipCode, other.zipCode);
        }*/

        @Override
        public int compareTo(Person other) {
            return ComparisonChain.start()
                    .compare(this.lastName, other.lastName).result();
        }
    }


}
