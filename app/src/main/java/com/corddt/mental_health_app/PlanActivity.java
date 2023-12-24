package com.corddt.mental_health_app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlanActivity extends AppCompatActivity {

    private EditText editTextPlan;
    private RecyclerView recyclerViewPlans;
    private PlanAdapter planAdapter;
    private DatabaseHelper databaseHelper;
    private List<Plan> plans;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        databaseHelper = new DatabaseHelper(this);
        editTextPlan = findViewById(R.id.editTextPlan);
        recyclerViewPlans = findViewById(R.id.recyclerViewPlans);
        recyclerViewPlans.setLayoutManager(new LinearLayoutManager(this));
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

    private void loadPlans() {
        plans = databaseHelper.getAllPlans();
        planAdapter = new PlanAdapter(plans, this);
        recyclerViewPlans.setAdapter(planAdapter);
    }

    private void addPlan() {
        String planContent = editTextPlan.getText().toString();
        if (!planContent.isEmpty()) {
            databaseHelper.addPlan(new Plan(0, planContent, false));
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
            databaseHelper.updatePlan(plan.getId(), updatedPlan);
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
            databaseHelper.togglePlanStatus(plan.getId());
            loadPlans();
        }
    }
}
