package com.example.umesh.omrscanner;

import java.util.ArrayList;

/**
 * Created by basu on 7/23/2017.
 */

public class SurveyInfo {
    String title;
    ArrayList<String> questions;
    int numberOfQuestions;

    public SurveyInfo(String title, ArrayList<String> questions, int numberOfQuestions) {
        this.title = title;
        this.questions = questions;
        this.numberOfQuestions = numberOfQuestions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}
