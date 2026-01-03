package com.stocksim.stocksim;


import com.stocksim.stocksim.DTOs.UpdateDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


@Service
public class StockManager {

    private DBManager db;
    private int id;
    private ArrayList<String> stockNames = new ArrayList<>(Arrays.asList("AAPL","GOOGL"));
    private OrderBook orderBook = new OrderBook(stockNames);
    private long startTime = 1;



    @Value("${player.startCapital}")
    private double capital;



    private HashMap<String,Integer> quantities;

    public StockManager()
    {
        id = 1;
    }

    public HashMap<String,Integer> getQuantities() {
        return quantities;
    }

    @PostConstruct
    public void init(){
        db = new DBManager("test.db");
        try {
            db.startTimestamp(stockNames, startTime);
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Starten des Timestamps", e);
        }
        orderBook.setCapital(capital);
    }

    public void setOrder(Order order){
        orderBook.setOrder(order);
    }

    @Scheduled(fixedRate = 1000)
    public void update() throws SQLException {
        // Preise aus DBManager abfragen und im OrderBook aktualisieren
        for (String symbol : stockNames) {
            double price = db.getValue(symbol, "CLOSE");
            orderBook.setCurrentPrice(price, symbol);
        }
        db.nextTimestamp();
        orderBook.update();
        quantities = orderBook.getQuantities();
        capital =  orderBook.getCapital();


    }

    public UpdateDTO getUpdateDTO(){
        String formattedDate = Instant.ofEpochSecond(db.getTimestamp()).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        return new UpdateDTO(capital, orderBook, formattedDate,orderBook.getQuantities(),orderBook.getCurrentPrice());
    }
   /* public void setPrice(double price){
        orderBook.setCurrentPrice(price);
    }*/

    public void buy(@RequestBody Order buyOrder){
        orderBook.setOrder(buyOrder);
    }
    public void sell(@RequestBody Order sellOrder){
        orderBook.setOrder(sellOrder);
    }

}
