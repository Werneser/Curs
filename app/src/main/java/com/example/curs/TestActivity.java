package com.example.curs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private LinearLayout questionContainer;

    private AppDatabase db;
    private int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        db = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
        questionContainer = findViewById(R.id.question_container);
        Button submitButton = findViewById(R.id.submit_button);


        Test test = (Test) getIntent().getSerializableExtra("test");
        if (test != null) {
            new GetQuestionsTask().execute(test);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetQuestionsAns().execute(test);
            }
        });
    }
    private class GetQuestionsTask extends AsyncTask<Test, Void, List<Question>> {

        @Override
        protected List<Question> doInBackground(Test... tests) {
            Test test = tests[0];

            return db.testDao().getQuestionsForTest(test.id);
        }

        @Override
        protected void onPostExecute(List<Question> questions) {
            super.onPostExecute(questions);

            displayQuestions(questions);
        }
    }
    private void displayQuestions(List<Question> questions) {
        // Отображаем вопросы на экране
        for (Question question : questions) {
            View questionView = createQuestionView(question);
            questionContainer.addView(questionView);
        }
    }

    private class GetQuestionsAns extends AsyncTask<Test, Void, List<Question>> {

        @Override
        protected List<Question> doInBackground(Test... tests) {
            Test test = tests[0];
            List<Question> questions = db.testDao().getQuestionsForTest(test.id);
            checkAnswers(questions);

            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            int userId = prefs.getInt("user_id", -1);

            TestResult testResult = new TestResult(test.id, userId, correctAnswers, test.questions.size());
            db.testResultDao().insert(testResult);

            return questions;
        }

        @Override
        protected void onPostExecute(List<Question> questions) {
            super.onPostExecute(questions);

            Toast.makeText(TestActivity.this, "Correct answers: " + correctAnswers, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(TestActivity.this, ChoseActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private View createQuestionView(Question question) {
        View view = getLayoutInflater().inflate(R.layout.item_test_question, null);
        TextView questionText = view.findViewById(R.id.question_text);
        questionText.setText(question.getQuestion());

        RadioGroup optionsGroup = view.findViewById(R.id.options_group);

        if (question.getType() == 3) {
            EditText answerInput = new EditText(this);
            answerInput.setHint("Your answer");
            optionsGroup.addView(answerInput);
        }
        if (question.getType() == 1) {
            for (int i = 0; i < question.getOptions().size(); i++) {
                String option = question.getOptions().get(i);
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(option);
                optionsGroup.addView(radioButton);
            }
        }
        if (question.getType() == 2) {
            for (int i = 0; i < question.getOptions().size(); i++) {
                String option = question.getOptions().get(i);
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(option);
                optionsGroup.addView(checkBox);
            }
        }

        return view;
    }


    private void checkAnswers(List<Question> questions) {
        correctAnswers = 0;
        for (int i = 0; i < questionContainer.getChildCount(); i++) {
            View questionView = questionContainer.getChildAt(i);
            RadioGroup optionsGroup = questionView.findViewById(R.id.options_group);
            Question question = questions.get(i);

            if (question.getType() == 3) {
                EditText answerInput = (EditText) optionsGroup.getChildAt(0);
                String userAnswer = answerInput.getText().toString();
                if (question.getOptions().get(0).equalsIgnoreCase(userAnswer)) {
                    correctAnswers++;
                }
            } else {
                int selectedId = optionsGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = optionsGroup.findViewById(selectedId);
                if (selectedRadioButton != null) {
                    int selectedIndex = optionsGroup.indexOfChild(selectedRadioButton);
                    if (question.getCorrectAnswers().get(selectedIndex)) {
                        correctAnswers++;
                    }
                }
            }
        }
    }

}
