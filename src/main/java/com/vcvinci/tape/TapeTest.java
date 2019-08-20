package com.vcvinci.tape;

import com.squareup.tape2.QueueFile;

import java.io.File;
import java.io.IOException;

/**
 * @author vinci
 * @Title: TapeTest
 * @ProjectName Leecode
 * @Description: TODO
 * @date 2019-06-0309:47
 */
public class TapeTest {
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/vinci/bin/tape/test.log");
        QueueFile queueFile = new QueueFile.Builder(file).build();
        byte[] bytes = "data".getBytes();
        System.out.println("不用特殊：" + bytes);
        queueFile.add(bytes);
        byte[] data = queueFile.peek();
        System.out.println(data.toString());
    }
}
