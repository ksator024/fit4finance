package com.stocksim.stocksim.DTOs;

import com.stocksim.stocksim.BuyOrder;
import com.stocksim.stocksim.OrderBook;
import com.stocksim.stocksim.SellOrder;

import java.util.ArrayList;

public class OrderBookDTO {

    private ArrayList<BuyOrder> buyOrders = new ArrayList<BuyOrder>();
    private ArrayList<SellOrder> sellOrders = new ArrayList<SellOrder>();


    public OrderBookDTO(OrderBook orderBook) {
        this.buyOrders = orderBook.getBuyOrders();
        this.sellOrders = orderBook.getSellOrders();

    }


    public ArrayList<BuyOrder> getBuyOrders() {
        return buyOrders;
    }

    public ArrayList<SellOrder> getSellOrders() {
        return sellOrders;
    }
}
