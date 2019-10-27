package com.example.bellashdefinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.util.DataSet;

public class ResultActivity extends AppCompatActivity {
    public static final String KEY_READ_MODE = "read_mode";

    private AppCompatButton btnCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        btnCart = findViewById(R.id.btn_cart);

        btnCart.setText(String.valueOf(DataSet.cart.size()));

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(KEY_READ_MODE)) {
            if (b.getBoolean(KEY_READ_MODE, false)) {
                btnCart.setVisibility(View.GONE);
                findViewById(R.id.btn_add_to_cart).setVisibility(View.GONE);
            }
        }
    }

    public void addToCart(View v) {
        Product product = new Product();
        product.setName("Name A");
        product.setPrice(1000);
        DataSet.cart.add(product);

        finish();
    }

    public void goToCartActivity(View v) {
        startActivity(new Intent(this, CartActivity.class));
    }
}
