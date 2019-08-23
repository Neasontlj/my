package com.moyou.activity.strategyPerson;

public class AmericanPerson implements PersonStrategy {
    @Override
    public void add(Person person) {
        System.out.println("AmericanPerson");
    }
}
