package com.example.meowing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private final String LOGPREF = "loginSaved";
    private SharedPreferences sharedPreferences;
    private TextView register;
    private TextView textEmail, textPassword;
    private Button login;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPsw;
    private TextView resetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = this.getSharedPreferences(LOGPREF, this.MODE_PRIVATE);

        register = findViewById(R.id.rgBtn);
        register.setOnClickListener(this);

        login = findViewById(R.id.loginBtn);
        login.setOnClickListener(this);

        resetPassword = findViewById(R.id.pswRst);
        resetPassword.setOnClickListener(this);

        textEmail = findViewById(R.id.email);
        textPassword = findViewById(R.id.password);

        tilEmail = findViewById(R.id.tilEmail);
        tilPsw = findViewById(R.id.tilPsw);

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        String spEmail = sharedPreferences.getString("username", null);
        String spPassword = sharedPreferences.getString("password", null);
        if(spEmail != null && spPassword != null){
            userLogin(spEmail, spPassword);
        }

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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rgBtn:
                startActivity(new Intent(this, Registration.class));
                break;
            case R.id.loginBtn:
                String email = textEmail.getText().toString().trim();
                String password = textPassword.getText().toString().trim();
                userLogin(email, password);
                break;
            case R.id.pswRst:
                startActivity(new Intent(this, ResetPassword.class));
                break;
        }
    }


    private void userLogin(String email, String password) {

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

        if (password.isEmpty()) {
            tilPsw.setError("Inserire password");
            tilPsw.requestFocus();
            return;
        }

        if (password.length() < 6) {
            tilPsw.setError("La password deve contenere almeno 6 caratteri");
            tilPsw.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    progressBar.setVisibility(View.GONE);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()){
                        Log.println(Log.DEBUG, "email", "verificata");
                        startActivity(new Intent(Login.this, MainActivity.class));
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", email);
                        editor.putString("password", password);
                        editor.commit();
                        finish();
                    } else {
                        Log.println(Log.DEBUG, "email", "non verificata");
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Controlla la mail", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Accesso negato, controlla i dati inseriti", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}