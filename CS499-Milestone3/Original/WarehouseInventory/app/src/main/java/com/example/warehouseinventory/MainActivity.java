package com.example.warehouseinventory;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehouseinventory.data.UserRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private UserRepository userRepo;
    private TextInputEditText etUsername;
    private TextInputEditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_login);

        userRepo = new UserRepository(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        MaterialButton btnCreate = findViewById(R.id.btnCreateAccount);

        btnLogin.setOnClickListener(v -> handleLogin());
        btnCreate.setOnClickListener(v -> handleCreateAccount());
    }

    private void handleLogin() {
        String username = safeText(etUsername);
        String password = safeText(etPassword);

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter username and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userRepo.validateLogin(username, password)) {
            Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, InventoryActivity.class));
        } else {
            Toast.makeText(this, "Invalid login. Create an account if you're new.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCreateAccount() {
        String username = safeText(etUsername);
        String password = safeText(etPassword);

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter username and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userRepo.userExists(username)) {
            Toast.makeText(this, "Username already exists. Log in instead.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean created = userRepo.createUser(username, password);
        if (created) {
            Toast.makeText(this, "Account created. You can log in now.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Could not create account. Try a different username.", Toast.LENGTH_SHORT).show();
        }
    }

    private String safeText(TextInputEditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}