package com.example.bellashdefinder.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.adapter.OrderListAdapter;
import com.example.bellashdefinder.model.Order;

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

            Order order = new Order();

//            Customer customer = new Customer();
//            customer.setName("Name" + i);
//            customer.setAddress("Address" + i);
//            customer.setPhone("Phone" + i);
//            customer.setEmail("Email" + i);
//            order.setCustomerId(customer);
//
//            Product product = new Product();
//            product.setName("Product" + i);
//            product.setPrice(i);
//            product.setCategory("Category" + i);
//            order.getProductIdList().add(product);

            orderList.add(order);
        }

        OrderListAdapter adapter = new OrderListAdapter(orderList, new OrderListAdapter.OnClickListener() {
            @Override
            public void onClick(Order order) {
                Intent i = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                i.putExtra(OrderDetailActivity.KEY_ORDER, order);
                startActivity(i);
            }
        });
        rv.setAdapter(adapter);
    }
}
