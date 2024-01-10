package com.corddt.mental_health_app;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DiaryActivity extends AppCompatActivity {

    private EditText editTextDiary;
    private Button btnSaveDiary;
    private ListView listViewDiaries;
    private DiaryEntryAdapter diaryAdapter;
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
        diaryAdapter = new DiaryEntryAdapter(this, diaryEntries);
        listViewDiaries.setAdapter(diaryAdapter);

        loadDiaries();

        btnSaveDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDiary();
            }
        });

        Button btnToCryingGirl = findViewById(R.id.btnToCryingGirl);
        btnToCryingGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryActivity.this, CryingGirlActivity.class);
                startActivity(intent);
            }
        });
        listViewDiaries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editDiaryEntry(diaryEntries.get(position));
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
            if (!isDiaryEntryExist()) {
                ContentValues values = new ContentValues();
                values.put("entry", diaryText);
                database.insert("diary_entries", null, values);
                Toast.makeText(this, "The diary has been saved.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "There is already a diary entry for today, and if you want to add something, you can make modifications to the existing diary entry.", Toast.LENGTH_SHORT).show();
            }
            editTextDiary.setText("");
            loadDiaries();
        }
    }

    private boolean isDiaryEntryExist() {
        Cursor cursor = database.rawQuery("SELECT * FROM diary_entries WHERE timestamp = date('now')", null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private void loadDiaries() {
        diaryEntries.clear();
        Cursor cursor = database.rawQuery("SELECT entry, timestamp FROM diary_entries GROUP BY timestamp ORDER BY timestamp DESC", null);
        while (cursor.moveToNext()) {
            String entry = cursor.getString(0);
            String timestamp = cursor.getString(1);
            diaryEntries.add(timestamp + ": " + entry);
        }
        cursor.close();
        diaryAdapter.notifyDataSetChanged();
    }

    private void editDiaryEntry(String diaryEntry) {
        String[] parts = diaryEntry.split(": ", 2);
        if (parts.length < 2) return;
        String date = parts[0];
        String content = parts[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit diary.");
        final EditText input = new EditText(this);
        input.setText(content);
        builder.setView(input);

        builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateDiaryEntry(date, input.getText().toString());
            }
        });
        builder.setNegativeButton("cancel", null);
        builder.show();
    }

    private void updateDiaryEntry(String date, String newContent) {
        ContentValues values = new ContentValues();
        values.put("entry", newContent);
        database.update("diary_entries", values, "timestamp = ?", new String[]{date});
        loadDiaries();
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }

    private class DiaryEntryAdapter extends ArrayAdapter<String> {
        public DiaryEntryAdapter(Context context, List<String> diaryEntries) {
            super(context, 0, diaryEntries);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.diary_list_item, parent, false);
            }

            String diaryEntry = getItem(position);
            String[] parts = diaryEntry.split(": ", 2);

            TextView tvDiaryDate = convertView.findViewById(R.id.tvDiaryDate);
            TextView tvDiaryEntry = convertView.findViewById(R.id.tvDiaryEntry);

            if (parts.length >= 2) {
                tvDiaryDate.setText(parts[0]); // 显示日期
                tvDiaryEntry.setText(parts[1]); // 显示日记内容
            }

            return convertView;
        }
    }
}