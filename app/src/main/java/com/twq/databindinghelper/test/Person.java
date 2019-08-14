package com.twq.databindinghelper.test;

/**
 * Created by tang.wangqiang on 2019/2/22.
 */

public class Person {
    //私有属性
    private String name = "Tom";
    //公有属性
    public int age = 18;

    //构造方法
    public Person() {
    }

    //私有方法
    private void say() {
        System.out.println("private say()...");
    }

    //公有方法
    public void work() {
        System.out.println("public work()...");
    }
}
