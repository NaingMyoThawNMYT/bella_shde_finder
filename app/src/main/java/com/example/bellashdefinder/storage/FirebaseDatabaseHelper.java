package com.example.bellashdefinder.storage;

import com.example.bellashdefinder.model.Customer;
import com.example.bellashdefinder.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseDatabaseHelper {

    public static DatabaseReference getTableProduct() {
        return FirebaseDatabase.getInstance().getReference("table_product");
    }

    public static DatabaseReference getTableCustomer() {
        return FirebaseDatabase.getInstance().getReference("table_customer");
    }

    public static DatabaseReference getTableOrder() {
        return FirebaseDatabase.getInstance().getReference("table_order");
    }

    public static List<Order> parseOrderList(DataSnapshot dataSnapshot) {
        List<Order> orderList = new ArrayList<>();

        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            orderList.add(parseOrder(childSnapshot));
        }

        return orderList;
    }

    private static Order parseOrder(DataSnapshot dataSnapshot) {
        Order order = new Order();

        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
        order.setId(String.valueOf(map.get("id")));
        order.setCustomerId(String.valueOf(map.get("customerId")));
        order.setDiscountAmount(Double.valueOf(String.valueOf(map.get("discountAmount"))));
        order.setProductIdList((List<String>) map.get("productIdList"));

        return order;
    }

    public static List<Customer> parseCustomerList(DataSnapshot dataSnapshot) {
        List<Customer> customerList = new ArrayList<>();

        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            customerList.add(parseCustomer(childSnapshot));
        }

        return customerList;
    }

    private static Customer parseCustomer(DataSnapshot dataSnapshot) {
        Customer customer = new Customer();

        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
        customer.setId(String.valueOf(map.get("id")));
        customer.setName(String.valueOf(map.get("name")));
        customer.setAddress(String.valueOf(map.get("address")));
        customer.setPhone(String.valueOf(map.get("phone")));
        customer.setEmail(String.valueOf(map.get("email")));

        return customer;
    }
}
