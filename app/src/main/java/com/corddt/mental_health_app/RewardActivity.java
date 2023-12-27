package com.corddt.mental_health_app;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RewardActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private ListView listViewDiaryAndPlans;
    private TextView tvDiaryCount, tvPlanCount;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        calendarView = findViewById(R.id.calendarView);
        listViewDiaryAndPlans = findViewById(R.id.listViewDiaryAndPlans);
        tvDiaryCount = findViewById(R.id.tvDiaryCount);
        tvPlanCount = findViewById(R.id.tvPlanCount);
        databaseHelper = new DatabaseHelper(this);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                updateDiaryAndPlansList(year, month, dayOfMonth);
            }
        });
    }

    private void updateDiaryAndPlansList(int year, int month, int dayOfMonth) {
        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
        List<String> diaryAndPlans = databaseHelper.getDiaryAndPlansByDate(date);

        // 更新日记和计划的数量
        int diaryCount = 0, planCount = 0;
        for (String item : diaryAndPlans) {
            if (item.startsWith("Diary:")) diaryCount++;
            if (item.startsWith("Plan:")) planCount++;
        }
        tvDiaryCount.setText("Diaries: " + diaryCount);
        tvPlanCount.setText("Plans: " + planCount);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, diaryAndPlans);
        listViewDiaryAndPlans.setAdapter(adapter);
    }
}
