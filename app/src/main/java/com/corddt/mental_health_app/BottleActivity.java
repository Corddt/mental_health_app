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
        int completedPlansCount = databaseHelper.getCompletedPlansCount();
        int imageResource = getResourceIdForBottle(completedPlansCount);
        bottleImageView.setImageResource(imageResource);
    }

    private int getResourceIdForBottle(int completedPlansCount) {
        // 根据完成的计划数量选择相应的瓶子图像资源
        switch (completedPlansCount) {
            case 1:
                return R.drawable.bottle1;
            case 2:
                return R.drawable.bottle2;
            case 3:
                return R.drawable.bottle3;
            // 可以继续添加更多的情况
            default:
                return R.drawable.bottle0; // 默认为空瓶子
        }
    }
}
