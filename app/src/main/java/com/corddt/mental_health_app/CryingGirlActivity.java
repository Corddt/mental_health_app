package com.corddt.mental_health_app;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CryingGirlActivity extends AppCompatActivity {

    private ImageView girlImageView;
    private Button motivateButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crying_girl);

        girlImageView = findViewById(R.id.cryingGirlImageView);
        motivateButton = findViewById(R.id.motivateButton);
        databaseHelper = new DatabaseHelper(this);

        updateGirlStatus();

        motivateButton.setOnClickListener(v -> {
            // 处理按钮点击事件 - 可能会激励用户完成任务
        });
    }

    private void updateGirlStatus() {
        boolean isInactive = databaseHelper.checkUserInactivity(3);
        if (isInactive) {
            girlImageView.setImageResource(R.drawable.crying_girl);
            showReminderDialog("You haven't completed any tasks in the last three days. Let's get back on track!");
        } else {
            girlImageView.setImageResource(R.drawable.smiling_girl);
            showReminderDialog("Great job staying active! Keep it up!");
        }
    }

    private void showReminderDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
