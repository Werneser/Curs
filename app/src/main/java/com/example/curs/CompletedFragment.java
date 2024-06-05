package com.example.curs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class CompletedFragment extends Fragment {
    private AppDatabase db;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseClient.getInstance(getActivity()).getAppDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed, container, false);
        listView = view.findViewById(R.id.completed_list_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadCompletedTests();
    }

    private void loadCompletedTests() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);
        if (userId != -1) {
            new Thread(() -> {
                // Получаем список результатов тестов для текущего пользователя
                List<TestResult> testResults = db.testResultDao().getResultsForUser(userId);
                List<String> resultList = new ArrayList<>();
                for (TestResult result : testResults) {
                    String resultText = "Test ID: " + result.getTestId() + ", Correct Answers: " + result.getCorrectAnswers() + "/" + result.getTotalQuestions();
                    resultList.add(resultText);
                }
                getActivity().runOnUiThread(() -> {
                    adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, resultList);
                    listView.setAdapter(adapter);
                });
            }).start();
        }
    }

}
