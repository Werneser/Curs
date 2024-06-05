package com.example.curs;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "test_results",
        foreignKeys = {@ForeignKey(entity = Test.class, parentColumns = "id", childColumns = "testId"),
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId")})
public class TestResult {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int testId;
    private int userId;
    private int correctAnswers;
    private int totalQuestions;

    public TestResult(int testId, int userId, int correctAnswers, int totalQuestions) {
        this.testId = testId;
        this.userId = userId;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
}
