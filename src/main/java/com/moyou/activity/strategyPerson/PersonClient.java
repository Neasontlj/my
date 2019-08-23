package com.moyou.activity.strategyPerson;

/**
 * 策略模式简单实现
 */
public class PersonClient {
    public static void main(String[] args) {
        PersonStrategy china = new ChinaPerson();
        PersonStrategy american = new AmericanPerson();
        PersonStrategy english = new EnglishPerson();
        Person person = new Person("1","200","小王","20",china);
        person.add();
        person = new Person("2","100","mockey","18",american);
        person.add();

    }
}
