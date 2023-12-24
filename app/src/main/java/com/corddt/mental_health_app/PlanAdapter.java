package com.corddt.mental_health_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    private List<Plan> planList;
    private Context context;

    public PlanAdapter(List<Plan> planList, Context context) {
        this.planList = planList;
        this.context = context;
    }

    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlanViewHolder holder, int position) {
        Plan plan = planList.get(position);
        holder.planTextView.setText(plan.getContent());
        // Change the text color based on completion status
        holder.planTextView.setTextColor(plan.isCompleted() ? context.getResources().getColor(android.R.color.holo_green_dark) : context.getResources().getColor(android.R.color.black));
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        public TextView planTextView;

        public PlanViewHolder(View itemView) {
            super(itemView);
            planTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}
