package com.corddt.mental_health_app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlanActivity extends AppCompatActivity {

    private EditText editTextPlan;
    private RecyclerView recyclerViewPlans;
    private PlanAdapter planAdapter;
    private SQLiteDatabase database;
    private List<Plan> plans;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        editTextPlan = findViewById(R.id.editTextPlan);
        recyclerViewPlans = findViewById(R.id.recyclerViewPlans);
        recyclerViewPlans.setLayoutManager(new LinearLayoutManager(this));
        openDatabase();
        loadPlans();

        Button btnSavePlan = findViewById(R.id.btnSavePlan);
        btnSavePlan.setOnClickListener(v -> addPlan());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToToggleStatusCallback());
        itemTouchHelper.attachToRecyclerView(recyclerViewPlans);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                View view = recyclerViewPlans.findChildViewUnder(e.getX(), e.getY());
                if (view != null) {
                    int position = recyclerViewPlans.getChildAdapterPosition(view);
                    editPlanDialog(plans.get(position));
                    return true;
                }
                return false;
            }
        });

        recyclerViewPlans.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        Button btnToBottle = findViewById(R.id.btnToBottle);
        btnToBottle.setOnClickListener(v -> {
            Intent intent = new Intent(PlanActivity.this, BottleActivity.class);
            startActivity(intent);
        });
    }

    private void openDatabase() {
        database = openOrCreateDatabase("MotivationalDiary1.db", MODE_PRIVATE, null);
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String CREATE_PLANS_TABLE = "CREATE TABLE IF NOT EXISTS plans ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "plan TEXT,"
                + "completed INTEGER DEFAULT 0,"
                + "timestamp TEXT DEFAULT (strftime('%Y-%m-%d', 'now')))";
        database.execSQL(CREATE_PLANS_TABLE);
    }

    private void loadPlans() {
        plans = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM plans", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String content = cursor.getString(cursor.getColumnIndex("plan"));
            int completed = cursor.getInt(cursor.getColumnIndex("completed"));
            plans.add(new Plan(id, content, completed == 1));
        }
        cursor.close();
        planAdapter = new PlanAdapter(plans, this);
        recyclerViewPlans.setAdapter(planAdapter);
    }

    private void addPlan() {
        String planContent = editTextPlan.getText().toString();
        if (!planContent.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put("plan", planContent);
            values.put("completed", 0);
            database.insert("plans", null, values);
            editTextPlan.setText("");
            loadPlans();
        }
    }

    private void editPlanDialog(Plan plan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Plan");
        final EditText input = new EditText(this);
        input.setText(plan.getContent());
        builder.setView(input);
        builder.setPositiveButton("Update", (dialog, which) -> {
            String updatedPlan = input.getText().toString();
            ContentValues values = new ContentValues();
            values.put("plan", updatedPlan);
            database.update("plans", values, "id = ?", new String[]{String.valueOf(plan.getId())});
            loadPlans();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private class SwipeToToggleStatusCallback extends ItemTouchHelper.SimpleCallback {
        SwipeToToggleStatusCallback() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Plan plan = plans.get(position);
            togglePlanStatus(plan.getId());
            loadPlans();
        }

        private void togglePlanStatus(int planId) {
            Cursor cursor = database.query("plans", new String[]{"completed", "timestamp"}, "id = ?", new String[]{String.valueOf(planId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int currentStatus = cursor.getInt(cursor.getColumnIndex("completed"));
                String timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));

                if (currentStatus == 0 && canCompletePlan()) {
                    ContentValues values = new ContentValues();
                    values.put("completed", 1);
                    values.put("timestamp", getCurrentTimestamp());
                    database.update("plans", values, "id = ?", new String[]{String.valueOf(planId)});
                    Toast.makeText(PlanActivity.this, "You're amazing, you've taken another step towards mastering your own life.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PlanActivity.this, "The gap between the two plans is too short (less than 25 minutes). Please wait a little longer.", Toast.LENGTH_LONG).show();
                }
                cursor.close();
            }
        }

        private boolean canCompletePlan() {
            Cursor cursor = database.rawQuery("SELECT timestamp FROM plans WHERE completed = 1 ORDER BY timestamp DESC LIMIT 1", null);
            if (cursor != null && cursor.moveToFirst()) {
                String lastCompletedTimestamp = cursor.getString(cursor.getColumnIndex("timestamp"));
                cursor.close();
                return isTimeDifferenceSufficient(lastCompletedTimestamp);
            }
            return true; // 如果没有先前的完成计划，允许完成当前计划
        }

        private boolean isTimeDifferenceSufficient(String lastCompletedTimestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try {
                Date lastCompletedDate = sdf.parse(lastCompletedTimestamp);
                long timeDifference = new Date().getTime() - lastCompletedDate.getTime();
                return timeDifference >= (25 * 60 * 1000); // 25分钟
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private String getCurrentTimestamp() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(new Date());
        }
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
