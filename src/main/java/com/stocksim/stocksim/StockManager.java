package com.stocksim.stocksim;


import com.stocksim.stocksim.DTOs.UpdateDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class StockManager {

    private DBManager db;
    private DBManagerNews dbNews;
    private int id;
    private ArrayList<String> stockNames;
    private OrderBook orderBook;
    private long startTime;
    private long endTime;
    private double capital;
    private HashMap<String,Integer> quantities;
    private ArrayList<News> newsList = new ArrayList<>();
    private boolean paused = false;
    private long currentTime;
    private News nextNews;
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

    public StockManager(DBManager db, int id, DBManagerNews dbNews)
    {
        this.db = db;
        this.dbNews = dbNews;
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
        dbNews.startTimestamp(startTime, endTime);
        currentTime = db.getTimestamp();
        orderBook.setCapital(capital);
        // Setze die initialen Preise
        for (String symbol : stockNames) {
            double price = db.getValue(symbol, "CLOSE");
            orderBook.setCurrentPrice(price, symbol);
        }
       // nextNews = dbNews.next();
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
        currentTime = db.getTimestamp();
        for (String symbol : stockNames) {
            double price = db.getValue(symbol, "CLOSE");
            orderBook.setCurrentPrice(price, symbol);
        }
       /* if(nextNews.getTimestamp() <= currentTime){
            if(nextNews != null) {
                newsList.add(nextNews);
                nextNews = dbNews.next();
            }
        }*/


    }

    public UpdateDTO getUpdateDTO(){

        return new UpdateDTO(capital, orderBook, db.getTimestamp(),orderBook.getQuantities(),orderBook.getCurrentPrice(),newsList);
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
