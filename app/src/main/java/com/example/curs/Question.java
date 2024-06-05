package com.example.curs;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "questions",
        foreignKeys = @ForeignKey(entity = Test.class,
                parentColumns = "id",
                childColumns = "testId",
                onDelete = ForeignKey.CASCADE))
public class Question {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int testId; // ID теста, к которому относится вопрос
    public int questionType; // Тип вопроса: одиночный выбор, множественный выбор, развернутый ответ
    public String question; // Текст вопроса

    @TypeConverters(Converters.class)
    public List<String> options; // Список вариантов ответов

    @TypeConverters(Converters.class)
    public List<Boolean> correctAnswers; // Список правильных ответов для множественного выбора

    public Question(int questionType, String question) {
        this.questionType = questionType;
        this.question = question;
        this.options = new ArrayList<>();
        this.correctAnswers = new ArrayList<>();
    }

    public void addOption(String option, boolean isCorrect) {
        this.options.add(option);
        this.correctAnswers.add(isCorrect);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<Boolean> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswer(List<Boolean> correctAnswer) {
        this.correctAnswers = correctAnswer;
    }

    public int getType() {
        return questionType;
    }

    public void setType(int type) {
        this.questionType = type;
    }
}
