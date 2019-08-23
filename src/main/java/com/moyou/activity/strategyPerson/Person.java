package com.moyou.activity.strategyPerson;

import lombok.Data;

@Data
public class Person {
    private String id;
    private String money;
    private String name;
    private String age;

    private PersonStrategy personStrategy;

    public void add(){
        personStrategy.add(this);
    }

    public Person(String id,String money,String name,String age,PersonStrategy personStrategy){
        this.id = id;
        this.money = money;
        this.name = name;
        this.age = age;
        this.personStrategy = personStrategy;
    }
}
