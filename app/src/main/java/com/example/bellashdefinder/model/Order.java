package com.example.bellashdefinder.model;

import java.io.Serializable;

public class Order implements Serializable {
    private Customer customer;
    private Product product;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
