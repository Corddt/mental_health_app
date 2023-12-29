package com.corddt.mental_health_app;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class RewardActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private ListView listViewDiaryAndPlans;
    private TextView tvDiaryCount, tvPlanCount;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        calendarView = findViewById(R.id.calendarView);
        listViewDiaryAndPlans = findViewById(R.id.listViewDiaryAndPlans);
        tvDiaryCount = findViewById(R.id.tvDiaryCount);
        tvPlanCount = findViewById(R.id.tvPlanCount);
        openDatabase();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                updateDiaryAndPlansList(year, month, dayOfMonth);
            }
        });
    }

    private void openDatabase() {
        database = openOrCreateDatabase("MotivationalDiary1.db", MODE_PRIVATE, null);
    }

    private void updateDiaryAndPlansList(int year, int month, int dayOfMonth) {
        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
        List<String> diaryAndPlans = getDiaryAndPlansByDate(date);

        int diaryCount = 0, planCount = 0;
        for (String item : diaryAndPlans) {
            if (item.startsWith("Diary:")) diaryCount++;
            if (item.startsWith("Plan:")) planCount++;
        }
        tvDiaryCount.setText("Diaries: " + diaryCount);
        tvPlanCount.setText("Plans: " + planCount);

        // 检测是否存在日记或计划
        if (diaryCount == 0 && planCount == 0) {
            // 没有日记或计划的情况，使用心理学上的鼓励性表述
            diaryAndPlans.add("No entries for today. Remember, jotting down your thoughts and plans can be a great way to reflect and grow.");
        } else {
            // 存在日记或计划的情况，鼓励用户继续坚持
            diaryAndPlans.add("Great work! Keep up with your journaling and planning. It's a powerful tool for personal development.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, diaryAndPlans);
        listViewDiaryAndPlans.setAdapter(adapter);
    }


    private List<String> getDiaryAndPlansByDate(String date) {
        List<String> diaryAndPlans = new ArrayList<>();
        Cursor diaryCursor = database.rawQuery("SELECT * FROM diary_entries WHERE timestamp = ?", new String[]{date});
        while (diaryCursor.moveToNext()) {
            String entry = diaryCursor.getString(diaryCursor.getColumnIndex("entry"));
            diaryAndPlans.add("Diary: " + entry);
        }
        diaryCursor.close();

        Cursor planCursor = database.rawQuery("SELECT * FROM plans WHERE timestamp = ?", new String[]{date});
        while (planCursor.moveToNext()) {
            String content = planCursor.getString(planCursor.getColumnIndex("plan"));
            boolean completed = planCursor.getInt(planCursor.getColumnIndex("completed")) == 1;
            diaryAndPlans.add("Plan: " + content + " (Completed: " + (completed ? "Yes" : "No") + ")");
        }
        planCursor.close();

        return diaryAndPlans;
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
