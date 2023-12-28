package com.corddt.mental_health_app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class BottleActivity extends AppCompatActivity {

    private ImageView bottleImageView;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle);

        bottleImageView = findViewById(R.id.bottleImageView);
        openDatabase();
        updateBottleAnimation();
    }

    private void openDatabase() {
        database = openOrCreateDatabase("MotivationalDiary1.db", MODE_PRIVATE, null);
    }

    private void updateBottleAnimation() {
        int completedPlansCount = getCompletedPlansCount();
        int imageResource = getResourceIdForBottle(completedPlansCount);
        bottleImageView.setImageResource(imageResource);
    }

    private int getCompletedPlansCount() {
        int count = 0;
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM plans WHERE completed = 1", null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    private int getResourceIdForBottle(int completedPlansCount) {
        switch (completedPlansCount) {
            case 1:
                return R.drawable.bottle1;
            case 2:
                return R.drawable.bottle2;
            case 3:
                return R.drawable.bottle3;
            default:
                return R.drawable.bottle0;
        }
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
