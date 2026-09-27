package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.medicare.database.AppDatabase;
import com.example.medicare.database.Medicine;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    AppDatabase db;

    TextView tvMedicine1Name, tvMedicine1Details;
    TextView tvMedicine2Name, tvMedicine2Details;

    Button btnTake1, btnTake2, btnAddMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        tvMedicine1Name = findViewById(R.id.tvMedicine1Name);
        tvMedicine1Details = findViewById(R.id.tvMedicine1Details);
        tvMedicine2Name = findViewById(R.id.tvMedicine2Name);
        tvMedicine2Details = findViewById(R.id.tvMedicine2Details);

        btnTake1 = findViewById(R.id.btnTake1);
        btnTake2 = findViewById(R.id.btnTake2);
        btnAddMedicine = findViewById(R.id.btnAddMedicine);

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
        btnAddMedicine.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddMedicineActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMedicines();
    }

    private void loadMedicines() {

        new Thread(() -> {

            List<Medicine> medicines = db.medicineDao().getAllMedicines();

            runOnUiThread(() -> {

                if (medicines.size() > 0) {

                    Medicine medicine1 = medicines.get(0);

                    tvMedicine1Name.setText("💊  " + medicine1.name);
                    tvMedicine1Details.setText(
                            medicine1.dosage + " • " + medicine1.time
                    );

                    btnTake1.setVisibility(Button.VISIBLE);

                } else {
                    tvMedicine1Name.setText("No medicine added");
                    tvMedicine1Details.setText("");
                    btnTake1.setVisibility(Button.GONE);
                }

                if (medicines.size() > 1) {

                    Medicine medicine2 = medicines.get(1);

                    tvMedicine2Name.setText("💊  " + medicine2.name);
                    tvMedicine2Details.setText(
                            medicine2.dosage + " • " + medicine2.time
                    );

                    btnTake2.setVisibility(Button.VISIBLE);

                } else {
                    tvMedicine2Name.setText("");
                    tvMedicine2Details.setText("");
                    btnTake2.setVisibility(Button.GONE);
                }
            });

        }).start();
    }
}