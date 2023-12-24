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
    private OnPlanListener onPlanListener;

    public PlanAdapter(List<Plan> planList, Context context, OnPlanListener onPlanListener) {
        this.planList = planList;
        this.context = context;
        this.onPlanListener = onPlanListener;
    }

    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new PlanViewHolder(view, onPlanListener);
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

    public static class PlanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView planTextView;
        OnPlanListener onPlanListener;

        public PlanViewHolder(View itemView, OnPlanListener onPlanListener) {
            super(itemView);
            planTextView = itemView.findViewById(android.R.id.text1);
            this.onPlanListener = onPlanListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onPlanListener.onPlanClick(getAdapterPosition());
        }
    }

    public interface OnPlanListener {
        void onPlanClick(int position);
    }
}
