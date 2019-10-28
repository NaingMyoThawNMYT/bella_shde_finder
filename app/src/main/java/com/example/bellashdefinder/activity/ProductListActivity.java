package com.example.bellashdefinder.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.adapter.ProductListAdapter;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.storage.FirebaseDatabaseHelper;
import com.example.bellashdefinder.util.DataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductListActivity extends AppCompatActivity {
    private RecyclerView rv;
    private AppCompatSpinner categorySpinner;

    private ProductListAdapter adapter;
    private ProgressDialog dialog;

    private DatabaseReference tableProduct;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        rv = findViewById(R.id.rv_product_list);
        categorySpinner = findViewById(R.id.spn_category);

        adapter = new ProductListAdapter(new ArrayList<Product>(), new ProductListAdapter.OnClickListener() {
            @Override
            public void onClick(Product product) {
                goToProductDetailActivity(product);
            }

            @Override
            public void onLongClick(Product product) {
                showProductDeleteConfirmDialog(ProductListActivity.this, product);
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
        final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                categoryList);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.getFilter().filter(position == 0 ? "" : categoryList[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");

        tableProduct = FirebaseDatabaseHelper.getTableProduct();
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    private void goToProductDetailActivity(Product product) {
        Intent i = new Intent(ProductListActivity.this, ProductDetailActivity.class);
        i.putExtra(ProductDetailActivity.KEY_PRODUCT, product);
        startActivity(i);
    }

    private void showProductDeleteConfirmDialog(final Context context, final Product product) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure?");
        builder.setMessage("Delete " + product.getName() + "?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tableProduct.child(product.getId()).removeValue();

                storageRef.child(product.getId()).delete();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchAndSetProductList();
    }

    private void fetchAndSetProductList() {
        DatabaseReference tableProduct = FirebaseDatabaseHelper.getTableProduct();

        dialog.show();

        // Read from the database
        tableProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog.dismiss();
                adapter.setDataSet(parseProductList((Map<String, Object>) dataSnapshot.getValue()));
                String filter = (String) categorySpinner.getSelectedItem();
                adapter.getFilter().filter(filter.equals("All") ? "" : filter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private List<Product> parseProductList(Map<String, Object> map) {
        List<Product> productList = new ArrayList<>();

        if (map == null) {
            return productList;
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            productList.add(parseProduct((Map) entry.getValue()));
        }

        return productList;
    }

    private Product parseProduct(Map entry) {
        Product product = new Product();

        product.setCategory(String.valueOf(entry.get("category")));
        product.setFinishFit(String.valueOf(entry.get("finishFit")));
        product.setId(String.valueOf(entry.get("id")));
        product.setName(String.valueOf(entry.get("name")));
        product.setPrice(Double.valueOf(String.valueOf(entry.get("price"))));
        product.setShadeFamily(String.valueOf(entry.get("shadeFamily")));
        product.setSkinType(String.valueOf(entry.get("skinType")));

        return product;
    }
}
