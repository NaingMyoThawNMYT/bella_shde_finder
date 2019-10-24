package com.example.bellashdefinder.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bellashdefinder.R;

public class LoginActivity extends AppCompatActivity {
    public static final String KEY_ADMIN_MODE = "admin_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(KEY_ADMIN_MODE)) {
            if (!b.getBoolean(KEY_ADMIN_MODE)) {
                findViewById(R.id.btn_skip_login).setVisibility(View.VISIBLE);
            }
        }
    }
}
