package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    EditText etFullname ,etEmail, etPswd , etConfirmPswd;
    TextView tvLogin;
    Button btnRegister;
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


        btnRegister.setOnClickListener(v -> {

            String userName = etFullname.getText().toString().trim();
            String pswd = etPswd.getText().toString().trim();

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }
}