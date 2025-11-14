package com.stocksim.stocksim;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
//@CrossOrigin(origins = "http://localhost:5173")
public class TraderController
{
    @Autowired
    private StockManager stockManager;






    @PostMapping("/")
    public String ausgabe(@RequestBody Bot bot){

        stockManager.addTrader(bot);
        return "Erfolgreich geaddet";
    }

    @GetMapping("/trader")
    public ArrayList<Trader> ausgabeTraders(){
        return stockManager.ausgabe();
    }

    @GetMapping("/stock")
    public String ausgabeStock(){

        return stockManager.getCurrentPrice();
    }


}
