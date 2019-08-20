package com.vcvinci.classloader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @Auther: Administrator
 * @Date: 2018/6/11 17:32
 * @Description:
 */
public class ClassLoaderTree {
    public static void main(String[] args) throws IOException {
        /*ClassLoader loader = ClassLoaderTree.class.getClassLoader();
        *//*while (loader != null) {
            System.out.println(loader.toString());
            loader = loader.getParent();
        }*//*
        Enumeration<URL> paths = loader
                .getResources(ClassLoaderTree.class.getName());

      while (paths.hasMoreElements()) {
          URL path = (URL) paths.nextElement();
          System.out.println(path);
      }*/
        int i = 8 / 0;
        System.out.println(i);
    }
}
