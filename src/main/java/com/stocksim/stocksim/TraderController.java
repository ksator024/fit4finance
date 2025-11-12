package com.stocksim.stocksim;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TraderController {

    @GetMapping("/hallo")
    public Trader hallo(){

        Trader trader = new Bot(1,"hans");

        return trader;
    }





}
