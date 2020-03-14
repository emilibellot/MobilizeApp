package com.app.mobilize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText user, pass, email;
    Button register;

    private String username = "";
    private String password = "";
    private String mail ="";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseFirestore db;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        init();
    }

    public void init() {

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        email = findViewById(R.id.email);

        register = findViewById(R.id.button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user.getText().toString();
                password = pass.getText().toString();
                mail = email.getText().toString();
                // If one of the fields is empty
                if ( mail.isEmpty() || username.isEmpty() || password.isEmpty() ) {
                    alertDialog("Completa todos los campos.");
                }
                // Email is not empty and it have a valid email format
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches())
                    email.setError("Enter a valid email address");

                    // Password is not empty and the length of the password is between 6 and 10 characters
                else if ( password.length() < 6 || password.length() > 10 ) {
                    alertDialog("Enter a valid password!");
                    pass.setError("Enter a password between 6 and 10 alphanumeric characters");
                }
                else {
                    RegisterUser();
                }
            }
        });
    }

    public void alertDialog(String message) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Error").setCancelable(true);

        AlertDialog alert = builder.create();

        alert.setTitle("ERROR");
        alert.show();
    }

    public void RegisterUser () {
        // Create the user with the email and password introduced
        mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Getting the id of the current user

                    List<Integer> peso = new ArrayList<Integer>();

                    Usuari user = new Usuari(username, password, mail);

                    db.collection("users").document(username).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent sig = new Intent(RegisterActivity.this, RegisterMessageActivity.class);
                            sig.putExtra("text", "Se ha enviado un correo de confirmación a tu cuenta de correo electrónico: " + mail + ".");
                            enviaCorreoConfirmacion();
                            finish();
                            startActivity(sig);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            alertDialog("Error en el registro. Inténtelo de nuevo mas tarde.");
                        }
                    });
                } else {
                    // If the user aldready exists show the alert
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthUserCollisionException e) {
                        alertDialog("USER ALREADY EXISTS");
                    } catch(Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });
    }
    public void enviaCorreoConfirmacion() {
        if ( mAuth.getCurrentUser().isEmailVerified() == false ) {
            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if ( task.isSuccessful() );
                    else alertDialog("No se pudo enviar el correo de confirmación, inténtelo de nuevo más tarde.");
                }
            });
        }
    }
}