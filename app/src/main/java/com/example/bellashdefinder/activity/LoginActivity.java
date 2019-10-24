package com.example.bellashdefinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bellashdefinder.R;

public class LoginActivity extends AppCompatActivity {
    public static final String KEY_ADMIN_MODE = "admin_mode";
    private boolean adminMode = false;

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
    }

    public void login(View v) {
        if (adminMode) {
            goToAdminActivity(v);
        } else {
            goToCustomerActivity(v);
        }
    }

    public void goToAdminActivity(View v) {
        startActivity(new Intent(this, AdminActivity.class));
    }

    public void goToCustomerActivity(View v) {
        startActivity(new Intent(this, CustomerActivity.class));
    }
}
