package com.example.curs;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeacherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.nav_create_test) {
                    Intent createTestIntent = new Intent(TeacherActivity.this, CreateTestActivity.class);
                    startActivity(createTestIntent);
                    return true;
                }
                if (item.getItemId() == R.id.nav_view_results) {
                    selectedFragment = new TeacherHomeFragment();
                }
                if (item.getItemId() == R.id.nav_settings) {
                    selectedFragment = new SettingsFragment();
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
                return true;
            }
        });

        // Загрузка фрагмента по умолчанию
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_view_results);
        }
    }
}
