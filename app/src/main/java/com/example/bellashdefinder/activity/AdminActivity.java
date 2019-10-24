package com.example.bellashdefinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bellashdefinder.R;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void goToProductList(View v) {
        startActivity(new Intent(this, ProductListActivity.class));
    }

    public void goToOrderList(View v) {
        startActivity(new Intent(this, OrderListActivity.class));
    }
}
