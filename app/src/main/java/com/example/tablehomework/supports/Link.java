package com.example.tablehomework.supports;

public class Link {
    public String link;
    public String name;

    public Link(){}
    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Link(String link, String name) {
        this.link = link;
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
