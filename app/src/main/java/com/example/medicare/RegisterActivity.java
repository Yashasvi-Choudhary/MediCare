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

public class RegisterActivity extends AppCompatActivity {

    EditText etFullname, etEmail, etPswd, etConfirmPswd;
    TextView tvLogin;
    Button btnRegister;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        etFullname = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPswd = findViewById(R.id.etPassword);
        etConfirmPswd = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

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

        btnRegister.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(
                    RegisterActivity.this,
                    LoginActivity.class
            );
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {

        String name = etFullname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPswd.getText().toString().trim();
        String confirmPassword = etConfirmPswd.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()
                || confirmPassword.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            etEmail.setError("Enter a valid email address");

            Toast.makeText(
                    this,
                    "Please enter a valid email",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (password.length() < 6) {

            etPswd.setError("Password must be at least 6 characters");

            Toast.makeText(
                    this,
                    "Password must be at least 6 characters",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (!password.equals(confirmPassword)) {

            etConfirmPswd.setError("Passwords do not match");

            Toast.makeText(
                    this,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        new Thread(() -> {

            User existingUser = db.userDao().getUserByEmail(email);

            if (existingUser != null) {

                runOnUiThread(() -> {
                    etEmail.setError("Email already registered");

                    Toast.makeText(
                            this,
                            "Email already registered",
                            Toast.LENGTH_SHORT
                    ).show();
                });

                return;
            }

            User user = new User();

            user.name = name;
            user.email = email;
            user.password = password;

            db.userDao().insert(user);

            runOnUiThread(() -> {

                Toast.makeText(
                        this,
                        "Registration successful",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent = new Intent(
                        RegisterActivity.this,
                        LoginActivity.class
                );

                startActivity(intent);
                finish();
            });

        }).start();
    }
}