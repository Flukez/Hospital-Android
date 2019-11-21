package com.android.hospitalapplication.ModelClasses;

/**
 * Created by Gaurav on 05-01-2018.
 */

public class Report {
    private String name,url;

    public Report() {
    }

    public Report(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
