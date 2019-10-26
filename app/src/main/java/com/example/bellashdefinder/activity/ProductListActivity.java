package com.example.bellashdefinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.adapter.ProductListAdapter;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.util.DataSet;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private RecyclerView rv;
    private AppCompatSpinner categorySpinner;

    private ProductListAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        rv = findViewById(R.id.rv_product_list);
        categorySpinner = findViewById(R.id.spn_category);

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
                goToProductDetailActivity(product);
            }
        });
        rv.setAdapter(adapter);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProductDetailActivity(null);
            }
        });

        final String[] categoryList = new String[4];
        categoryList[0] = "All";
        System.arraycopy(DataSet.categoryList, 0, categoryList, 1, 3);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                categoryList);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ProductListActivity.this, categoryList[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void goToProductDetailActivity(Product product) {
        Intent i = new Intent(ProductListActivity.this, ProductDetailActivity.class);
        i.putExtra(ProductDetailActivity.KEY_PRODUCT, product);
        startActivity(i);
    }
}
