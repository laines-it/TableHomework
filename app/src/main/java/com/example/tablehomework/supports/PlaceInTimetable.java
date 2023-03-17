package com.example.tablehomework.supports;

public class PlaceInTimetable {
    public Lesson lesson;
    public int day,number;

    public PlaceInTimetable(Lesson lesson, int day, int number) {
        this.lesson = lesson;
        this.day = day;
        this.number = number;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
