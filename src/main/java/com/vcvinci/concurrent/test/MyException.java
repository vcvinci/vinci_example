package com.vcvinci.concurrent.test;

public class MyException extends Exception {

    public MyException(String message) {
        super(message);
    }

    /*
     * ��дfillInStackTrace������ʹ������Զ�����쳣�����ռ��̵߳������쳣ջ��Ϣ������
     * ��߼����쳣������
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public static void main(String[] args) {
        try {
            throw new MyException("����MyException��д��fillInStackTrace��������ô�������ռ��߳�����ջ��Ϣ��");
        } catch (MyException e) {
            e.printStackTrace(); // �ڿ���̨�Ĵ�ӡ���Ϊ��demo.blog.java.exception.MyException: ����MyException��д��fillInStackTrace��������ô�������ռ��߳�����ջ��Ϣ��
        }
    }
}
