package com.example.medicare;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.medicare.database.AppDatabase;
import com.example.medicare.database.Medicine;

import java.util.Calendar;

public class AddMedicineActivity extends AppCompatActivity {

    EditText etMedicineName, etQuantity, etTime, etStartDate, etEndDate;
    Spinner spDosage, spFrequency;
    Button btnSaveMedicine;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_medicine);

        etMedicineName = findViewById(R.id.etMedicineName);
        etQuantity = findViewById(R.id.etQuantity);
        etTime = findViewById(R.id.etTime);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);

        spDosage = findViewById(R.id.spDosage);
        spFrequency = findViewById(R.id.spFrequency);

        btnSaveMedicine = findViewById(R.id.btnSaveMedicine);

        db = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "medicare_database"
                )
                .addMigrations(
                        AppDatabase.MIGRATION_1_2,
                        AppDatabase.MIGRATION_2_3
                )
                .build();

        setupSpinners();

        etTime.setOnClickListener(v -> showTimePicker());
        etStartDate.setOnClickListener(v -> showStartDatePicker());
        etEndDate.setOnClickListener(v -> showEndDatePicker());

        btnSaveMedicine.setOnClickListener(v -> saveMedicine());
    }

    private void setupSpinners() {

        String[] dosages = {
                "Select Dosage",
                "1 Tablet",
                "2 Tablets",
                "1 Capsule",
                "2 Capsules",
                "5 ml",
                "10 ml"
        };

        String[] frequencies = {
                "Select Frequency",
                "Once Daily",
                "Twice Daily",
                "Thrice Daily"
        };

        ArrayAdapter<String> dosageAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                dosages
        );

        ArrayAdapter<String> frequencyAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                frequencies
        );

        spDosage.setAdapter(dosageAdapter);
        spFrequency.setAdapter(frequencyAdapter);
    }

    private void showTimePicker() {

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {

                    String amPm;

                    if (selectedHour >= 12) {
                        amPm = "PM";
                    } else {
                        amPm = "AM";
                    }

                    int hour12 = selectedHour % 12;

                    if (hour12 == 0) {
                        hour12 = 12;
                    }

                    String time = String.format(
                            "%02d:%02d %s",
                            hour12,
                            selectedMinute,
                            amPm
                    );

                    etTime.setText(time);
                },
                hour,
                minute,
                false
        );

        timePickerDialog.show();
    }

    private void showStartDatePicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    String date = String.format(
                            "%02d/%02d/%04d",
                            dayOfMonth,
                            month + 1,
                            year
                    );

                    etStartDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void showEndDatePicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    String date = String.format(
                            "%02d/%02d/%04d",
                            dayOfMonth,
                            month + 1,
                            year
                    );

                    etEndDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void saveMedicine() {

        String name = etMedicineName.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();

        String dosage = spDosage.getSelectedItem().toString();
        String frequency = spFrequency.getSelectedItem().toString();

        if (name.isEmpty()) {
            etMedicineName.setError("Enter medicine name");
            return;
        }

        if (spDosage.getSelectedItemPosition() == 0) {
            Toast.makeText(
                    this,
                    "Please select dosage",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (quantityText.isEmpty()) {
            etQuantity.setError("Enter quantity");
            return;
        }

        if (time.isEmpty()) {
            Toast.makeText(
                    this,
                    "Please select time",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (spFrequency.getSelectedItemPosition() == 0) {
            Toast.makeText(
                    this,
                    "Please select frequency",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (startDate.isEmpty()) {
            Toast.makeText(
                    this,
                    "Please select start date",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (endDate.isEmpty()) {
            Toast.makeText(
                    this,
                    "Please select end date",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        int quantity = Integer.parseInt(quantityText);

        Medicine medicine = new Medicine();

        medicine.name = name;
        medicine.dosage = dosage;
        medicine.quantity = quantity;
        medicine.time = time;
        medicine.frequency = frequency;
        medicine.startDate = startDate;
        medicine.endDate = endDate;

        new Thread(() -> {

            db.medicineDao().insert(medicine);

            runOnUiThread(() -> {

                Toast.makeText(
                        this,
                        "Medicine saved",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            });

        }).start();
    }
}