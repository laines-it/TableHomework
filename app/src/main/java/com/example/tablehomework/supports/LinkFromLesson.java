package com.example.tablehomework.supports;

public class LinkFromLesson {
    public String link;
    public String image;

    public LinkFromLesson(){}
    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LinkFromLesson(String link, String image) {
        this.link = link;
        this.image = image;
    }

    public void setLink(String link) {
        this.link = link;
    }
}


