package com.stocksim.stocksim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockSimApplication {

    public static void main(String[] args) {

        SpringApplication.run(StockSimApplication.class, args);
        System.out.println("Das gitignore ist vermutlich scheiße.");
    }

}
