package com.example.phmsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterUser extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Get references to the email and password text fields
        EditText emailField = findViewById(R.id.email);
        EditText passwordField = findViewById(R.id.password);

        // Get reference to the sign-up button
        Button signUpButton = findViewById(R.id.loginbutton);

        // Set click listener on sign-up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user's email and password from text fields
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                // Validate email and password
                if (email.isEmpty()) {
                    emailField.setError("Email is required!");
                    emailField.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailField.setError("Please enter a valid email!");
                    emailField.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    passwordField.setError("Password required");
                    passwordField.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    passwordField.setError("Min password length is 6 char");
                    passwordField.requestFocus();
                    return;
                }

                // Call createUserWithEmailAndPassword() method of FirebaseAuth to create new user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterUser.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign-up success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // TODO: add code to update UI
                                    // Move the user to dashboard screen
                                    Intent intent = new Intent(RegisterUser.this, Dashboard.class);
                                    startActivity(intent);
                                    finish(); // Close the current activity to prevent the user from going back to the sign-up screen
                                } else {
                                    // If sign-up fails, display a message to the user.
                                    Toast.makeText(RegisterUser.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


}