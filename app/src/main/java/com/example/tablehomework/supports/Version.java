package com.example.tablehomework.supports;

import java.util.List;

public class Version{
    List<String> note;
    Double patch;
    String link;
    public Version(){}
    public Version(List<String> note, Double patch,String link) {
        this.note = note;
        this.patch = patch;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getNote() {
        return note;
    }

    public void setNote(List<String> note) {
        this.note = note;
    }

    public Double getPatch() {
        return patch;
    }

    public void setPatch(Double patch) {
        this.patch = patch;
    }
}
