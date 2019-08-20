package com.vcvinci.guava;

import static com.google.common.base.Preconditions.checkNotNull;

//import static org.apache.commons.lang.StringUtils.*;
//import static org.junit.Assert.*;
public class PreconditionTest {

    public static void main(String[] args) {
        Integer testNum = 2;
        System.out.println(checkNotNull(testNum));
    }

}
