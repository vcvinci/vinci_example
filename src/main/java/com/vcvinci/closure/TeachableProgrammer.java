package com.vcvinci.closure;

public class TeachableProgrammer extends Programmer {
    public TeachableProgrammer(){}

    public TeachableProgrammer(String name) {
        super(name);
    }

    //��ѧ������Ȼ��TeachableProgrammer�ඨ��
    private void teach() {
        System.out.println(getName() + "��ʦ�ڽ�̨�Ͻ���...");
    }

    private class Closure implements Teachable {
        @Override
        public void work() {
            teach();
        }
    }

    //����һ���Ǿ�̬�ڲ������ã������ⲿ��ͨ���÷Ǿ�̬�ڲ����������ص��ⲿ��ķ���
    public Teachable getCallbackReference() {
        return new Closure();
    }
}
