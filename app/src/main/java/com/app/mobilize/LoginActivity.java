package com.app.mobilize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;

    String uid;

    Button login,register;

    FirebaseAuth mAuth;

    TextView tv;

    String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        init();
    }

    public void init() {
        email = findViewById(R.id.etUser);
        password = findViewById(R.id.etPassword);

        tv = findViewById(R.id.textRegister);
        tv.setText("Don’t have an account?");

        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.registerButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sigIntent();
            }
        });
    }

    public void makeToast ( String s ) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    public void alertDialog(String message) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Error").setCancelable(true);

        AlertDialog alert = builder.create();

        alert.setTitle("Error");
        alert.show();
    }

    public void loginUser() {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser().reload();
        if (mAuth.getCurrentUser().isEmailVerified() == false){
            progressDialog.dismiss();
            alertDialog("Confirma primero tu dirección de correo electrónico!");
        }
        else {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        uid = mAuth.getCurrentUser().getUid();
                        Toast.makeText(getApplicationContext(), uid, Toast.LENGTH_SHORT).show();
                        sig2Intent();
                    } else {
                        progressDialog.dismiss();
                        alertDialog("Error en el correo electrónico o contraseña");
                    }
                }
            });
        }
    }

    public void sigIntent () {
        Intent sig = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(sig);
        finish();
    }

    public void sig2Intent () {
        Intent sig = new Intent(LoginActivity.this, MainActivity.class);
        sig.putExtra("email", email.getText().toString());
        startActivity(sig);
    }


}