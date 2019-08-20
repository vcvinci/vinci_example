package com.vcvinci.exception;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @Auther: Administrator
 * @Date: 2018/7/13 15:03
 * @Description:
 */
public class ByteTest {

    public static byte[] objectToByte(Object obj) {
        byte[] bytes = null;
        try {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private Object ByteToObject(byte[] bytes) {
        Object obj = null;
        try {
            // bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    public static byte[] serialize(Integer data) {
        if (data == null) {
            return null;
        }

        return new byte[] {
                (byte) (data >>> 24),
                (byte) (data >>> 16),
                (byte) (data >>> 8),
                data.byteValue()
        };
    }

    public static void main(String[] args) {
        Integer test = new Integer(129);
        byte[] valueBytes = ByteTest.objectToByte(test);
        Arrays.asList(valueBytes).forEach(System.out::print);


        byte[] valueBytes2 = ByteTest.serialize(test);
        Arrays.asList(valueBytes2).forEach(System.out::print);

    }
}
