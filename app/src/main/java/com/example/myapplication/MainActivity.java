package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    CheckBox mouseCheckbox, keyboardCheckbox, webcamCheckbox;
    TextView totalPriceTextView;
    Button orderButton;
    EditText customerNameEditText;
    LinearLayout inputLayout;
    DatabaseHelper db;
    ArrayList<String> orderHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mouseCheckbox = findViewById(R.id.mouseCheckbox);
        keyboardCheckbox = findViewById(R.id.keyboardCheckbox);
        webcamCheckbox = findViewById(R.id.webcamCheckbox);
        totalPriceTextView = findViewById(R.id.totalPrice);
        orderButton = findViewById(R.id.orderButton);
        customerNameEditText = findViewById(R.id.customerNameEditText);
        inputLayout = findViewById(R.id.inputLayout);
        db = new DatabaseHelper(this);

        mouseCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> toggleInputVisibility());
        keyboardCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> toggleInputVisibility());
        webcamCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> toggleInputVisibility());

        orderButton.setOnClickListener(v -> {
            Pair<String, Double> orderData = calculateAndSaveOrder();
            String customerName = customerNameEditText.getText().toString();
            String orderSummary = orderData.first;
            double totalPrice = orderData.second;

            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            orderHistory.add("Data: " + currentDate + "\n" + orderSummary + "Cena: " + totalPrice + " zł\n");

            Toast.makeText(this, "Zamówienie zapisane!\n" + currentDate, Toast.LENGTH_LONG).show();
        });
    }

    private Pair<String, Double> calculateAndSaveOrder() {
        StringBuilder items = new StringBuilder();
        double totalPrice = 0;

        if (mouseCheckbox.isChecked()) {
            items.append("Mysz Dell MS116, cena 35 zł\n");
            totalPrice += 35;
        }
        if (keyboardCheckbox.isChecked()) {
            items.append("Klawiatura TITANUM TK101, cena 19 zł\n");
            totalPrice += 19;
        }
        if (webcamCheckbox.isChecked()) {
            items.append("Kamera DUXO WEBCAM-X13, cena 89 zł\n");
            totalPrice += 89;
        }

        totalPriceTextView.setText("Cena: " + totalPrice + " zł");
        return new Pair<>(items.toString(), totalPrice);
    }

    private void toggleInputVisibility() {
        if (mouseCheckbox.isChecked() || keyboardCheckbox.isChecked() || webcamCheckbox.isChecked()) {
            inputLayout.setVisibility(View.VISIBLE);
        } else {
            inputLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_order_history:
                Intent intent = new Intent(this, OrderHistoryActivity.class);

                intent.putExtra("orderHistory", String.join("\n", orderHistory));
                startActivity(intent);
                return true;
            case R.id.sendmail:
                Pair<String, Double> orderData = calculateAndSaveOrder();
                String customerName = customerNameEditText.getText().toString();
                sendOrderEmail(customerName, orderData.first, orderData.second);
                return true;
            case R.id.information:
                Toast.makeText(this, "Mikołaj Ornowski", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendOrderEmail(String customerName, String items, double totalPrice) {
        String subject = "Twoje Zamówienie";
        String message = "Dziękujemy za zamówienie!\n\n" +
                "Klient: " + customerName + "\n" +
                "Zamówione przedmioty:\n" + items + "\n" +
                "Łączna cena: " + totalPrice + " zł\n\n" +
                "Pozdrawiamy,\nZespół Obsługi Zamówień";

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Wybierz aplikację do wysyłania e-maila"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Brak aplikacji e-mail na urządzeniu.", Toast.LENGTH_SHORT).show();
        }
    }
}
