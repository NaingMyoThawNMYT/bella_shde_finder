package com.example.bellashdefinder.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.adapter.OrderListAdapter;
import com.example.bellashdefinder.model.Order;
import com.example.bellashdefinder.storage.FirebaseDatabaseHelper;
import com.example.bellashdefinder.util.DataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderListActivity extends AppCompatActivity {
    private DatabaseReference tableOrder, tableCustomer, tableProduct;
    private OrderListAdapter adapter;

    private RecyclerView rv;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        tableOrder = FirebaseDatabaseHelper.getTableOrder();
        tableCustomer = FirebaseDatabaseHelper.getTableCustomer();
        tableProduct = FirebaseDatabaseHelper.getTableProduct();

        rv = findViewById(R.id.rv_order_list);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        adapter = new OrderListAdapter(new ArrayList<Order>(), new OrderListAdapter.OnClickListener() {
            @Override
            public void onClick(Order order) {
                Intent i = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                i.putExtra(OrderDetailActivity.KEY_ORDER, order);
                startActivity(i);
            }
        });
        rv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchAndSetOrderList();
    }

    private void fetchAndSetOrderList() {
        DataSet.customerList.clear();
        DataSet.productList.clear();

        // get order list
        tableOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    dialog.dismiss();
                    adapter.setDataSet(new ArrayList<Order>());
                    return;
                }

                final List<Order> orderList = FirebaseDatabaseHelper.parseOrderList(dataSnapshot);

                // get customer list
                tableCustomer.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DataSet.customerList.addAll(FirebaseDatabaseHelper.parseCustomerList(dataSnapshot));

                        // get product list
                        tableProduct.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dialog.dismiss();

                                DataSet.productList.addAll(ProductListActivity.parseProductList((Map<String, Object>) dataSnapshot.getValue()));

                                // refresh ui
                                adapter.setDataSet(orderList);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }
}
