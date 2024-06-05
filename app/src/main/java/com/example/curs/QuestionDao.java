package com.example.curs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuestionDao {
    @Insert
    void insert(Question question);

    @Query("SELECT * FROM questions WHERE testId = :testId")
    List<Question> getQuestionsForTest(int testId);
}

