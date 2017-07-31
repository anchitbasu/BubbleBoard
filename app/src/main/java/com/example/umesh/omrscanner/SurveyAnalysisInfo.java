package com.example.umesh.omrscanner;

/**
 * Created by basu on 7/30/2017.
 */

public class SurveyAnalysisInfo {
    String questionText;
    int numberFavourable, numberUnfavourable, numberNeutral;

    public SurveyAnalysisInfo(String questionText, int numberFavourable, int numberUnfavourable, int numberNeutral) {
        this.questionText = questionText;
        this.numberFavourable = numberFavourable;
        this.numberUnfavourable = numberUnfavourable;
        this.numberNeutral = numberNeutral;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getNumberFavourable() {
        return numberFavourable;
    }

    public void setNumberFavourable(int numberFavourable) {
        this.numberFavourable = numberFavourable;
    }

    public int getNumberUnfavourable() {
        return numberUnfavourable;
    }

    public void setNumberUnfavourable(int numberUnfavourable) {
        this.numberUnfavourable = numberUnfavourable;
    }

    public int getNumberNeutral() {
        return numberNeutral;
    }

    public void setNumberNeutral(int numberNeutral) {
        this.numberNeutral = numberNeutral;
    }
}

