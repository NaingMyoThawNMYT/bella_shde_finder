package com.example.bellashdefinder.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.bellashdefinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    public static final String KEY_ADMIN_MODE = "admin_mode";
    private boolean adminMode = false;

    private FirebaseAuth mAuth;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(KEY_ADMIN_MODE)) {
            adminMode = b.getBoolean(KEY_ADMIN_MODE);
            if (!adminMode) {
                findViewById(R.id.btn_skip_login).setVisibility(View.VISIBLE);
            }
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
    }

    public void login(View v) {
        Editable edtEmail = ((AppCompatEditText) findViewById(R.id.edt_email)).getText();
        Editable edtPassword = ((AppCompatEditText) findViewById(R.id.edt_password)).getText();

        String email = edtEmail != null ? edtEmail.toString() : null;
        String password = edtPassword != null ? edtPassword.toString() : null;

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this,
                    "Fill email and password completely!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (adminMode && !TextUtils.isEmpty(email) && !email.equals("admin@gmail.com")) {
            Toast.makeText(LoginActivity.this,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        dialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();

                        if (task.isSuccessful()) {
                            // Sign in success
                            if (adminMode) {
                                goToAdminActivity();
                            } else {
                                goToCustomerActivity();
                            }

                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void goToAdminActivity() {
        startActivity(new Intent(this, AdminActivity.class));
    }

    public void goToCustomerActivity() {
        goToCustomerActivity(null);
    }

    public void goToCustomerActivity(View v) {
        startActivity(new Intent(this, CustomerActivity.class));
    }
}
