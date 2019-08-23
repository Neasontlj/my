package com.moyou.activity.strategyPerson;

public class EnglishPerson implements PersonStrategy {
    @Override
    public void add(Person person) {
        System.out.println("EnglishPerson");
    }
}
