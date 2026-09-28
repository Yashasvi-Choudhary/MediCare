package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.medicare.database.AppDatabase;
import com.example.medicare.database.Medicine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    AppDatabase db;

    TextView tvGreeting, tvDate, tvMedicineDate;
    TextView tvViewAllMedicines;
    LinearLayout emptyMedicineState;

    RecyclerView rvTodayMedicines;

    Button btnAddMedicine;
    ImageButton btnNotifications;

    MedicineAdapter medicineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        tvGreeting = findViewById(R.id.tvGreeting);
        tvDate = findViewById(R.id.tvDate);
        tvMedicineDate = findViewById(R.id.tvMedicineDate);

        tvViewAllMedicines = findViewById(R.id.tvViewAllMedicines);
        emptyMedicineState = findViewById(R.id.emptyMedicineState);

        rvTodayMedicines = findViewById(R.id.rvTodayMedicines);

        btnAddMedicine = findViewById(R.id.btnAddMedicine);
        btnNotifications = findViewById(R.id.btnNotifications);

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

        setupRecyclerView();
        setGreetingAndDate();

        btnAddMedicine.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddMedicineActivity.class);
            startActivity(intent);
        });

        btnNotifications.setOnClickListener(v -> {
            // Notifications screen will be connected later
        });

        tvViewAllMedicines.setOnClickListener(v -> {
            // My Medicines screen will be connected later
        });
    }

    private void setupRecyclerView() {
        medicineAdapter = new MedicineAdapter(new ArrayList<>());

        rvTodayMedicines.setLayoutManager(
                new LinearLayoutManager(this)
        );

        rvTodayMedicines.setAdapter(medicineAdapter);
    }

    private void setGreetingAndDate() {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 5 && hour < 12) {
            tvGreeting.setText("Good Morning 👋");
        } else if (hour >= 12 && hour < 17) {
            tvGreeting.setText("Good Afternoon 👋");
        } else if (hour >= 17 && hour < 21) {
            tvGreeting.setText("Good Evening 👋");
        } else {
            tvGreeting.setText("Good Night 👋");
        }

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());

        String today = dateFormat.format(calendar.getTime());

        tvDate.setText(today);
        tvMedicineDate.setText("Today's Medicines");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db != null) {
            loadTodaysMedicines();
        }
    }

    private void loadTodaysMedicines() {

        new Thread(() -> {

            List<Medicine> allMedicines =
                    db.medicineDao().getAllMedicines();

            List<Medicine> todaysMedicines = new ArrayList<>();

            String today = new SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
            ).format(Calendar.getInstance().getTime());

            for (Medicine medicine : allMedicines) {

                if (isMedicineForToday(medicine, today)) {
                    todaysMedicines.add(medicine);
                }
            }

            runOnUiThread(() -> {

                if (todaysMedicines.isEmpty()) {

                    rvTodayMedicines.setVisibility(View.GONE);
                    emptyMedicineState.setVisibility(View.VISIBLE);

                } else {

                    rvTodayMedicines.setVisibility(View.VISIBLE);
                    emptyMedicineState.setVisibility(View.GONE);

                    medicineAdapter.updateList(todaysMedicines);
                }
            });

        }).start();
    }

    private boolean isMedicineForToday(Medicine medicine, String today) {

        try {

            SimpleDateFormat dateFormat =
                    new SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                    );

            Calendar todayCalendar = Calendar.getInstance();
            todayCalendar.setTime(dateFormat.parse(today));

            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(dateFormat.parse(medicine.startDate));

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(dateFormat.parse(medicine.endDate));

            if (todayCalendar.before(startCalendar)
                    || todayCalendar.after(endCalendar)) {
                return false;
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}