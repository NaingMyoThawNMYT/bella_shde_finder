package com.example.bellashdefinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bellashdefinder.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToAdminActivity(View v) {
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra(LoginActivity.KEY_ADMIN_MODE, true);
        startActivity(i);
    }

    public void goToCustomerActivity(View v) {
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra(LoginActivity.KEY_ADMIN_MODE, false);
        startActivity(i);
    }
}
