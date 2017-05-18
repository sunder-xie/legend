package com.tqmall.util;

import lombok.Data;

/**
 * Created by wanghui on 11/27/15.
 */
@Data
public class Person {
    private String name;
    private Integer age;
    private String address;

    public Person(String name, Integer age){
        this.name = name;
        this.age = age;
    }

}
