package com.stocksim.stocksim;

import java.sql.Timestamp;

public class Order {
    private int id;
    private int quantity;
    private double price;
    private long date;
    private String name;

    public Order(int id,   int quantity, double price, String name) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        date = System.currentTimeMillis();
        this.name = name;
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
    public String getName(){ return name;}


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", price=" + price +
                ", date=" + date +
                ", name='" + name + '\'' +
                '}';
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

