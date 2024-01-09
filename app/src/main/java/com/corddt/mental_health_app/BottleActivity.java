package com.corddt.mental_health_app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BottleActivity extends AppCompatActivity {

    private LottieAnimationView lottieBottleView;
    private SQLiteDatabase database;
    private TextView tvTodayPlansCount, tvCompletedPlansCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle);

        lottieBottleView = findViewById(R.id.lottieBottleView);
        tvTodayPlansCount = findViewById(R.id.tvTodayPlansCount);
        tvCompletedPlansCount = findViewById(R.id.tvCompletedPlansCount);
        openDatabase();
        updateBottleAnimation();
    }


    private void openDatabase() {
        database = openOrCreateDatabase("MotivationalDiary1.db", MODE_PRIVATE, null);
    }

    private void updateBottleAnimation() {
        int todayPlansCount = getTodayPlansCount();
        int completedPlansCount = getCompletedPlansCount();

        tvTodayPlansCount.setText("Today Plans: " + todayPlansCount);
        tvCompletedPlansCount.setText("Completed Plans: " + completedPlansCount);

        switch (completedPlansCount) {
            case 0:
                lottieBottleView.setAnimation("default_animation.json");
                break;
            case 1:
                lottieBottleView.setAnimation("Bottle02_Animation.json");
                break;
            case 2:
                lottieBottleView.setAnimation("Bottle01_Animation.json");
                break;
            default:
                lottieBottleView.setAnimation("Bottle03_Animation.json"); // 默认动画
                break;
        }
        lottieBottleView.setRepeatCount(LottieDrawable.INFINITE);
        lottieBottleView.playAnimation();
    }

    private int getTodayPlansCount() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM plans WHERE timestamp = ?", new String[]{currentDate});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    private int getCompletedPlansCount() {
        // 获取当前日期
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        // 查询当天完成的计划数量
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM plans WHERE completed = 1 AND timestamp = ?", new String[]{currentDate});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }





    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
