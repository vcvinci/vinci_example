package com.vcvinci.concurrent.test;

public class InnerClassTest {
    private String name ;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public class InnerClass{
        public InnerClass(){
            name = "chenssy";
            age = 23;
        }

        public void display(){
            System.out.println("name£º" + getName() +"   ;age£º" + getAge());
        }
    }

    public static void main(String[] args) {
        InnerClassTest outerClass = new InnerClassTest();
        InnerClass innerClass = outerClass.new InnerClass();
        innerClass.display();
    }
}
