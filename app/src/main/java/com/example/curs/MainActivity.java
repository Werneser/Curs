package com.example.curs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
//        insertTestQuestions();
    }

    private void insertTestQuestions() {
        Test test1 = new Test("test1");
        Question question1 = new Question(1, "На чем написана курсовая?");
        question1.addOption("Java", true);
        question1.addOption("Kotlin", false);
        Question question2 = new Question(3, "Какая группа?");
        question2.addOption("ИКБО-07-22", true);
        test1.addQuestion(question1);
        test1.addQuestion(question2);
        Test test2 = new Test("test2");
        Question question3 = new Question(1, "Сколько практик?");
        question3.addOption("10", false);
        question3.addOption("11", false);
        question3.addOption("12", true);
        Question question4 = new Question(2, "Выберите все ответы");
        question4.addOption("ИКБО-07-22", true);
        question4.addOption("1", true);
        question4.addOption("2", true);
        question4.addOption("3", true);
        test2.addQuestion(question3);
        test2.addQuestion(question4);

        new Thread(() -> {
            db.testDao().insert(test1);
            db.testDao().insert(test2);
        }).start();
    }
    public void login(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        new Thread(() -> {
            User user = db.userDao().getUser(username, password);
            runOnUiThread(() -> {
                if (user != null) {
                    SharedPreferences.Editor editor = getSharedPreferences("user_prefs", MODE_PRIVATE).edit();
                    editor.putInt("user_id", user.id);
                    editor.apply();

                    Intent intent;
                    switch (user.userType) {
                        case "teacher":
                            intent = new Intent(MainActivity.this, TeacherActivity.class);
                            break;
                        case "student":
                        default:
                            intent = new Intent(MainActivity.this, ChoseActivity.class);
                            break;
                    }
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }


    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}

