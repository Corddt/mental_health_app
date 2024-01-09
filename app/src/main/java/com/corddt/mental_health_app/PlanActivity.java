package com.corddt.mental_health_app;

import android.animation.Animator;
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

import com.airbnb.lottie.LottieAnimationView;

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
    private LottieAnimationView congratulationsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        editTextPlan = findViewById(R.id.editTextPlan);
        recyclerViewPlans = findViewById(R.id.recyclerViewPlans);
        recyclerViewPlans.setLayoutManager(new LinearLayoutManager(this));
        congratulationsView = findViewById(R.id.congratulationsView);

        // 初始化 plans 列表
        plans = new ArrayList<>();

        // 设置 RecyclerView 的适配器
        planAdapter = new PlanAdapter(plans, this);
        recyclerViewPlans.setAdapter(planAdapter);

        openDatabase();

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

        loadPlans(); // 加载当天的计划
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
        plans.clear();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Cursor cursor = database.rawQuery("SELECT * FROM plans WHERE timestamp = ?", new String[]{currentDate});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String content = cursor.getString(cursor.getColumnIndex("plan"));
            int completed = cursor.getInt(cursor.getColumnIndex("completed"));
            plans.add(new Plan(id, content, completed == 1));
        }
        cursor.close();
        planAdapter.notifyDataSetChanged(); // 确保数据变化后刷新适配器
    }

    private void addPlan() {
        String planContent = editTextPlan.getText().toString();
        if (!planContent.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put("plan", planContent);
            values.put("completed", 0);
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            values.put("timestamp", currentDate); // 只使用日期部分
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
            togglePlanStatus(plan.getId(), position);
        }

        private void togglePlanStatus(int planId, int position) {
            Cursor cursor = database.query("plans", new String[]{"completed"}, "id = ?", new String[]{String.valueOf(planId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int currentStatus = cursor.getInt(cursor.getColumnIndex("completed"));
                ContentValues values = new ContentValues();
                values.put("completed", currentStatus == 0 ? 1 : 0); // 切换状态
                database.update("plans", values, "id = ?", new String[]{String.valueOf(planId)});
                plans.get(position).setCompleted(currentStatus == 0); // 更新列表中的计划状态
                planAdapter.notifyItemChanged(position); // 仅刷新改变的项
                if (currentStatus == 0) {
                    playCongratulationsAnimation();
                }
            }
            cursor.close();
        }

        private void playCongratulationsAnimation() {
            congratulationsView.setVisibility(View.VISIBLE); // 确保视图可见
            congratulationsView.setAnimation("WellDone.json");
            congratulationsView.playAnimation();
            congratulationsView.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    congratulationsView.setVisibility(View.GONE); // 动画播放完后隐藏
                }

                @Override
                public void onAnimationCancel(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
        }
    }


    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
