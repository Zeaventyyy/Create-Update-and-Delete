package com.example.zeave.crud;

class Employee {

    private int id, age;

    private String name, username, password;

    public Employee(int id, String name, int age, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.age = age;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return  username;
    }

    public int getAge() {
        return age;
    }

    public String getPassword() {
        return password;
    }

}
