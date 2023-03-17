package com.example.tablehomework.supports;

public class Chat{
    public String name;
    public String access;
    public int id;

    public Chat(){}
    public Chat(String name, String access, int id) {
        this.name = name;
        this.access = access;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
