package com.example.mypet.domain;

import android.provider.ContactsContract;

import java.util.Date;

public class Note {
    private String title;
    private String date;
    private String content;

    public Note(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
