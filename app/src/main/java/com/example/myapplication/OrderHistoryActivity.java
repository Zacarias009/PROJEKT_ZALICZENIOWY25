package com.example.myapplication;

import static com.example.myapplication.R.layout.activity_order_history;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OrderHistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_order_history);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Lista Zamówień");
        }

        String orderHistory = getIntent().getStringExtra("orderHistory");

        TextView historyTextView = findViewById(R.id.historyTextView);

        if (orderHistory == null || orderHistory.isEmpty()) {
            orderHistory = "Brak historii zamówień";
        }

        historyTextView.setText(orderHistory);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
