package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.medicare.database.AppDatabase;
import com.example.medicare.database.User;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    TextView tvRegister;
    Button btnLogin;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

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

        btnLogin.setOnClickListener(v -> loginUser());

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );
            startActivity(intent);
        });
    }

    private void loginUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        new Thread(() -> {

            User user = db.userDao().login(email, password);

            runOnUiThread(() -> {

                if (user != null) {

                    getSharedPreferences("MedicarePrefs", MODE_PRIVATE)
                            .edit()
                            .putInt("userId", user.id)
                            .apply();

                    Toast.makeText(
                            this,
                            "Login successful",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent = new Intent(
                            LoginActivity.this,
                            HomeActivity.class
                    );

                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(
                            this,
                            "Invalid email or password",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });

        }).start();
    }
}