package com.example.tablehomework.supports;

import java.util.HashMap;

public class MessageInChat extends HashMap {
    public String text;
    public int id;
    public long time;

    public MessageInChat(String text, int id, long time) {
        this.text = text;
        this.id = id;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
