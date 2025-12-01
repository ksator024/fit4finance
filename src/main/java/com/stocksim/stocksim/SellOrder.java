package com.stocksim.stocksim;

public class SellOrder extends Order{



    public SellOrder(int id, Trader trader, Stock stock, int quantity, double price) {
        super(id,  quantity, price);
    }
}
