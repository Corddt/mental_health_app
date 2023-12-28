package com.corddt.mental_health_app;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

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

        findViewById(R.id.btnSavePlan).setOnClickListener(v -> addPlan());

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
                + "timestamp TEXT DEFAULT (strftime('%Y-%m-%d', 'now')))"; // 添加 timestamp 列
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
            values.put("completed", 0); // Assuming new plans are not completed
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
            Cursor cursor = database.query("plans", new String[]{"completed"}, "id = ?", new String[]{String.valueOf(planId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int currentStatus = cursor.getInt(cursor.getColumnIndex("completed"));
                ContentValues values = new ContentValues();
                values.put("completed", currentStatus == 0 ? 1 : 0);
                database.update("plans", values, "id = ?", new String[]{String.valueOf(planId)});
                cursor.close();
            }
        }
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
