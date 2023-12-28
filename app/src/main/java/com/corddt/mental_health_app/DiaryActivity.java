package com.corddt.mental_health_app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class DiaryActivity extends AppCompatActivity {

    private EditText editTextDiary;
    private Button btnSaveDiary;
    private ListView listViewDiaries;
    private ArrayAdapter<String> diaryAdapter;
    private List<String> diaryEntries;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        diaryEntries = new ArrayList<>();
        editTextDiary = findViewById(R.id.editTextDiary);
        btnSaveDiary = findViewById(R.id.btnSaveDiary);
        listViewDiaries = findViewById(R.id.listViewDiaries);
        openDatabase();
        diaryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, diaryEntries);
        listViewDiaries.setAdapter(diaryAdapter);
        loadDiaries();

        btnSaveDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDiary();
            }
        });
    }

    private void openDatabase() {
        database = openOrCreateDatabase("MotivationalDiary1.db", MODE_PRIVATE, null);
        createTablesIfNotExist();
    }

    private void createTablesIfNotExist() {
        String CREATE_DIARY_TABLE = "CREATE TABLE IF NOT EXISTS diary_entries ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "entry TEXT,"
                + "timestamp TEXT DEFAULT (strftime('%Y-%m-%d', 'now')))";
        database.execSQL(CREATE_DIARY_TABLE);
    }



    private void saveDiary() {
        String diaryText = editTextDiary.getText().toString();
        if (!diaryText.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put("entry", diaryText);
            database.insert("diary_entries", null, values);
            editTextDiary.setText("");
            loadDiaries();
        }
    }

    private void loadDiaries() {
        diaryEntries.clear();
        Cursor cursor = database.rawQuery("SELECT entry FROM diary_entries", null);
        while (cursor.moveToNext()) {
            diaryEntries.add(cursor.getString(0));
        }
        cursor.close();
        diaryAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
