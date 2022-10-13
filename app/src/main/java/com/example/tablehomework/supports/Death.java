package com.example.tablehomework.supports;

import java.util.ArrayList;
import java.util.List;

public class Death {
    public String subject;
    public String event;
    public long date;
    public ArrayList<Link> links;
    public Death(){}
    public Death(String subject, String event, long date, ArrayList<Link> links) {
        this.subject = subject;
        this.event = event;
        this.date = date;
        this.links = links;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
