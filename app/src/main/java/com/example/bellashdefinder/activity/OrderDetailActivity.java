package com.example.bellashdefinder.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.model.Order;

public class OrderDetailActivity extends AppCompatActivity {
    public static final String KEY_ORDER = "order";

    private AppCompatImageView imgView;
    private AppCompatTextView tvInvoiceNo,
            tvProductName,
            tvProductPrice,
            tvProductCategory,
            tvCustomerName,
            tvCustomerAddress,
            tvCustomerPhone,
            tvCustomerEmail;

    private String invoiceNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        imgView = findViewById(R.id.img_v_product);
        tvInvoiceNo = findViewById(R.id.tv_invoice_no);
        tvProductName = findViewById(R.id.tv_product_name);
        tvProductPrice = findViewById(R.id.tv_product_price);
        tvProductCategory = findViewById(R.id.tv_product_category);
        tvCustomerName = findViewById(R.id.tv_customer_name);
        tvCustomerAddress = findViewById(R.id.tv_customer_address);
        tvCustomerPhone = findViewById(R.id.tv_customer_phone);
        tvCustomerEmail = findViewById(R.id.tv_customer_email);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(KEY_ORDER)) {
            Order order = (Order) b.get(KEY_ORDER);

            if (order != null) {
                invoiceNo = String.valueOf(System.currentTimeMillis());

                tvInvoiceNo.setText(invoiceNo);
//                tvProductName.setText(order.getProduct().getName());
//                tvProductPrice.setText(String.valueOf(NumberUtil.getOneDigit(order.getProduct().getPrice())));
//                tvProductCategory.setText(order.getProduct().getCategory());
                tvCustomerName.setText(order.getCustomer().getName());
                tvCustomerAddress.setText(order.getCustomer().getAddress());
                tvCustomerPhone.setText(order.getCustomer().getPhone());
                tvCustomerEmail.setText(order.getCustomer().getEmail());
            }
        }
    }

    public void showInvoicedCompleteDialog(View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Successfully invoiced " + invoiceNo);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                finish();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }
}
