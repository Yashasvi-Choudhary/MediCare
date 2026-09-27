package com.example.medicare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.medicare.database.AppDatabase;
import com.example.medicare.database.Medicine;


public class AddMedicineActivity extends AppCompatActivity {

    EditText etMedicineName, etDosage, etQuantity, etTime, etFrequency, etStartDate, etEndDate;
    Button btnSaveMedicine;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_medicine);

        etMedicineName = findViewById(R.id.etMedicineName);
        etDosage = findViewById(R.id.etDosage);
        etQuantity = findViewById(R.id.etQuantity);
        etTime = findViewById(R.id.etTime);
        etFrequency = findViewById(R.id.etFrequency);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        btnSaveMedicine = findViewById(R.id.btnSaveMedicine);

        db = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "medicare_database"
                )
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .build();

        btnSaveMedicine.setOnClickListener(v -> saveMedicine());
    }

    private void saveMedicine() {
        Medicine medicine = new Medicine();

        medicine.name = etMedicineName.getText().toString().trim();
        medicine.dosage = etDosage.getText().toString().trim();
        medicine.quantity = Integer.parseInt(etQuantity.getText().toString().trim());
        medicine.time = etTime.getText().toString().trim();
        medicine.frequency = etFrequency.getText().toString().trim();
        medicine.startDate = etStartDate.getText().toString().trim();
        medicine.endDate = etEndDate.getText().toString().trim();

        new Thread(() -> {
            db.medicineDao().insert(medicine);

            runOnUiThread(() -> {
                Toast.makeText(this, "Medicine saved", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}