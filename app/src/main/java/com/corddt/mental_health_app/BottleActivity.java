package com.corddt.mental_health_app;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class BottleActivity extends AppCompatActivity {

    private ImageView bottleImageView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle);

        bottleImageView = findViewById(R.id.bottleImageView);
        databaseHelper = new DatabaseHelper(this);

        updateBottleAnimation();
    }

    private void updateBottleAnimation() {
        int completedTasksCount = databaseHelper.getCompletedTasksCount();
        int imageResource = getResourceIdForBottle(completedTasksCount);
        bottleImageView.setImageResource(imageResource);
    }

    private int getResourceIdForBottle(int completedTasksCount) {
        switch (completedTasksCount) {
            case 1:
                return R.drawable.bottle1;
            case 2:
                return R.drawable.bottle2;
            case 3:
                return R.drawable.bottle3;
            // 添加更多情况，根据完成的任务数量
            default:
                return R.drawable.bottle0;
        }
    }
}
