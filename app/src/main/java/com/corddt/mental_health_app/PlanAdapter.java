package com.corddt.mental_health_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    private List<Plan> planList;
    private Context context;
    private SQLiteDatabase database;

    public PlanAdapter(List<Plan> planList, Context context, SQLiteDatabase database) {
        this.planList = planList;
        this.context = context;
        this.database = database;
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
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        public TextView planTextView;
        public CheckBox completedCheckbox;

        public PlanViewHolder(View itemView) {
            super(itemView);
            planTextView = itemView.findViewById(R.id.text_plan_content);
            completedCheckbox = itemView.findViewById(R.id.checkbox_completed);
        }
    }
}

