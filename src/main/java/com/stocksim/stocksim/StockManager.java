package com.stocksim.stocksim;


import com.stocksim.stocksim.DTOs.UpdateDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


@Service
public class StockManager {


    DBManager db = new DBManager("stocks.db");
    private int id;
    private ArrayList<String> stockNames = new ArrayList<>(Arrays.asList("GOOGL", "APPL", "DAX"));
    private OrderBook orderBook = new OrderBook(stockNames);



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
        orderBook.setCapital(capital);
    }
    public void setOrder(Order order){
        orderBook.setOrder(order);
        DBManager db = new DBManager("stocks.db");
        db.startTimestamp();
    }

    @Scheduled(fixedRate = 1000)
    public void update(){
        orderBook.update();
        quantities = orderBook.getQuantities();
        capital =  orderBook.getCapital();

    }

    public UpdateDTO getUpdateDTO(){
        return new UpdateDTO(capital, orderBook, 10,orderBook.getQuantities(),orderBook.getCurrentPrice());
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
