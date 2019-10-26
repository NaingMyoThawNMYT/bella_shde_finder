package com.example.bellashdefinder.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.adapter.OrderListAdapter;
import com.example.bellashdefinder.model.Order;
import com.example.bellashdefinder.model.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        rv = findViewById(R.id.rv_order_list);

        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Product product = new Product();
            product.setName(String.valueOf(i));

            Order order = new Order();
            order.setCustomer(String.valueOf(i));
            order.setProduct(product);
            order.setAddress(String.valueOf(i));

            orderList.add(order);
        }

        OrderListAdapter adapter = new OrderListAdapter(orderList, new OrderListAdapter.OnClickListener() {
            @Override
            public void onClick(Order order) {
                Toast.makeText(OrderListActivity.this, order.getCustomer(), Toast.LENGTH_SHORT).show();
            }
        });
        rv.setAdapter(adapter);
    }
}
