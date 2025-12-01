package com.stocksim.stocksim;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
@Service
public class StockManager {

    private int id;
    @Autowired
    private OrderBook orderBook;


    @Value("${player.startCapital}")
    private double capital;

    private int quantity;

    public   StockManager(){
        id = 1;

    }

    public int getQuantity() {
        return quantity;
    }

    @PostConstruct
    public void init(){
        orderBook.setCapital(capital);
    }
    public void setOrder(Order order){
        orderBook.setOrder(order);

    }

    @Scheduled(fixedRate = 1000)
    public void update(){
        orderBook.update();
        quantity = orderBook.getQuantity();
        capital =  orderBook.getCapital();
    }

    public void buy(@RequestBody Order buyOrder){
        orderBook.setOrder(buyOrder);
    }
    public void sell(@RequestBody Order sellOrder){
        orderBook.setOrder(sellOrder);
    }

}
