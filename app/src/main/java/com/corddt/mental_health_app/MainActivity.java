package com.corddt.mental_health_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnDiary;
    private Button btnPlan;
    private Button btnBottle;
    private Button btnReward;
    private Button btnCryingGirl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDiary = findViewById(R.id.btnDiary);
        btnPlan = findViewById(R.id.btnPlan);
        btnBottle = findViewById(R.id.btnBottle);
        btnReward = findViewById(R.id.btnReward);
        btnCryingGirl = findViewById(R.id.btnCryingGirl);

        btnDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaryActivity();
            }
        });

        btnPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlanActivity();
            }
        });

        btnBottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottleActivity();
            }
        });

        btnReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRewardActivity();
            }
        });

        btnCryingGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCryingGirlActivity();
            }
        });
    }

    private void openDiaryActivity() {
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    private void openPlanActivity() {
        Intent intent = new Intent(this, PlanActivity.class);
        startActivity(intent);
    }

    private void openBottleActivity() {
        Intent intent = new Intent(this, BottleActivity.class);
        startActivity(intent);
    }

    private void openRewardActivity() {
        Intent intent = new Intent(this, RewardActivity.class);
        startActivity(intent);
    }

    private void openCryingGirlActivity() {
        Intent intent = new Intent(this, CryingGirlActivity.class);
        startActivity(intent);
    }
}
