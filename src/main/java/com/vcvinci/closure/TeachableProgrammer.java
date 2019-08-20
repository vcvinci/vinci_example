package com.vcvinci.closure;

public class TeachableProgrammer extends Programmer {
    public TeachableProgrammer(){}

    public TeachableProgrammer(String name) {
        super(name);
    }

    //教学工作依然由TeachableProgrammer类定义
    private void teach() {
        System.out.println(getName() + "教师在讲台上讲解...");
    }

    private class Closure implements Teachable {
        @Override
        public void work() {
            teach();
        }
    }

    //返回一个非静态内部类引用，允许外部类通过该非静态内部类引用来回调外部类的方法
    public Teachable getCallbackReference() {
        return new Closure();
    }
}
