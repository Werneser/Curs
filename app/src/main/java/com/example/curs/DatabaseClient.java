package com.example.curs;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
    private Context context;
    private static DatabaseClient instance;

    // Объект базы данных
    private AppDatabase appDatabase;

    private DatabaseClient(Context context) {
        this.context = context;

        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "database-name")
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
