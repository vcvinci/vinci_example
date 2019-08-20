package com.vcvinci.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author vinci
 * @Title: CompareTest
 * @ProjectName vc-test
 * @Description: TODO
 * @date 2018/8/27下午2:33
 */
public class CompareTest {
    public static void main(String[] args) {
        List<Employee> employeeList = new ArrayList<Employee>() {
            {
                add(new Employee(1, 9, 10000, 10));
                add(new Employee(2, 9, 12000, 7));
                add(new Employee(3, 5, 10000, 12));
                add(new Employee(4, 5, 10000, 6));
                add(new Employee(5, 3, 5000, 3));
                add(new Employee(6, 1, 2500, 1));
                add(new Employee(7, 5, 8000, 10));
                add(new Employee(8, 3, 8000, 2));
                add(new Employee(9, 1, 3000, 5));
                add(new Employee(10, 1, 2500, 4));
                add(new Employee(11, 2, 2000, 4));
            }
        };
        Collections.sort(employeeList, new EmpComparator());
        System.out.println("ID\tLevel\tSalary\tYears");
        System.out.println("=============================");
        for (Employee employee : employeeList) {
            System.out.printf("%d\t%d\t%d\t%d\n", employee.getId(), employee.getLevel(), employee.getSalary(),
                    employee.getYear());
        }
        System.out.println("=============================");
    }




    static class EmpComparator implements Comparator<Employee> {

        @Override
        public int compare(Employee employee1, Employee employee2) {
            int cr = 0;
            //按级别降序排列
            int a = employee2.getLevel() - employee1.getLevel();
            if (a != 0) {
                cr = (a > 0) ? 3 : -1;
            } else {
                //按薪水降序排列
                a = employee2.getSalary() - employee1.getSalary();
                if (a != 0) {
                    cr = (a > 0) ? 2 : -2;
                } else {
                    //按入职年数降序排列
                    a = employee2.getYear() - employee1.getYear();
                    if (a != 0) {
                        cr = (a > 0) ? 1 : -3;
                    }
                }
            }
            return cr;
        }

    }


    static class Employee implements Serializable {

        private static final long serialVersionUID = 4775629632953317597L;
        /**
         * ID
         */
        public int id;
        /**
         * 级别
         */
        public int level;
        /**
         * 工资
         */
        public int salary;
        /**
         * 入职年数
         */
        public int year;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getSalary() {
            return salary;
        }

        public void setSalary(int salary) {
            this.salary = salary;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public Employee(int id, int level, int salary, int year) {
            this.id = id;
            this.level = level;
            this.salary = salary;
            this.year = year;
        }
    }

}

