package com.corddt.mental_health_app;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class PlanActivity extends AppCompatActivity {

    private EditText editTextPlan;
    private Button btnSavePlan;
    private ListView listViewPlans;
    private ArrayAdapter<String> planAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        databaseHelper = new DatabaseHelper(this);

        editTextPlan = findViewById(R.id.editTextPlan);
        btnSavePlan = findViewById(R.id.btnSavePlan);
        listViewPlans = findViewById(R.id.listViewPlans);

        loadPlans();

        btnSavePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plan = editTextPlan.getText().toString();
                if (!plan.isEmpty()) {
                    databaseHelper.addPlan(plan);
                    editTextPlan.setText("");
                    loadPlans();
                }
            }
        });
    }

    private void loadPlans() {
        List<String> plans = databaseHelper.getAllPlans();
        planAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, plans);
        listViewPlans.setAdapter(planAdapter);
    }
}
