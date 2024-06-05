package com.example.curs;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "tests")
public class Test implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String testName;
    public int rating;

    @TypeConverters(Converters.class)
    public List<Question> questions;

    public Test(String testName) {
        this.testName = testName;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }


    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
