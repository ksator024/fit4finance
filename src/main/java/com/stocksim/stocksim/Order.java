package com.stocksim.stocksim;

import java.sql.Timestamp;

public class Order {
    private int id;
    private Trader trader;
    private Stock stock;
    private int quantity;
    private double price;
    private String type;
    private Timestamp timestamp;


    public Order(int id, Trader trader, Stock stock, int quantity, double price, String type, Timestamp timestamp) {
        this.id = id;
        this.trader = trader;
        this.stock = stock;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.timestamp = timestamp;
    }




}
