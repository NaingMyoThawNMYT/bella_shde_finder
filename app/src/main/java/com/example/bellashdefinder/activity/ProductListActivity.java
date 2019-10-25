package com.example.bellashdefinder.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.adapter.ProductListAdapter;
import com.example.bellashdefinder.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private RecyclerView rv;

    private ProductListAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        rv = findViewById(R.id.rv_product_list);

        productList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Product product = new Product();
            product.setName("Name " + i);
            product.setPrice(i * 1000);
            productList.add(product);
        }

        adapter = new ProductListAdapter(productList, new ProductListAdapter.OnClickListener() {
            @Override
            public void onClick(Product product) {
                Intent i = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                i.putExtra(ProductDetailActivity.KEY_PRODUCT, product);
                startActivity(i);
            }
        });
        rv.setAdapter(adapter);
    }
}
