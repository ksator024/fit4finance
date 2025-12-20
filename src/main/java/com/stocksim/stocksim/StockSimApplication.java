package com.stocksim.stocksim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StockSimApplication {

    public static void main(String[] args) {


        SpringApplication.run(StockSimApplication.class, args);
    }

}
