package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button btnAddMedicine, btnTake1, btnTake2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        btnAddMedicine = findViewById(R.id.btnAddMedicine);
        btnTake1 = findViewById(R.id.btnTake1);
        btnTake2 = findViewById(R.id.btnTake2);

        btnAddMedicine.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddMedicineActivity.class);
            startActivity(intent);
        });

        btnTake1.setOnClickListener(v -> {
            Toast.makeText(this, "Paracetamol marked as Taken", Toast.LENGTH_SHORT).show();
            btnTake1.setText("Taken");
        });

        btnTake2.setOnClickListener(v -> {
            Toast.makeText(this, "Vitamin D marked as Taken", Toast.LENGTH_SHORT).show();
            btnTake2.setText("Taken");
        });
    }
}