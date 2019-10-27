package com.example.bellashdefinder.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.adapter.ProductListAdapter;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.util.DataSet;

public class CartActivity extends AppCompatActivity {
    private RecyclerView rv;
    private ProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rv = findViewById(R.id.rv_shopping_list);

        adapter = new ProductListAdapter(DataSet.cart, new ProductListAdapter.OnClickListener() {
            @Override
            public void onClick(Product product) {
                goToResultActivity(product);
            }

            @Override
            public void onLongClick(Product product) {
                showProductRemoveConfirmDialog(CartActivity.this, product);
            }
        });

        rv.setAdapter(adapter);
    }

    private void goToResultActivity(Product product) {
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra(ProductDetailActivity.KEY_PRODUCT, product);
        i.putExtra(ResultActivity.KEY_READ_MODE, true);
        startActivity(i);
    }

    private void showProductRemoveConfirmDialog(final Context context, final Product product) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure?");
        builder.setMessage("Remove " + product.getName() + "?");
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataSet.cart.remove(product);
                adapter.notifyDataSetChanged();
                dialogInterface.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }
}
