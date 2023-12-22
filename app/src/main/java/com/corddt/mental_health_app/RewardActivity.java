package com.corddt.mental_health_app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class RewardActivity extends AppCompatActivity {

    private ListView listViewRewards;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        listViewRewards = findViewById(R.id.listViewRewards);
        databaseHelper = new DatabaseHelper(this);

        loadRewards();
    }

    private void loadRewards() {
        List<String> rewards = databaseHelper.getAllRewards(); // 假设有这样的方法
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rewards);
        listViewRewards.setAdapter(adapter);
    }
}
