package com.stocksim.stocksim.DTOs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.stocksim.stocksim.OrderBook;

import java.util.HashMap;

@JsonPropertyOrder({"timeStamp","capital", "price", "quantities", "orderBook"})
public class UpdateDTO {
    private double capital;
    private String timeStamp;
    private HashMap<String,Double> price;
    private OrderBookDTO orderBook;
    private HashMap<String,Integer> quantities;


    public UpdateDTO(double capital, OrderBook orderBook, String timeStamp, HashMap<String,Integer> quantities, HashMap<String,Double> price) {
        this.capital = capital;
        this.timeStamp = timeStamp;
        this.orderBook = new OrderBookDTO(orderBook);
        this.quantities = quantities;
        this.price = price;
    }

    public double getCapital() {
        return capital;
    }
    public HashMap<String,Double>    getPrice() {
        return price;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public OrderBookDTO getOrderBook() {
        return orderBook;
    }

    public HashMap<String, Integer> getQuantities() {
        return quantities;
    }


}
