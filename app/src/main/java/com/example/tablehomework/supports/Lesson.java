package com.example.tablehomework.supports;

import java.util.List;

public class Lesson{
    public String subject;
    public String time;
    public String room;
    public String homework;

    public Lesson(){}
    public Lesson(String subject, String time, String room, String homework) {
        this.subject = subject;
        this.time = time;
        this.room = room;
        this.homework = homework;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }
}
