package com.example.curs;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Test.class, Question.class, User.class, TestResult.class}, version = 2)
@TypeConverters({Converters.class, QuestionConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TestDao testDao();
    public abstract UserDao userDao();
    public abstract TestResultDao testResultDao();
    public abstract QuestionDao questionDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE User ADD COLUMN userType TEXT");
        }
    };
}





