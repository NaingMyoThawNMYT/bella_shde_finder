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
        startActivity(new Intent(this, AdminActivity.class));
    }

    public void goToCustomerActivity(View v) {
        startActivity(new Intent(this, CustomerActivity.class));
    }
}
