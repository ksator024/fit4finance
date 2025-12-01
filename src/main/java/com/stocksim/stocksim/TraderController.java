package com.stocksim.stocksim;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
//@CrossOrigin(origins = "http://localhost:5173")
public class TraderController
{
    @Autowired
    private StockManager stockManager;

    @GetMapping("/quantity")
    public int getQuantity()
    {
        return stockManager.getQuantity();
    }

    @PostMapping("/buy")
    public void buy(@RequestBody BuyOrder buyOrder)
    {
        stockManager.buy(buyOrder);
    }
    @PostMapping("/sell")
    public void sell(@RequestBody SellOrder sellOrder)
        {
        stockManager.sell(sellOrder);
        }





}
