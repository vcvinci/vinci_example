package com.vcvinci.designPattern;

/**
 * @author vinci
 * @Title: Person
 * @ProjectName vinci_example
 * @Description: TODO
 * @date 2019-08-2123:18
 */
public class Person {


    private String name;
    private int age;
    private String work;
    private String weight;
    private String high;
    private String money;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getWork() {
        return work;
    }

    public String getWeight() {
        return weight;
    }

    public String getHigh() {
        return high;
    }

    public String getMoney() {
        return money;
    }

    public static final class PersonBuilder {
        private String name;
        private int age;
        private String work;
        private String weight;
        private String high;
        private String money;

        private PersonBuilder() {
        }

        public static PersonBuilder aPerson() {
            return new PersonBuilder();
        }

        public PersonBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PersonBuilder age(int age) {
            this.age = age;
            return this;
        }

        public PersonBuilder work(String work) {
            this.work = work;
            return this;
        }

        public PersonBuilder weight(String weight) {
            this.weight = weight;
            return this;
        }

        public PersonBuilder high(String high) {
            this.high = high;
            return this;
        }

        public PersonBuilder money(String money) {
            this.money = money;
            return this;
        }

        public Person build() {
            Person person = new Person();
            person.weight = this.weight;
            person.money = this.money;
            person.work = this.work;
            person.high = this.high;
            person.name = this.name;
            person.age = this.age;
            return person;
        }
    }

    public static void main(String[] args) {

        System.out.println(cal(15));

        /*public static int cal(int n){
            int count;
            for(count = 0; n; n &= n - 1){
                count++;
            }
            return count;
        }*/


    }

    public static int cal (int n){
        int count ;
        for (count = 0; n != 0; n &= n - 1) {
            count++;
        }
        return count;
    }

    public static int cal2(int a){
        int count = 0;
        while(a != 0){
            a = a & (a-1);
            count++;
        }
        return count;
    }

    public static int  NumberOf1(int n) {
        int count = 0;
        while(n != 0)     //循环结束条件一个关键的点，无符号的int在移位32次（32位，4个字节）后，会变成0
        {
            n = (n-1)&n;//这个运算每做一次就会使得二进制中的1少一个
            count++;
        }
        return count;
    }

}
