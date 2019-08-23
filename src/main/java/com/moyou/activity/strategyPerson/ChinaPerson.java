package com.moyou.activity.strategyPerson;

public class ChinaPerson implements  PersonStrategy{
    @Override
    public void add(Person person) {
        System.out.println("ChinaPerson");
    }
}
