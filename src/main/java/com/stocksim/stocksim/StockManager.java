package com.stocksim.stocksim;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class StockManager {

    private int timeStamp = 0;
    private ArrayList<Trader> traders = new ArrayList<Trader>();
    //private ArrayList<Stock> stocks = new ArrayList<Stock>();
    @Autowired
    private Stock stock;
    @Scheduled(fixedRate = 1000)
    public void update(){
        timeStamp++;
    }
    public void addTrader(Trader trader){
        traders.add(trader);
    }

    public int getCounter() {
        return timeStamp;
    }

    public ArrayList<Trader> ausgabe(){
        return traders;
    }

    public String getCurrentPrice(){

        String time = LocalTime.now().format((DateTimeFormatter.ofPattern("HH:mm:ss:SSS")));
        return stock.getCurrentPrice(timeStamp) + " time: " + time ;
    }

    @PostConstruct
    public void init(){



    }

}
