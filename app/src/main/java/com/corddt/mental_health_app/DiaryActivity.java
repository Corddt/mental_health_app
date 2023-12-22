package com.corddt.mental_health_app;

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
    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> diaryAdapter;
    private List<String> diaryEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        databaseHelper = new DatabaseHelper(this);
        diaryEntries = new ArrayList<>();

        editTextDiary = findViewById(R.id.editTextDiary);
        btnSaveDiary = findViewById(R.id.btnSaveDiary);
        listViewDiaries = findViewById(R.id.listViewDiaries);

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

    private void saveDiary() {
        String diaryText = editTextDiary.getText().toString();
        databaseHelper.addDiaryEntry(diaryText);
        editTextDiary.setText("");
        loadDiaries();
    }

    private void loadDiaries() {
        diaryEntries.clear();
        diaryEntries.addAll(databaseHelper.getAllDiaries());
        diaryAdapter.notifyDataSetChanged();
    }
}
