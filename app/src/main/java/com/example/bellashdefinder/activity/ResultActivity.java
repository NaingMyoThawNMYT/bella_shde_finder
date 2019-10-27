package com.example.bellashdefinder.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.util.DataSet;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ((AppCompatButton) findViewById(R.id.btn_cart)).setText(String.valueOf(DataSet.cart.size()));
    }

    public void addToCart(View v) {
        DataSet.cart.add(new Product());

        finish();
    }
}
