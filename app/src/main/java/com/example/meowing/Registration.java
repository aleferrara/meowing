package com.example.meowing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText textNome, textCognome, textEmail, textPassword, textPassword2;
    private ProgressBar progressBar;
    private TextView lgnBtn;
    private Button registerBtn;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPsw;
    private TextInputLayout tilPsw2;
    private TextInputLayout tilNome;
    private TextInputLayout tilCognome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        lgnBtn = findViewById(R.id.lgBtn);
        lgnBtn.setOnClickListener(this);

        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);

        textNome = findViewById(R.id.nome);
        textCognome = findViewById(R.id.cognome);
        textEmail = findViewById(R.id.email);
        textPassword = findViewById(R.id.password);
        textPassword2 = findViewById(R.id.password2);

        tilNome = findViewById(R.id.tilNome);
        tilCognome = findViewById(R.id.tilCognome);
        tilEmail = findViewById(R.id.tilEmail);
        tilPsw = findViewById(R.id.tilPsw);
        tilPsw2 = findViewById(R.id.tilPsw2);

        progressBar = findViewById(R.id.progressBar);

        textNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                tilNome.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textCognome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                tilCognome.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                tilEmail.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                tilPsw.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                tilPsw2.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.lgBtn:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.registerBtn:
                registerUser();
                break;
        }

    }

    private void registerUser() {

        String nome = textNome.getText().toString().trim();
        String cognome = textCognome.getText().toString().trim();
        String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();
        String password2 = textPassword2.getText().toString().trim();

        if (nome.isEmpty()){
            tilNome.setError("Inserire nome");
            tilNome.requestFocus();
            return;
        }

        if (cognome.isEmpty()){
            tilCognome.setError("Inserire cognome");
            tilCognome.requestFocus();
            return;
        }

        if (email.isEmpty()){
            tilEmail.setError("Inserire email");
            tilEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            tilEmail.setError("Inserire email valida");
            tilEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            tilPsw.setError("Inserire password");
            tilPsw.requestFocus();
            return;
        }

        if (password.length() < 6){
            tilPsw.setError("La password deve contenere almeno 6 caratteri");
            tilPsw.requestFocus();
            return;
        }

        if (password2.isEmpty()){
            tilPsw.setError("Inserire password");
            tilPsw.requestFocus();
            return;
        }

        if (password2.length() < 6){
            tilPsw2.setError("La password deve contenere almeno 6 caratteri");
            tilPsw2.requestFocus();
            return;
        }

        if (!(password.equals(password2))){
            tilPsw.setError("Le password non corrispondono");
            tilPsw.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(nome, cognome, email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(Registration.this, "Utente registrato correttamente", Toast.LENGTH_LONG).show();
                                                            startActivity(new Intent(Registration.this, PostReg.class));
                                                            progressBar.setVisibility(View.GONE);
                                                        } else {
                                                            Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(Registration.this, "Impossibile registrare l'utente", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(Registration.this, "Impossibile registrare l'utente", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

    }
}