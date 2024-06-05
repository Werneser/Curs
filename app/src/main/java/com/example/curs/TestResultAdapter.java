package com.example.curs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TestResultAdapter extends RecyclerView.Adapter<TestResultAdapter.TestResultViewHolder> {
    private List<TestResult> testResults;

    public TestResultAdapter(List<TestResult> testResults) {
        this.testResults = testResults;
    }

    public TestResultAdapter() {}

    @NonNull
    @Override
    public TestResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_result, parent, false);
        return new TestResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestResultViewHolder holder, int position) {
        TestResult result = testResults.get(position);
        holder.bind(result);
    }

    @Override
    public int getItemCount() {
        return testResults.size();
    }

    public static class TestResultViewHolder extends RecyclerView.ViewHolder {
        private TextView testInfoTextView;

        public TestResultViewHolder(@NonNull View itemView) {
            super(itemView);
            testInfoTextView = itemView.findViewById(R.id.test_info_text_view);
        }

        public void bind(TestResult result) {
            // Отображение информации о результате теста
            testInfoTextView.setText("Test ID: " + result.getTestId() + ", Correct Answers: " +
                    result.getCorrectAnswers() + "/" + result.getTotalQuestions());
        }
    }
}