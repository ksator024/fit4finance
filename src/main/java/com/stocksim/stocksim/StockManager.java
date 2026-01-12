package com.stocksim.stocksim;


import com.stocksim.stocksim.DTOs.UpdateDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class StockManager {

    private DBManager db;
    private int id;
    private ArrayList<String> stockNames;
    private OrderBook orderBook;
    private long startTime;
    private long endTime;
    private double capital;
    private HashMap<String,Integer> quantities;

    public StockManager(DBManager db, OrderBook orderBook)
    {
        this.db = db;
        this.orderBook = orderBook;

        this.id = 1;
    }

    public HashMap<String,Integer> getQuantities() {
        return quantities;
    }

    public void init() throws SQLException {


        this.stockNames = new ArrayList<>();
        this.stockNames.add("AAPL");
        this.stockNames.add("GOOGL");
        this.stockNames.add("MSFT");
        this.startTime = 1262304000; // 01.01.2021
        this.endTime = 1672444800;   // 31.12.2022
        this.capital = 100000.0; // Startkapital
        db.startTimestamp(stockNames, startTime);
        orderBook.setCapital(capital);
        // Setze die initialen Preise
        for (String symbol : stockNames) {
            double price = db.getValue(symbol, "CLOSE");
            orderBook.setCurrentPrice(price, symbol);
        }
    }

    public void setOrder(Order order){
        orderBook.setOrder(order);
    }

    public void update() throws SQLException {
        // Preise aus DBManager abfragen und im OrderBook aktualisieren

        orderBook.update();
        quantities = orderBook.getQuantities();
        capital =  orderBook.getCapital();
        db.nextTimestamp();
        for (String symbol : stockNames) {
            double price = db.getValue(symbol, "CLOSE");
            orderBook.setCurrentPrice(price, symbol);
        }


    }

    public UpdateDTO getUpdateDTO(){
        return new UpdateDTO(capital, orderBook, db.getTimestamp(),orderBook.getQuantities(),orderBook.getCurrentPrice());
    }


    public void buy(@RequestBody Order buyOrder){
        orderBook.setOrder(buyOrder);
    }
    public void sell(@RequestBody Order sellOrder){
        orderBook.setOrder(sellOrder);
    }

}
