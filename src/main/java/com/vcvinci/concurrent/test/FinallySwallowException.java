package com.vcvinci.concurrent.test;

public class FinallySwallowException {

    public static void main(String[] args) throws Exception {
        System.out.println(swallowException()); // ��ӡ��2�������Ǵ�ӡ���쳣ջ
    }

    public static int swallowException() throws Exception {
        try {
            throw new Exception();
        } finally {
            return 2;
        }
    }
}
