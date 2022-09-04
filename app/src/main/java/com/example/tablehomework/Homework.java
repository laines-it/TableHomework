package com.example.tablehomework;

public class Homework {
    public String date;
    public String subject;
    public String task;
    public Homework(){}

    public Homework(String date, String subject, String task) {
        this.date = date;
        this.subject = subject;
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
