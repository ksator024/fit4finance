package com.stocksim.stocksim;

import java.sql.Timestamp;

public class Order {
    private int id;
    private int quantity;
    private double price;
    private long date;

    public Order(int id,   int quantity, double price) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        date = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public long getDate() {
        return date;
    }
}
