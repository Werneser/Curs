package com.example.curs;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class RegisterActivity extends AppCompatActivity {
    private AppDatabase db;
    private EditText usernameEditText, passwordEditText;
    private RadioGroup userTypeGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();


        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        userTypeGroup = findViewById(R.id.user_type_group);
    }

    public void register(View view) {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        int selectedId = userTypeGroup.getCheckedRadioButtonId();

        // Проверка наличия данных
        if (username.isEmpty() || password.isEmpty() || selectedId == -1) {
            Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return; // Завершить метод, чтобы предотвратить создание пустой записи пользователя
        }

        RadioButton selectedRadioButton = findViewById(selectedId);
        String userType = selectedRadioButton.getText().toString().toLowerCase();

        User user = new User(username, password, userType);

        new Thread(() -> {
            db.userDao().insert(user);
            runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show());
            finish();
        }).start();
    }
}
