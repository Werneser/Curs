package com.example.curs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TestResultDao {
    @Insert
    void insert(TestResult testResult);

    @Query("SELECT * FROM test_results WHERE userId = :userId")
    List<TestResult> getResultsForUser(int userId);
}