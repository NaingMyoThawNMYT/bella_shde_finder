package com.example.bellashdefinder.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.util.NumberUtil;

public class ProductDetailActivity extends AppCompatActivity {
    public static final String KEY_PRODUCT = "product";

    private AppCompatEditText edtName, edtPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        edtName = findViewById(R.id.edt_name);
        edtPrice = findViewById(R.id.edt_price);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(KEY_PRODUCT)) {
            Product product = (Product) b.get(KEY_PRODUCT);

            assert product != null;

            edtName.setText(product.getName());
            edtPrice.setText(String.valueOf(NumberUtil.getOneDigit(product.getPrice())));
        }
    }
}
