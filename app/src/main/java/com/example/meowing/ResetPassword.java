package com.example.meowing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText emailText;
    private Button resetPasswordButton;
    private ProgressBar progressBar;
    private TextInputLayout tilEmail;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailText = findViewById(R.id.emailRst);
        resetPasswordButton = findViewById(R.id.pswRst);
        progressBar = findViewById(R.id.progressBar);
        tilEmail = findViewById(R.id.tilEmail);
        mAuth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {

        String email = emailText.getText().toString().trim();

        if (email.isEmpty()) {
            tilEmail.setError("Inserire email");
            tilEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            tilEmail.setError("Inserire email valida");
            tilEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(ResetPassword.this, "Controlla la tua email per resettare la password!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(ResetPassword.this, Login.class));
                } else {
                    Toast.makeText(ResetPassword.this, "Qualcosa è andato storto, riprova", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}