package com.example.curs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TestDao {
    @Insert
    void insert(Test test);

    @Query("SELECT * FROM tests")
    List<Test> getAllTests();

    @Query("SELECT * FROM questions WHERE testId = :testId")
    List<Question> getQuestionsForTest(int testId);

    @Query("SELECT id FROM tests WHERE testName = :testName")
    int getTestIdByName(String testName);
}
