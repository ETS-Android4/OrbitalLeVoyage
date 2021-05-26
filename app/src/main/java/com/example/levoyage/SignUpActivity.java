package com.example.levoyage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    private EditText signUpEmail, signUpPassword, signUpUsername;
    private Button signUpButton;
    private ImageButton backToLoginButton;
    private FirebaseAuth myFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpEmail = findViewById(R.id.SignUpEmail);
        signUpPassword = findViewById(R.id.SignUpPassword);
        signUpUsername = findViewById(R.id.SignUpUsername);
        signUpButton = findViewById(R.id.SignUpButton);
        myFirebaseAuth = FirebaseAuth.getInstance();
        backToLoginButton = findViewById(R.id.BackToLoginButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signUpEmail.getText().toString();
                String pwd = signUpPassword.getText().toString();
                String name = signUpUsername.getText().toString();
                if (email.isEmpty() && pwd.isEmpty() && name.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Fields are empty!!",
                            Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()) {
                    signUpUsername.setError("Please enter your username");
                    signUpUsername.requestFocus();
                } else if (email.isEmpty()) {
                    signUpEmail.setError("Please enter your email");
                    signUpEmail.requestFocus();
                } else if (pwd.isEmpty()) {
                    signUpPassword.setError("Please enter your password");
                    signUpPassword.requestFocus();
                } else {
                    myFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(
                            SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Sign up failed",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name).build();
                                        myFirebaseAuth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignUpActivity.this, "Sign up successful",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });

        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
}