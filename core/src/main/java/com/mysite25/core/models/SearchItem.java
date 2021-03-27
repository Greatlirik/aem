package com.mysite25.core.models;

public class SearchItem {

    private String title;

    private String path;

    public SearchItem(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }
}