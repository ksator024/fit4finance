package com.stocksim.stocksim.DTOs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.stocksim.stocksim.BuyOrder;
import com.stocksim.stocksim.OrderBook;
import com.stocksim.stocksim.SellOrder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

@JsonPropertyOrder({"timeStamp", "price", "capital", "quantities", "orderBook"})
public class UpdateDTO {
    private double capital;
    private long timeStamp;
    private double price;
    private OrderBookDTO orderBook;
    private HashMap<String,Integer> quantities;


    public UpdateDTO(double capital, OrderBook orderBook, long timeStamp,HashMap<String,Integer> quantities,double price) {
        this.capital = capital;
        this.timeStamp = timeStamp;
        this.orderBook = new OrderBookDTO(orderBook);
        this.quantities = quantities;
        this.price = price;
    }

    public double getCapital() {
        return capital;
    }
    public double getPrice() {
        return price;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public OrderBookDTO getOrderBook() {
        return orderBook;
    }

    public HashMap<String, Integer> getQuantities() {
        return quantities;
    }


}
