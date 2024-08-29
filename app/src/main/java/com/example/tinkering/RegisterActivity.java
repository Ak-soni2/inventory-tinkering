package com.example.tinkering;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText userNameEditText, emailEditText, passwordEditText;
    private Button createAccountButton;
    private TextView loginTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userNameEditText = findViewById(R.id.user_name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        createAccountButton = findViewById(R.id.create_account_button);
        loginTextView = findViewById(R.id.login_text);

        mAuth = FirebaseAuth.getInstance();

        // check if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToMainActivity();
            return;
        }

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!email.endsWith("@iitj.ac.in")) {
                    Toast.makeText(RegisterActivity.this, "Please use IITJ email", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(userName, email, password);
                }
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser(String userName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        saveUserDetails(userName, email);
                        navigateToMainActivity();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Account creation failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserDetails(String userName, String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("full_name", userName);
        editor.putString("email", email);
        editor.apply();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Do nothing on back press to prevent navigation back to this activity
    }
}
//register activity