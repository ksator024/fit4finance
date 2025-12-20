package com.stocksim.stocksim;


import com.stocksim.stocksim.DTOs.UpdateDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
//@CrossOrigin(origins = "http://localhost:5173")
public class TraderController
{
    @Autowired
    private StockManager stockManager;



    @GetMapping("/quantity")
    public HashMap<String,Integer> getQuantity()
    {
        return stockManager.getQuantities();
    }

    @PostMapping("/buy")
    public void buy(@RequestBody BuyOrder buyOrder)

    {

        stockManager.buy(buyOrder);

    }


    @GetMapping("/update")
    public UpdateDTO update(){

        return stockManager.getUpdateDTO();

    }
    @PostMapping("/setPrice")
    public void setPrice( @RequestBody double value){
        stockManager.setPrice(value);
    }


    @PostMapping("/sell")
    public void sell(@RequestBody SellOrder sellOrder)
        {
        stockManager.sell(sellOrder);
        }





}
