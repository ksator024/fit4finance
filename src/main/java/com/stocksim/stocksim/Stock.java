package com.stocksim.stocksim;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Component
public class Stock {

    private Map<Integer,Double> price = new HashMap<Integer, Double>();


    @PostConstruct
    public void init(){
        for(int i = 0; i< 60; i++){
            price.put(i,Math.random()*100+1);
        }
        System.out.println("price loaded");
        System.out.println(price);
    }

    public double getCurrentPrice(int timeStamp){

        System.out.println("Preis: " + price);
        return price.get((timeStamp % 10));
    }

}
