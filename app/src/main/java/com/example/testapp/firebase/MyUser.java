package com.example.testapp.firebase;

public class MyUser {
    String name;
    String lastName;
    int age;

    public MyUser(String name, String lastName, int age) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }

    public MyUser() {
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return name +" "+lastName+" -> " + age;
    }
}
