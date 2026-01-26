package com.stocksim.stocksim.DTOs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.stocksim.stocksim.News;
import com.stocksim.stocksim.OrderBook;
import com.stocksim.stocksim.SimulationStatus;

import java.util.ArrayList;
import java.util.HashMap;

@JsonPropertyOrder({"timeStamp","capital", "price", "quantities", "orderBook"})
public class UpdateDTO {
    private double capital;
    private long timeStamp;
    private HashMap<String,Double> price;
    private OrderBookDTO orderBook;
    private HashMap<String,Integer> quantities;
    private ArrayList<News> news;
    private SimulationStatus status;


    public UpdateDTO(double capital, OrderBook orderBook, long timeStamp, HashMap<String,Integer> quantities, HashMap<String,Double> price, ArrayList<News> news,SimulationStatus status) {
        this.capital = capital;
        this.timeStamp = timeStamp;
        this.orderBook = new OrderBookDTO(orderBook);
        this.quantities = quantities;
        this.price = price;
        this.news = news;
        this.status = status;
    }

    public double getCapital() {
        return capital;
    }
    public HashMap<String,Double>    getPrice() {
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

    public ArrayList<News> getNews() {
        return news;
    }

    public SimulationStatus getStatus() {
        return status;
    }
}
