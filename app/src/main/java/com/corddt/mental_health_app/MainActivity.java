package com.corddt.mental_health_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnDiary;
    private Button btnPlan;
    private Button btnReward;
    private Button btnContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDiary = findViewById(R.id.btnDiary);
        btnPlan = findViewById(R.id.btnPlan);
        btnReward = findViewById(R.id.btnReward);
        btnContact = findViewById(R.id.btnContact);
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


        btnReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRewardActivity();
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                startActivity(intent);
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


    private void openRewardActivity() {
        Intent intent = new Intent(this, RewardActivity.class);
        startActivity(intent);
    }

}
