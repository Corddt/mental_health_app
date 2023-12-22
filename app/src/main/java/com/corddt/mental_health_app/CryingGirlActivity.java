package com.corddt.mental_health_app;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class CryingGirlActivity extends AppCompatActivity {

    private ImageView cryingGirlImageView;
    private TextView reminderTextView;
    private Button motivateButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crying_girl);

        cryingGirlImageView = findViewById(R.id.cryingGirlImageView);
        reminderTextView = findViewById(R.id.reminderTextView);
        motivateButton = findViewById(R.id.motivateButton);
        databaseHelper = new DatabaseHelper(this);

        updateCryingGirlStatus();

        motivateButton.setOnClickListener(v -> {
            // 处理按钮点击事件 - 可能会激励用户完成任务
        });
    }

    private void updateCryingGirlStatus() {
        // 检查用户是否连续三天没有完成任何任务
        boolean isInactive = databaseHelper.checkUserInactivity(3); // 假设这个方法检查指定天数的不活动状态
        if (isInactive) {
            // 显示哭泣女孩图像和提醒信息
            cryingGirlImageView.setImageResource(R.drawable.crying_girl);
            reminderTextView.setText("You haven't completed any tasks in the last three days. Let's get back on track!");
        } else {
            // 如果用户活跃，则隐藏哭泣女孩图像并清除提醒信息
            cryingGirlImageView.setVisibility(View.GONE);
            reminderTextView.setText("");
        }
    }
}
