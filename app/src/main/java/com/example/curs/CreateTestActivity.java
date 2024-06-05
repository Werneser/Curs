package com.example.curs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateTestActivity extends AppCompatActivity {
    private AppDatabase db;
    private LinearLayout questionsContainer;
    private Button addOptionButton;
    private EditText testNameEditText;

    private View lastInteractedQuestionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);
        db = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
        questionsContainer = findViewById(R.id.questions_container);
        addOptionButton = findViewById(R.id.add_option_button);
        testNameEditText = findViewById(R.id.test_name_edit_text);

        // Добавляем три вопроса по умолчанию
        for (int i = 0; i < 3; i++) {
            addQuestionView();
        }

        addOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastInteractedQuestionView != null) {
                    LinearLayout optionsContainer = lastInteractedQuestionView.findViewById(R.id.options_container);
                    RadioGroup questionTypeGroup = lastInteractedQuestionView.findViewById(R.id.question_type_group);
                    int checkedId = questionTypeGroup.getCheckedRadioButtonId();
                    boolean isMultipleChoice = checkedId == R.id.radio_multiple_choice;
                    if (isMultipleChoice) {
                        addOptionView(optionsContainer, true);
                    }
                    boolean isSingleChoice = checkedId == R.id.radio_single_choice;
                    if (isSingleChoice) {
                        addSingleOptionView(optionsContainer);
                    }
                } else {
                    Toast.makeText(CreateTestActivity.this, "Please select a question type first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button addQuestionButton = findViewById(R.id.add_question_button);
        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestionView();
            }
        });

        Button saveTestButton = findViewById(R.id.save_test_button);
        saveTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTestToDatabase();
            }
        });
    }

    private void addQuestionView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View questionView = inflater.inflate(R.layout.item_question, questionsContainer, false);
        questionsContainer.addView(questionView);

        RadioGroup questionTypeGroup = questionView.findViewById(R.id.question_type_group);
        LinearLayout optionsContainer = questionView.findViewById(R.id.options_container);

        questionTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                lastInteractedQuestionView = questionView;
                optionsContainer.removeAllViews();
                optionsContainer.setVisibility(View.VISIBLE);

                if (checkedId == R.id.radio_single_choice) {
                    addSingleOptionView(optionsContainer);
                    addOptionButton.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radio_multiple_choice) {
                    addOptionButton.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radio_fill_in_blank) {
                    EditText answerInput = new EditText(CreateTestActivity.this);
                    answerInput.setHint("Correct Answer");
                    optionsContainer.addView(answerInput);
                    addOptionButton.setVisibility(View.GONE);
                } else {
                    optionsContainer.setVisibility(View.GONE);
                    addOptionButton.setVisibility(View.GONE);
                }
            }
        });
    }

    private void addOptionView(ViewGroup container, boolean withCheckBox) {
        EditText optionInput = new EditText(this);
        optionInput.setHint("Option");
        container.addView(optionInput);

        if (withCheckBox) {
            RadioButton correctRadioButton = new RadioButton(this);
            correctRadioButton.setText("Correct");
            container.addView(correctRadioButton);
        }
    }

    private void addSingleOptionView(ViewGroup container) {
        EditText optionInput = new EditText(this);
        optionInput.setHint("Option");
        container.addView(optionInput);

        RadioButton correctRadioButton = new RadioButton(this);
        correctRadioButton.setText("Correct");
        container.addView(correctRadioButton);

        correctRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    uncheckOtherRadioButtons(container, correctRadioButton);
                }
            }
        });
    }

    private void uncheckOtherRadioButtons(ViewGroup container, RadioButton checkedButton) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            if (view instanceof RadioButton && view != checkedButton) {
                ((RadioButton) view).setChecked(false);
            }
        }
    }

    private void saveTestToDatabase() {
        String testName = testNameEditText.getText().toString().trim();
        if (testName.isEmpty()) {
            Toast.makeText(this, "Please enter a test name", Toast.LENGTH_SHORT).show();
            return;
        }

        final Test Newtest = new Test(testName);

        for (int i = 0; i < questionsContainer.getChildCount(); i++) {
            View questionView = questionsContainer.getChildAt(i);
            Question question = addQuestionToTest(questionView);
            Newtest.addQuestion(question);
        }

        if (Newtest.questions.isEmpty()) {
            Toast.makeText(this, "No questions to save", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                db.testDao().insert(Newtest);
                String testName = Newtest.testName;
                int testId = db.testDao().getTestIdByName(testName);

                for (Question question : Newtest.questions) {
                    question.testId = testId;
                    db.questionDao().insert(question);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CreateTestActivity.this, "Test saved successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateTestActivity.this, TeacherActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).start();
    }


    private Question addQuestionToTest(View questionView) {
        EditText questionEditText = questionView.findViewById(R.id.question_text);
        String questionText = questionEditText.getText().toString();

        RadioGroup questionTypeGroup = questionView.findViewById(R.id.question_type_group);
        int checkedId = questionTypeGroup.getCheckedRadioButtonId();
        int questionType = 0;
        if (checkedId == R.id.radio_single_choice) {
            questionType = 1;
        } else if (checkedId == R.id.radio_multiple_choice) {
            questionType = 2;
        } else if (checkedId == R.id.radio_fill_in_blank) {
            questionType = 3;
        }

        Question question = new Question(questionType, questionText);

        LinearLayout optionsContainer = questionView.findViewById(R.id.options_container);
        for (int i = 0; i < optionsContainer.getChildCount(); i++) {
            View optionView = optionsContainer.getChildAt(i);
            if (optionView instanceof EditText) {
                EditText optionEditText = (EditText) optionView;
                String optionText = optionEditText.getText().toString();
                boolean isCorrect = false;
                if (questionType == 1 || questionType == 2) {
                    RadioButton correctRadioButton = optionsContainer.findViewById(R.id.radio_correct);
                    if (correctRadioButton != null) {
                        isCorrect = correctRadioButton.isChecked();
                    }
                }
                question.addOption(optionText, isCorrect);
            }
        }

        return question;
    }
}
