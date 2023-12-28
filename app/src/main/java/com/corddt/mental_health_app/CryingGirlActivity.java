package com.corddt.mental_health_app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CryingGirlActivity extends AppCompatActivity {

    private ImageView girlImageView;
    private Button motivateButton;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crying_girl);

        girlImageView = findViewById(R.id.cryingGirlImageView);
        motivateButton = findViewById(R.id.motivateButton);
        openDatabase();
        updateGirlStatus();

        motivateButton.setOnClickListener(v -> {
            // 处理按钮点击事件 - 可能会激励用户完成任务
        });
    }

    private void openDatabase() {
        database = openOrCreateDatabase("MotivationalDiary1.db", MODE_PRIVATE, null);
    }

    private void updateGirlStatus() {
        boolean isInactive = checkUserInactivity(3);
        if (isInactive) {
            girlImageView.setImageResource(R.drawable.crying_girl);
            showReminderDialog("You haven't completed any tasks in the last three days. Let's get back on track!");
        } else {
            girlImageView.setImageResource(R.drawable.smiling_girl);
            showReminderDialog("Great job staying active! Keep it up!");
        }
    }

    private boolean checkUserInactivity(int days) {
        String dateLimit = "datetime('now', '-" + days + " days')";
        String query = "SELECT COUNT(*) FROM diary_entries WHERE timestamp > " + dateLimit;
        Cursor cursor = database.rawQuery(query, null);
        boolean isInactive = true;
        if (cursor.moveToFirst()) {
            isInactive = cursor.getInt(0) == 0; // 如果计数为0，则表示用户不活跃
        }
        cursor.close();
        return isInactive;
    }

    private void showReminderDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
