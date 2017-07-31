package com.example.umesh.omrscanner;

import java.util.ArrayList;

/**
 * Created by basu on 7/12/2017.
 */

public class SurveysResponseInfo {
    String title, className;
    int status;
    long numberOfQuestions;

    public SurveysResponseInfo(String title, String className, int status, long numberOfQuestions) {
        this.title = title;
        this.className = className;
        this.status = status;
        this.numberOfQuestions = numberOfQuestions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}
