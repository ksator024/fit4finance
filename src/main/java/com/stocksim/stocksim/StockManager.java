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
    private boolean paused = false;

    public void setPaused(String action) {
        if(action.equals("start")) {
            paused = true;
        }
        else if(action.equals("stop")) {
            paused = false;
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public StockManager(DBManager db, int id)
    {
        this.db = db;
        this.id = 1;
        Scenario scenario = ScenarioManager.getScenario(id);
        this.stockNames = scenario.getStockName();
        this.capital = scenario.getCapital();
        this.startTime = scenario.getStartTime();
        this.endTime = scenario.getEndTime();
        this.orderBook = new OrderBook(stockNames);

    }

    public HashMap<String,Integer> getQuantities() {
        return quantities;
    }

    public void init() throws SQLException {


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

    public void cancelOrder(int id){
        orderBook.cancelOrder(id);
    }
    public void buy(@RequestBody Order buyOrder){
        orderBook.setOrder(buyOrder);
    }
    public void sell(@RequestBody Order sellOrder){
        orderBook.setOrder(sellOrder);
    }

}
