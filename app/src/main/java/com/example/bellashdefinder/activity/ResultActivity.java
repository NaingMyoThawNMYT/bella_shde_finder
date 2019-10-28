package com.example.bellashdefinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.util.BitmapUtil;
import com.example.bellashdefinder.util.DataSet;
import com.example.bellashdefinder.util.NumberUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ResultActivity extends AppCompatActivity {
    public static final String KEY_READ_MODE = "read_mode";
    private Product product;

    private AppCompatButton btnCart;
    private AppCompatImageView imgView;
    private AppCompatTextView tvName, tvPrice;

    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        btnCart = findViewById(R.id.btn_cart);
        imgView = findViewById(R.id.img_v_product);
        tvName = findViewById(R.id.tv_name);
        tvPrice = findViewById(R.id.tv_price);

        storageRef = FirebaseStorage.getInstance().getReference();

        btnCart.setText(String.valueOf(DataSet.cart.size()));

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(ProductDetailActivity.KEY_PRODUCT)) {
            if (b.getBoolean(KEY_READ_MODE, false) ||
                    b.get(ProductDetailActivity.KEY_PRODUCT) == null) {
                btnCart.setVisibility(View.GONE);
                findViewById(R.id.btn_add_to_cart).setVisibility(View.GONE);
            }

            if (b.get(ProductDetailActivity.KEY_PRODUCT) != null) {
                product = (Product) b.get((ProductDetailActivity.KEY_PRODUCT));

                if (product != null) {
                    if (DataSet.photos.get(product.getId()) == null) {
                        storageRef.child(product.getId()).getBytes(Long.MAX_VALUE).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                            @Override
                            public void onComplete(@NonNull Task<byte[]> task) {
                                if (task.isSuccessful()) {
                                    DataSet.photos.put(product.getId(), BitmapUtil.byteArrayToBitmap(task.getResult()));
                                    imgView.setImageBitmap(DataSet.photos.get(product.getId()));
                                }
                            }
                        });
                    } else {
                        imgView.setImageBitmap(DataSet.photos.get(product.getId()));
                    }

                    tvName.setText(product.getName());
                    tvPrice.setText(String.valueOf(NumberUtil.getOneDigit(product.getPrice())));
                }
            }
        }
    }

    public void addToCart(View v) {
        DataSet.cart.add(product);

        finish();
    }

    public void goToCartActivity(View v) {
        startActivity(new Intent(this, CartActivity.class));
    }
}
