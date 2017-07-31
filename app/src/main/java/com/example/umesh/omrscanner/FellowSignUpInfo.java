package com.example.umesh.omrscanner;

import java.util.ArrayList;

/**
 * Created by basu on 7/24/2017.
 */

public class FellowSignUpInfo {
    String name,city,schoolName;
    ArrayList<String> classGrades;
    ArrayList<String> classSections;

    public FellowSignUpInfo(String name, String city, String schoolName, ArrayList<String> classGrades, ArrayList<String> classSections) {
        this.name = name;
        this.city = city;
        this.schoolName = schoolName;
        this.classGrades = classGrades;
        this.classSections = classSections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public ArrayList<String> getClassGrades() {
        return classGrades;
    }

    public void setClassGrades(ArrayList<String> classGrades) {
        this.classGrades = classGrades;
    }

    public ArrayList<String> getClassSections() {
        return classSections;
    }

    public void setClassSections(ArrayList<String> classSections) {
        this.classSections = classSections;
    }
}
