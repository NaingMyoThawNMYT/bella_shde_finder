package com.example.bellashdefinder.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.adapter.ProductListAdapter;
import com.example.bellashdefinder.model.Order;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.storage.FirebaseDatabaseHelper;
import com.example.bellashdefinder.util.NumberUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

public class OrderDetailActivity extends AppCompatActivity {
    public static final String KEY_ORDER = "order";
    private DatabaseReference tableCustomer, tableOrder;

    private AppCompatTextView tvInvoiceNo,
            tvCustomerName,
            tvCustomerAddress,
            tvCustomerPhone,
            tvCustomerEmail,
            tvTotal;
    private RecyclerView rvOrderItemList;
    private ProgressDialog dialog;

    private String invoiceNo;
    private ProductListAdapter adapter;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        tvInvoiceNo = findViewById(R.id.tv_invoice_no);
        tvCustomerName = findViewById(R.id.tv_customer_name);
        tvCustomerAddress = findViewById(R.id.tv_customer_address);
        tvCustomerPhone = findViewById(R.id.tv_customer_phone);
        tvCustomerEmail = findViewById(R.id.tv_customer_email);
        tvTotal = findViewById(R.id.tv_total);
        rvOrderItemList = findViewById(R.id.rv_order_item_list);

        tableCustomer = FirebaseDatabaseHelper.getTableCustomer();
        tableOrder = FirebaseDatabaseHelper.getTableOrder();

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(KEY_ORDER)) {
            order = (Order) b.get(KEY_ORDER);

            if (order != null) {
                findViewById(R.id.btn_invoice).setEnabled(true);

                invoiceNo = String.valueOf(System.currentTimeMillis());

                tvInvoiceNo.setText(invoiceNo);
                tvCustomerName.setText(order.getCustomer().getName());
                tvCustomerAddress.setText(order.getCustomer().getAddress());
                tvCustomerPhone.setText(order.getCustomer().getPhone());
                tvCustomerEmail.setText(order.getCustomer().getEmail());

                double total = 0;

                for (Product product : order.getProductList()) {
                    total += product.getPrice();
                }

                if (order.getDiscountPercent() > 0) {
                    double discountAmount = NumberUtil.convertPercentToAmount(order.getDiscountPercent(), total);
                    total -= discountAmount;
                }

                tvTotal.setText(String.valueOf(NumberUtil.getOneDigit(total)));

                adapter = new ProductListAdapter(order.getProductList(), new ProductListAdapter.OnClickListener() {
                    @Override
                    public void onClick(Product product) {
                    }

                    @Override
                    public void onLongClick(Product product) {
                    }
                });

                rvOrderItemList.setAdapter(adapter);
            }
        }
    }

    public void deleteOrder(View v) {
        dialog.dismiss();
        tableCustomer.child(order.getCustomerId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                tableOrder.child(order.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        showInvoicedCompleteDialog();
                    }
                });
            }
        });
    }

    public void showInvoicedCompleteDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Successfully invoiced " + invoiceNo);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }
}
