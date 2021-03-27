package com.mysite25.core.models;

public class BreadcrumbItem {

    private String path;

    private String title;

    public BreadcrumbItem(String path, String title) {
        this.path = path;
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }
}
