package com.vcvinci.closure;

public class Programmer {
    private String name;

    public Programmer() {
    }

    public Programmer(String name) {
        this.name = name;
    }

    public void work() {
        System.out.println(name + "�ڵ��������ü���...");
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
