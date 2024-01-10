package com.corddt.mental_health_app;

import android.animation.Animator;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

        // 打开数据库
        openDatabase();

        // 设置 RecyclerView 的适配器
        planAdapter = new PlanAdapter(plans, this);
        recyclerViewPlans.setAdapter(planAdapter);

        Button btnSavePlan = findViewById(R.id.btnSavePlan);
        btnSavePlan.setOnClickListener(v -> addPlan());

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

    private class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
        private List<Plan> planList;
        private Context context;

        public PlanAdapter(List<Plan> planList, Context context) {
            this.planList = planList;
            this.context = context;
        }

        @Override
        public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_list_item, parent, false);
            return new PlanViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PlanViewHolder holder, int position) {
            Plan plan = planList.get(position);
            holder.planTextView.setText(plan.getContent());
            holder.completedCheckbox.setChecked(plan.isCompleted());

            holder.completedCheckbox.setOnCheckedChangeListener(null);
            holder.completedCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                togglePlanStatus(plan.getId(), isChecked);
                plan.setCompleted(isChecked);
            });
        }

        private void togglePlanStatus(int planId, boolean isCompleted) {
            ContentValues values = new ContentValues();
            values.put("completed", isCompleted ? 1 : 0);
            database.update("plans", values, "id = ?", new String[]{String.valueOf(planId)});

            if (isCompleted) {
                playCongratulationsAnimation();
            }
        }

        @Override
        public int getItemCount() {
            return planList.size();
        }

        public class PlanViewHolder extends RecyclerView.ViewHolder {
            public TextView planTextView;
            public CheckBox completedCheckbox;

            public PlanViewHolder(View itemView) {
                super(itemView);
                planTextView = itemView.findViewById(R.id.text_plan_content);
                completedCheckbox = itemView.findViewById(R.id.checkbox_completed);
            }
        }
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
